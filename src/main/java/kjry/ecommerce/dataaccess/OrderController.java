package kjry.ecommerce.dataaccess;

import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.datamodels.Orders;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.dtos.OrdersDTO;

public class OrderController implements DatabaseController<OrdersDTO>{
    
    @Override
    public OrdersDTO findById(String id) {
        OrdersDTO temp = null;
        for(Orders order: DatabaseWrapper.getOrdersList()){
            if(order.getId().equals(id)){
                temp = EntityDTOConverter.convertEntityToDto(order);
                break;
            }
        }
        return temp;
    }

    @Override
    public boolean create(OrdersDTO entity) {
        boolean error = true;
        try {
            Orders temp = EntityDTOConverter.convertDtoToEntity(entity);
            DatabaseWrapper.getOrdersList().add(temp);

            // Adjust stock quantities based on the order
            for (Pair<Products, Integer> pair : temp.getProductLists()) {
                Products product = pair.getKey();
                int quantityOrdered = pair.getValue();
                if (DatabaseWrapper.getProductStock().containsKey(product)) {
                    int currentStock = DatabaseWrapper.getProductStock().get(product);
                    int newStock = currentStock - quantityOrdered;
                    if (newStock >= 0) {
                        DatabaseWrapper.getProductStock().put(product, newStock);
                    } else {
                        System.out.println("Insufficient stock for product: " + product.getId());
                        throw new Exception();
                    }
                } else {
                    System.out.println("Product not found in stock: " + product.getId());
                    throw new Exception();
                }
            }

            error = false;
        } catch (Exception ex) {
            System.out.println("Error occurred when creating Order. Order will not be created.");
            ex.printStackTrace();
        }
        return error;
    }

    @Override
    public boolean update(OrdersDTO entity) {
        boolean error = true;
        for(int i = 0; i < DatabaseWrapper.getOrdersList().size(); i++){
            if(DatabaseWrapper.getOrdersList().get(i).getId().equals(entity.getId())){
                DatabaseWrapper.getOrdersList().set(i, EntityDTOConverter.convertDtoToEntity(entity));
                error = false;
                break;
            }
        }
        return error;
    }

    @Override
    public boolean removeById(String id) {
        boolean error = true;
        for(int i = 0; i < DatabaseWrapper.getOrdersList().size(); i++){
            if(DatabaseWrapper.getOrdersList().get(i).getId().equals(id)){
                DatabaseWrapper.getOrdersList().remove(i);
                error = false;
                break;
            }
        }
        return error;
    }

    @Override
    public OrdersDTO[] getAll() {
        OrdersDTO[] temp = new OrdersDTO[DatabaseWrapper.getOrdersList().size()];
        for(int i = 0; i < DatabaseWrapper.getOrdersList().size(); i++){
            temp[i] = EntityDTOConverter.convertEntityToDto(DatabaseWrapper.getOrdersList().get(i));
        }
        System.out.println(DatabaseWrapper.getOrdersList().size());
        return temp;
    }
    
}
