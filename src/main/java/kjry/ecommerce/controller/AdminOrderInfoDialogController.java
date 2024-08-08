package kjry.ecommerce.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;

public class AdminOrderInfoDialogController implements Initializable {

    @FXML
    private VBox alertParentVBox;

    @FXML
    private ChoiceBox<OrdersDTO.StatusDTO> orderStatusChoiceBox;

    @FXML
    private TextField orderiDTextField;

    @FXML
    private TextField orderingDateTextField;

    @FXML
    private ListView<String> productsListView;

    @FXML
    private Button saveButton;

    @FXML
    private TextField userIDTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextArea addressTextField;

    private OrdersDTO order;
    private boolean viewOnly;
    private Stage parent;

    public void setOrder(OrdersDTO order, boolean viewOnly) {
        this.order = order;
        this.viewOnly = viewOnly;
        if (viewOnly) {
            orderStatusChoiceBox.setDisable(viewOnly);
            addressTextField.setEditable(!viewOnly);
            saveButton.setVisible(!viewOnly);
            saveButton.setManaged(!viewOnly);
        }
        populateField();
    }

    public void setParentStage(Stage stage) {
        this.parent = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderiDTextField.setEditable(false);
        orderingDateTextField.setEditable(false);
        userIDTextField.setEditable(false);
        usernameTextField.setEditable(false);
        orderStatusChoiceBox.getItems().setAll(OrdersDTO.StatusDTO.values());
    }

    private void populateField() {
        orderiDTextField.setText(order.getId());
        orderingDateTextField.setText(new SimpleDateFormat("dd-MM-yyyy").format(order.getOrderingDate()));
        userIDTextField.setText(order.getUser().getId());
        usernameTextField.setText(order.getUser().getName());
        orderStatusChoiceBox.getSelectionModel().select(order.getStatus());
        addressTextField.setText(order.getAddress());
        String[] products = new String[order.getProductLists().size()];
        int i = 0;
        for (Pair<ProductsDTO, Integer> x : order.getProductLists()) {
            products[i] = String.format("id: %s, name: %s, qty: %d", x.getKey().getId(), x.getKey().getName(), x.getValue());
            i++;
        }
        productsListView.getItems().setAll(products);
    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) {
        validateFields();   
    }

    private void validateFields() {
        boolean valid = true;

        boolean addressValid = ValidationUtils.isNotEmpty(addressTextField.getText());
                System.out.println(addressValid);
        ValidationUtils.setFieldValidity(addressTextField, addressValid);
        valid &= addressValid;
        System.out.println(valid);

        if(valid){
            saveOrder();
            this.parent.close();
        }
    }

    private void saveOrder() {
        order.setStatus(orderStatusChoiceBox.getSelectionModel().getSelectedItem());
        order.setAddress(addressTextField.getText());
    }

}
