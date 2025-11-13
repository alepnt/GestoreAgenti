package com.example.GestoreAgenti.fx.controller; // Esegue: package com.example.GestoreAgenti.fx.controller;

import com.example.GestoreAgenti.fx.controller.dashboard.DashboardController; // Esegue: import com.example.GestoreAgenti.fx.controller.dashboard.DashboardController;
import com.example.GestoreAgenti.fx.data.FxDataService; // Esegue: import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;
import javafx.event.ActionEvent; // Esegue: import javafx.event.ActionEvent;
import javafx.fxml.FXML; // Esegue: import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Esegue: import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Esegue: import javafx.scene.Parent;
import javafx.scene.Scene; // Esegue: import javafx.scene.Scene;
import javafx.scene.control.Alert; // Esegue: import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar; // Esegue: import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType; // Esegue: import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox; // Esegue: import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog; // Esegue: import javafx.scene.control.Dialog;
import javafx.scene.control.Label; // Esegue: import javafx.scene.control.Label;
import javafx.scene.control.PasswordField; // Esegue: import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField; // Esegue: import javafx.scene.control.TextField;
import javafx.stage.Stage; // Esegue: import javafx.stage.Stage;
import javafx.scene.Node; // Esegue: import javafx.scene.Node;
import javafx.scene.layout.GridPane; // Esegue: import javafx.scene.layout.GridPane;
import javafx.geometry.Insets; // Esegue: import javafx.geometry.Insets;

import java.io.IOException; // Esegue: import java.io.IOException;
import java.util.Optional; // Esegue: import java.util.Optional;

/**
 * Controller della schermata di login.
 */
public class LoginController { // Esegue: public class LoginController {

    private record RegistrationForm(String fullName, String role, String teamName, String email, String password) {} // Esegue: private record RegistrationForm(String fullName, String role, String teamName, String email, String password) {}

    @FXML // Esegue: @FXML
    private TextField emailField; // Esegue: private TextField emailField;

    @FXML // Esegue: @FXML
    private PasswordField passwordField; // Esegue: private PasswordField passwordField;

    @FXML // Esegue: @FXML
    private Label errorLabel; // Esegue: private Label errorLabel;

    private FxDataService dataService; // Esegue: private FxDataService dataService;
    private Stage primaryStage; // Esegue: private Stage primaryStage;

    @FXML // Esegue: @FXML
    private void initialize() { // Esegue: private void initialize() {
        errorLabel.setText(""); // Esegue: errorLabel.setText("");
    } // Esegue: }

    public void setDataService(FxDataService dataService) { // Esegue: public void setDataService(FxDataService dataService) {
        this.dataService = dataService; // Esegue: this.dataService = dataService;
    } // Esegue: }

    public void setPrimaryStage(Stage primaryStage) { // Esegue: public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage; // Esegue: this.primaryStage = primaryStage;
    } // Esegue: }

    @FXML // Esegue: @FXML
    private void handleLogin(ActionEvent event) { // Esegue: private void handleLogin(ActionEvent event) {
        if (dataService == null) { // Esegue: if (dataService == null) {
            errorLabel.setText("Servizio dati non disponibile"); // Esegue: errorLabel.setText("Servizio dati non disponibile");
            return; // Esegue: return;
        } // Esegue: }
        String email = emailField.getText(); // Esegue: String email = emailField.getText();
        String password = passwordField.getText(); // Esegue: String password = passwordField.getText();

        if (!isEmailValid(email)) { // Esegue: if (!isEmailValid(email)) {
            errorLabel.setText("Inserisci un'email valida."); // Esegue: errorLabel.setText("Inserisci un'email valida.");
            return; // Esegue: return;
        } // Esegue: }

        if (!isPasswordValid(password)) { // Esegue: if (!isPasswordValid(password)) {
            errorLabel.setText("La password deve contenere maiuscole, minuscole, numeri e caratteri speciali."); // Esegue: errorLabel.setText("La password deve contenere maiuscole, minuscole, numeri e caratteri speciali.");
            return; // Esegue: return;
        } // Esegue: }

        errorLabel.setText(""); // Esegue: errorLabel.setText("");

        Optional<Employee> employee = dataService.authenticate(email, password); // Esegue: Optional<Employee> employee = dataService.authenticate(email, password);
        if (employee.isPresent()) { // Esegue: if (employee.isPresent()) {
            openDashboard(employee.get()); // Esegue: openDashboard(employee.get());
        } else { // Esegue: } else {
            errorLabel.setText("Credenziali non valide. Riprova."); // Esegue: errorLabel.setText("Credenziali non valide. Riprova.");
        } // Esegue: }
    } // Esegue: }

