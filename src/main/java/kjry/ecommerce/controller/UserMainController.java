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
    private Button settingsButton;

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
    }

    private void clearContentVBox() {
        if (parentHBox.getChildren().size() == 2) {
            parentHBox.getChildren().remove(1);
            parentHBox.layout();
        }
    }

}
