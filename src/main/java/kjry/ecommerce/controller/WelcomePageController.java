package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.UserService;
import kjry.ecommerce.services.ValidationUtils;

public class WelcomePageController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usernameTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginBtn.setOnAction(ev -> {
            boolean valid = true;

            boolean usernameValid = ValidationUtils.isNotEmpty(usernameTextField.getText());
            ValidationUtils.setFieldValidity(usernameTextField, usernameValid);
            valid &= usernameValid;

            boolean passwordValid = ValidationUtils.isValidPassword(passwordField.getText());
            ValidationUtils.setFieldValidity(passwordField, passwordValid);
            valid &= passwordValid;

            if (valid) {
                UsersDTO tempUser = UserService.getUser(usernameTextField.getText());
                boolean validUser = false;
                if (tempUser != null) {
                    String password = passwordField.getText();
                    if (password.equals(tempUser.getPassword())) {
                        Stage primaryStage = App.getPrimaryStage();
                        App.setCurrentUserId(tempUser.getId());
                        validUser = true;
                        if (tempUser instanceof CustomersDTO) {
                            try {
                                Parent root = new FXMLLoader(getClass().getResource("/views/UserMain.fxml")).load();
                                Scene scene = new Scene(root);
                                primaryStage.setScene(scene);
                                primaryStage.setTitle("KJRY ECOMMERCE");
                                primaryStage.setResizable(false);
                                primaryStage.centerOnScreen();
                                primaryStage.show();
                            } catch (IOException ex) {
                                System.out.println("There are exceptions when loading the User Main Page.");
                            }
                        } else {
                            try {
                                Parent root = new FXMLLoader(getClass().getResource("/views/AdminMain.fxml")).load();
                                Scene scene = new Scene(root);
                                primaryStage.setScene(scene);
                                primaryStage.setTitle("KJRY ECOMMERCE");
                                primaryStage.setResizable(false);
                                primaryStage.centerOnScreen();
                                primaryStage.show();
                            } catch (IOException ex) {
                                System.out.println("There are exceptions when loading the Admin Main Page.");
                            }
                        }
                    }
                }
                if (!validUser) {
                    Alert informAlert = new Alert(Alert.AlertType.INFORMATION);
                    informAlert.setContentText("Invalid username and password. Please try again or contact the Admins.");
                    informAlert.setHeaderText("Invalid Password or User");
                    informAlert.showAndWait();
                }
            } else {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please fill in valid credentials. Please try again.");
                warningAlert.setHeaderText("Invalid Password or User");
                warningAlert.showAndWait();
            }

        });

        signUpBtn.setOnAction(ev -> {
            Stage primaryStage = App.getPrimaryStage();
            try {
                Parent root = new FXMLLoader(getClass().getResource("/views/SignUpPage.fxml")).load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("KJRY ECOMMERCE");
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();
            } catch (IOException ex) {
                System.out.println("There are exceptions when loading the Sign Up Page.");
            }
        });
    }

}
