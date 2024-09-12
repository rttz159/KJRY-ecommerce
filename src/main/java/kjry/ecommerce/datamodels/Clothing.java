package kjry.ecommerce.datamodels;

public class Clothing extends Products {

    private Type type;
    private Size size;

    public enum Size {
        S,
        M,
        L
    }

    public enum Type {
        SHIRT,
        PANTS,
        SKIRT
    }

    public Clothing(String id, String name, double costPrice, double sellingPrice, Size size, Type type) {
        super(id, name, costPrice, sellingPrice);
        this.type = type;
        this.size = size;
    }
    
    public Clothing(){}

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return this.size;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return super.toString() + "Clothing{" + "type=" + type + ", size=" + size + '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((Clothing)o).getId());
    }
}
