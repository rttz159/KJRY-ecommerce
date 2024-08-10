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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.CustomersDTO.NotificationTypeDTO;
import kjry.ecommerce.dtos.EmployeesDTO;
import kjry.ecommerce.dtos.EmployeesDTO.JobRoleDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.UserService;

public class AdminUserController implements Initializable {

    @FXML
    private TableView<UsersDTO> adminUserTableview;

    @FXML
    private TableColumn<UsersDTO, Button> actionColumn;

    @FXML
    private TableColumn<UsersDTO, Date> birthDateColumn;

    @FXML
    private TableColumn<UsersDTO, Character> genderColumn;

    @FXML
    private TableColumn<UsersDTO, String> idColumn;

    @FXML
    private TableColumn<UsersDTO, String> nameColumn;

    @FXML
    private TableColumn<UsersDTO, String> userRoleColumn;

    @FXML
    private Button removeButton;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    private ObservableList<UsersDTO> list = FXCollections.observableArrayList(
            new EmployeesDTO("E01", "pass123", "Alice", "alice@example.com", "1234567890", 'F', new Date(90, 0, 1), JobRoleDTO.MANAGER),
            new EmployeesDTO("E02", "pass456", "Bob", "bob@example.com", "2345678901", 'M', new Date(92, 4, 15), JobRoleDTO.MARKETING),
            new EmployeesDTO("E03", "pass789", "Charlie", "charlie@example.com", "3456789012", 'M', new Date(85, 10, 20), JobRoleDTO.STOCK),
            new CustomersDTO("C01", "cust123", "Dave", "dave@example.com", "4567890123", 'M', new Date(95, 6, 7), NotificationTypeDTO.APP),
            new CustomersDTO("C02", "cust456", "Eve", "eve@example.com", "5678901234", 'F', new Date(89, 2, 3), new ArrayList<NotificationTypeDTO>() {
                {
                    add(NotificationTypeDTO.APP);
                    add(NotificationTypeDTO.EMAIL);
                }
            }, new ArrayList<>() {
                {
                    add(new Pair(new ClothingDTO("12", "Smoking", 0.0, 0.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                    add(new Pair(new ClothingDTO("12", "Smoking", 0.0, 0.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                    add(new Pair(new ClothingDTO("12", "Smoking", 0.0, 0.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                    add(new Pair(new ClothingDTO("12", "Smoking", 0.0, 0.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                    add(new Pair(new ClothingDTO("12", "Smoking", 0.0, 0.0, ClothingDTO.SizeDTO.M, ClothingDTO.TypeDTO.SHIRT), 1));
                }
            }
            ),
            new CustomersDTO("C03", "cust789", "Fay", "fay@example.com", "6789012345", 'F', new Date(91, 9, 13), new ArrayList<NotificationTypeDTO>() {
                {
                    add(NotificationTypeDTO.SMS);
                    add(NotificationTypeDTO.APP);
                }
            }, new ArrayList<>()
            ));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(column -> new TableCell<UsersDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toUpperCase());
                }
            }
        });

        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        birthDateColumn.setCellFactory(column -> new TableCell<UsersDTO, Date>() {
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

        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        actionColumn.setCellFactory(column -> new TableCell<UsersDTO, Button>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    UsersDTO user = getTableView().getItems().get(getIndex());
                    System.out.println("Action button clicked for user: " + user.getName());
                    userPromptDialog(user, false);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });

        adminUserTableview.setItems(list);

        editButton.setOnAction(
                event -> {
                    UsersDTO user = adminUserTableview.getSelectionModel().getSelectedItem();
                    try {
                        System.out.println("Edit button clicked for user: " + user.getName());
                        userPromptDialog(user, true);
                        UserService service = new UserService(user);
                        service.updateUser();
                        //list = FXCollections.observableArrayList(UserService.getAllUsers());
                        //adminUserTableview.setItems(list);
                        adminUserTableview.refresh();
                    } catch (NullPointerException x) {
                        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                        warningAlert.setContentText("Please select an user before proceed.");
                        warningAlert.setHeaderText("No User Selected");
                        warningAlert.showAndWait();
                    }
                }
        );

        addButton.setOnAction(event -> {
            final boolean[] buttonClicked = {false};
            final UsersDTO[] user = new UsersDTO[1];
            Stage dialogStage1 = new Stage();
            Button customerBtn = new Button("Customer");
            Button employeeBtn = new Button("Employee");
            VBox vbox = new VBox();
            Label title = new Label("Choose The User Type");
            HBox hbox = new HBox();
            hbox.getChildren().add(customerBtn);
            hbox.getChildren().add(employeeBtn);
            Scene scene = new Scene(vbox);
            scene.getStylesheets().add(getClass().getResource("/views/css/AddButton.css").toExternalForm());
            dialogStage1.setScene(scene);
            dialogStage1.initModality(Modality.APPLICATION_MODAL);
            hbox.setStyle("-fx-spacing: 10px;");
            vbox.getChildren().add(title);
            vbox.setStyle("-fx-alignment: center;-fx-padding: 20px; -fx-background-color: white;-fx-spacing: 10px;");
            vbox.getChildren().add(hbox);
            customerBtn.setOnAction(ev2 -> {
                user[0] = new CustomersDTO();
                buttonClicked[0] = true;
                dialogStage1.close();
            });
            employeeBtn.setOnAction(ev2 -> {
                user[0] = new EmployeesDTO();
                buttonClicked[0] = true;
                dialogStage1.close();
            });

            dialogStage1.showAndWait();

            if (buttonClicked[0]) {
                if (userPromptDialog(user[0], true)) {
                    list.add(user[0]);
                    //UserService.createUser(user[0]);
                    //list = FXCollections.observableArrayList(UserService.getAllUsers());
                    //adminUserTableview.setItems(list);
                    this.adminUserTableview.refresh();
                }
            }
        });

        removeButton.setOnAction(event -> {
            UsersDTO user = adminUserTableview.getSelectionModel().getSelectedItem();
            Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION);
            warningAlert.setHeaderText("User will be PERMANENTLY DELETED.");
            warningAlert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    //UserService service = new UserService(user);
                    //service.deleteUser();
                    //list = FXCollections.observableArrayList(UserService.getAllUsers());
                    //adminUserTableview.setItems(list);
                    list.remove(user);
                    this.adminUserTableview.refresh();
                }
            });
        });

    }

    private boolean userPromptDialog(UsersDTO user, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminUserInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            AdminUserInfoDialogController controller = loader.getController();
            controller.setUser(user, !editable);

            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("User Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
            return controller.isCreate();
        } catch (IOException ex) {
            System.out.println("Error when loading AdminUserInfoAlert.fxml");
            ex.printStackTrace();
            return false;
        }
    }

}
