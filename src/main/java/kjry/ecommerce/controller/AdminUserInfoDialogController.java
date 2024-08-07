package kjry.ecommerce.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.EmployeesDTO;
import kjry.ecommerce.dtos.UsersDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Pair;
import kjry.ecommerce.dtos.ProductsDTO;

public class AdminUserInfoDialogController {

    @FXML
    private TextField birthDateTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ChoiceBox<Character> genderChoiceBox;

    @FXML
    private TextField iDTextField;

    @FXML
    private HBox jobRoleHBox;

    @FXML
    private ChoiceBox<EmployeesDTO.JobRoleDTO> jobRoleChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private CheckComboBox<CustomersDTO.NotificationTypeDTO> notificationTypeCheckComboBox;

    @FXML
    private HBox notificationTypeHBox;

    @FXML
    private HBox notificationTypeHBox1;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField notificationTypeTextField;

    @FXML
    private ListView<String> shoppingCartListView;

    @FXML
    private VBox shoppingCartVBox;

    @FXML
    private Button saveButton;

    @FXML
    private VBox alertParentVBox;

    private UsersDTO user;

    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    @FXML
    public void initialize() {
        notificationTypeCheckComboBox.getItems().setAll(CustomersDTO.NotificationTypeDTO.values());
        jobRoleChoiceBox.getItems().setAll(EmployeesDTO.JobRoleDTO.values());
        genderChoiceBox.getItems().setAll(FXCollections.observableArrayList(new Character('F'), new Character('M')));
        shoppingCartListView.setEditable(false);
    }

    public void setUser(UsersDTO user, boolean viewOnly) {
        this.user = user;

        if (viewOnly) {
            populateFields();
            setEditable(false);
            saveButton.setVisible(false);
            saveButton.setManaged(false);
            notificationTypeHBox1.setVisible(true);
            notificationTypeTextField.setEditable(false);
            notificationTypeHBox.setVisible(false);
            notificationTypeHBox.setManaged(false);
        } else {
            if (user != null) {
                populateFields();
                setEditable(true);
                saveButton.setVisible(true);
                notificationTypeHBox1.setVisible(false);
                notificationTypeHBox1.setManaged(false);
                notificationTypeHBox.setVisible(true);
                shoppingCartVBox.setVisible(false);
                shoppingCartVBox.setManaged(false);
            }
        }
    }

    private void clearFields() {
        iDTextField.clear();
        passwordTextField.clear();
        nameTextField.clear();
        emailTextField.clear();
        phoneTextField.clear();
        genderChoiceBox.getSelectionModel().clearSelection();
        jobRoleChoiceBox.getSelectionModel().clearSelection();
        birthDateTextField.clear();
        shoppingCartListView.getItems().clear();
        notificationTypeCheckComboBox.getCheckModel().clearChecks();
    }

    //TODO fix the bug where the fxml is not loaded when Clicking the Add Button
    private void populateFields() {
        if (user instanceof CustomersDTO) {
            CustomersDTO customer = (CustomersDTO) user;
            jobRoleHBox.setManaged(false);
            jobRoleHBox.setVisible(false);
            jobRoleChoiceBox.setVisible(false);
            notificationTypeHBox1.setVisible(true);
            iDTextField.setText(customer.getId());
            passwordTextField.setText(customer.getPassword());
            nameTextField.setText(customer.getName());
            emailTextField.setText(customer.getEmail());
            phoneTextField.setText(customer.getPhoneNo());
            if (customer.getGender() == 'F') {
                genderChoiceBox.getSelectionModel().select(0);
            } else {
                genderChoiceBox.getSelectionModel().select(1);
            }
            birthDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(customer.getBirthDate()));
            StringBuilder ss = new StringBuilder();
            for (CustomersDTO.NotificationTypeDTO x : customer.getNotificationTypes()) {
                ss.append(x.name());
                ss.append(", ");
            }
            notificationTypeTextField.setText(ss.substring(0, ss.length() - 2));
            notificationTypeCheckComboBox.getCheckModel().check(CustomersDTO.NotificationTypeDTO.APP);
            for (CustomersDTO.NotificationTypeDTO x : customer.getNotificationTypes()) {
                notificationTypeCheckComboBox.getCheckModel().check(x);
            }
            ArrayList<String> shoppingCartList = new ArrayList<>();
            ss.delete(0, ss.length());
            for (Pair<ProductsDTO, Integer> x : customer.getShoppingCart()) {
                ss.append(String.format("ID: %s   Name: %s   Price: %.2f   Qty: %d", x.getKey().getId(), x.getKey().getName(), x.getKey().getSellingPrice(), x.getValue()));
                shoppingCartList.add(ss.toString());
                ss.delete(0, ss.length());
            }
            if (shoppingCartList.isEmpty()) {
                shoppingCartListView.getItems().add("No item.");
            } else {
                for (int i = 0; i < shoppingCartList.size(); i++) {
                    System.out.println(shoppingCartList.get(i));
                    shoppingCartListView.getItems().add(shoppingCartList.get(i));
                }
            }
            birthDateTextField.setPromptText("dd-MM-yyyy");

        } else if (user instanceof EmployeesDTO) {
            EmployeesDTO employee = (EmployeesDTO) user;
            notificationTypeTextField.setVisible(false);
            shoppingCartListView.setVisible(false);
            notificationTypeHBox.setVisible(false);
            notificationTypeHBox.setManaged(false);
            notificationTypeHBox1.setVisible(false);
            notificationTypeHBox1.setManaged(false);
            jobRoleHBox.setVisible(true);
            notificationTypeCheckComboBox.setVisible(false);
            shoppingCartVBox.setVisible(false);
            shoppingCartVBox.setManaged(false);
            iDTextField.setText(employee.getId());
            passwordTextField.setText(employee.getPassword());
            nameTextField.setText(employee.getName());
            emailTextField.setText(employee.getEmail());
            phoneTextField.setText(employee.getPhoneNo());
            if (employee.getGender() == 'F') {
                genderChoiceBox.getSelectionModel().select(0);
            } else {
                genderChoiceBox.getSelectionModel().select(1);
            }
            birthDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(employee.getBirthDate()));
            jobRoleChoiceBox.getSelectionModel().select(employee.getJobRole());
            birthDateTextField.setPromptText("dd-MM-yyyy");
        }
    }

    private void setEditable(boolean editable) {
        iDTextField.setEditable(editable);
        passwordTextField.setEditable(editable);
        nameTextField.setEditable(editable);
        emailTextField.setEditable(editable);
        phoneTextField.setEditable(editable);
        birthDateTextField.setEditable(editable);
        jobRoleChoiceBox.setDisable(!editable);
        genderChoiceBox.setDisable(!editable);
        notificationTypeCheckComboBox.setDisable(!editable);
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        if (user == null) {
            return;
        }

        if (user instanceof CustomersDTO) {
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
        } else if (user instanceof EmployeesDTO) {
            EmployeesDTO employee = (EmployeesDTO) user;
            employee.setId(iDTextField.getText());
            employee.setPassword(passwordTextField.getText());
            employee.setName(nameTextField.getText());
            employee.setEmail(emailTextField.getText());
            employee.setPhoneNo(phoneTextField.getText());
            employee.setGender(genderChoiceBox.getSelectionModel().getSelectedItem());
            try {
                employee.setBirthDate(new SimpleDateFormat("dd-MM-yyyy").parse(birthDateTextField.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            employee.setJobRole(jobRoleChoiceBox.getSelectionModel().getSelectedItem());
        }

        this.parentStage.close();
    }
}
