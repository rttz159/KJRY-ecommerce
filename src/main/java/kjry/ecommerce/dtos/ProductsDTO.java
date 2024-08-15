package kjry.ecommerce.dtos;

public abstract class ProductsDTO {

    protected String id;
    protected String name;
    protected int stockQty = -1;
    protected double costPrice;
    protected double sellingPrice;
    protected String imagePath;
    protected boolean isActive = true;

    protected ProductsDTO(String id, String name, double costPrice, double sellingPrice, String imagePath, int stockQty) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.imagePath = imagePath;
        this.stockQty = stockQty;
    }

    protected ProductsDTO(String id, String name, double costPrice, double sellingPrice, int stockQty) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.stockQty = stockQty;
    }

    protected ProductsDTO(String id, String name, double costPrice, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    protected ProductsDTO() {

    }

    public boolean getIsActive() {
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

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
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

    public String getType() {
        if (this instanceof ClothingDTO) {
            return "Clothing";
        } else {
            return "Accesories";
        }
    }

}
