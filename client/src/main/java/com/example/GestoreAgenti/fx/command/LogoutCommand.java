package com.example.GestoreAgenti.fx.command; // Esegue: package com.example.GestoreAgenti.fx.command;

import java.io.IOException; // Esegue: import java.io.IOException;
import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.function.Supplier; // Esegue: import java.util.function.Supplier;
import java.util.logging.Level; // Esegue: import java.util.logging.Level;
import java.util.logging.Logger; // Esegue: import java.util.logging.Logger;

import com.example.GestoreAgenti.fx.controller.LoginController; // Esegue: import com.example.GestoreAgenti.fx.controller.LoginController;
import com.example.GestoreAgenti.fx.data.FxDataService; // Esegue: import com.example.GestoreAgenti.fx.data.FxDataService;

import javafx.fxml.FXMLLoader; // Esegue: import javafx.fxml.FXMLLoader;
import javafx.scene.Parent; // Esegue: import javafx.scene.Parent;
import javafx.scene.Scene; // Esegue: import javafx.scene.Scene;
import javafx.stage.Stage; // Esegue: import javafx.stage.Stage;

public class LogoutCommand implements Command { // Esegue: public class LogoutCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(LogoutCommand.class.getName()); // Esegue: private static final Logger LOGGER = Logger.getLogger(LogoutCommand.class.getName());

    private final Supplier<Stage> stageSupplier; // Esegue: private final Supplier<Stage> stageSupplier;
    private final FxDataService dataService; // Esegue: private final FxDataService dataService;
    private final Runnable cleanup; // Esegue: private final Runnable cleanup;

    public LogoutCommand(Supplier<Stage> stageSupplier, FxDataService dataService, Runnable cleanup) { // Esegue: public LogoutCommand(Supplier<Stage> stageSupplier, FxDataService dataService, Runnable cleanup) {
        this.stageSupplier = Objects.requireNonNull(stageSupplier, "stageSupplier"); // Esegue: this.stageSupplier = Objects.requireNonNull(stageSupplier, "stageSupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService"); // Esegue: this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.cleanup = Objects.requireNonNull(cleanup, "cleanup"); // Esegue: this.cleanup = Objects.requireNonNull(cleanup, "cleanup");
    } // Esegue: }

    @Override // Esegue: @Override
    public void execute() { // Esegue: public void execute() {
        try { // Esegue: try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml")); // Esegue: FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml"));
            Parent root = loader.load(); // Esegue: Parent root = loader.load();
            LoginController controller = loader.getController(); // Esegue: LoginController controller = loader.getController();
            Stage stage = stageSupplier.get(); // Esegue: Stage stage = stageSupplier.get();
            if (stage == null) { // Esegue: if (stage == null) {
                LOGGER.warning("Nessuna finestra disponibile per completare il logout"); // Esegue: LOGGER.warning("Nessuna finestra disponibile per completare il logout");
                return; // Esegue: return;
            } // Esegue: }
            controller.setPrimaryStage(stage); // Esegue: controller.setPrimaryStage(stage);
            controller.setDataService(dataService); // Esegue: controller.setDataService(dataService);
            Scene scene = new Scene(root); // Esegue: Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm()); // Esegue: scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
            stage.setScene(scene); // Esegue: stage.setScene(scene);
            stage.setTitle("Gestore Agenti - Accesso dipendenti"); // Esegue: stage.setTitle("Gestore Agenti - Accesso dipendenti");
        } catch (IOException e) { // Esegue: } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossibile completare il logout", e); // Esegue: LOGGER.log(Level.WARNING, "Impossibile completare il logout", e);
        } finally { // Esegue: } finally {
            dataService.clearCurrentEmployee(); // Esegue: dataService.clearCurrentEmployee();
            cleanup.run(); // Esegue: cleanup.run();
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
