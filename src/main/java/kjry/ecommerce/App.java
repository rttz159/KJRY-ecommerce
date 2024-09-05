package kjry.ecommerce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import kjry.ecommerce.dataaccess.DatabaseWrapper;

public class App extends Application {

    private static String currentUserId;
    
    private static Scene scene;
    
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = new FXMLLoader(App.class.getResource("/views/WelcomePage.fxml")).load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("KJRY ECOMMERCE");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseWrapper dw = new DatabaseWrapper();
        launch();
        DatabaseWrapper.updateFile();
    }

    public static void setCurrentUserId(String userid){
        currentUserId = userid;
    }
    
    public static String getCurrentUserId(){
        return currentUserId;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}