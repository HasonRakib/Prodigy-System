package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeDashboardController {
    @FXML
    private Label dashboardLabel;

    @FXML
    private void initialize() {
        // Initialization code here
        dashboardLabel.setText("Welcome to the Employee Dashboard!");
    }
}
