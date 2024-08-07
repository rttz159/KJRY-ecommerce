package kjry.ecommerce.dtos;

import java.util.ArrayList;
import java.util.Date;
import javafx.util.Pair;

public class OrdersDTO {
    
    public enum StatusDTO{
        DONE,
        PROCESSING,
        PENDING,
        CANCELLED
    }
    
    private String id;
    private String address;
    private UsersDTO user;
    private StatusDTO status;
    private ArrayList<Pair<ProductsDTO,Integer>> productLists;
    private Date orderingDate;
    public static final double SERVICETAX = 0.06;
    
    public OrdersDTO(String id,String address, UsersDTO user,StatusDTO status,ArrayList<Pair<ProductsDTO,Integer>> productLists,Date orderingDate){
        this.id = id;
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = orderingDate;
    }
    
    public OrdersDTO(String address,UsersDTO user,StatusDTO status,ArrayList<Pair<ProductsDTO,Integer>> productLists){
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public ArrayList<Pair<ProductsDTO, Integer>> getProductLists() {
        return productLists;
    }

    public void addProduct(ProductsDTO product,int i) {
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
