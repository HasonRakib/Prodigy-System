package app.controllers;

import app.utils.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Optional;

public class ProjectManagerDashboardController {

    @FXML
    private TextField employeeNameField;
    @FXML
    private TextField employeeIDField;
    @FXML
    private ListView<String> employeeListView;
    @FXML
    private TextField taskIDField;
    @FXML
    private TextArea subtaskDescriptionField;
    @FXML
    private TextField subtaskEmployeeIDField;
    @FXML
    private ListView<String> assignedTasksListView;
    @FXML
    private ListView<String> taskListView;
    @FXML
    private ComboBox<String> taskStatusComboBox;
    @FXML
    private ListView<String> tasksForStatusUpdateListView;
    @FXML
    private TextField deleteEmployeeIDField;
    @FXML
    private TextField deleteTaskIDField;

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        String name = employeeNameField.getText();
        String id = employeeIDField.getText();
        if (addEmployeeToDatabase(name, id)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully.");
            loadEmployees();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
        }
    }

    private boolean addEmployeeToDatabase(String name, String id) {
        String query = "INSERT INTO Employees (ID, Name) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleViewAllEmployees(ActionEvent event) {
        loadEmployees();
    }

    private void loadEmployees() {
        ObservableList<String> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM Employees";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employees.add(rs.getString("ID") + ": " + rs.getString("Name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        employeeListView.setItems(employees);
    }

    @FXML
    private void handleAssignSubtask(ActionEvent event) {
        String taskID = taskIDField.getText();
        String subtaskDescription = subtaskDescriptionField.getText();
        String employeeID = subtaskEmployeeIDField.getText();
        if (assignSubtaskToEmployee(taskID, subtaskDescription, employeeID)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Subtask assigned successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to assign subtask.");
        }
    }

    private boolean assignSubtaskToEmployee(String taskID, String subtaskDescription, String employeeID) {
        String query = "INSERT INTO Subtasks (TaskID, Description, EmployeeID) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, taskID);
            pstmt.setString(2, subtaskDescription);
            pstmt.setString(3, employeeID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleViewAllTasks(ActionEvent event) {
        loadTasks();
    }

    private void loadTasks() {
        ObservableList<String> tasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM Tasks WHERE ManagerID = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "PROJECT_MANAGER_ID"); // Replace with actual project manager ID
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(rs.getString("ID") + ": " + rs.getString("Description"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        assignedTasksListView.setItems(tasks);
    }

    @FXML
    private void handleUploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            String taskID = taskListView.getSelectionModel().getSelectedItem();
            if (uploadFileToTask(selectedFile, taskID)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "File uploaded successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload file.");
            }
        }
    }

    private boolean uploadFileToTask(File file, String taskID) {
        // Implement file upload logic
        return true;
    }

    @FXML
    private void handleDownloadFile(ActionEvent event) {
        String taskID = taskListView.getSelectionModel().getSelectedItem();
        FileChooser fileChooser = new FileChooser();
        File destinationFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (destinationFile != null) {
            if (downloadFileFromTask(taskID, destinationFile)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "File downloaded successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to download file.");
            }
        }
    }

    private boolean downloadFileFromTask(String taskID, File destinationFile) {
        // Implement file download logic
        return true;
    }

    @FXML
    private void handleUpdateTaskStatus(ActionEvent event) {
        String taskID = tasksForStatusUpdateListView.getSelectionModel().getSelectedItem();
        String newStatus = taskStatusComboBox.getSelectionModel().getSelectedItem();
        if (updateTaskStatus(taskID, newStatus)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Task status updated successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update task status.");
        }
    }

    private boolean updateTaskStatus(String taskID, String newStatus) {
        String query = "UPDATE Tasks SET Status = ? WHERE ID = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, taskID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleDeleteEmployee(ActionEvent event) {
        String employeeID = deleteEmployeeIDField.getText();
        if (deleteEmployee(employeeID)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully.");
            loadEmployees();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
        }
    }

    private boolean deleteEmployee(String employeeID) {
        String query = "DELETE FROM Employees WHERE ID = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employeeID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleDeleteTask(ActionEvent event) {
        String taskID = deleteTaskIDField.getText();
        if (deleteTask(taskID)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Task deleted successfully.");
            loadTasks();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete task.");
        }
    }

    private boolean deleteTask(String taskID) {
        String query = "DELETE FROM Tasks WHERE ID = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, taskID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/app/views/LoginView.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
