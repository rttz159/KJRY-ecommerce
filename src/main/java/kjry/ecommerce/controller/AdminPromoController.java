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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.PromoDTO;
import kjry.ecommerce.services.NotificationService;
import kjry.ecommerce.services.PromoService;

public class AdminPromoController implements Initializable {

    @FXML
    private VBox AdminPromoParentVBox;

    @FXML
    private Button addButton;

    @FXML
    private TableView<PromoDTO> adminPromoTableview;

    @FXML
    private TableColumn<PromoDTO, String> codeColumn;

    @FXML
    private TableColumn<PromoDTO, Integer> durationColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<PromoDTO, String> idColumn;

    @FXML
    private TableColumn<PromoDTO, Button> notifyColumn;

    @FXML
    private Button removeButton;

    @FXML
    private TableColumn<PromoDTO, Date> startingDateColumn;

    @FXML
    private TableColumn<PromoDTO, Button> viewColumn;

    private ObservableList<PromoDTO> list = FXCollections.observableArrayList(PromoService.getAllPromo(false));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeName"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("availableDay"));
        startingDateColumn.setCellValueFactory(new PropertyValueFactory<>("startingDate"));
        startingDateColumn.setCellFactory(column -> new TableCell<PromoDTO, Date>() {
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
        viewColumn.setCellFactory(column -> new TableCell<PromoDTO, Button>() {
            private final Button button = new Button("Details");

            {
                button.setOnAction(event -> {
                    PromoDTO promo = getTableView().getItems().get(getIndex());
                    promoPromptDialog(promo, false);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });

        notifyColumn.setCellFactory(column -> new TableCell<PromoDTO, Button>() {
            private final Button button = new Button("Notify");

            {
                button.setOnAction(event -> {
                    PromoDTO promo = getTableView().getItems().get(getIndex());
                    Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    warningAlert.setHeaderText("Promotion will be notified to each user.");
                    warningAlert.showAndWait().ifPresent(result -> {
                        if (result == ButtonType.OK) {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            NotificationService.notify(String.format("Promotion Alert!\nCode: %s\nGet %.2f%% off on all clothing!\nStarts: %s\nHurry, it's only for %d days!", promo.getCodeName(), promo.getPercentage(), format.format(promo.getStartingDate()), promo.getAvailableDay()));
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        });
        adminPromoTableview.getItems().setAll(list);
        removeButton.setOnAction(event -> {
            PromoDTO promo = adminPromoTableview.getSelectionModel().getSelectedItem();
            Alert warningAlert = new Alert(Alert.AlertType.CONFIRMATION);
            warningAlert.setHeaderText("User will be ARCHIVED.");
            warningAlert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    PromoService.removePromo(promo.getId());
                    list = FXCollections.observableArrayList(PromoService.getAllPromo(false));
                    adminPromoTableview.setItems(list);
                    this.adminPromoTableview.refresh();
                }
            });
        });

        addButton.setOnAction(ev -> {
            PromoDTO promo = new PromoDTO();
            if (promoPromptDialog(promo, true)) {
                PromoService.createPromo(promo);
                list = FXCollections.observableArrayList(PromoService.getAllPromo(false));
                adminPromoTableview.setItems(list);
                this.adminPromoTableview.refresh();
            }
        });

        editButton.setOnAction(ev -> {
            PromoDTO promo = adminPromoTableview.getSelectionModel().getSelectedItem();
            try {
                if (promoPromptDialog(promo, true)) {
                    PromoService.updatePromo(promo);
                    list = FXCollections.observableArrayList(PromoService.getAllPromo(false));
                    adminPromoTableview.setItems(list);
                    this.adminPromoTableview.refresh();
                }
            } catch (NullPointerException x) {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setContentText("Please select a promo before proceed.");
                warningAlert.setHeaderText("No Promo Selected");
                warningAlert.showAndWait();
            }
        });
    }

    private boolean promoPromptDialog(PromoDTO promo, boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminPromoInfoAlert.fxml"));
            VBox dialogContent = loader.load();

            AdminPromoInfoDialogController controller = loader.getController();
            controller.setPromo(promo, editable);
            Stage dialogStage = new Stage();
            controller.setParentStage(dialogStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Promo Information");
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(dialogContent));
            dialogStage.showAndWait();
            return controller.getIsCreated();
        } catch (IOException ex) {
            System.out.println("Error when loading AdminPromoInfoAlert.fxml");
            ex.printStackTrace();
            return false;
        }
    }
}
