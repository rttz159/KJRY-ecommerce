package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.ProductImageManager;
import kjry.ecommerce.services.ProductService;

public class AdminProductController implements Initializable {

    @FXML
    private VBox AdminProductParentVBox;

    @FXML
    private TableColumn<ProductsDTO, Button> actionTableColumn;

    @FXML
    private TableColumn<ProductsDTO, Double> costPriceTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> idTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> nameTableColumn;

    @FXML
    private TableView<ProductsDTO> productsTableView;

    @FXML
    private TableColumn<ProductsDTO, Integer> qtyTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> typeTableColumn;

    @FXML
    private TableColumn<ProductsDTO, Double> sellingPriceTableColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    private ObservableList<ProductsDTO> list = FXCollections.observableArrayList(ProductService.getAllProducts());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        sellingPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyTableColumn.setCellValueFactory(new PropertyValueFactory<>("stockQty"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        actionTableColumn.setCellFactory(column -> new TableCell<ProductsDTO, Button>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    ProductsDTO product = getTableView().getItems().get(getIndex());
                    System.out.println("Action button clicked for product: " + product.getName());
                    productPromptDialog(product, false);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });

        editButton.setOnAction(event -> {
            ProductsDTO product = productsTableView.getSelectionModel().getSelectedItem();
            try {
                System.out.println("Edit button clicked for product: " + product.getName());
                if (productPromptDialog(product, true)) {
                    ProductService.updateProduct(product, product.getStockQty());
                    list = FXCollections.observableArrayList(ProductService.getAllProducts());
                    productsTableView.setItems(list);
                    productsTableView.refresh();
                }
            } catch (NullPointerException x) {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select a product before proceed.");
                warningAlert.setHeaderText("No Product Selected");
                warningAlert.showAndWait();
            }
        });

        addButton.setOnAction(event -> {
            final boolean[] buttonClicked = {false};
            final ProductsDTO[] product = new ProductsDTO[1];
            Stage dialogStage1 = new Stage();
            Button clothingBtn = new Button("Clothing");
            Button accessoriesBtn = new Button("Accessories");
            VBox vbox = new VBox();
            Label title = new Label("Choose The User Type");
            HBox hbox = new HBox();
            hbox.getChildren().add(clothingBtn);
            hbox.getChildren().add(accessoriesBtn);
            Scene scene = new Scene(vbox);
            scene.getStylesheets().add(getClass().getResource("/views/css/AddButton.css").toExternalForm());
            dialogStage1.setScene(scene);
            dialogStage1.initModality(Modality.APPLICATION_MODAL);
            hbox.setStyle("-fx-spacing: 10px;");
            vbox.getChildren().add(title);
            vbox.setStyle("-fx-alignment: center;-fx-padding: 20px; -fx-background-color: white;-fx-spacing: 10px;");
            vbox.getChildren().add(hbox);
            clothingBtn.setOnAction(ev2 -> {
                product[0] = new ClothingDTO();
                buttonClicked[0] = true;
                dialogStage1.close();
            });
            accessoriesBtn.setOnAction(ev2 -> {
                product[0] = new AccessoriesDTO();
                buttonClicked[0] = true;
                dialogStage1.close();
            });

            dialogStage1.showAndWait();

            if (buttonClicked[0]) {
                if (productPromptDialog(product[0], true)) {
                    ProductService.createProduct(product[0], product[0].getStockQty());
                    list = FXCollections.observableArrayList(ProductService.getAllProducts());
                    productsTableView.setItems(list);
                    this.productsTableView.refresh();
                }
            }
        });

        removeButton.setOnAction(event -> {
            ProductsDTO product = productsTableView.getSelectionModel().getSelectedItem();
            Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION);
            warningAlert.setHeaderText("Product will be PERMANENTLY DELETED.");
            warningAlert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    ProductImageManager imageManager = new ProductImageManager();
                    imageManager.removeProductImage(product);
                    ProductService.removeProduct(product);
                    list = FXCollections.observableArrayList(ProductService.getAllProducts());
                    productsTableView.setItems(list);
                    this.productsTableView.refresh();
                }
            });
        });

        productsTableView.setItems(list);
    }

    private boolean productPromptDialog(ProductsDTO product, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminProductInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            AdminProductInfoDialogController controller = loader.getController();
            controller.setProduct(product, !editable);

            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Product Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
            return controller.isCreate();
        } catch (IOException ex) {
            System.out.println("Error when loading AdminOrderInfoAlert.fxml");
            ex.printStackTrace();
            return false;
        }
    }

}
