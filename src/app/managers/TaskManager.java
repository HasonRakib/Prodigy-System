package app.managers;

import app.models.Task;
import app.utils.DatabaseManager;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskManager {
    public void createTask(Task task) {
        String sql = "INSERT INTO Tasks (ID, Title, Description, DueDate, Priority, Status, CreatedBy, AssignedTo, ProjectID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getDueDate());
            pstmt.setString(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.setInt(7, task.getCreatedBy());
            pstmt.setInt(8, task.getAssignedTo());
            pstmt.setInt(9, task.getProjectID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
