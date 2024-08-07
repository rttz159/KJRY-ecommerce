package kjry.ecommerce.services;

import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.ProductController;
import kjry.ecommerce.dtos.ProductsDTO;

public class ProductService {
    private static DatabaseController dbController = new ProductController();
    
    public static ProductsDTO[] getAllProducts(){
        return (ProductsDTO[]) dbController.getAll();
    }
    
    public static ProductsDTO getProduct(String id){
        return (ProductsDTO) dbController.findById(id);
    }
    
    public static boolean createProduct(ProductsDTO dto){
        if(dto != null){
            return dbController.create(dto);
        }else{
            return false;
        }
    }
    
    public static boolean createProduct(ProductsDTO dto, int stockQty){
        if(dto != null){
            ProductController tempController = (ProductController) dbController;
            return tempController.create(dto, stockQty);
        }else{
            return false;
        }
    }
    
    
    public static boolean updateProduct(ProductsDTO dto){
        if(dto != null){
            return dbController.update(dto);
        }else{
            return false;
        }
    }

    public static boolean updateProduct(ProductsDTO dto, int stockQty){
        if(dto != null){
            ProductController tempController = (ProductController) dbController;
            return tempController.update(dto, stockQty);
        }else{
            return false;
        }
    }
    
    public static boolean removeProduct(ProductsDTO dto){
        if(dto != null){
            return dbController.removeById(dto.getId());
        }else{
            return false;
        }
    }

    public static boolean removeProduct(String id){
        if(id != null){
            return dbController.removeById(id);
        }else{
            return false;
        }
    }
    
}
