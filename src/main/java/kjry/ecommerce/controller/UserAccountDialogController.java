package kjry.ecommerce.controller;

import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.services.ValidationUtils;

public class UserAccountDialogController {

    @FXML
    private VBox alertParentVBox;

    @FXML
    private TextField birthDateTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ChoiceBox<Character> genderChoiceBox;

    @FXML
    private TextField iDTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button saveButton;

    private CustomersDTO cust;

    private Stage parentStage;

    private boolean isCreate = false;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    @FXML
    public void initialize() {
        genderChoiceBox.getItems().setAll(FXCollections.observableArrayList(new Character('F'), new Character('M')));
    }

    public void setUser(CustomersDTO user) {
        this.cust = user;
        iDTextField.setText(cust.getId());
        passwordTextField.setText(cust.getPassword());
        nameTextField.setText(cust.getName());
        emailTextField.setText(cust.getEmail());
        phoneTextField.setText(cust.getPhoneNo());
        genderChoiceBox.getSelectionModel().select(cust.getGender() == 'F' ? 0 : 1);
        birthDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(cust.getBirthDate()));
        birthDateTextField.setPromptText("DD-MM-YYYY");
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        validateFields();
    }

    private void validateFields() {
        boolean valid = true;

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
            this.isCreate = true;
            this.parentStage.close();
        }
    }

    private void saveUserData() {
        cust.setId(iDTextField.getText());
        cust.setPassword(passwordTextField.getText());
        cust.setName(nameTextField.getText());
        cust.setEmail(emailTextField.getText());
        cust.setPhoneNo(phoneTextField.getText());
        cust.setGender(genderChoiceBox.getSelectionModel().getSelectedItem());
        try {
            cust.setBirthDate(new SimpleDateFormat("dd-MM-yyyy").parse(birthDateTextField.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCreate() {
        return isCreate;
    }
}
