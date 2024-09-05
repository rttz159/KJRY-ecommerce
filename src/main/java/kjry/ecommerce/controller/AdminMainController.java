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
import javafx.stage.Stage;
import kjry.ecommerce.App;

public class AdminMainController implements Initializable {

    @FXML
    private Label adminLabel;

    @FXML
    private Button ordersButton;

    @FXML
    private Button productsButton;

    @FXML
    private Button usersButton;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button archivedButton;
    
    @FXML
    private Button promoButton;
    
    @FXML
    private Button logoutButton;

    @FXML
    private HBox parentHBox;

    @FXML
    private VBox sidePanelParentVBox;

    @FXML
    private VBox sidePanelVBox;

    private VBox content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            clearContentVBox();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashBoard.fxml"));
            content = loader.load();
            parentHBox.getChildren().add(content);
        } catch (IOException ex) {
            System.out.println("Error occurs when loading the fxml file");
        }

        usersButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminUsers.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        ordersButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminOrders.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        productsButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminProducts.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        dashBoardButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashBoard.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });

        archivedButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminArchived.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });
        
        promoButton.setOnAction(ev -> {
            try {
                clearContentVBox();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminPromo.fxml"));
                content = loader.load();
                parentHBox.getChildren().add(content);
            } catch (IOException ex) {
                System.out.println("Error occurs when loading the fxml file");
            }
        });
        
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

    }

    private void clearContentVBox() {
        if (parentHBox.getChildren().size() == 2) {
            parentHBox.getChildren().remove(1);
            parentHBox.layout();
        }
    }

}
