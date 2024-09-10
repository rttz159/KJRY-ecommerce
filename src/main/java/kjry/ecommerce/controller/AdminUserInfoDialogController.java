package kjry.ecommerce.controller;

import kjry.ecommerce.services.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.EmployeesDTO;
import kjry.ecommerce.dtos.UsersDTO;

import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.UserService;

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

    private boolean viewOnly;

    private boolean isCreate = false;

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
        this.viewOnly = viewOnly;

        if (user.getId() != null) {
            populateFields();
        } else {
            if (user instanceof CustomersDTO) {
                shoppingCartVBox.setVisible(false);
                shoppingCartVBox.setManaged(false);
                shoppingCartListView.setVisible(false);
                notificationTypeHBox1.setVisible(false);
                notificationTypeTextField.setEditable(false);
                notificationTypeHBox1.setManaged(false);
                notificationTypeHBox.setVisible(true);
                notificationTypeHBox.setManaged(true);
                jobRoleHBox.setManaged(false);
                jobRoleHBox.setVisible(false);
                jobRoleChoiceBox.setVisible(false);
                jobRoleChoiceBox.setManaged(false);
                notificationTypeCheckComboBox.getCheckModel().check(CustomersDTO.NotificationTypeDTO.APP);
            } else {
                notificationTypeTextField.setVisible(false);
                shoppingCartListView.setVisible(false);
                notificationTypeHBox.setVisible(false);
                notificationTypeHBox.setManaged(false);
                notificationTypeHBox1.setVisible(false);
                notificationTypeHBox1.setManaged(false);
                shoppingCartVBox.setVisible(false);
                shoppingCartVBox.setManaged(false);
                jobRoleHBox.setManaged(true);
                jobRoleHBox.setVisible(true);
                jobRoleChoiceBox.setVisible(true);
                jobRoleChoiceBox.setManaged(true);
            }
        }

        boolean isEditable = !viewOnly;
        setEditable(isEditable);
        birthDateTextField.setPromptText("DD-MM-YYYY");
        saveButton.setVisible(isEditable);
        saveButton.setManaged(isEditable);
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
        notificationTypeCheckComboBox.getCheckModel().check(CustomersDTO.NotificationTypeDTO.APP);
    }

    private void populateFields() {
        iDTextField.setEditable(false);
        iDTextField.setStyle("-fx-background-color:#c3c3c3;");
        if (user instanceof CustomersDTO) {
            populateCustomerFields((CustomersDTO) user);
        } else if (user instanceof EmployeesDTO) {
            populateEmployeeFields((EmployeesDTO) user);
        }
    }

    private void populateCustomerFields(CustomersDTO customer) {
        jobRoleHBox.setManaged(false);
        jobRoleHBox.setVisible(false);
        jobRoleChoiceBox.setVisible(false);
        jobRoleChoiceBox.setManaged(false);

        if (viewOnly) {
            notificationTypeHBox1.setVisible(true);
            notificationTypeTextField.setEditable(false);
            notificationTypeHBox.setVisible(false);
            notificationTypeHBox.setManaged(false);
        } else {
            notificationTypeHBox1.setVisible(false);
            notificationTypeHBox1.setManaged(false);
            notificationTypeHBox.setVisible(true);
            notificationTypeHBox.setManaged(true);
        }

        iDTextField.setText(customer.getId());
        passwordTextField.setText(customer.getPassword());
        nameTextField.setText(customer.getName());
        emailTextField.setText(customer.getEmail());
        phoneTextField.setText(customer.getPhoneNo());

        genderChoiceBox.getSelectionModel().select(customer.getGender() == 'F' ? 0 : 1);
        birthDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(customer.getBirthDate()));

        updateNotificationTypeFields(customer);
        updateShoppingCartFields(customer);
    }

    private void updateNotificationTypeFields(CustomersDTO customer) {
        notificationTypeCheckComboBox.getItems().setAll(CustomersDTO.NotificationTypeDTO.values());
        notificationTypeCheckComboBox.getCheckModel().clearChecks();

        for (CustomersDTO.NotificationTypeDTO type : customer.getNotificationTypes()) {
            notificationTypeCheckComboBox.getCheckModel().check(type);
        }

        StringBuilder sb = new StringBuilder();
        for (CustomersDTO.NotificationTypeDTO type : customer.getNotificationTypes()) {
            sb.append(type.name()).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        notificationTypeTextField.setText(sb.toString());
    }

    private void updateShoppingCartFields(CustomersDTO customer) {
        shoppingCartListView.getItems().clear();
        if (customer.getShoppingCart().isEmpty()) {
            shoppingCartListView.getItems().add("No item.");
        } else {
            for (Pair<ProductsDTO, Integer> item : customer.getShoppingCart()) {
                ProductsDTO product = item.getKey();
                String text = String.format("ID: %s   Name: %s   Price: %.2f   Qty: %d",
                        product.getId(), product.getName(), product.getSellingPrice(), item.getValue());
                shoppingCartListView.getItems().add(text);
            }
        }
    }

    private void populateEmployeeFields(EmployeesDTO employee) {
        notificationTypeTextField.setVisible(false);
        shoppingCartListView.setVisible(false);
        notificationTypeHBox.setVisible(false);
        notificationTypeHBox.setManaged(false);
        notificationTypeHBox1.setVisible(false);
        notificationTypeHBox1.setManaged(false);
        shoppingCartVBox.setVisible(false);
        shoppingCartVBox.setManaged(false);

        jobRoleHBox.setVisible(true);
        notificationTypeCheckComboBox.setVisible(false);
        shoppingCartVBox.setVisible(false);
        shoppingCartVBox.setManaged(false);

        iDTextField.setText(employee.getId());
        passwordTextField.setText(employee.getPassword());
        nameTextField.setText(employee.getName());
        emailTextField.setText(employee.getEmail());
        phoneTextField.setText(employee.getPhoneNo());

        genderChoiceBox.getSelectionModel().select(employee.getGender() == 'F' ? 0 : 1);
        birthDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(employee.getBirthDate()));

        jobRoleChoiceBox.getItems().setAll(EmployeesDTO.JobRoleDTO.values());
        jobRoleChoiceBox.getSelectionModel().select(employee.getJobRole());
    }

    private void setEditable(boolean editable) {
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
        validateFields();
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

        if (user instanceof EmployeesDTO) {
            boolean jobRoleValid = jobRoleChoiceBox.getSelectionModel().getSelectedItem() != null;
            ValidationUtils.setFieldValidity(jobRoleChoiceBox, jobRoleValid);
            valid &= jobRoleValid;
        }

        if (valid) {
            saveUserData();
            this.isCreate = true;
            this.parentStage.close();
        }
    }

    private void saveUserData() {
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
    }

    public boolean isCreate() {
        return isCreate;
    }

}
