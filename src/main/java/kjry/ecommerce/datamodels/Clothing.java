package kjry.ecommerce.datamodels;

import java.io.File;

public class Clothing extends Products{
    
    public enum Size{
        S,
        M,
        L
    }
    
    public enum Type{
        SHIRT,
        PANTS,
        SKIRT
    }
    
    private Type type;
    private Size size;
   
    public Clothing(String id, String name, double costPrice, double sellingPrice, Size size, Type type) {
        super(id, name, costPrice, sellingPrice);
        this.type = type;
        this.size = size;
    }
    
    public void setSize(Size size){
        this.size = size;
    }
    
    public Size getSize(){
        return this.size;
    }
    
    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }
    
}
