package app;

import app.utils.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the database
            System.out.println("Initializing the database...");
            DatabaseManager.initialize();
            System.out.println("Database initialized successfully.");

            // Load the login view
            System.out.println("Loading LoginView.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/LoginView.fxml"));
            Parent root = loader.load();
            System.out.println("LoginView.fxml loaded successfully.");

            primaryStage.setTitle("Prodigy");
            primaryStage.setScene(new Scene(root, 400, 300));
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
