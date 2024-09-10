package kjry.ecommerce.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kjry.ecommerce.App;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.PromoDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.OrderService;
import kjry.ecommerce.services.ProductService;
import kjry.ecommerce.services.PromoService;
import kjry.ecommerce.services.UserService;

public class UserShoppingCartController implements Initializable {

    @FXML
    private VBox AdminProductParentVBox;

    @FXML
    private HBox buttonHBox;

    @FXML
    private Button editButton;

    @FXML
    private Button checkoutButton;

    @FXML
    private TableColumn<ShoppingCartItem, String> nameTableColumn;

    @FXML
    private TableView<ShoppingCartItem> productsTableView;

    @FXML
    private TableColumn<ShoppingCartItem, Integer> qtyTableColumn;

    @FXML
    private Button removeButton;

    @FXML
    private TableColumn<ShoppingCartItem, Double> sellingPriceTableColumn;

    @FXML
    private TableColumn<ShoppingCartItem, String> typeTableColumn;

    private ObservableList<ShoppingCartItem> list = FXCollections.observableArrayList();

    private String id = App.getCurrentUserId();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sellingPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyTableColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        list = FXCollections.observableArrayList();
        for (Pair<ProductsDTO, Integer> x : ((CustomersDTO) UserService.getUser(id)).getShoppingCart()) {
            list.add(new ShoppingCartItem(x));
        }

