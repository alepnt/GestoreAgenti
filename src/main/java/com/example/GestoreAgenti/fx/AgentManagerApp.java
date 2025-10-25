package com.example.GestoreAgenti.fx;

import com.example.GestoreAgenti.fx.controller.LoginController;
import com.example.GestoreAgenti.fx.data.FxDataService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Avvia l'interfaccia JavaFX e mantiene un'istanza condivisa del servizio dati
 * che simula le funzioni dell'applicazione gestionale.
 */
public class AgentManagerApp extends Application {

    private final FxDataService dataService = new FxDataService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.setDataService(dataService);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
        primaryStage.setTitle("Gestore Agenti - Accesso dipendenti");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
