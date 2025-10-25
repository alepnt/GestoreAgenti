package com.example.GestoreAgenti.fx.controller;

import com.example.GestoreAgenti.fx.controller.dashboard.DashboardController;
import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller della schermata di login.
 */
public class LoginController {

    @FXML
    private TextField employeeIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private FxDataService dataService;
    private Stage primaryStage;

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    public void setDataService(FxDataService dataService) {
        this.dataService = dataService;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        if (dataService == null) {
            errorLabel.setText("Servizio dati non disponibile");
            return;
        }
        String employeeId = employeeIdField.getText();
        String password = passwordField.getText();

        Optional<Employee> employee = dataService.authenticate(employeeId, password);
        if (employee.isPresent()) {
            openDashboard(employee.get());
        } else {
            errorLabel.setText("Credenziali non valide. Riprova.");
        }
    }

    private void openDashboard(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/dashboard-view.fxml"));
            Parent root = loader.load();
            DashboardController controller = loader.getController();
            controller.initializeData(dataService, employee);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestore Agenti - Area di lavoro");
        } catch (IOException e) {
            errorLabel.setText("Impossibile aprire la dashboard");
        }
    }
}
