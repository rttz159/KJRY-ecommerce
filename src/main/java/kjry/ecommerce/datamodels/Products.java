package kjry.ecommerce.datamodels;

public abstract class Products implements Comparable {

    protected String id;
    protected String name;
    protected double costPrice;
    protected double sellingPrice;
    protected String imagePath;
    protected boolean isActive = true;

    public Products(String id, String name, double costPrice, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    public Products() {
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imageFilePath) {
        this.imagePath = imageFilePath;
    }

    @Override
    public String toString() {
        return "Products{" + "id=" + id + ", name=" + name + ", costPrice=" + costPrice + ", sellingPrice=" + sellingPrice + ", imagePath=" + imagePath + ", isActive=" + isActive + '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((Products) o).getId());
    }
}
