package kjry.ecommerce.datamodels;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class Products {

    protected String id;
    protected String name;
    protected double costPrice;
    protected double sellingPrice;
    protected String imagePath;

    public Products(String id, String name, double costPrice, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.imagePath = "image/unavailable.png";
    }

    public Products() {
        this.imagePath = "image/unavailable.png";
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
        String path = null;
        Path sourcePath = Paths.get(imageFilePath);
        Path destinationFolder = Paths.get("/image");
        try {
            if (Files.notExists(destinationFolder)) {
                Files.createDirectories(destinationFolder);
            }

            Path destinationPath = destinationFolder.resolve(sourcePath.getFileName());

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            String copiedFilePath = destinationPath.toAbsolutePath().toString();

            System.out.println("File copied to: " + copiedFilePath);

            path = String.format("image/%s", sourcePath.getFileName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imagePath = path;
    }

}
