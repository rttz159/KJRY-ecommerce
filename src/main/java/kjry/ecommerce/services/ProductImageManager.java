package kjry.ecommerce.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kjry.ecommerce.dtos.ProductsDTO;

public class ProductImageManager {

    public final Path IMAGEFOLDER = getAppDirectory().resolve("kjryEcommerce_images");
    
    public ProductImageManager() {
    }

    private Path getAppDirectory() {
        try {
            File jarFile = new File(ProductImageManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            return jarFile.getParentFile().getParentFile().toPath();
        } catch (Exception e) {
            e.printStackTrace();
            return Paths.get("");
        }
    }
    
    public void loadImage(ProductsDTO product, ImageView productImage) {
        try {
            String imagePath = product.getImagePath();
            Path fullPath = IMAGEFOLDER.resolve(imagePath);
            File imageFile = fullPath.toFile();
            if (imageFile.exists()) {
                Image image = new Image("file:" + imageFile.getAbsolutePath());
                productImage.setImage(image);
            } else {
                System.out.println("Image file does not exist: " + imageFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveProductImage(Path sourcePath, ProductsDTO product) {
        Path appDirectory = getAppDirectory();
        Path destinationFolder = appDirectory.resolve("kjryEcommerce_images");

        try {
            if (Files.notExists(destinationFolder)) {
                Files.createDirectories(destinationFolder);
            }
            Path destinationPath = destinationFolder.resolve(sourcePath.getFileName());
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            String copiedFilePath = destinationPath.toAbsolutePath().toString();
            System.out.println("File copied to: " + copiedFilePath);
            product.setImagePath(sourcePath.getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeProductImage(ProductsDTO product) {
        String imagePath = product.getImagePath();
        Path appDirectory = getAppDirectory();
        Path imageFilePath = appDirectory.resolve("kjryEcommerce_images").resolve(imagePath);
        try {
            if (Files.exists(imageFilePath)) {
                Files.delete(imageFilePath);
                System.out.println("File deleted: " + imageFilePath.toAbsolutePath().toString());
            } else {
                System.out.println("File does not exist: " + imageFilePath.toAbsolutePath().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
