package com.example.GestoreAgenti.fx.command;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.GestoreAgenti.fx.controller.LoginController;
import com.example.GestoreAgenti.fx.data.FxDataService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogoutCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(LogoutCommand.class.getName());

    private final Supplier<Stage> stageSupplier;
    private final FxDataService dataService;
    private final Runnable cleanup;

    public LogoutCommand(Supplier<Stage> stageSupplier, FxDataService dataService, Runnable cleanup) {
        this.stageSupplier = Objects.requireNonNull(stageSupplier, "stageSupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.cleanup = Objects.requireNonNull(cleanup, "cleanup");
    }

    @Override
    public void execute() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            Stage stage = stageSupplier.get();
            if (stage == null) {
                LOGGER.warning("Nessuna finestra disponibile per completare il logout");
                return;
            }
            controller.setPrimaryStage(stage);
            controller.setDataService(dataService);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Gestore Agenti - Accesso dipendenti");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Impossibile completare il logout", e);
        } finally {
            dataService.clearCurrentEmployee();
            cleanup.run();
        }
    }
}
