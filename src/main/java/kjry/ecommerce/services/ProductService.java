package kjry.ecommerce.services;

import java.util.Iterator;
import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.ProductController;
import kjry.ecommerce.datamodels.DynamicList;
import kjry.ecommerce.dtos.ProductsDTO;

public class ProductService {

    private static DatabaseController dbController = new ProductController();

    public static ProductsDTO[] getAllProducts(boolean withInactive) {
        DynamicList<ProductsDTO> temp = dbController.getAll();
        if (!withInactive) {
            Iterator<ProductsDTO> iterator = temp.iterator();
            while (iterator.hasNext()) {
                ProductsDTO product = iterator.next();
                if (!product.getIsActive()) {
                    iterator.remove();
                }
            }
        }
        ProductsDTO[] tempArr = new ProductsDTO[temp.getSize()];
        int i = 0;
        for (ProductsDTO prod : temp) {
            tempArr[i] = prod;
            i++;
        }
        return tempArr;
    }

    public static ProductsDTO getProduct(String id) {
        return (ProductsDTO) dbController.findById(id);
    }

    public static boolean createProduct(ProductsDTO dto) {
        if (dto != null) {
            return dbController.create(dto);
        } else {
            return false;
        }
    }

    public static boolean createProduct(ProductsDTO dto, int stockQty) {
        if (dto != null) {
            ProductController tempController = (ProductController) dbController;
            return tempController.create(dto, stockQty);
        } else {
            return false;
        }
    }

    public static boolean updateProduct(ProductsDTO dto) {
        if (dto != null) {
            return dbController.update(dto);
        } else {
            return false;
        }
    }

    public static boolean updateProduct(ProductsDTO dto, int stockQty) {
        if (dto != null) {
            ProductController tempController = (ProductController) dbController;
            return tempController.update(dto, stockQty);
        } else {
            return false;
        }
    }

    public static boolean removeProduct(ProductsDTO dto) {
        if (dto != null) {
            return dbController.removeById(dto.getId());
        } else {
            return false;
        }
    }

    public static boolean removeProduct(String id) {
        if (id != null) {
            return dbController.removeById(id);
        } else {
            return false;
        }
    }

}
