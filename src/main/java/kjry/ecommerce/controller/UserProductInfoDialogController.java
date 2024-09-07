package kjry.ecommerce.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.ProductImageManager;
import kjry.ecommerce.services.UserService;

public class UserProductInfoDialogController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private VBox alertParentVBox;

    @FXML
    private Button cancelButton;

    @FXML
    private ImageView productImage;

    @FXML
    private Spinner<Integer> qtySpinner;

    @FXML
    private TextField sellingPriceTextField;

    @FXML
    private ChoiceBox<ClothingDTO.SizeDTO> sizeChoiceBox;

    @FXML
    private HBox sizeHBox;
    
    @FXML
    private Label nameLabel;

    @FXML
    private TextField stockQtyTextField;

    @FXML
    private ChoiceBox<ClothingDTO.TypeDTO> typeChoiceBox;

    @FXML
    private HBox typeHBox;

    @FXML
    private ChoiceBox<Boolean> washableChoiceBox;

    @FXML
    private HBox washableHBox;

    private ProductsDTO product;
    private Stage parent;
    private boolean isAdd = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeChoiceBox.getItems().addAll(ClothingDTO.TypeDTO.values());
        typeChoiceBox.setDisable(true);
        sizeChoiceBox.getItems().addAll(ClothingDTO.SizeDTO.values());
        sizeChoiceBox.setDisable(true);
        washableChoiceBox.getItems().addAll(true, false);
        washableChoiceBox.setDisable(true);
        addButton.setOnAction(ev->{
            UsersDTO currentUser = UserService.getUser(App.getCurrentUserId());
            UserService userService = new UserService(currentUser);
            userService.appendShoppingCart(product, qtySpinner.getValue());
            isAdd = true;
            this.parent.close();
        });
        
        cancelButton.setOnAction(ev->{
            this.parent.close();
        });
        
    }

    public void setProduct(ProductsDTO product) {
        this.product = product;
        if (product instanceof ClothingDTO) {
            washableChoiceBox.setVisible(false);
            washableHBox.setVisible(false);
            washableHBox.setManaged(false);
        } else {
            typeHBox.setVisible(false);
            typeHBox.setManaged(false);
            sizeHBox.setVisible(false);
            sizeHBox.setManaged(false);
            typeChoiceBox.setVisible(false);
            sizeChoiceBox.setVisible(false);
        }
        populateField();
        qtySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        product.getStockQty()
                )
        );
        
    }

    public void setParentStage(Stage stage) {
        this.parent = stage;
    }

    private void populateField() {
        if (product.getImagePath() == null) {
            URL resourceUrl = getClass().getClassLoader().getResource("image/unavailable.png");
            if (resourceUrl != null) {
                productImage.setImage(new Image(resourceUrl.toString()));
            } else {
                System.out.println("Default image resource not found.");
            }
        } else {
            System.out.println(product.getImagePath());
            ProductImageManager imageManager = new ProductImageManager();
            imageManager.loadImage(product, productImage);
        }
        sellingPriceTextField.setText(String.format("%.2f", product.getSellingPrice()));
        nameLabel.setText(product.getName());
        stockQtyTextField.setText(String.format("%d", product.getStockQty()));
        if (product instanceof ClothingDTO) {
            ClothingDTO clothing = (ClothingDTO) product;
            typeChoiceBox.getSelectionModel().select(clothing.getClothingType());
            sizeChoiceBox.getSelectionModel().select(clothing.getSize());
        } else {
            AccessoriesDTO accessories = (AccessoriesDTO) product;
            washableChoiceBox.getSelectionModel().select(accessories.isWashable());
        }
    }

    public boolean isAdd() {
        return isAdd;
    }

}
