package kjry.ecommerce.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.EmployeesDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.PromoDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.ProductService;
import kjry.ecommerce.services.PromoService;
import kjry.ecommerce.services.UserService;

public class AdminArchivedController implements Initializable {

    @FXML
    private TableView<Object> archivedTableView;

    @FXML
    private TableColumn<Object, String> idTableColumn;

    @FXML
    private TableColumn<Object, String> nameTableColumn;

    @FXML
    private TableColumn<Object, String> typeTableColumn;

    @FXML
    private Button restoreButton;

    private ObservableList<Object> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idTableColumn.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof UsersDTO) {
                return new SimpleStringProperty(((UsersDTO) item).getId());
            } else if (item instanceof ProductsDTO) {
                return new SimpleStringProperty(((ProductsDTO) item).getId());
            } else if (item instanceof PromoDTO) {
                return new SimpleStringProperty(((PromoDTO) item).getId());
            }
            return new SimpleStringProperty("");
        });

        nameTableColumn.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof UsersDTO) {
                return new SimpleStringProperty(((UsersDTO) item).getName());
            } else if (item instanceof ProductsDTO) {
                return new SimpleStringProperty(((ProductsDTO) item).getName());
            } else if (item instanceof PromoDTO) {
                return new SimpleStringProperty(((PromoDTO) item).getCodeName());
            }
            return new SimpleStringProperty("");
        });

        typeTableColumn.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof UsersDTO) {
                return new SimpleStringProperty("Users");
            } else if (item instanceof ProductsDTO) {
                return new SimpleStringProperty("Products");
            } else if (item instanceof PromoDTO) {
                return new SimpleStringProperty("Promotions");
            }
            return new SimpleStringProperty("");
        });

        populateList();
        archivedTableView.setItems(list);

        restoreButton.setOnAction(ev -> {
            Object selectedItem = archivedTableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select an item before proceeding.");
                warningAlert.setHeaderText("No Item Selected");
                warningAlert.showAndWait();
                return;
            }

            try {
                if (selectedItem instanceof UsersDTO) {
                    UsersDTO tempUser = (UsersDTO) selectedItem;
                    if (tempUser instanceof EmployeesDTO) {
                        EmployeesDTO tempEmp = (EmployeesDTO) tempUser;
                        tempEmp.setIsActive(true);
                        UserService userService = new UserService(tempEmp);
                        userService.updateUser();
                    } else if (tempUser instanceof CustomersDTO) {
                        CustomersDTO tempCust = (CustomersDTO) tempUser;
                        tempCust.setIsActive(true);
                        UserService userService = new UserService(tempCust);
                        userService.updateUser();
                    }
                } else if (selectedItem instanceof ProductsDTO) {
                    ProductsDTO tempProd = (ProductsDTO) selectedItem;
                    if (tempProd instanceof AccessoriesDTO) {
                        AccessoriesDTO tempAcc = (AccessoriesDTO) tempProd;
                        tempAcc.setIsActive(true);
                        ProductService.updateProduct(tempAcc);
                    } else if (tempProd instanceof ClothingDTO) {
                        ClothingDTO tempCloth = (ClothingDTO) tempProd;
                        tempCloth.setIsActive(true);
                        ProductService.updateProduct(tempCloth);
                    }
                }else if(selectedItem instanceof PromoDTO){
                    PromoDTO tempPromo = (PromoDTO)selectedItem;
                    tempPromo.setIsActive(true);
                    PromoService.updatePromo(tempPromo);
                }
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setContentText("Restored successfully.");
                infoAlert.showAndWait();
                populateList();
                archivedTableView.setItems(list);
                archivedTableView.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void populateList() {
        list = FXCollections.observableArrayList();
        for (UsersDTO x : UserService.getAllUsers(true)) {
            if (!x.getIsActive()) {
                list.add(x);
            }
        }

        for (ProductsDTO x : ProductService.getAllProducts(true)) {
            if (!x.getIsActive()) {
                list.add(x);
            }
        }
        
        for (PromoDTO x : PromoService.getAllPromo(true)) {
            if (!x.isIsActive()) {
                list.add(x);
            }
        }
    }
}
