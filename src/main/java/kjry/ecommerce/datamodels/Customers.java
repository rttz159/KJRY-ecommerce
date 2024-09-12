package kjry.ecommerce.datamodels;

import java.util.ArrayList;
import java.util.Date;

public class Customers extends Users {

    private ArrayList<Pair<Products, Integer>> shoppingCart = new ArrayList<>();
    private ArrayList<String> notification = new ArrayList<>();

    public enum NotificationType {
        SMS,
        EMAIL,
        APP
    }

    private ArrayList<NotificationType> notificationTypes = new ArrayList<>() {
        {
            add(NotificationType.APP);
        }
    };

    public Customers(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate) {
        super(id, password, name, email, phoneNo, gender, birthDate);
    }

    public Customers(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate, NotificationType type) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        if (type != NotificationType.APP) {
            this.notificationTypes.add(type);
        }
    }

    public Customers(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate, ArrayList<NotificationType> notification, ArrayList<Pair<Products, Integer>> shoppingCart) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        this.shoppingCart = shoppingCart;
        this.notificationTypes = notification;
    }
    
    public Customers(){}

    public ArrayList<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    public void addNotificationType(NotificationType x) {
        if (!this.notificationTypes.contains(x)) {
            this.notificationTypes.add(x);
        }
    }

    public void removeNotificationType(NotificationType x) {
        this.notificationTypes.remove(x);
    }

    public ArrayList<Pair<Products, Integer>> getShoppingCart() {
        return this.shoppingCart;
    }

    public void addToCart(Pair<Products, Integer> x) {
        this.shoppingCart.add(x);
    }

    public void removeFromCart(Pair<Products, Integer> x) {
        this.shoppingCart.remove(x);
    }

    public ArrayList<String> getNotification() {
        return notification;
    }

    public void addNotification(String message) {
        this.notification.add(message);
    }

    @Override
    public String toString() {
        return super.toString() + "Customers{" + "shoppingCart=" + shoppingCart + ", notification=" + notification + ", notificationTypes=" + notificationTypes + '}';
    }
    
    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((Customers)o).getId());
    }
}
