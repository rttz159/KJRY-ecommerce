package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import kjry.ecommerce.datamodels.*;
import kjry.ecommerce.datamodels.Pair;

public class PopulateData {

    public static void main(String[] args) {
        DatabaseWrapper dw = new DatabaseWrapper();
        ArrayList<Users> usersList = DatabaseWrapper.getUsersList();
        ArrayList<Products> productsList = DatabaseWrapper.getProductsList();
        ArrayList<Orders> ordersList = DatabaseWrapper.getOrdersList();
        HashMap<Products, Integer> productStock = DatabaseWrapper.getProductStock();


        for (int i = 1; i <= 10; i++) {
            Customers customer = new Customers(
                    "CUST" + i,
                    "password" + i,
                    "Customer" + i,
                    "customer" + i + "@example.com",
                    "0123456789" + i,
                    'M',
                    new Date(),
                    Customers.NotificationType.values()[i%3]
            );
            usersList.add(customer);
        }

        for (int i = 1; i <= 5; i++) {
            Employees employee = new Employees(
                    "EMP" + i,
                    "password" + i,
                    "Employee" + i,
                    "employee" + i + "@example.com",
                    "0987654321" + i,
                    i % 2 == 0 ? 'F' : 'M',
                    new Date(),
                    Employees.JobRole.values()[i % 3] 
            );
            usersList.add(employee);
        }

        for (int i = 1; i <= 6; i++) {
            Clothing clothing = new Clothing(
                    "CLOTH" + i,
                    "Clothing Item " + i,
                    20.0 * i, 
                    30.0 * i, 
                    Clothing.Size.values()[i % 3], 
                    Clothing.Type.values()[i % 3]
            );
            productsList.add(clothing);
        }

        for (int i = 1; i <= 6; i++) {
            Accessories accessory = new Accessories(
                    "ACC" + i,
                    "Accessory Item " + i,
                    15.0 * i, 
                    25.0 * i, 
                    true 
            );
            productsList.add(accessory);
        }

        for (int i = 1; i <= 15; i++) {
            Users user = usersList.get(i % 10); 
            ArrayList<Pair<Products, Integer>> productList = new ArrayList<>();
            productList.add(new Pair<>(productsList.get(i % 12), i)); 

            Orders order = new Orders(
                    "123 Street, City" + i,
                    user,
                    Orders.Status.values()[i % 4], 
                    productList
            );
            ordersList.add(order);
        }
        
        for (Products product : productsList) {
            productStock.put(product, 10);
        }

        DatabaseWrapper.updateFile();
    }
}
