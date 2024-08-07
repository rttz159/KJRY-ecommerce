package kjry.ecommerce.dtos;

import kjry.ecommerce.datamodels.*;
import java.io.File;

public class ClothingDTO extends ProductsDTO{
    
    public enum SizeDTO{
        S,
        M,
        L
    }
    
    public enum TypeDTO{
        SHIRT,
        PANTS,
        SKIRT
    }
    
    private TypeDTO type;
    private SizeDTO size;

    public ClothingDTO(String id, String name, double costPrice, double sellingPrice, File imageFile, SizeDTO size,TypeDTO type) {
        super(id, name, costPrice, sellingPrice, imageFile);
        this.type = type;
        this.size = size;
    }
    
    public ClothingDTO(String id, String name, double costPrice, double sellingPrice, SizeDTO size,TypeDTO type) {
        super(id, name, costPrice, sellingPrice);
        this.type = type;
        this.size = size;
    }
    
    public void setSize(SizeDTO size){
        this.size = size;
    }
    
    public SizeDTO getSize(){
        return this.size;
    }
    
    public void setType(TypeDTO type){
        this.type = type;
    }

    public TypeDTO getType(){
        return this.type;
    }
    
}
