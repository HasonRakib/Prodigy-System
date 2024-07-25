package app.models;

import app.utils.IDGenerator;

public class Task {
    private String id;
    private String title;
    private String description;
    private String dueDate;
    private String priority;
    private String status;
    private int createdBy;
    private int assignedTo;
    private int projectID;

    public Task(String title, String description, String dueDate, String priority, String status, int createdBy, int assignedTo, int projectID) {
        this.id = IDGenerator.generateTaskID();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.projectID = projectID;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public int getProjectID() {
        return projectID;
    }
}
