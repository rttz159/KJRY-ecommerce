package kjry.ecommerce.controller;

import kjry.ecommerce.services.ValidationUtils;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.ProductImageManager;
import kjry.ecommerce.services.ProductService;

public class AdminProductInfoDialogController implements Initializable {

    @FXML
    private VBox alertParentVBox;

    @FXML
    private Button chooseButton;

    @FXML
    private TextField costPriceTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private ImageView productImage;

    @FXML
    private Button saveButton;

    @FXML
    private TextField sellingPriceTextField;

    @FXML
    private TextField stockQtyTextField;

    @FXML
    private ChoiceBox<ClothingDTO.TypeDTO> typeChoiceBox;

    @FXML
    private ChoiceBox<ClothingDTO.SizeDTO> sizeChoiceBox;

    @FXML
    private HBox typeHBox;

    @FXML
    private HBox sizeHBox;

    @FXML
    private HBox washableHBox;

    @FXML
    private ChoiceBox<Boolean> washableChoiceBox;

    private ProductsDTO product;
    private boolean viewOnly;
    private Stage parent;
    private Path tempImagePath;
    private boolean isCreate = false;

    @FXML
    void handleSaveButtonAction(ActionEvent event) {
        validateFields();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeChoiceBox.getItems().addAll(ClothingDTO.TypeDTO.values());
        sizeChoiceBox.getItems().addAll(ClothingDTO.SizeDTO.values());
        washableChoiceBox.getItems().addAll(true, false);

        chooseButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose an image");
            fc.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File selectedFile = fc.showOpenDialog(parent);

            if (selectedFile != null) {
                URI fileUri = selectedFile.toURI();
                Path filePath = Paths.get(fileUri);
                productImage.setImage(new Image(selectedFile.toURI().toString()));
                tempImagePath = filePath.toAbsolutePath();
                System.out.println("Selected file: " + tempImagePath.toString());
            } else {
                System.out.println("File selection was canceled.");
            }

        });
    }

    public void setProduct(ProductsDTO product, boolean viewOnly) {
        this.product = product;
        this.viewOnly = viewOnly;
        if (viewOnly) {
            washableChoiceBox.setDisable(viewOnly);
            sizeChoiceBox.setDisable(viewOnly);
            typeChoiceBox.setDisable(viewOnly);
            chooseButton.setDisable(viewOnly);
            idTextField.setEditable(!viewOnly);
            descriptionTextField.setEditable(!viewOnly);
            stockQtyTextField.setEditable(!viewOnly);
            sellingPriceTextField.setEditable(!viewOnly);
            costPriceTextField.setEditable(!viewOnly);
            saveButton.setVisible(!viewOnly);
            saveButton.setManaged(!viewOnly);
        }
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
        if (product.getId() != null) {
            populateField();
        }
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
        costPriceTextField.setText(String.format("%.2f", product.getCostPrice()));
        sellingPriceTextField.setText(String.format("%.2f", product.getSellingPrice()));
        descriptionTextField.setText(product.getName());
        idTextField.setText(product.getId());
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

    private void validateFields() {
        boolean valid = true;

        boolean costPriceValid = ValidationUtils.isValidDouble(costPriceTextField.getText());
        ValidationUtils.setFieldValidity(costPriceTextField, costPriceValid);
        valid &= costPriceValid;

        boolean sellingPriceValid = ValidationUtils.isValidDouble(sellingPriceTextField.getText());
        ValidationUtils.setFieldValidity(sellingPriceTextField, sellingPriceValid);
        valid &= sellingPriceValid;

        boolean descriptionValid = ValidationUtils.isNotEmpty(descriptionTextField.getText());
        ValidationUtils.setFieldValidity(descriptionTextField, descriptionValid);
        valid &= descriptionValid;

        String[] ids = new String[ProductService.getAllProducts(false).length];
        int i = 0;
        for (ProductsDTO x : ProductService.getAllProducts(true)) {
            ids[i] = x.getId();
            i++;
        }

        for (int j = 0; j < ids.length; j++) {
            if (ids[j].equals(idTextField.getText())) {
                ids[j] = "-1";
                break;
            }
        }

        boolean idValid = ValidationUtils.isUnqiue(idTextField.getText(), ids);
        ValidationUtils.setFieldValidity(idTextField, idValid);
        valid &= idValid;

        boolean stockQtyValid = ValidationUtils.isValidPositiveInteger(stockQtyTextField.getText());
        ValidationUtils.setFieldValidity(stockQtyTextField, stockQtyValid);
        valid &= stockQtyValid;

        if (product instanceof ClothingDTO) {
            boolean typeValid = ValidationUtils.isValidNull(typeChoiceBox.getSelectionModel().getSelectedItem());
            ValidationUtils.setFieldValidity(typeChoiceBox, typeValid);
            valid &= typeValid;

            boolean sizeValid = ValidationUtils.isValidNull(sizeChoiceBox.getSelectionModel().getSelectedItem());
            ValidationUtils.setFieldValidity(sizeChoiceBox, sizeValid);
            valid &= sizeValid;

        } else {
            boolean washableValid = ValidationUtils.isValidNull(washableChoiceBox.getSelectionModel().getSelectedItem());
            ValidationUtils.setFieldValidity(washableChoiceBox, washableValid);
            valid &= washableValid;
        }

        if (valid) {
            saveOrder();
            this.isCreate = true;
            this.parent.close();
        }
    }

    private void saveOrder() {
        if (tempImagePath != null) {
            ProductImageManager imageManager = new ProductImageManager();
            imageManager.saveProductImage(tempImagePath, product);
        }
        product.setCostPrice(Double.parseDouble(costPriceTextField.getText()));
        product.setSellingPrice(Double.parseDouble(sellingPriceTextField.getText()));
        product.setId(idTextField.getText());
        product.setName(descriptionTextField.getText());
        product.setStockQty(Integer.parseInt(stockQtyTextField.getText()));
        if (product instanceof ClothingDTO) {
            ClothingDTO clothing = (ClothingDTO) product;
            clothing.setClothingType(typeChoiceBox.getSelectionModel().getSelectedItem());
            clothing.setSize(sizeChoiceBox.getSelectionModel().getSelectedItem());
        } else {
            AccessoriesDTO accessories = (AccessoriesDTO) product;
            accessories.setWashable(washableChoiceBox.getSelectionModel().getSelectedItem());
        }
    }

    public boolean isCreate() {
        return isCreate;
    }

}
