package kjry.ecommerce.dtos;

import java.util.ArrayList;
import java.util.Date;
import kjry.ecommerce.datamodels.Pair;

public class CustomersDTO extends UsersDTO{
    
    public enum NotificationTypeDTO{
        SMS,
        EMAIL,
        APP
    }
    
    private ArrayList<NotificationTypeDTO> notificationTypes = new ArrayList<>(){
        {
            add(NotificationTypeDTO.APP);
        }
    };
    
    private ArrayList<Pair<ProductsDTO,Integer>> shoppingCart = new ArrayList<>();
    private ArrayList<String> notification = new ArrayList<>();
    
    public CustomersDTO(){}
    
    public CustomersDTO(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate) {
        super(id, password, name, email, phoneNo, gender, birthDate);
    }
    
    public CustomersDTO(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate, NotificationTypeDTO type) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        if(type != NotificationTypeDTO.APP){
            this.notificationTypes.add(type);
        }
    }
    
    public CustomersDTO(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate, ArrayList<NotificationTypeDTO> notification,ArrayList<Pair<ProductsDTO,Integer>> shoppingCart) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        this.shoppingCart = shoppingCart;
        this.notificationTypes = notification;
    }

    public ArrayList<NotificationTypeDTO> getNotificationTypes() {
        return notificationTypes;
    }
    
    public void addNotificationType(NotificationTypeDTO x){
        if(!this.notificationTypes.contains(x)){
            this.notificationTypes.add(x);
        }
    }
    
    public void removeNotificationType(NotificationTypeDTO x){
        this.notificationTypes.remove(x);
    }

    public void setShoppingCart(ArrayList<Pair<ProductsDTO,Integer>>  x){
        this.shoppingCart = x;
    }
    
    public ArrayList<Pair<ProductsDTO,Integer>> getShoppingCart(){
        return this.shoppingCart;
    }
    
    public void addToCart(Pair<ProductsDTO,Integer> x){
        this.shoppingCart.add(x);
    }
    
    public void removeFromCart(Pair<ProductsDTO,Integer> x){
        this.shoppingCart.remove(x);
    }
    
    public ArrayList<String> getNotification() {
        return notification;
    }
    
    public void addNotification(String message){
        this.notification.add(message);
    }
    
}
