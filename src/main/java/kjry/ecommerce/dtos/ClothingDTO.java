package kjry.ecommerce.dtos;

public class ClothingDTO extends ProductsDTO {

    public enum SizeDTO {
        S,
        M,
        L
    }

    public enum TypeDTO {
        SHIRT,
        PANTS,
        SKIRT
    }

    private TypeDTO type;
    private SizeDTO size;

    public ClothingDTO(String id, String name, double costPrice, double sellingPrice, String imagePath, int stockQty, SizeDTO size, TypeDTO type) {
        super(id, name, costPrice, sellingPrice, imagePath, stockQty);
        this.type = type;
        this.size = size;
    }

    public ClothingDTO(String id, String name, double costPrice, double sellingPrice, int stockQty, SizeDTO size, TypeDTO type) {
        super(id, name, costPrice, sellingPrice, "image/unavailable.png", stockQty);
        this.type = type;
        this.size = size;
    }

    public ClothingDTO(String id, String name, double costPrice, double sellingPrice, SizeDTO size, TypeDTO type) {
        super(id, name, costPrice, sellingPrice);
        this.type = type;
        this.size = size;
    }

    public ClothingDTO() {
        this.imagePath = "image/unavailable.png";
    }

    public void setSize(SizeDTO size) {
        this.size = size;
    }

    public SizeDTO getSize() {
        return this.size;
    }

    public void setClothingType(TypeDTO type) {
        this.type = type;
    }

    public TypeDTO getClothingType() {
        return this.type;
    }

}
