package app.controllers;

import app.views.WelcomeViewStyler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.scene.layout.VBox;

public class WelcomeController {
   @FXML
    private VBox rootPane;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        try {
            WelcomeViewStyler.styleWelcomeView( rootPane/*,loginButton, exitButton*/);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close(); // Close the current stage

            // Load the login view
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/LoginView.fxml"));
            Parent root = loader.load();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root, 400, 300));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
