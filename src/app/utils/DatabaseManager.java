package app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:prodigy.db";

    // Connect to SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Initialize database with necessary tables
    public static void initialize() {
        String userTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "ID TEXT PRIMARY KEY, " +
                "Username TEXT NOT NULL UNIQUE, " +
                "Password TEXT NOT NULL, " +
                "Role TEXT NOT NULL" +
                ");";

        String taskTable = "CREATE TABLE IF NOT EXISTS Tasks (" +
                "ID TEXT PRIMARY KEY, " +
                "Title TEXT NOT NULL, " +
                "Description TEXT, " +
                "DueDate DATE, " +
                "Priority TEXT, " +
                "Status TEXT, " +
                "CreatedBy INTEGER, " +
                "AssignedTo INTEGER, " +
                "ProjectID INTEGER, " +
                "FOREIGN KEY (CreatedBy) REFERENCES Users(ID), " +
                "FOREIGN KEY (AssignedTo) REFERENCES Users(ID), " +
                "FOREIGN KEY (ProjectID) REFERENCES Projects(ID)" +
                ");";

        String subtaskTable = "CREATE TABLE IF NOT EXISTS Subtasks (" +
                "ID TEXT PRIMARY KEY, " +
                "Title TEXT NOT NULL, " +
                "Description TEXT, " +
                "TaskID TEXT, " +
                "FOREIGN KEY (TaskID) REFERENCES Tasks(ID)" +
                ");";

        String messageTable = "CREATE TABLE IF NOT EXISTS Messages (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SenderID INTEGER NOT NULL, " +
                "ReceiverID INTEGER NOT NULL, " +
                "Content TEXT NOT NULL, " +
                "Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (SenderID) REFERENCES Users(ID), " +
                "FOREIGN KEY (ReceiverID) REFERENCES Users(ID)" +
                ");";

        String fileTable = "CREATE TABLE IF NOT EXISTS Files (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FileName TEXT NOT NULL, " +
                "FilePath TEXT NOT NULL, " +
                "TaskID TEXT, " +
                "FOREIGN KEY (TaskID) REFERENCES Tasks(ID)" +
                ");";

        String notificationTable = "CREATE TABLE IF NOT EXISTS Notifications (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "Message TEXT NOT NULL, " +
                "IsRead INTEGER DEFAULT 0, " +
                "Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (UserID) REFERENCES Users(ID)" +
                ");";

        String projectTable = "CREATE TABLE IF NOT EXISTS Projects (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT NOT NULL, " +
                "Description TEXT, " +
                "StartDate DATE, " +
                "EndDate DATE" +
                ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "userID TEXT PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(taskTable);
            stmt.execute(subtaskTable);
            stmt.execute(messageTable);
            stmt.execute(fileTable);
            stmt.execute(notificationTable);
            stmt.execute(projectTable);
            stmt.execute(createUsersTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
