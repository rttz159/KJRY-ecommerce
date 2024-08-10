package kjry.ecommerce.dtos;

public class AccessoriesDTO extends ProductsDTO {

    private boolean washable;

    public AccessoriesDTO() {
        this.imagePath = "image/unavailable.png";
    }

    public AccessoriesDTO(String id, String name, double costPrice, double sellingPrice, String imagePath, int stockQty, boolean washable) {
        super(id, name, costPrice, sellingPrice, imagePath, stockQty);
        this.washable = washable;
    }

    public AccessoriesDTO(String id, String name, double costPrice, double sellingPrice, int stockQty, boolean washable) {
        super(id, name, costPrice, sellingPrice, "image/unavailable.png", stockQty);
        this.washable = washable;
    }

    public boolean isWashable() {
        return washable;
    }

    public void setWashable(boolean washable) {
        this.washable = washable;
    }

}
