package kjry.ecommerce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import kjry.ecommerce.dataaccess.DatabaseWrapper;
import kjry.ecommerce.dtos.UsersDTO;
import kjry.ecommerce.services.UserService;

public class App extends Application {

    private static int currentUserIdx;
    
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = loadFXML("/views/AdminMain");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("KJRY ECOMMERCE");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        DatabaseWrapper dw = new DatabaseWrapper();
        launch();
        DatabaseWrapper.updateFile();
    }

    public static void setCurrentUser(UsersDTO user){
        int i = 0;
        for(UsersDTO x : UserService.getAllUsers()){
            if(user.getId().equals(x.getId())){
                currentUserIdx = i;
            }
            i++;
        }
    }
    
    public static UsersDTO getCurrentUser(){
        return UserService.getAllUsers()[App.currentUserIdx];
    }
    
}