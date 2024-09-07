package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.ProductImageManager;

public class UserProductTilesController{
    @FXML
    private VBox alertParentVBox;

    @FXML
    private Button detailsButton;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView productImage;

    @FXML
    private TextField sellingPriceTextField;

    @FXML
    private TextField stockQtyTextField;
   
    private ProductsDTO product;

    public void setProduct(ProductsDTO product) {
        this.product = product;
        populateField();
        if(product.getStockQty() == 0){
            detailsButton.setDisable(true);
            detailsButton.setText("Unavailable");
        }
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
    }
    
    @FXML
    private void showDetails(ActionEvent event) {
        showProductDetails(product);
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
}
