package kjry.ecommerce.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.OrderController;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;

public class OrderService {

    private static DatabaseController dbController = new OrderController();

    public static boolean createOrder(OrdersDTO dto) {
        if (dto != null) {
            return dbController.create(dto);
        } else {
            return false;
        }
    }

    public static boolean cancelOrder(OrdersDTO dto) {
        if (dto != null) {
            dto.setStatus(OrdersDTO.StatusDTO.CANCELLED);
            return dbController.update(dto);
        } else {
            return false;
        }
    }

    public static OrdersDTO[] getAllOrder(boolean withCancelled) {
        ArrayList<OrdersDTO> temp = new ArrayList<>();
        Collections.addAll(temp, (OrdersDTO[]) dbController.getAll());
        if (!withCancelled) {
            Iterator<OrdersDTO> iterator = temp.iterator();
            while (iterator.hasNext()) {
                OrdersDTO order = iterator.next();
                if (order.getStatus() == OrdersDTO.StatusDTO.CANCELLED) {
                    iterator.remove();
                }
            }
        }

        OrdersDTO[] tempArr = new OrdersDTO[temp.size()];
        int i = 0;
        for (OrdersDTO order : temp) {
            tempArr[i] = order;
            i++;
        }
        return tempArr;
    }

    public static boolean updateOrder(OrdersDTO dto) {
        if (dto != null) {
            return dbController.update(dto);
        } else {
            return false;
        }
    }

    public static double calculateBill(OrdersDTO dto) {
        double total = 0;
        for (Pair<ProductsDTO, Integer> prod : dto.getProductLists()) {
            total += prod.getKey().getSellingPrice() * prod.getValue();
        }

        if (dto.getPromo() == null) {
            total = total * (1 + OrdersDTO.SERVICETAX);
        } else {
            total = (total * (1 + OrdersDTO.SERVICETAX)) * (1 - (dto.getPromo().getPercentage() / 100));
        }
        
        return total;
    }

    public static boolean makePayment(OrdersDTO dto, String choice) {
        PaymentService paymentService;
        switch (choice) {
            case "Card":
                paymentService = new PaymentService(new CardPaymentMethod());
                return paymentService.makePayment(calculateBill(dto));
            case "Bank":
                paymentService = new PaymentService(new OnlineBankingPaymentMethod());
                return paymentService.makePayment(calculateBill(dto));
            default:
                return false;
        }
    }

}
