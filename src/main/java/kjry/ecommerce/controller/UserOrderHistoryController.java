package kjry.ecommerce.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.services.OrderService;

public class UserOrderHistoryController implements Initializable {

    @FXML
    private VBox UserOrderParentVBox;

    @FXML
    private TableColumn<OrdersDTO, Date> dateTableColumn;

    @FXML
    private TableColumn<OrdersDTO, String> orderIdTableColumn;

    @FXML
    private TableColumn<OrdersDTO, String> amountTableColumn;

    @FXML
    private TableView<OrdersDTO> orderTableView;

    @FXML
    private TableColumn<OrdersDTO, OrdersDTO.StatusDTO> statusTableColumn;

    private ObservableList<OrdersDTO> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (OrdersDTO x : OrderService.getAllOrder(true)) {
            if (x.getUser().getId().equals(App.getCurrentUserId())) {
                list.add(x);
            }
        }

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

        amountTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrdersDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrdersDTO, String> param) {
                return new SimpleObjectProperty<>(String.format("%.2f", OrderService.calculateBill(param.getValue())));
            }
        });
        orderTableView.setItems(list);
    }

}
