package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.OrderService;

public class AdminOrderController implements Initializable {

    @FXML
    private VBox AdminOrderParentVBox;

    @FXML
    private TableView<OrdersDTO> orderTableView;

    @FXML
    private TableColumn<OrdersDTO, Button> actionTableColumn;

    @FXML
    private TableColumn<OrdersDTO, Date> dateTableColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<OrdersDTO, String> orderIdTableColumn;

    @FXML
    private TableColumn<OrdersDTO, OrdersDTO.StatusDTO> statusTableColumn;

    @FXML
    private TableColumn<OrdersDTO, UsersDTO> userIdTableColumn;

    @FXML
    private TableColumn<OrdersDTO, UsersDTO> usernameTableColumn;

    private ObservableList<OrdersDTO> list = FXCollections.observableArrayList(OrderService.getAllOrder());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderingDate"));
        dateTableColumn.setCellFactory(column -> new TableCell<OrdersDTO, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
        userIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        userIdTableColumn.setCellFactory(column -> new TableCell<OrdersDTO, UsersDTO>() {

            @Override
            protected void updateItem(UsersDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId());
                }
            }
        });
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        usernameTableColumn.setCellFactory(column -> new TableCell<OrdersDTO, UsersDTO>() {

            @Override
            protected void updateItem(UsersDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        actionTableColumn.setCellFactory(column -> new TableCell<OrdersDTO, Button>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    OrdersDTO order = getTableView().getItems().get(getIndex());
                    System.out.println("Action button clicked for order: " + order.getId());
                    orderPromptDialog(order, false);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
        orderTableView.setItems(list);

        editButton.setOnAction(ev -> {
            OrdersDTO order = orderTableView.getSelectionModel().getSelectedItem();
            try {
                System.out.println("Edit button clicked for order: " + order.getId());
                if (orderPromptDialog(order, true)) {
                    OrderService.updateOrder(order);
                    list = FXCollections.observableArrayList(OrderService.getAllOrder());
                    orderTableView.setItems(list);
                    orderTableView.refresh();
                }
            } catch (NullPointerException x) {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select an order before proceed.");
                warningAlert.setHeaderText("No Order Selected");
                warningAlert.showAndWait();
            }
        });

    }

    private boolean orderPromptDialog(OrdersDTO order, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminOrderInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            AdminOrderInfoDialogController controller = loader.getController();
            controller.setOrder(order, !editable);

            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Order Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
            return controller.isCreate();
        } catch (IOException ex) {
            System.out.println("Error when loading AdminOrderInfoAlert.fxml");
            ex.printStackTrace();
            return false;
        }
    }

}
