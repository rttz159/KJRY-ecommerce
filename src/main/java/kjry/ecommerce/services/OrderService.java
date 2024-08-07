package kjry.ecommerce.services;

import javafx.util.Pair;
import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.OrderController;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;

public class OrderService {
    
    private static DatabaseController dbController = new OrderController();
    
    private static boolean updateOrderStatus(OrdersDTO dto){
        dto.setStatus(OrdersDTO.StatusDTO.DONE);
        return dbController.update(dto);
    }
    
    public static boolean createOrder(OrdersDTO dto){
        if(dto != null){
            return dbController.create(dto);
        }else{
            return false;
        }
    }
    
    public static boolean cancelOrder(OrdersDTO dto){
        if(dto != null){
            dto.setStatus(OrdersDTO.StatusDTO.CANCELLED);
            return dbController.update(dto);
        }else{
            return false;
        }
    }
    
    public static OrdersDTO[] getAllOrder(){
        return (OrdersDTO[]) dbController.getAll();
    }
    
    public static double calculateBill(OrdersDTO dto){
        double total = 0;
        for(Pair<ProductsDTO,Integer> prod: dto.getProductLists()){
            total += prod.getKey().getSellingPrice() * prod.getValue();
        }
        
        return total * OrdersDTO.SERVICETAX;
    }
    
    public static boolean makePayment(OrdersDTO dto, String choice){
        PaymentService paymentService;
        switch(choice){
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
