package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.dtos.ProductsDTO;

public class ProductController implements DatabaseController<ProductsDTO>{
    
    @Override
    public ProductsDTO findById(String id) {
        ProductsDTO temp = null;
        for(Products products: DatabaseWrapper.getProductsList()){
            if(products.getId().equals(id)){
                temp = EntityDTOConverter.convertEntityToDto(products);
                int stockQuantity = DatabaseWrapper.getProductStock().getOrDefault(products, 0);
                temp.setStockQty(stockQuantity);
                break;
            }
        }
        return temp;    
    }

    @Override
    public boolean create(ProductsDTO entity) {
        return create(entity, 0);
    }
    
    public boolean create(ProductsDTO entity, int initialStock) {
        boolean error = true;
        try {
            Products temp = EntityDTOConverter.convertDtoToEntity(entity);
            DatabaseWrapper.getProductsList().add(temp);
            DatabaseWrapper.getProductStock().put(temp, initialStock);
            error = false;
        } catch (Exception ex) {
            System.out.println("Error occurred when creating Product. Product will not be created.");
        }
        return error;
    }

    @Override
    public boolean update(ProductsDTO entity) {
        return update(entity, -1); // Use -1 to indicate no change to stock if not provided
    }

    public boolean update(ProductsDTO entity, int newStockQuantity) {
        boolean error = true;
        try {
            for (int i = 0; i < DatabaseWrapper.getProductsList().size(); i++) {
                if (DatabaseWrapper.getProductsList().get(i).getId().equals(entity.getId())) {
                    
                    Products updatedProduct = EntityDTOConverter.convertDtoToEntity(entity);
                    DatabaseWrapper.getProductsList().set(i, updatedProduct);
                    if (newStockQuantity >= 0) {
                        DatabaseWrapper.getProductStock().put(updatedProduct, newStockQuantity);
                    }

                    error = false;
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error occurred when updating Product. Product will not be updated.");
        }
        return error;
    }

    @Override
    public boolean removeById(String id) {
        boolean error = true;
        for (int i = 0; i < DatabaseWrapper.getProductsList().size(); i++) {
            if (DatabaseWrapper.getProductsList().get(i).getId().equals(id)) {
                DatabaseWrapper.getProductStock().remove(DatabaseWrapper.getProductsList().get(i));
                DatabaseWrapper.getProductsList().get(i).setIsActive(false);
                ArrayList<Products> temp = DatabaseWrapper.getProductsList();
                error = false;
                break;
            }
        }
        return error;
    }
    
    @Override
    public ProductsDTO[] getAll() {
        ProductsDTO[] temp = new ProductsDTO[DatabaseWrapper.getProductsList().size()];
        for(int i = 0; i < DatabaseWrapper.getProductsList().size(); i++){
            temp[i] = EntityDTOConverter.convertEntityToDto(DatabaseWrapper.getProductsList().get(i));
        }
        return temp;
    }
    
    public int getProductStockQty(ProductsDTO dto) {
        int stockQuantity = -1;

        try {
            Products product = EntityDTOConverter.convertDtoToEntity(dto);
            Integer quantity = DatabaseWrapper.getProductStock().get(product);
            if (quantity != null) {
                stockQuantity = quantity;
            } else {
                System.out.println("Product not found in stock: " + dto.getId());
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Error occurred while retrieving stock quantity for product: " + dto.getId());
            ex.printStackTrace();
        }

        return stockQuantity;
    }
    
}
