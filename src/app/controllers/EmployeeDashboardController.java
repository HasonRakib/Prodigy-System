package app.controllers;

import app.utils.DatabaseManager;
//import app.utils.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDashboardController {

    @FXML
    private ListView<String> messageListView;
    @FXML
    private TextField messageField;

    @FXML
    private ListView<String> taskListView;

    @FXML
    private TextField taskIdField;
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField emailRecipientField;
    @FXML
    private TextField emailSubjectField;
    @FXML
    private TextArea emailBodyField;

    @FXML
    private void initialize() {
        loadTasks();
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
    }

    @FXML
    private void handleSendMessage(ActionEvent event) {
        String message = messageField.getText();
        if (message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Message cannot be empty.");
            return;
        }

        // Add the message to the list view (for simplicity, you can extend it to store in a database)
        messageListView.getItems().add("You: " + message);
        messageField.clear();
    }

    @FXML
    private void handleUpdateTaskStatus(ActionEvent event) {
        String taskId = taskIdField.getText();
        String status = statusComboBox.getValue();

        if (taskId.isEmpty() || status == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Task ID and status must be provided.");
            return;
        }

        if (updateTaskStatusInDatabase(taskId, status)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Task status updated successfully.");
            loadTasks();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update task status.");
        }
    }

    private boolean updateTaskStatusInDatabase(String taskId, String status) {
        String query = "UPDATE Tasks SET Status = ? WHERE ID = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setString(2, taskId);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /*@FXML
    private void handleSendEmail(ActionEvent event) {
        String recipient = emailRecipientField.getText();
        String subject = emailSubjectField.getText();
        String body = emailBodyField.getText();

        if (recipient.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All email fields must be filled.");
            return;
        }

        // Use a utility class to send the email (EmailSender is a hypothetical class)
        if (EmailSender.sendEmail(recipient, subject, body)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Email sent successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send email.");
        }
    }*/

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/LoginView.fxml"));
            Parent loginView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the login screen.");
        }
    }

    private void loadTasks() {
        ObservableList<String> tasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM Tasks WHERE AssignedTo = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Use a placeholder for the employee ID (assuming it's stored in a variable `employeeID`)
            String employeeID = "employee123"; // Replace this with the actual employee ID
            pstmt.setString(1, employeeID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(rs.getString("ID") + ": " + rs.getString("Title") + " - " + rs.getString("Status"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        taskListView.setItems(tasks);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