        removeButton.setOnAction(ev -> {

            ShoppingCartItem tempitem = productsTableView.getSelectionModel().getSelectedItem();
            if (tempitem != null) {
                CustomersDTO tempCust = ((CustomersDTO) UserService.getUser(id));
                Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION);
                warningAlert.setHeaderText("Item will be removed.");
                warningAlert.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        UserService userService = new UserService(tempCust);
                        ProductsDTO tempProd = null;
                        for (ProductsDTO x : ProductService.getAllProducts(false)) {
                            if (x.getId().equals(tempitem.getId())) {
                                tempProd = x;
                            }
                        }
                        userService.removeProductInShoppingCart(tempProd);
                        list = FXCollections.observableArrayList();
                        for (Pair<ProductsDTO, Integer> x : ((CustomersDTO) UserService.getUser(id)).getShoppingCart()) {
                            list.add(new ShoppingCartItem(x));
                        }
                        productsTableView.setItems(list);
                        productsTableView.refresh();
                    }
                });
            } else {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select an item before proceed.");
                warningAlert.setHeaderText("No Item Selected");
                warningAlert.showAndWait();
            }
        }
        );

        editButton.setOnAction(ev -> {
            ShoppingCartItem tempitem = productsTableView.getSelectionModel().getSelectedItem();
            if (tempitem != null) {
                final ProductsDTO[] tempProdWrapper = {null};

                for (ProductsDTO dto : ProductService.getAllProducts(false)) {
                    if (dto.getId().equals(tempitem.id)) {
                        tempProdWrapper[0] = dto;
                        break;
                    }
                }
                CustomersDTO tempCust = ((CustomersDTO) UserService.getUser(id));
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Input Required");
                dialog.setHeaderText("Please enter the value:");
                dialog.setContentText("Value:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(value -> {
                    try {
                        int number = Integer.parseInt(value);
                        if (number < 1 || number > tempProdWrapper[0].getStockQty()) {
                            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                            warningAlert.setContentText(String.format("Please enter valid range (%d - %d).", 1, tempProdWrapper[0].getStockQty()));
                            warningAlert.showAndWait();
                        } else {
                            UsersDTO currentUser = UserService.getUser(App.getCurrentUserId());
                            UserService userService = new UserService(currentUser);
                            ProductsDTO tempProd = null;
                            for (ProductsDTO x : ProductService.getAllProducts(false)) {
                                if (x.getId().equals(tempitem.getId())) {
                                    tempProd = x;
                                }
                            }
                            userService.removeProductInShoppingCart(tempProd);
                            userService.appendShoppingCart(tempProd, number);
                            list = FXCollections.observableArrayList();
                            for (Pair<ProductsDTO, Integer> x : ((CustomersDTO) UserService.getUser(id)).getShoppingCart()) {
                                list.add(new ShoppingCartItem(x));
                            }
                            productsTableView.setItems(list);
                            productsTableView.refresh();
                        }
                    } catch (NumberFormatException e) {
                        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                        warningAlert.setContentText("Please enter integer only.");
                        warningAlert.showAndWait();
                    }
                });
            } else {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select an item before proceed.");
                warningAlert.setHeaderText("No Item Selected");
                warningAlert.showAndWait();
            }
        });

        checkoutButton.setOnAction(ev -> {
            if (!list.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Payment Method");
                alert.setHeaderText("Choose your payment method");
                alert.setContentText("Please select one of the following options and enter your address and promo code(Optional):");

                ButtonType cardPaymentButton = new ButtonType("Card Payment", ButtonBar.ButtonData.OK_DONE);
                ButtonType onlineBankingButton = new ButtonType("Online Banking", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField addressField = new TextField();
                addressField.setPromptText("Enter your address");
                Label addressLabel = new Label("Address:");

                TextField promoCodeField = new TextField();
                promoCodeField.setPromptText("Enter promo code");
                Label promoCodeLabel = new Label("Promo Code:");

                grid.add(addressLabel, 0, 0);
                grid.add(addressField, 1, 0);

                grid.add(promoCodeLabel, 0, 1);
                grid.add(promoCodeField, 1, 1);

                alert.getDialogPane().setContent(grid);

                alert.getButtonTypes().setAll(cardPaymentButton, onlineBankingButton, cancelButton);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    CustomersDTO tempCust = (CustomersDTO) UserService.getUser(App.getCurrentUserId());

                    String enteredAddress = addressField.getText();
                    String enteredPromoCode = promoCodeField.getText();
                    OrdersDTO tempOrder;
                    if (result.get() != cancelButton) {
                        if (!enteredAddress.isBlank()) {
                            String method;
                            if (result.get() == cardPaymentButton) {
                                method = "Card";
                            } else {
                                method = "Bank";
                            }
                            System.out.println(tempCust.getId());
                            PromoDTO tempPromo = null;
                            tempOrder = new OrdersDTO();
                            for (PromoDTO x : PromoService.getAllPromo(false)) {
                                if (enteredPromoCode.equals(x.getCodeName())) {
                                    if (x.isValid()) {
                                        tempPromo = x;
                                    }
                                    break;
                                }
                            }
                            if (tempPromo == null) {
                                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                infoAlert.setContentText("The promo code is invalid. The payment will be proceeded without discount.");
                                infoAlert.showAndWait();
                            }
                            tempOrder.setPromo(tempPromo);
                            tempOrder.setStatus(OrdersDTO.StatusDTO.PROCESSING);
                            tempOrder.setAddress(enteredAddress);
                            LocalDate localDate = LocalDate.now();
                            tempOrder.setOrderingDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            tempOrder.setUser(tempCust);
                            tempOrder.setProductLists(tempCust.getShoppingCart());
                            if (OrderService.makePayment(tempOrder, method)) {
                                tempCust.setShoppingCart(new ArrayList<>());
                                UserService userService = new UserService(tempCust);
                                userService.updateUser();
                                OrderService.createOrder(tempOrder);
                                list = FXCollections.observableArrayList();
                                for (Pair<ProductsDTO, Integer> x : ((CustomersDTO) UserService.getUser(id)).getShoppingCart()) {
                                    list.add(new ShoppingCartItem(x));
                                }
                                productsTableView.setItems(list);
                                productsTableView.refresh();
                                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                infoAlert.setContentText("The payment is success. Sending your orders.");
                                infoAlert.showAndWait();
                            } else {
                                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                infoAlert.setContentText("The payment failed or cancelled. Nothing will change, please contact admin if the problem persist.");
                                infoAlert.showAndWait();
                            }
                        } else {
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                            infoAlert.setContentText("The payment failed. Nothing will change, please enter a valid address.");
                            infoAlert.showAndWait();
                        }
                    } else {
                        System.out.println("Payment canceled.");
                    }
                }
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setContentText("Your shopping cart is empty, please add something.");
                infoAlert.showAndWait();
            }
        });

        productsTableView.setItems(list);
        Label unfoundProduct = new Label("No item in cart.");
        productsTableView.setPlaceholder(unfoundProduct);
        productsTableView.refresh();
    }

    public class ShoppingCartItem {

        private String id;

        private final StringProperty name = new StringPropertyBase() {
            @Override
            public Object getBean() {
                return ShoppingCartItem.this;
            }

            @Override
            public String getName() {
                return "name";
            }
        };

        private final DoubleProperty sellingPrice = new DoublePropertyBase() {
            @Override
            public Object getBean() {
                return ShoppingCartItem.this;
            }

            @Override
            public String getName() {
                return "sellingPrice";
            }
        };

        private final StringProperty type = new StringPropertyBase() {
            @Override
            public Object getBean() {
                return ShoppingCartItem.this;
            }

            @Override
            public String getName() {
                return "type";
            }
        };

        private final IntegerProperty qty = new IntegerPropertyBase() {
            @Override
            public Object getBean() {
                return ShoppingCartItem.this;
            }

            @Override
            public String getName() {
                return "qty";
            }
        };

        public ShoppingCartItem(Pair<ProductsDTO, Integer> x) {
            this.name.set(x.getKey().getName());
            this.sellingPrice.set(x.getKey().getSellingPrice());
            this.type.set(x.getKey().getType());
            this.id = x.getKey().getId();
            this.qty.set(x.getValue());
        }

        public StringProperty nameProperty() {
            return name;
        }

        public DoubleProperty sellingPriceProperty() {
            return sellingPrice;
        }

        public StringProperty typeProperty() {
            return type;
        }

        public IntegerProperty qtyProperty() {
            return qty;
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public double getSellingPrice() {
            return sellingPrice.get();
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice.set(sellingPrice);
        }

        public String getType() {
            return type.get();
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public int getQty() {
            return qty.get();
        }

        public void setQty(int qty) {
            this.qty.set(qty);
        }

        public String getId() {
            return this.id;
        }
    }

}
