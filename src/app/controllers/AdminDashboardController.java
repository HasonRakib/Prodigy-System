package app.controllers;

import app.utils.DatabaseManager;
import app.utils.IDGenerator;
import app.managers.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private TextField pmUsernameField;
    @FXML
    private PasswordField pmPasswordField;

    @FXML
    private TextField taskTitleField;
    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private TextField taskIdField;
    @FXML
    private TextField pmIdField;

    @FXML
    private ListView<String> userListView;
    @FXML
    private ListView<String> taskListView;

    @FXML
    private TextField projectTitleField;
    @FXML
    private TextArea projectDescriptionField;
    @FXML
    private DatePicker projectStartDatePicker;
    @FXML
    private DatePicker projectEndDatePicker;

    @FXML
    private TextField deletePMUsernameField;

    private UserManager userManager = new UserManager();

    @FXML
    private void initialize() {
        loadUsers();
        loadTasks();
    }

    @FXML
    private void handleCreateProject(ActionEvent event) {
        String projectID = IDGenerator.generateProjectID();
        String title = projectTitleField.getText();
        String description = projectDescriptionField.getText();
        LocalDate startDate = projectStartDatePicker.getValue();
        LocalDate endDate = projectEndDatePicker.getValue();

        if (addProjectToDatabase(projectID, title, description, startDate, endDate)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project created successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create project.");
        }
    }

    private boolean addProjectToDatabase(String projectID, String title, String description, LocalDate startDate, LocalDate endDate) {
        String query = "INSERT INTO Projects (ID, Title, Description, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, projectID);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setDate(4, java.sql.Date.valueOf(startDate));
            pstmt.setDate(5, java.sql.Date.valueOf(endDate));

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

   
    @FXML
    private void handleAddProjectManager() {
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Add Project Manager");
        usernameDialog.setHeaderText("Enter Project Manager Username");
        usernameDialog.setContentText("Username:");

        Optional<String> usernameResult = usernameDialog.showAndWait();
        if (!usernameResult.isPresent()) {
            return;
        }
        String username = usernameResult.get();

        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Add Project Manager");
        passwordDialog.setHeaderText("Enter Project Manager Password");
        passwordDialog.setContentText("Password:");

        Optional<String> passwordResult = passwordDialog.showAndWait();
        if (!passwordResult.isPresent()) {
            return;
        }
        String password = passwordResult.get();

        String role = "PROJECT_MANAGER";
        boolean registered = userManager.registerUser(username, password, role);
        if (registered) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Project Manager added successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to add Project Manager.");
        }
    }

    @FXML
    private void handleViewAllUsers(ActionEvent event) {
        loadUsers();
    }

    @FXML
    private void handleCreateTask(ActionEvent event) {
        String taskID = IDGenerator.generateTaskID();
        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();

        if (addTaskToDatabase(taskID, title, description)) {
            showAlert(AlertType.INFORMATION, "Success", "Task created successfully.");
            loadTasks();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to create task.");
        }
    }

    @FXML
    private void handleViewAllTasks(ActionEvent event) {
        loadTasks();
    }

    @FXML
    private void handleAssignTask(ActionEvent event) {
        String taskID = taskIdField.getText();
        String pmID = pmIdField.getText();

        if (assignTaskToProjectManager(taskID, pmID)) {
            showAlert(AlertType.INFORMATION, "Success", "Task assigned successfully.");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to assign task.");
        }
    }

    @FXML
    private void handleDeleteProjectManager(ActionEvent event) {
        String username = deletePMUsernameField.getText();

        if (userManager.deleteUser(username)) {
            showAlert(AlertType.INFORMATION, "Success", "Project Manager deleted successfully.");
            loadUsers();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to delete Project Manager.");
        }
    }

    @FXML
    private void handleDeleteTask(ActionEvent event) {
        String taskID = taskIdField.getText();

        if (deleteTaskFromDatabase(taskID)) {
            showAlert(AlertType.INFORMATION, "Success", "Task deleted successfully.");
            loadTasks();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to delete task.");
        }
    }

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

    private void loadUsers() {
        ObservableList<String> users = FXCollections.observableArrayList();
        String query = "SELECT * FROM Users";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(rs.getString("userID") + ": " + rs.getString("Username") + " (" + rs.getString("Role") + ")");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        userListView.setItems(users);
    }

    private void loadTasks() {
        ObservableList<String> tasks = FXCollections.observableArrayList();
        String query = "SELECT * FROM Tasks";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                tasks.add(rs.getString("ID") + ": " + rs.getString("Title") + " - " + rs.getString("Status"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        taskListView.setItems(tasks);
    }

    private boolean addTaskToDatabase(String taskID, String title, String description) {
        String query = "INSERT INTO Tasks (ID, Title, Description, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, taskID);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, "Pending");

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean assignTaskToProjectManager(String taskID, String pmID) {
        String query = "UPDATE Tasks SET AssignedTo = ? WHERE ID = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, pmID);
            pstmt.setString(2, taskID);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean deleteTaskFromDatabase(String taskID) {
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

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
