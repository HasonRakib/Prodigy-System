package app.controllers;

import app.utils.IDGenerator;

import javafx.scene.control.Alert.AlertType;

import app.managers.UserManager;
import app.models.User;
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
    private TextField employeeUsernameField;
    @FXML
    private TextField employeePasswordField;
    @FXML
    private TextField employeeIdField;
    @FXML
    private ListView<String> employeeListView;
    /*@FXML
    private TextField subtaskTitleField;
    @FXML
    private TextArea subtaskDescriptionField;*/
    @FXML
    private TextField SubTaskTitleField;
    @FXML
    private TextArea SubTaskDescriptionField;

    @FXML
    private TextField subtaskIdField;
    @FXML
    private TextField subtaskEmployeeIDField;

    @FXML
    private TextField taskIDTextField; // TextField for entering the task ID
    @FXML
    private TextField taskStatusTextField; // TextField for entering the new status

    @FXML
    private ListView<String> assignedTasksListView;
    @FXML
    private ListView<String> taskListView;

    @FXML
    private ListView<String> subtasksListView;
    /*@FXML
    private ComboBox<String> taskStatusComboBox;
    @FXML
    private ListView<String> tasksForStatusUpdateListView;*/
    @FXML
    private TextField deleteEmployeeUsernameField;
    @FXML
    private TextField deleteTaskIDField;

    @FXML
    private void handleAddEmployee() {
        String username = employeeUsernameField.getText();
        String password = employeePasswordField.getText();

        
         if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }
        
        boolean success = UserManager.AddEmployee(username, password);
        if (success) {
            showAlert("Success", "Employee added successfully.");
        } else {
            showAlert("Error", "Failed to add Employee.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleViewAllEmployees(ActionEvent event) {
        loadEmployees();
    }

    private void loadEmployees() {
        ObservableList<String> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM Users WHERE Role = 'EMPLOYEE'";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employees.add(rs.getString("userID") + ": " + rs.getString("Username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        employeeListView.setItems(employees);
    }

    /*@FXML
    private void handleCreateSubtask(ActionEvent event) {
        String title = subtaskTitleField.getText();
        String description = subtaskDescriptionField.getText();

        if (title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Title or Description cannot be empty.");
            System.out.println("Title: " + subtaskTitleField.getText());
            System.out.println("Description: " + subtaskDescriptionField.getText());
            return;

        }
        // Generate the subtask ID using your ID generator class
        String subtaskID = IDGenerator.generateSubTaskID();

        if (addSubtaskToDatabase(subtaskID, title, description)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Subtask created successfully.");
            subtaskTitleField.clear();  // Clear the input fields after successful creation
            subtaskDescriptionField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create subtask.");
        }
    }

    private boolean addSubtaskToDatabase(String subtaskID,String title, String description) { 
        String query = "INSERT INTO Subtasks (ID, Title, Description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, subtaskID);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
        */

        @FXML
        private void handleCreateSubTask(ActionEvent event) {
            //String taskID = IDGenerator.generateTaskID();
            String subtaskID = IDGenerator.generateSubTaskID();
            String title = SubTaskTitleField.getText();
            String description = SubTaskDescriptionField.getText();

            if (title.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Title or Description cannot be empty.");
                System.out.println("Title: " + SubTaskTitleField.getText());
                System.out.println("Description: " + SubTaskDescriptionField.getText());
                return;
    
            }
    
            if (addSubTaskToDatabase(subtaskID, title, description)) {
                showAlert(AlertType.INFORMATION, "Success", "SubTask created successfully.");
                loadTasks();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to create task.");
            }
        }
        private boolean addSubTaskToDatabase(String subtaskID, String title, String description) {
            String query = "INSERT INTO Subtasks (ID, Title, Description) VALUES (?, ?, ?)";
    
            try (Connection conn = DatabaseManager.connect();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
    
                pstmt.setString(1, subtaskID);
                pstmt.setString(2, title);
                pstmt.setString(3, description);
                //pstmt.setString(4, "Pending");
    
                pstmt.executeUpdate();
                return true;
    
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

    @FXML
    private void handleAssignSubtask(ActionEvent event) {
        String subtaskID = subtaskIdField.getText();
        String empID = employeeIdField.getText();

        if (assignSubtaskToEmployee(subtaskID, empID)) {
            showAlert(AlertType.INFORMATION, "Success", "Task assigned successfully.");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to assign task.");
        }
    }
    private boolean assignSubtaskToEmployee(String subtaskID, String empID) {
        String query = "UPDATE Subtasks SET AssignedTo = ? WHERE ID = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, empID);
            pstmt.setString(2, subtaskID);

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
         UserManager userManager = UserManager.getInstance();
         User currentUser = userManager.getCurrentUser();

         if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        String currentUserID = currentUser.getUserID();

        ObservableList<String> tasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM Tasks WHERE AssignedTo = ?";
        try (Connection conn = DatabaseManager.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, currentUserID);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(rs.getString("ID") + ": " + rs.getString("Title") + " - " + rs.getString("Description"));
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
        String taskID = taskIDTextField.getText();
        String newStatus = taskStatusTextField.getText();

        if (taskID != null && !taskID.isEmpty() && newStatus != null && !newStatus.isEmpty()) {
            if (updateTaskStatus(taskID, newStatus)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Task status updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update task status.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter both Task ID and new status.");
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
        //String employeeID = deleteEmployeeIDField.getText();
        String EmployeeUsername = deleteEmployeeUsernameField.getText();
        if (deleteEmployee(EmployeeUsername)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully.");
            loadEmployees();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
        }
    }

    private boolean deleteEmployee(String EmployeeUsername) {
        String query = "DELETE FROM Users WHERE Username = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1,EmployeeUsername);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleViewSubTasks(ActionEvent event) {
        loadSubtasks();
    }
    private void loadSubtasks() {
        ObservableList<String> subtasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM Subtasks ";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
           // pstmt.setString(1, currentUserID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subtasks.add(rs.getString("ID") + ": " + rs.getString("Title") + " - " + rs.getString("Description"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        subtasksListView.setItems(subtasks);
    }

    @FXML
    private void handleDeleteSubTask(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Subtask");
        dialog.setHeaderText("Enter Subtask ID");
        dialog.setContentText("Subtask ID:");

        dialog.showAndWait().ifPresent(subtaskID -> {
            if (deleteSubTask(subtaskID)) {
                showAlert(AlertType.INFORMATION, "Success", "Subtask deleted successfully.");
                loadSubtasks();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to delete subtask.");
            }
        });
    }
    private boolean deleteSubTask(String subtaskID) {
        String query = "DELETE FROM Subtasks WHERE ID = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, subtaskID);
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
