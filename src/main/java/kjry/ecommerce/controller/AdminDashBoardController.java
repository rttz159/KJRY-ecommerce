package kjry.ecommerce.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import kjry.ecommerce.App;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.PromoDTO;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.OrderService;
import kjry.ecommerce.services.ProductImageManager;
import kjry.ecommerce.services.PromoService;
import kjry.ecommerce.services.UserService;

public class AdminDashBoardController implements Initializable {

    @FXML
    private PieChart custGenderPieChart;

    @FXML
    private PieChart custSubPieChart;

    @FXML
    private PieChart revenuePieChart;

    @FXML
    private Label greetingLabel;

    @FXML
    private BarChart<String, Integer> orderStatusBarChart;

    @FXML
    private Label popularItemIdLabel;

    @FXML
    private Label leastItemIdLabel;

    @FXML
    private ImageView leastItemImageView;

    @FXML
    private Label leastItemNameLabel;

    @FXML
    private Label leastItemtotalSalesLabel;

    @FXML
    private Label popularItemNameLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private VBox leastItemVBox;

    @FXML
    private VBox popularItemVBox;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private ImageView popularItemImageView;

    private ProductsDTO mostPopularItem;
    private ProductsDTO leastPopularItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UsersDTO currentUser = UserService.getUser(App.getCurrentUserId());
        String greetings = String.format("%s%s!", greetingLabel.getText(), currentUser.getName());
        greetingLabel.setText(greetings);

        int[] genderCount = {0, 0};
        int[] subCount = {0, 0, 0};
        for (UsersDTO x : UserService.getAllUsers(false)) {
            if (x instanceof CustomersDTO) {
                if (x.getGender() == 'F') {
                    genderCount[0]++;
                } else {
                    genderCount[1]++;
                }
                for (CustomersDTO.NotificationTypeDTO y : ((CustomersDTO) x).getNotificationTypes()) {
                    switch (y) {
                        case APP:
                            subCount[0]++;
                            break;
                        case EMAIL:
                            subCount[1]++;
                            break;
                        default:
                            subCount[2]++;
                            break;
                    }
                }
            }
        }

        ObservableList<PieChart.Data> custGenderPieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Female", genderCount[0]),
                        new PieChart.Data("Male", genderCount[1])
                );

        custGenderPieChart.setData(custGenderPieChartData);
        custGenderPieChart.setStyle("-fx-background-color: #E3E3E3;");
        custGenderPieChart.setLegendVisible(true);
        custGenderPieChart.setLabelLineLength(5);
        custGenderPieChart.setLegendSide(Side.LEFT);
        for (PieChart.Data data : custGenderPieChartData) {
            data.nameProperty().bind(
                    javafx.beans.binding.Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        }

        ObservableList<PieChart.Data> custSubPieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("APP", subCount[0]),
                        new PieChart.Data("EMAIL", subCount[1]),
                        new PieChart.Data("SMS", subCount[2])
                );
        custSubPieChart.setData(custSubPieChartData);
        custSubPieChart.setStyle("-fx-background-color: #E3E3E3;");
        custSubPieChart.setLegendVisible(true);
        custSubPieChart.setLabelLineLength(5);
        custSubPieChart.setLegendSide(Side.LEFT);
        for (PieChart.Data data : custSubPieChartData) {
            data.nameProperty().bind(
                    javafx.beans.binding.Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        }

        double[] revenue = {0.0, 0.0};
        for (OrdersDTO x : OrderService.getAllOrder()) {
            double tempPercentage = 0.0;
            if (x.getPromo() != null) {
                for (PromoDTO z : PromoService.getAllPromo(true)) {
                    if (z.getId().equals(x.getPromo().getId())) {
                        tempPercentage = z.getPercentage();
                        break;
                    }
                }
            }
            ArrayList<Pair<ProductsDTO, Integer>> pairList = x.getProductLists();
            for (Pair<ProductsDTO, Integer> y : pairList) {
                int idx = 0;
                if (y.getKey() instanceof AccessoriesDTO) {
                    idx = 1;
                }
                revenue[idx] += (((y.getKey().getSellingPrice()* (1 - (tempPercentage / 100.00)) - y.getKey().getCostPrice()) * y.getValue()));
            }
        }

        totalRevenueLabel.setText(String.format("RM %.2f", revenue[0] + revenue[1]));

        ObservableList<PieChart.Data> revenueChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Cloth", revenue[0]),
                        new PieChart.Data("Accessories", revenue[1])
                );
        revenuePieChart.setData(revenueChartData);
        revenuePieChart.setStyle("-fx-background-color: #E3E3E3;");
        revenuePieChart.setLegendVisible(true);
        revenuePieChart.setLabelLineLength(20);
        revenuePieChart.setLegendSide(Side.LEFT);
        for (PieChart.Data data : revenueChartData) {
            data.nameProperty().bind(
                    javafx.beans.binding.Bindings.concat(
                            data.getName(), ": RM ", String.format("%.2f", data.pieValueProperty().doubleValue())
                    )
            );
        }

        int[] orderStatusCount = {0, 0, 0, 0};
        for (OrdersDTO x : OrderService.getAllOrder()) {
            if (x.getStatus().equals(OrdersDTO.StatusDTO.CANCELLED)) {
                orderStatusCount[0]++;
            } else if (x.getStatus().equals(OrdersDTO.StatusDTO.DONE)) {
                orderStatusCount[1]++;
            } else if (x.getStatus().equals(OrdersDTO.StatusDTO.PENDING)) {
                orderStatusCount[2]++;
            } else if (x.getStatus().equals(OrdersDTO.StatusDTO.PROCESSING)) {
                orderStatusCount[3]++;
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Order Status Count");

        series.getData().add(new XYChart.Data<>("CANCELLED", orderStatusCount[0]));
        series.getData().add(new XYChart.Data<>("DONE", orderStatusCount[1]));
        series.getData().add(new XYChart.Data<>("PENDING", orderStatusCount[2]));
        series.getData().add(new XYChart.Data<>("PROCESSING", orderStatusCount[3]));

        for (XYChart.Data<String, Integer> data : series.getData()) {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Label label = new Label(data.getYValue().toString());
                    label.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-background-color:transparent;");
                    StackPane.setAlignment(label, javafx.geometry.Pos.CENTER);
                    ((StackPane) newNode).getChildren().add(label);
                }
            });
        }

        orderStatusBarChart.getData().addAll(series);
        orderStatusBarChart.setLegendVisible(false);

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

        int min = Integer.MAX_VALUE;
        for (Map.Entry<ProductsDTO, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() < min) {
                this.leastPopularItem = entry.getKey();
                min = entry.getValue();
            }
        }

        ProductImageManager imageManager = new ProductImageManager();
        imageManager.loadImage(mostPopularItem, popularItemImageView);
        popularItemIdLabel.setText(String.format("ID:   %s", mostPopularItem.getId()));
        popularItemNameLabel.setText(String.format("Name: %s", mostPopularItem.getName()));
        totalSalesLabel.setText(String.format("Total Sales: %d", max));
        popularItemVBox.setStyle("-fx-background-color: #E3E3E3;");

        imageManager.loadImage(leastPopularItem, leastItemImageView);
        leastItemIdLabel.setText(String.format("ID:   %s", leastPopularItem.getId()));
        leastItemNameLabel.setText(String.format("Name: %s", leastPopularItem.getName()));
        leastItemtotalSalesLabel.setText(String.format("Total Sales: %d", min));
        leastItemVBox.setStyle("-fx-background-color: #E3E3E3;");

    }

}
