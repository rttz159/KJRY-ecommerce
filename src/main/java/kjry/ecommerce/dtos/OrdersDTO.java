package kjry.ecommerce.dtos;

import java.util.ArrayList;
import java.util.Date;
import kjry.ecommerce.datamodels.Pair;

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
    private PromoDTO promo = null;
    public static final double SERVICETAX = 0.06;
    
    public OrdersDTO(){}
    
    public OrdersDTO(String id,String address, UsersDTO user,StatusDTO status,ArrayList<Pair<ProductsDTO,Integer>> productLists,Date orderingDate,PromoDTO promo){
        this.id = id;
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.orderingDate = orderingDate;
        this.promo = promo;
    }
    
    public OrdersDTO(String address,UsersDTO user,StatusDTO status,ArrayList<Pair<ProductsDTO,Integer>> productLists,PromoDTO promo){
        this.address = address;
        this.user = user;
        this.status = status;
        this.productLists = productLists;
        this.promo = promo;
    }

    public PromoDTO getPromo() {
        return promo;
    }

    public void setPromo(PromoDTO promo) {
        this.promo = promo;
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

    public void setProductLists(ArrayList<Pair<ProductsDTO, Integer>> x) {
        this.productLists = x;
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
