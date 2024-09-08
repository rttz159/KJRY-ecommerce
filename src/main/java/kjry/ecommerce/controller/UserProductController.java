package kjry.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import kjry.ecommerce.App;

public class UserProductController implements Initializable {

    @FXML
    private VBox UserProductParentVBox;

    @FXML
    private TableColumn<ProductsDTO, Button> actionTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> idTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> nameTableColumn;

    @FXML
    private TableColumn<ProductsDTO, Integer> qtyTableColumn;

    @FXML
    private TableView<ProductsDTO> productsTableView;

    @FXML
    private TableColumn<ProductsDTO, Double> sellingPriceTableColumn;

    @FXML
    private TableColumn<ProductsDTO, String> typeTableColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button tileButton;

    @FXML
    private Button listButton;

    @FXML
    private ScrollPane tileScrollPane;

    @FXML
    private VBox tileVBox;

    private GridPane productGrid;

    private ObservableList<ProductsDTO> list = FXCollections.observableArrayList(ProductService.getAllProducts(false));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupButtonActions();

        productGrid = new GridPane();
        productGrid.setPrefWidth(595);
        productGrid.setHgap(10);
        productGrid.setVgap(10);

        loadProductTiles(productGrid);
        tileVBox.getChildren().add(productGrid);
        switchLayout(true);

        productsTableView.setItems(list);
        Label unfoundProduct = new Label("No product found.");
        productsTableView.setPlaceholder(unfoundProduct);
    }

    private void setupTableColumns() {
        sellingPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        qtyTableColumn.setCellValueFactory(new PropertyValueFactory<>("stockQty"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        actionTableColumn.setCellFactory(column -> new TableCell<ProductsDTO, Button>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> showProductDetails(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    ProductsDTO row = getTableRow().getItem();
                    button.setDisable(row.getStockQty() == 0);
                    button.setText(row.getStockQty() == 0 ? "Unavailable" : "Details");
                    setGraphic(button);
                }
            }
        });
    }

    private void setupButtonActions() {
        resetButton.setOnAction(ev -> {
            list = FXCollections.observableArrayList(ProductService.getAllProducts(false));
            productsTableView.setItems(list);
            this.productsTableView.refresh();
            productGrid.getChildren().clear();
            loadProductTiles(productGrid);
            tileVBox.getChildren().clear();
            tileVBox.getChildren().add(productGrid);
        });

        searchButton.setOnAction(ev -> searchProducts());

        tileButton.setOnAction(ev -> switchLayout(true));
        listButton.setOnAction(ev -> switchLayout(false));
    }

    private void searchProducts() {
        if (!searchTextField.getText().isBlank()) {
            String searchText = searchTextField.getText().toLowerCase();
            ProductsDTO[] tempProd = ProductService.getAllProducts(false);
            ArrayList<ProductsDTO> filteredProducts = new ArrayList<>();
            for (ProductsDTO product : tempProd) {
                if (product.getName().toLowerCase().contains(searchText) || product.getId().toLowerCase().contains(searchText)) {
                    filteredProducts.add(product);
                }
            }
            list = FXCollections.observableArrayList(filteredProducts);
            productsTableView.setItems(list);
            this.productsTableView.refresh();
            productGrid.getChildren().clear();
            loadProductTiles(productGrid);
            tileVBox.getChildren().clear();
            tileVBox.getChildren().add(productGrid);
        }
    }

    private void loadProductTiles(GridPane productGrid) {
        int row = 0;
        int col = 0;
        int columnsPerRow = 2;
        if (!list.isEmpty()) {
            for (ProductsDTO product : list) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/UserProductTiles.fxml"));
                    VBox productTile = fxmlLoader.load();

                    UserProductTilesController controller = fxmlLoader.getController();
                    controller.setProduct(product);

                    productGrid.add(productTile, col, row);

                    col++;
                    if (col == columnsPerRow) {
                        col = 0;
                        row++;
                    }
                } catch (IOException ex) {
                    System.out.println("Error when loading the Product Tiles.");
                    ex.printStackTrace();
                }
            }
        } else {
            Label unfoundProduct = new Label("No product found.");
            unfoundProduct.setPrefHeight(col);
            productGrid.add(unfoundProduct, col, row);
        }
    }

    private void showProductDetails(ProductsDTO product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProductInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            UserProductInfoDialogController controller = loader.getController();
            controller.setProduct(product);

            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Product Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();

            if (controller.isAdd()) {
                Alert informAlert = new Alert(Alert.AlertType.INFORMATION);
                informAlert.setContentText("Products added to the shopping cart.");
                informAlert.showAndWait();
            }
        } catch (IOException ex) {
            System.out.println("Error when loading UserProductInfoAlert.fxml");
            ex.printStackTrace();
        }
    }

    private void switchLayout(boolean isTile) {
        productsTableView.setVisible(!isTile);
        productsTableView.setManaged(!isTile);
        tileScrollPane.setVisible(isTile);
        tileScrollPane.setManaged(isTile);
    }
}
