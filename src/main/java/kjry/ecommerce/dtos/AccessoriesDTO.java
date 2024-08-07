package kjry.ecommerce.dtos;

import java.io.File;

public class AccessoriesDTO extends ProductsDTO{
    private boolean washable;

    public AccessoriesDTO(String id, String name, double costPrice, double sellingPrice, File imageFile,boolean washable) {
        super(id, name, costPrice, sellingPrice, imageFile);
        this.washable = washable;
    }

    public boolean isWashable() {
        return washable;
    }

    public void setWashable(boolean washable) {
        this.washable = washable;
    }
    
}
