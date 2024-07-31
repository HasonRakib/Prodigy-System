package app;

//import java.io.FileInputStream;
//import java.io.InputStream;

import app.utils.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import javafx.scene.image.Image;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the database
            System.out.println("Initializing the database...");
            DatabaseManager.initialize();
            System.out.println("Database initialized successfully.");

            // Load the welcome view
            System.out.println("Loading WelcomeView.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/WelcomeView.fxml"));
            Parent root = loader.load();
            System.out.println("WelcomeView.fxml loaded successfully.");
            
            primaryStage.setTitle("PRODIGY SYSTEM");

            // Load and set multiple icons
            /*try (InputStream iconStream1 = new FileInputStream("/icons/webp.net-resizeimage.png");
                 InputStream iconStream2 = new FileInputStream("/icons/webp.net-resizeimage(1).png")) {

                primaryStage.getIcons().addAll(
                    new Image(iconStream1),
                    new Image(iconStream2)
                );
            }*/

            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
            System.out.println("Stage shown successfully.");

        } catch (Exception e) {
            System.out.println("Exception in Application start method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
