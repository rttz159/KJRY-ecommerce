package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.services.UserService;

public class UserMainController implements Initializable {

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button ordersHistoryButton;

    @FXML
    private HBox parentHBox;

    @FXML
    private Button productsButton;

    @FXML
    private Button accountButton;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox sidePanelParentVBox;

    @FXML
    private VBox sidePanelVBox;

    @FXML
    private Label userLabel;

    private VBox content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoutButton.setOnAction(ev -> {
            Stage primaryStage = App.getPrimaryStage();
            try {
                Parent root = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml")).load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("KJRY ECOMMERCE");
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();
            } catch (IOException ex) {
                System.out.println("There are exceptions when loading the Welcome Page.");
            }
        });

        try {
            clearContentVBox();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashBoard.fxml"));
            content = loader.load();
            parentHBox.getChildren().add(content);
        } catch (IOException ex) {
            System.out.println("Error occurs when loading the fxml file");
        }

        dashBoardButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashBoard.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        productsButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserProducts.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        shoppingCartButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserShoppingCart.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        ordersHistoryButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserOrderHistory.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        accountButton.setOnAction(ev -> {
            try {
                CustomersDTO tempCust = (CustomersDTO)UserService.getUser(App.getCurrentUserId());
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserAccountAlert.fxml"));
                    VBox dialogContent = loader.load();

                    UserAccountDialogController controller = loader.getController();
                    controller.setUser(tempCust);

                    Stage dialogStage = new Stage();
                    controller.setParentStage(dialogStage);
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setTitle("Account Information");
                    dialogStage.setResizable(false);
                    dialogStage.setScene(new Scene(dialogContent));
                    dialogStage.showAndWait();
                } catch (IOException ex) {
                    System.out.println("Error when loading UserAccountAlert.fxml");
                    ex.printStackTrace();
                }
                UserService userservice = new UserService(tempCust);
                userservice.updateUser();
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashBoard.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });
    }

    private void clearContentVBox() {
        if (parentHBox.getChildren().size() == 2) {
            parentHBox.getChildren().remove(1);
            parentHBox.layout();
        }
    }

}
