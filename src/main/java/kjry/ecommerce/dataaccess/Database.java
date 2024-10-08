package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import kjry.ecommerce.datamodels.Orders;
import kjry.ecommerce.datamodels.Users;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.datamodels.Promo;
        
public abstract class Database {
    protected ArrayList<Users> userList;
    protected ArrayList<Products> productList;
    protected ArrayList<Orders> orderList;
    protected ArrayList<Promo> promoList;
    protected HashMap<Products, Integer> productStock;

    public ArrayList<Users> getUserList() {
        return userList;
    }

    public ArrayList<Orders> getOrderList() {
        return orderList;
    }

    public ArrayList<Products> getProductList() {
        return productList;
    }
    
    public ArrayList<Promo> getPromoList() {
        return promoList;
    }

    public HashMap<Products, Integer> getProductStock() {
        return productStock;
    }
    
    public abstract boolean readFile(String filepath);
    public abstract boolean createFile(String filepath);
    public abstract boolean updateFile(String filepath);
    
}