    private boolean isPasswordValid(String password) { // Esegue: private boolean isPasswordValid(String password) {
        if (password == null) { // Esegue: if (password == null) {
            return false; // Esegue: return false;
        } // Esegue: }
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase); // Esegue: boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase); // Esegue: boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit); // Esegue: boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch)); // Esegue: boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        return hasUppercase && hasLowercase && hasDigit && hasSpecial; // Esegue: return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    } // Esegue: }

    private void openDashboard(Employee employee) { // Esegue: private void openDashboard(Employee employee) {
        try { // Esegue: try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/dashboard-view.fxml")); // Esegue: FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/dashboard-view.fxml"));
            Parent root = loader.load(); // Esegue: Parent root = loader.load();
            DashboardController controller = loader.getController(); // Esegue: DashboardController controller = loader.getController();
            controller.initializeData(dataService, employee); // Esegue: controller.initializeData(dataService, employee);
            Scene scene = new Scene(root); // Esegue: Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm()); // Esegue: scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
            primaryStage.setScene(scene); // Esegue: primaryStage.setScene(scene);
            primaryStage.setTitle("Gestore Agenti - Area di lavoro"); // Esegue: primaryStage.setTitle("Gestore Agenti - Area di lavoro");
            if (!primaryStage.isShowing()) { // Esegue: if (!primaryStage.isShowing()) {
                primaryStage.show(); // Esegue: primaryStage.show();
            } // Esegue: }
        } catch (IOException e) { // Esegue: } catch (IOException e) {
            String cause = e.getMessage(); // Esegue: String cause = e.getMessage();
            if (cause == null || cause.isBlank()) { // Esegue: if (cause == null || cause.isBlank()) {
                cause = e.getClass().getSimpleName(); // Esegue: cause = e.getClass().getSimpleName();
            } // Esegue: }
            errorLabel.setText("Errore durante il caricamento della dashboard: " + cause); // Esegue: errorLabel.setText("Errore durante il caricamento della dashboard: " + cause);
        } // Esegue: }
    } // Esegue: }

