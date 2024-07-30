package app.views;

//import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class WelcomeViewStyler {
    public static void styleWelcomeView(VBox rootPane/*, Button loginButton, Button exitButton*/) {
        // Set background image
        Image backgroundImage = new Image("file:C:\\Users\\HP\\Desktop\\ProdigySystem\\Prodigy\\helpers\\images\\a-good-background-for-a-weather-app-upscaled.jpg");
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        rootPane.setBackground(new Background(bgImage));

        // Center everything
        rootPane.setSpacing(20);
        rootPane.setPadding(new javafx.geometry.Insets(100, 50, 100, 50));
        rootPane.setAlignment(javafx.geometry.Pos.CENTER);

        // Style buttons
        //loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #FF6F61; -fx-text-fill: white; -fx-padding: 10 20 10 20;");
        //exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #48C9B0; -fx-text-fill: white; -fx-padding: 10 20 10 20;");

        // Add hover effects to buttons
        //loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #FF6F61; -fx-text-fill: white; -fx-padding: 10 20 10 20;"));
        //loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #48C9B0; -fx-text-fill: white; -fx-padding: 10 20 10 20;"));
        
        //exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #FF6F61; -fx-text-fill: white; -fx-padding: 10 20 10 20;"));
       // exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #48C9B0; -fx-text-fill: white; -fx-padding: 10 20 10 20;"));

    }
}
