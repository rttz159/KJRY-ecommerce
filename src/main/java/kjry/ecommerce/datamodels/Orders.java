package kjry.ecommerce.datamodels;

import java.util.ArrayList;
import java.util.Date;

public class Orders implements Comparable{

    private String id;
    private Users user;
    private Status status;
    private ArrayList<Pair<Products, Integer>> productLists;
    private Date orderingDate;
    private String address;
    private Promo promoUsed;
    private static int orderId = 0;
    public static final double SERVICETAX = 0.06;

    public enum Status {
        DONE,
        PROCESSING,
        PENDING,
        CANCELLED
    }

    public Orders(String address, Users user, Status status, ArrayList<Pair<Products, Integer>> productLists,Promo promoUsed) {
        this.id = String.valueOf(orderId);
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = new Date();
        this.promoUsed = promoUsed;
        orderId++;
    }

    public Orders(String id, String address, Users user, Status status, ArrayList<Pair<Products, Integer>> productLists, Date orderingDate,Promo promoUsed) {
        this.id = id;
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = orderingDate;
        this.promoUsed = promoUsed;
        orderId++;
    }
    
    public Orders(){}

    public Promo getPromoUsed() {
        return promoUsed;
    }

    public void setPromoUsed(Promo promoUsed) {
        this.promoUsed = promoUsed;
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

    public void addProduct(Products product, int i) {
        this.productLists.add(new Pair(product, i));
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

    @Override
    public String toString() {
        return "Orders{" + "id=" + id + ", user=" + user.toString() + ", status=" + status + ", productLists=" + productLists + ", orderingDate=" + orderingDate + ", address=" + address + '}';
    }
    
    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((Orders)o).getId());
    }
}
