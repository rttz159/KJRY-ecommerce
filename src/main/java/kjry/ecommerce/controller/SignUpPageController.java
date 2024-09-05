package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.UserService;
import kjry.ecommerce.services.ValidationUtils;
import org.controlsfx.control.CheckComboBox;

public class SignUpPageController implements Initializable {

    @FXML
    private TextField birthDateTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private ChoiceBox<Character> genderChoiceBox;

    @FXML
    private TextField iDTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private CheckComboBox<CustomersDTO.NotificationTypeDTO> notificationTypeCheckComboBox;

    @FXML
    private HBox notificationTypeHBox;

    @FXML
    private VBox parentVbox;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Label subtitle_label;

    @FXML
    private Label title_label;

    private UsersDTO user = new CustomersDTO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notificationTypeCheckComboBox.getItems().setAll(CustomersDTO.NotificationTypeDTO.values());
        notificationTypeCheckComboBox.getCheckModel().check(CustomersDTO.NotificationTypeDTO.APP);
        genderChoiceBox.getItems().setAll(FXCollections.observableArrayList(new Character('F'), new Character('M')));
        cancelButton.setOnAction(ev -> {
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
        saveButton.setOnAction(ev -> {
            validateFields();
        });
    }

    private void validateFields() {
        boolean valid = true;

        String[] ids = new String[UserService.getAllUsers(false).length];
        int i = 0;
        for (UsersDTO x : UserService.getAllUsers(true)) {
            ids[i] = x.getId();
            i++;
        }

        if (user.getId() != null) {
            for (int j = 0; j < ids.length; j++) {
                if (ids[j].equals(iDTextField.getText())) {
                    ids[j] = "-1";
                    break;
                }
            }
        }

        boolean idValid = ValidationUtils.isUnqiue(iDTextField.getText(), ids);
        ValidationUtils.setFieldValidity(iDTextField, idValid);
        valid &= idValid;

        boolean nameValid = ValidationUtils.isNotEmpty(nameTextField.getText());
        ValidationUtils.setFieldValidity(nameTextField, nameValid);
        valid &= nameValid;

        boolean passwordValid = ValidationUtils.isValidPassword(passwordTextField.getText());
        ValidationUtils.setFieldValidity(passwordTextField, passwordValid);
        valid &= passwordValid;

        boolean emailValid = ValidationUtils.isValidEmail(emailTextField.getText());
        ValidationUtils.setFieldValidity(emailTextField, emailValid);
        valid &= emailValid;

        boolean phoneValid = ValidationUtils.isValidPhone(phoneTextField.getText());
        ValidationUtils.setFieldValidity(phoneTextField, phoneValid);
        valid &= phoneValid;

        boolean dateValid = ValidationUtils.isValidDate(birthDateTextField.getText());
        ValidationUtils.setFieldValidity(birthDateTextField, dateValid);
        valid &= dateValid;

        boolean genderValid = genderChoiceBox.getSelectionModel().getSelectedItem() != null;
        ValidationUtils.setFieldValidity(genderChoiceBox, genderValid);
        valid &= genderValid;

        if (valid) {
            saveUserData();
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
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setContentText("Please fill in valid credentials. Please try again.");
            warningAlert.setHeaderText("Invalid Fields");
            warningAlert.showAndWait();
        }

    }

    private void saveUserData() {
        CustomersDTO customer = (CustomersDTO) user;
        customer.setId(iDTextField.getText());
        customer.setPassword(passwordTextField.getText());
        customer.setName(nameTextField.getText());
        customer.setEmail(emailTextField.getText());
        customer.setPhoneNo(phoneTextField.getText());
        customer.setGender(genderChoiceBox.getSelectionModel().getSelectedItem());
        try {
            customer.setBirthDate(new SimpleDateFormat("dd-MM-yyyy").parse(birthDateTextField.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        customer.getNotificationTypes().clear();
        customer.getNotificationTypes().addAll(notificationTypeCheckComboBox.getCheckModel().getCheckedItems());
        customer.getShoppingCart().clear();

        UserService.createUser(user);
    }

}
