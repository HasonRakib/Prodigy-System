package app.controllers;

import app.models.User;

import java.net.URL;


import app.managers.UserManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private VBox rootPane;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    private UserManager userManager = UserManager.getInstance();

    

    @FXML
    private void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Project Manager", "Employee"));
    }

    

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = userManager.authenticateUser(username, password);

            if (user != null) {
                System.out.println("Login successful for user: " + user.getUsername());
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                navigateToDashboard(user);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Failed", "An error occurred during login.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (userManager.registerUser(username, password, role)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User " + username + " registered successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "User registration failed.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToDashboard(User user) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            String fxmlFile;
            switch (user.getRole()) {
                case ADMIN:
                    fxmlFile = "/app/views/AdminDashboard.fxml";
                    break;
                case PROJECT_MANAGER:
                    fxmlFile = "/app/views/ProjectManagerDashboard.fxml";
                    break;
                case EMPLOYEE:
                    fxmlFile = "/app/views/EmployeeDashboard.fxml";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown role: " + user.getRole());
            }

            System.out.println("Loading FXML file: " + fxmlFile);  // Debug print

            URL fxmlLocation = getClass().getResource(fxmlFile);
            if (fxmlLocation == null) {
            System.out.println("FXML file not found at: " + fxmlFile);  // Debug print
            throw new NullPointerException("Location is required.");
            }

            System.out.println("FXML file found at: " + fxmlLocation.toString());  // Debug print
            
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 600,600);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load the dashboard.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Navigate back to the welcome screen
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close(); // Close the current stage

            Stage welcomeStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/WelcomeView.fxml"));
            Parent root = loader.load();
            welcomeStage.setTitle("Welcome");
            welcomeStage.setScene(new Scene(root, 600, 600));
            welcomeStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
