package kjry.ecommerce.datamodels;

import java.util.ArrayList;
import java.util.Date;
import javafx.util.Pair;

public class Orders {
    
    public enum Status{
        DONE,
        PROCESSING,
        PENDING,
        CANCELLED
    }
    
    private String id;
    private Users user;
    private Status status;
    private ArrayList<Pair<Products,Integer>> productLists;
    private Date orderingDate;
    private String address;
    private static int orderId = 0;
    public static final double SERVICETAX = 0.06;
    
    public Orders(String address, Users user,Status status,ArrayList<Pair<Products,Integer>> productLists){
        this.id = String.valueOf(orderId);
        this.address =address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = new Date();
        orderId++;
    }
    
    public Orders(String id,String address, Users user,Status status,ArrayList<Pair<Products,Integer>> productLists,Date orderingDate){
        this.id = id;
        this.address =address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = orderingDate;
        orderId++;
    }

    public String getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Pair<Products, Integer>> getProductLists() {
        return productLists;
    }

    public void addProduct(Products product,int i) {
        this.productLists.add(new Pair(product,i));
    }

    public Date getOrderingDate() {
        return orderingDate;
    }

    public void setOrderingDate(Date orderingDate) {
        this.orderingDate = orderingDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
