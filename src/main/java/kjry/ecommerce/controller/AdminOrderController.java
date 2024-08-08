package kjry.ecommerce.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javafx.util.Pair;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
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
    private Button removeButton;
    
    @FXML
    private TableColumn<OrdersDTO, OrdersDTO.StatusDTO> statusTableColumn;
    
    @FXML
    private TableColumn<OrdersDTO, UsersDTO> userIdTableColumn;
    
    @FXML
    private TableColumn<OrdersDTO, UsersDTO> usernameTableColumn;
    
    private ObservableList<OrdersDTO> list = FXCollections.observableArrayList(
            new OrdersDTO("1", "123 Main St", new CustomersDTO("C01", "cust123", "Dave", "dave@example.com", "4567890123", 'M', new Date(95, 6, 7), new ArrayList<>(), new ArrayList<>()), OrdersDTO.StatusDTO.PENDING, new ArrayList<Pair<ProductsDTO, Integer>>() {
                {
                    add(new Pair(new ClothingDTO("1", "Shirt A", 10.0, 20.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 2));
                    add(new Pair(new ClothingDTO("2", "Pants B", 15.0, 30.0, ClothingDTO.SizeDTO.L, ClothingDTO.TypeDTO.PANTS), 1));
                    add(new Pair(new ClothingDTO("2", "Pants B", 15.0, 30.0, ClothingDTO.SizeDTO.L, ClothingDTO.TypeDTO.PANTS), 1));
                    add(new Pair(new ClothingDTO("2", "Pants B", 15.0, 30.0, ClothingDTO.SizeDTO.L, ClothingDTO.TypeDTO.PANTS), 1));
                }
            }, new Date()),
            new OrdersDTO("2", "456 Oak St", new CustomersDTO("C02", "cust456", "Eve", "eve@example.com", "5678901234", 'F', new Date(89, 2, 3), new ArrayList<>() {
                {
                    add(CustomersDTO.NotificationTypeDTO.EMAIL);
                    add(CustomersDTO.NotificationTypeDTO.APP);
                }
            }, new ArrayList<>() {
                {
                    add(new Pair(new ClothingDTO("3", "Skirt C", 12.0, 25.0, ClothingDTO.SizeDTO.S, ClothingDTO.TypeDTO.SKIRT), 1));
                }
            }), OrdersDTO.StatusDTO.DONE, new ArrayList<Pair<ProductsDTO, Integer>>() {
                {
                    add(new Pair(new ClothingDTO("4", "Shirt D", 10.0, 20.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 3));
                }
            }, new Date()),
            new OrdersDTO("3", "789 Pine St", new CustomersDTO("C03", "cust789", "Fay", "fay@example.com", "6789012345", 'F', new Date(91, 9, 13), new ArrayList<>() {
                {
                    add(CustomersDTO.NotificationTypeDTO.SMS);
                    add(CustomersDTO.NotificationTypeDTO.APP);
                }
            }, new ArrayList<>() {
                {
                    add(new Pair(new ClothingDTO("5", "Shirt E", 11.0, 22.0, ClothingDTO.SizeDTO.L, ClothingDTO.TypeDTO.SHIRT), 2));
                }
            }), OrdersDTO.StatusDTO.CANCELLED, new ArrayList<Pair<ProductsDTO, Integer>>() {
                {
                    add(new Pair(new ClothingDTO("6", "Pants F", 16.0, 32.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.PANTS), 1));
                }
            }, new Date()),
            new OrdersDTO("4", "321 Elm St", new CustomersDTO("C04", "cust012", "George", "george@example.com", "7890123456", 'M', new Date(90, 1, 25), new ArrayList<>() {
                {
                    add(CustomersDTO.NotificationTypeDTO.APP);
                }
            }, new ArrayList<>() {
                {
                    add(new Pair(new ClothingDTO("7", "Skirt G", 13.0, 27.0, ClothingDTO.SizeDTO.S, ClothingDTO.TypeDTO.SKIRT), 1));
                }
            }), OrdersDTO.StatusDTO.PROCESSING, new ArrayList<Pair<ProductsDTO, Integer>>() {
                {
                    add(new Pair(new ClothingDTO("8", "Shirt H", 14.0, 28.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                }
            }, new Date()),
            new OrdersDTO("5", "654 Maple St", new CustomersDTO("C05", "cust345", "Hannah", "hannah@example.com", "8901234567", 'F', new Date(88, 11, 12), new ArrayList<>() {
                {
                    add(CustomersDTO.NotificationTypeDTO.SMS);
                    add(CustomersDTO.NotificationTypeDTO.EMAIL);
                }
            }, new ArrayList<>() {
                {
                    add(new Pair(new ClothingDTO("9", "Pants I", 17.0, 34.0, ClothingDTO.SizeDTO.L, ClothingDTO.TypeDTO.PANTS), 2));
                }
            }), OrdersDTO.StatusDTO.PENDING, new ArrayList<Pair<ProductsDTO, Integer>>() {
                {
                    add(new Pair(new ClothingDTO("10", "Skirt J", 18.0, 36.0, ClothingDTO.SizeDTO.S, ClothingDTO.TypeDTO.SKIRT), 1));
                }
            }, new Date())
    );
    
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
        
        editButton.setOnAction(ev->{
            OrdersDTO order = orderTableView.getSelectionModel().getSelectedItem();
                    try {
                        System.out.println("Edit button clicked for order: " + order.getId());
                        orderPromptDialog(order, true);
                        //OrderService.updateOrder(order);
                        //list = FXCollections.observableArrayList(OrderService.getAllOrder());
                        orderTableView.refresh();
                    } catch (NullPointerException x) {
                        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                        warningAlert.setContentText("Please select an order before proceed.");
                        warningAlert.setHeaderText("No Order Selected");
                        warningAlert.showAndWait();
                    }
        });
        
    }
    
    private void orderPromptDialog(OrdersDTO order, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminOrderInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            AdminOrderInfoDialogController controller = loader.getController();
            controller.setOrder(order, !editable);

            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("User Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
        } catch (IOException ex) {
            System.out.println("Error when loading AdminOrderInfoAlert.fxml");
            ex.printStackTrace();
        }
    }
    
}