    @FXML // Esegue: @FXML
    private void handleRegister(ActionEvent event) { // Esegue: private void handleRegister(ActionEvent event) {
        if (dataService == null) { // Esegue: if (dataService == null) {
            errorLabel.setText("Servizio dati non disponibile"); // Esegue: errorLabel.setText("Servizio dati non disponibile");
            return; // Esegue: return;
        } // Esegue: }

        Dialog<RegistrationForm> dialog = new Dialog<>(); // Esegue: Dialog<RegistrationForm> dialog = new Dialog<>();
        dialog.setTitle("Registrazione dipendente"); // Esegue: dialog.setTitle("Registrazione dipendente");
        dialog.setHeaderText("Inserisci i dati per creare un nuovo account"); // Esegue: dialog.setHeaderText("Inserisci i dati per creare un nuovo account");
        if (primaryStage != null) { // Esegue: if (primaryStage != null) {
            dialog.initOwner(primaryStage); // Esegue: dialog.initOwner(primaryStage);
        } // Esegue: }

        ButtonType registerButtonType = new ButtonType("Registrati", ButtonBar.ButtonData.OK_DONE); // Esegue: ButtonType registerButtonType = new ButtonType("Registrati", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL); // Esegue: dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane(); // Esegue: GridPane grid = new GridPane();
        grid.setHgap(10); // Esegue: grid.setHgap(10);
        grid.setVgap(10); // Esegue: grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10)); // Esegue: grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(dataService.peekNextEmployeeId()); // Esegue: TextField idField = new TextField(dataService.peekNextEmployeeId());
        idField.setEditable(false); // Esegue: idField.setEditable(false);
        idField.setFocusTraversable(false); // Esegue: idField.setFocusTraversable(false);
        idField.setMaxWidth(Double.MAX_VALUE); // Esegue: idField.setMaxWidth(Double.MAX_VALUE);

        TextField nameField = new TextField(); // Esegue: TextField nameField = new TextField();
        nameField.setPromptText("Nome completo"); // Esegue: nameField.setPromptText("Nome completo");

        ComboBox<String> roleCombo = new ComboBox<>(); // Esegue: ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(dataService.getAvailableRoles()); // Esegue: roleCombo.setItems(dataService.getAvailableRoles());
        roleCombo.setPromptText("Seleziona il ruolo"); // Esegue: roleCombo.setPromptText("Seleziona il ruolo");
        roleCombo.setMaxWidth(Double.MAX_VALUE); // Esegue: roleCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> teamCombo = new ComboBox<>(); // Esegue: ComboBox<String> teamCombo = new ComboBox<>();
        teamCombo.setItems(dataService.getAvailableTeams()); // Esegue: teamCombo.setItems(dataService.getAvailableTeams());
        teamCombo.setPromptText("Seleziona il team"); // Esegue: teamCombo.setPromptText("Seleziona il team");
        teamCombo.setMaxWidth(Double.MAX_VALUE); // Esegue: teamCombo.setMaxWidth(Double.MAX_VALUE);

        TextField emailField = new TextField(); // Esegue: TextField emailField = new TextField();
        emailField.setPromptText("Email"); // Esegue: emailField.setPromptText("Email");

        PasswordField passwordInputField = new PasswordField(); // Esegue: PasswordField passwordInputField = new PasswordField();
        passwordInputField.setPromptText("Password"); // Esegue: passwordInputField.setPromptText("Password");

        grid.add(new Label("ID"), 0, 0); // Esegue: grid.add(new Label("ID"), 0, 0);
        grid.add(idField, 1, 0); // Esegue: grid.add(idField, 1, 0);
        grid.add(new Label("Nome"), 0, 1); // Esegue: grid.add(new Label("Nome"), 0, 1);
        grid.add(nameField, 1, 1); // Esegue: grid.add(nameField, 1, 1);
        grid.add(new Label("Ruolo"), 0, 2); // Esegue: grid.add(new Label("Ruolo"), 0, 2);
        grid.add(roleCombo, 1, 2); // Esegue: grid.add(roleCombo, 1, 2);
        grid.add(new Label("Team"), 0, 3); // Esegue: grid.add(new Label("Team"), 0, 3);
        grid.add(teamCombo, 1, 3); // Esegue: grid.add(teamCombo, 1, 3);
        grid.add(new Label("Email"), 0, 4); // Esegue: grid.add(new Label("Email"), 0, 4);
        grid.add(emailField, 1, 4); // Esegue: grid.add(emailField, 1, 4);
        grid.add(new Label("Password"), 0, 5); // Esegue: grid.add(new Label("Password"), 0, 5);
        grid.add(passwordInputField, 1, 5); // Esegue: grid.add(passwordInputField, 1, 5);

        dialog.getDialogPane().setContent(grid); // Esegue: dialog.getDialogPane().setContent(grid);

        Node registerButton = dialog.getDialogPane().lookupButton(registerButtonType); // Esegue: Node registerButton = dialog.getDialogPane().lookupButton(registerButtonType);
        registerButton.setDisable(true); // Esegue: registerButton.setDisable(true);

        Runnable validate = () -> { // Esegue: Runnable validate = () -> {
            boolean disable = nameField.getText().isBlank() // Esegue: boolean disable = nameField.getText().isBlank()
                    || roleCombo.getSelectionModel().getSelectedItem() == null // Esegue: || roleCombo.getSelectionModel().getSelectedItem() == null
                    || teamCombo.getSelectionModel().getSelectedItem() == null // Esegue: || teamCombo.getSelectionModel().getSelectedItem() == null
                    || !isEmailValid(emailField.getText()) // Esegue: || !isEmailValid(emailField.getText())
                    || !isPasswordValid(passwordInputField.getText()); // Esegue: || !isPasswordValid(passwordInputField.getText());
            registerButton.setDisable(disable); // Esegue: registerButton.setDisable(disable);
        }; // Esegue: };

        nameField.textProperty().addListener((obs, oldValue, newValue) -> validate.run()); // Esegue: nameField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());
        roleCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run()); // Esegue: roleCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run());
        teamCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run()); // Esegue: teamCombo.valueProperty().addListener((obs, oldValue, newValue) -> validate.run());
        emailField.textProperty().addListener((obs, oldValue, newValue) -> validate.run()); // Esegue: emailField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());
        passwordInputField.textProperty().addListener((obs, oldValue, newValue) -> validate.run()); // Esegue: passwordInputField.textProperty().addListener((obs, oldValue, newValue) -> validate.run());

        validate.run(); // Esegue: validate.run();

        dialog.setResultConverter(dialogButton -> { // Esegue: dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) { // Esegue: if (dialogButton == registerButtonType) {
                return new RegistrationForm( // Esegue: return new RegistrationForm(
                        nameField.getText(), // Esegue: nameField.getText(),
                        roleCombo.getSelectionModel().getSelectedItem(), // Esegue: roleCombo.getSelectionModel().getSelectedItem(),
                        teamCombo.getSelectionModel().getSelectedItem(), // Esegue: teamCombo.getSelectionModel().getSelectedItem(),
                        emailField.getText(), // Esegue: emailField.getText(),
                        passwordInputField.getText() // Esegue: passwordInputField.getText()
                ); // Esegue: );
            } // Esegue: }
            return null; // Esegue: return null;
        }); // Esegue: });

        dialog.showAndWait().ifPresent(form -> { // Esegue: dialog.showAndWait().ifPresent(form -> {
            if (!isEmailValid(form.email())) {
                showRegistrationError("Email non valida", "L'indirizzo email deve contenere il carattere '@'.");
                return;
            }
            if (!isPasswordValid(form.password())) {
                showRegistrationError("Password non valida",
                        "La password deve contenere maiuscole, minuscole, numeri e caratteri speciali.");
                return;
            }
            Optional<Employee> registered = dataService.registerEmployee( // Esegue: Optional<Employee> registered = dataService.registerEmployee(
                    form.fullName(), // Esegue: form.fullName(),
                    form.role(), // Esegue: form.role(),
                    form.teamName(), // Esegue: form.teamName(),
                    form.email(), // Esegue: form.email(),
                    form.password() // Esegue: form.password()
            ); // Esegue: );

            if (registered.isPresent()) { // Esegue: if (registered.isPresent()) {
                Alert success = new Alert(Alert.AlertType.INFORMATION); // Esegue: Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Registrazione completata"); // Esegue: success.setTitle("Registrazione completata");
                success.setHeaderText("Nuovo dipendente registrato"); // Esegue: success.setHeaderText("Nuovo dipendente registrato");
                success.setContentText("L'account per " + registered.get().fullName()
                        + " è stato creato. Puoi accedere con l'email "
                        + registered.get().email() + ".");
                if (primaryStage != null) { // Esegue: if (primaryStage != null) {
                    success.initOwner(primaryStage); // Esegue: success.initOwner(primaryStage);
                } // Esegue: }
                success.showAndWait(); // Esegue: success.showAndWait();
                emailField.setText(registered.get().email());
                passwordField.setText(form.password()); // Esegue: passwordField.setText(form.password());
                errorLabel.setText(""); // Esegue: errorLabel.setText("");
            } else { // Esegue: } else {
                showRegistrationError("Registrazione non riuscita",
                        "Impossibile registrare il dipendente. Verifica i dati e riprova.");
            } // Esegue: }
        }); // Esegue: });
    } // Esegue: }

    private boolean isEmailValid(String email) {
        return email != null && !email.isBlank() && email.contains("@");
    }

    private void showRegistrationError(String title, String message) {
        Alert failure = new Alert(Alert.AlertType.ERROR);
        failure.setTitle(title);
        failure.setHeaderText(title);
        failure.setContentText(message);
        if (primaryStage != null) {
            failure.initOwner(primaryStage);
        }
        failure.showAndWait();
    }
} // Esegue: }
