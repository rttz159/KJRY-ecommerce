package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import kjry.ecommerce.datamodels.Orders;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.datamodels.Users;

public class DatabaseWrapper {
    private static Database instance;
    private final static String filepath = "database.json";
    
    public DatabaseWrapper(){
        if(instance == null){
            instance = new GsonDatabase(filepath);
        }
    }
    
    public static String getFilepath(){
        return filepath;
    }
    
    public static void updateFile(){
        instance.updateFile(filepath);
    }
    
    public static ArrayList<Users> getUsersList(){
        return instance.getUserList();
    }
    
    public static ArrayList<Products> getProductsList(){
        return instance.getProductList();
    }
    
    public static ArrayList<Orders> getOrdersList(){
        return instance.getOrderList();
    }
    
    public static HashMap<Products, Integer> getProductStock(){
        return instance.getProductStock();
    }
    
}
