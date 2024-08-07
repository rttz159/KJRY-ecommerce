package kjry.ecommerce.datamodels;

public class Accessories extends Products{
    private boolean washable;

    public Accessories(String id, String name, double costPrice, double sellingPrice, boolean washable) {
        super(id, name, costPrice, sellingPrice);
        this.washable = washable;
    }

    public boolean isWashable() {
        return washable;
    }

    public void setWashable(boolean washable) {
        this.washable = washable;
    }
    
}
