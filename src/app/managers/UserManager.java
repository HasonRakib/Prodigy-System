package app.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.models.User;
import app.utils.DatabaseManager;
import app.utils.IDGenerator;

public class UserManager {

    private String generateUniqueUserID(String role) {
        String userID;
        boolean isUnique;
        do {
            userID = IDGenerator.generateUserID(role);
            isUnique = checkUniqueUserID(userID);
        } while (!isUnique);
        return userID;
    }

    private boolean checkUniqueUserID(String userID) {
        String query = "SELECT 1 FROM Users WHERE userID = ?";
        try (Connection conn = DatabaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean registerUser(String username, String password, String role) {
        String query = "INSERT INTO Users (userID, Username, Password, Role) VALUES (?, ?, ?, ?)";
        String userID = generateUniqueUserID(role);
        try (Connection conn = DatabaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userID);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role.toUpperCase().replace(" ", "_")); // Store role in uppercase, replace the space with an underscore
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String userID = rs.getString("userID");
                String roleStr = rs.getString("Role").toUpperCase().replace(" ", "_");  // Replace spaces with underscores
                User.Role role = User.Role.valueOf(roleStr);  // Convert string to enum
                return new User(userID, username, password, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM Users WHERE Username = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
