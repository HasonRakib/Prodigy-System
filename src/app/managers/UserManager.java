package app.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.models.User;
import app.utils.DatabaseManager;
import app.utils.IDGenerator;

public class UserManager {

    public boolean registerUser(String username, String password, User.Role role) {
        String userID = IDGenerator.generateUserID();
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (userID, username, password, role) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, userID);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role.name());
            int rowsAffected = stmt.executeUpdate();
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
                String role = rs.getString("role");
                return new User(userID, username, password, User.Role.valueOf(role));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
