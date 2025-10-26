package com.example.GestoreAgenti.fx.controller;

import com.example.GestoreAgenti.fx.controller.dashboard.DashboardController;
import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller della schermata di login.
 */
public class LoginController {

    private record RegistrationForm(String fullName, String role, String teamName, String email, String password) {}

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

    @FXML
    private void handleRegister(ActionEvent event) {
        if (dataService == null) {
            errorLabel.setText("Servizio dati non disponibile");
            return;
        }

        Dialog<RegistrationForm> dialog = new Dialog<>();
        dialog.setTitle("Registrazione dipendente");
        dialog.setHeaderText("Inserisci i dati per creare un nuovo account");
        if (primaryStage != null) {
            dialog.initOwner(primaryStage);
        }

        ButtonType registerButtonType = new ButtonType("Registrati", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(dataService.peekNextEmployeeId());
        idField.setEditable(false);
        idField.setFocusTraversable(false);
        idField.setMaxWidth(Double.MAX_VALUE);

        TextField nameField = new TextField();
        nameField.setPromptText("Nome completo");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(dataService.getAvailableRoles());
        roleCombo.setPromptText("Seleziona il ruolo");
        roleCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> teamCombo = new ComboBox<>();
        teamCombo.setItems(dataService.getAvailableTeams());
        teamCombo.setPromptText("Seleziona il team");
        teamCombo.setMaxWidth(Double.MAX_VALUE);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordInputField = new PasswordField();
        passwordInputField.setPromptText("Password");

        grid.add(new Label("ID"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Nome"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Ruolo"), 0, 2);
        grid.add(roleCombo, 1, 2);
        grid.add(new Label("Team"), 0, 3);
        grid.add(teamCombo, 1, 3);
        grid.add(new Label("Email"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Password"), 0, 5);
        grid.add(passwordInputField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        Node registerButton = dialog.getDialogPane().lookupButton(registerButtonType);
        registerButton.setDisable(true);

        Runnable validate = () -> {
            boolean disable = nameField.getText().isBlank()
                    || roleCombo.getSelectionModel().getSelectedItem() == null
                    || teamCombo.getSelectionModel().getSelectedItem() == null
                    || emailField.getText().isBlank()
                    || passwordInputField.getText().isBlank();
            registerButton.setDisable(disable);
        };

        nameField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());
        roleCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run());
        teamCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run());
        emailField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());
        passwordInputField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());

        validate.run();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return new RegistrationForm(
                        nameField.getText(),
                        roleCombo.getSelectionModel().getSelectedItem(),
                        teamCombo.getSelectionModel().getSelectedItem(),
                        emailField.getText(),
                        passwordInputField.getText()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(form -> {
            Optional<Employee> registered = dataService.registerEmployee(
                    form.fullName(),
                    form.role(),
                    form.teamName(),
                    form.email(),
                    form.password()
            );

            if (registered.isPresent()) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Registrazione completata");
                success.setHeaderText("Nuovo dipendente registrato");
                success.setContentText("L'account per " + registered.get().fullName()
                        + " è stato creato. Puoi accedere con l'ID "
                        + registered.get().id() + ".");
                if (primaryStage != null) {
                    success.initOwner(primaryStage);
                }
                success.showAndWait();
                employeeIdField.setText(registered.get().id());
                passwordField.setText(form.password());
                errorLabel.setText("");
            } else {
                Alert failure = new Alert(Alert.AlertType.ERROR);
                failure.setTitle("Registrazione non riuscita");
                failure.setHeaderText("Impossibile registrare il dipendente");
                failure.setContentText("Verifica che i dati siano corretti e riprova.");
                if (primaryStage != null) {
                    failure.initOwner(primaryStage);
                }
                failure.showAndWait();
            }
        });
    }
}
