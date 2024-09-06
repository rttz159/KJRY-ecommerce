package kjry.ecommerce.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import kjry.ecommerce.App;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.UserService;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.services.OrderService;
import kjry.ecommerce.services.ProductImageManager;

public class UserDashBoardController implements Initializable {

    @FXML
    private VBox AdminProductParentVBox;

    @FXML
    private ImageView banner1;

    @FXML
    private ImageView banner2;

    @FXML
    private ImageView banner3;

    @FXML
    private Label greetingLabel;

    @FXML
    private Button leftBannerButton;

    @FXML
    private Label popularItemIdLabel;

    @FXML
    private ImageView popularItemImageView;

    @FXML
    private Label popularItemNameLabel;

    @FXML
    private VBox popularItemVBox;

    @FXML
    private Button rightBannerButton;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private ListView<String> notificationMsgListView;

    ImageView[] banners;

    private int currentIdx = 0;

    final double distanceX = 560;

    private double[] originalX = new double[3];

    private ProductsDTO mostPopularItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        banners = new ImageView[]{banner1, banner2, banner3};
        for (int i = 0; i < 3; i++) {
            originalX[i] = banners[i].getTranslateX();
        }
        UsersDTO currentUser = UserService.getUser(App.getCurrentUserId());
        String greetings = String.format("%s%s!", greetingLabel.getText(), currentUser.getName());
        greetingLabel.setText(greetings);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> {
                    if (currentIdx != 2) {
                        moveToLeft();
                        currentIdx++;
                    } else {
                        bannerReset();
                    }
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        leftBannerButton.setOnAction(e -> {
            timeline.stop();
            if (currentIdx != 2) {
                moveToLeft();
                currentIdx++;
            }
            timeline.play();
        });
        rightBannerButton.setOnAction(e -> {
            timeline.stop();
            if (currentIdx != 0) {
                moveToRight();
                currentIdx--;
            } else {
                bannerReset();
            }
            timeline.play();
        });

        Map<ProductsDTO, Integer> hashMap = new HashMap<>();
        for (OrdersDTO x : OrderService.getAllOrder()) {
            for (Pair<ProductsDTO, Integer> y : x.getProductLists()) {
                if (hashMap.get(y.getKey()) != null) {
                    hashMap.put(y.getKey(), hashMap.get(y.getKey()) + y.getValue());
                } else {
                    hashMap.put(y.getKey(), y.getValue());
                }
            }
        }

        int max = 0;
        for (Map.Entry<ProductsDTO, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() > max) {
                this.mostPopularItem = entry.getKey();
                max = entry.getValue();
            }
        }

        ProductImageManager imageManager = new ProductImageManager();
        imageManager.loadImage(mostPopularItem, popularItemImageView);
        popularItemIdLabel.setText(String.format("ID:   %s", mostPopularItem.getId()));
        popularItemNameLabel.setText(String.format("Name: %s", mostPopularItem.getName()));
        totalSalesLabel.setText(String.format("Total Sales: %d", max));
        popularItemVBox.setStyle("-fx-background-color: #E3E3E3;");

        notificationMsgListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });

        if (currentUser instanceof CustomersDTO) {
            CustomersDTO tempCust = (CustomersDTO) currentUser;
            if (tempCust.getNotification().size() != 0) {
                notificationMsgListView.getItems().addAll(tempCust.getNotification());
            } else {
                Label noContentLabel = new Label("No message to display");
                notificationMsgListView.setPlaceholder(noContentLabel);
            }
        }
    }

    private void moveToLeft() {
        for (int i = 0; i < 3; i++) {
            toLeft(banners[i]);
        }
    }

    private void moveToRight() {
        for (int i = 0; i < 3; i++) {
            toRight(banners[i]);
        }
    }

    private void toRight(ImageView x) {
        Double currentX = x.getTranslateX();
        TranslateTransition translate = new TranslateTransition();
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.setNode(x);
        translate.setDuration(Duration.seconds(1));
        translate.setFromX(currentX);
        translate.setToX(currentX + distanceX);
        translate.play();
    }

    private void toLeft(ImageView x) {
        Double currentX = x.getTranslateX();
        TranslateTransition translate = new TranslateTransition();
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.setNode(x);
        translate.setDuration(Duration.seconds(1));
        translate.setFromX(currentX);
        translate.setToX(currentX - distanceX);
        translate.play();
    }

    private void bannerReset() {
        for (int i = 2; i >= 0; i--) {
            ImageView x = banners[i];
            Double currentX = x.getTranslateX();
            TranslateTransition translate = new TranslateTransition();
            translate.setInterpolator(Interpolator.EASE_BOTH);
            translate.setNode(x);
            translate.setDuration(Duration.seconds(1));
            translate.setFromX(currentX);
            translate.setToX(originalX[i]);
            translate.play();
        }
        currentIdx = 0;
    }

}
