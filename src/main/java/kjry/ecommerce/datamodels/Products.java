package kjry.ecommerce.datamodels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public abstract class Products {
    protected String id;
    protected String name;
    protected double costPrice;
    protected double sellingPrice;
    protected String imageFile;

    public Products(String id, String name, double costPrice, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
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

    public File getImageFile() {
        byte[] decodedBytes = Base64.getDecoder().decode(this.imageFile);
        String filePath = String.format("temp/%sImage", name);
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setImageFile(String imageFilePath){
        byte[] imageBytes;
        try {
            imageBytes = Files.readAllBytes(Paths.get(imageFilePath));
            this.imageFile = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException ex) {
            System.out.println("Error when setting imageFile to the Product");
        }
    }
    
    public void setImageFile(File imageFile){
        byte[] imageBytes;
        try {
            imageBytes = Files.readAllBytes(Paths.get(imageFile.getPath()));
            this.imageFile = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException ex) {
            System.out.println("Error when getting imageFile from the Product");
        }  
    }
    
    
}
