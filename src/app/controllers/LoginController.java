package app.controllers;

import app.managers.UserManager;
import app.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UserManager userManager;

    public LoginController() {
        userManager = new UserManager();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userManager.authenticateUser(username, password);
        if (user != null) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getUsername() + "!");
            // Proceed to the main application view
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isRegistered = userManager.registerUser(username, password, User.Role.EMPLOYEE);
        if (isRegistered) {
            showAlert(AlertType.INFORMATION, "Registration Successful", "User " + username + " registered successfully.");
        } else {
            showAlert(AlertType.ERROR, "Registration Failed", "User registration failed.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
