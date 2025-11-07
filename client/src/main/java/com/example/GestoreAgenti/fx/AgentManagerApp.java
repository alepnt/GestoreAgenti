package com.example.GestoreAgenti.fx; // Definisce il package dell'applicazione JavaFX del client.

import com.example.GestoreAgenti.fx.controller.LoginController; // Importa il controller della schermata di login per configurarlo durante l'avvio.
import com.example.GestoreAgenti.fx.data.FxDataService; // Importa il servizio dati locale che finge le interazioni con il backend.
import javafx.application.Application; // Importa la classe base Application necessaria per ogni app JavaFX.
import javafx.fxml.FXMLLoader; // Importa FXMLLoader per caricare i file FXML della UI.
import javafx.scene.Parent; // Importa Parent che rappresenta la radice della scena caricata da FXML.
import javafx.scene.Scene; // Importa Scene per costruire la scena grafica da mostrare nella finestra.
import javafx.stage.Stage; // Importa Stage per manipolare la finestra principale dell'applicazione.

/**
 * Avvia l'interfaccia JavaFX e mantiene un'istanza condivisa del servizio dati
 * che simula le funzioni dell'applicazione gestionale.
 */
public class AgentManagerApp extends Application { // Dichiara la classe principale ereditando da Application per usare il ciclo vita JavaFX.

    private final FxDataService dataService = new FxDataService(); // Crea il servizio dati da condividere fra i controller caricati.

    @Override // Indica che il metodo ridefinisce start della classe base Application.
    public void start(Stage primaryStage) throws Exception { // Ingresso del lifecycle JavaFX che riceve lo stage primario da configurare.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml")); // Prepara il caricatore FXML puntando al layout della schermata di login.
        Parent root = loader.load(); // Carica il file FXML generando la struttura dei nodi grafici.
        LoginController controller = loader.getController(); // Recupera il controller associato per inizializzarne le dipendenze.
        controller.setPrimaryStage(primaryStage); // Passa lo stage principale al controller per consentirgli di navigare tra le scene.
        controller.setDataService(dataService); // Inietta il servizio dati per permettere al controller di orchestrare le operazioni applicative.

        Scene scene = new Scene(root); // Crea la scena principale utilizzando la gerarchia FXML caricata.
        scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm()); // Aggancia il foglio di stile per applicare i temi grafici.
        primaryStage.setTitle("Gestore Agenti - Accesso dipendenti"); // Imposta il titolo della finestra principale visualizzato dal sistema operativo.
        primaryStage.setScene(scene); // Associa la scena preparata allo stage principale.
        primaryStage.setResizable(false); // Disabilita il ridimensionamento per mantenere una UI con layout fisso.
        primaryStage.show(); // Mostra la finestra iniziale completando la fase di avvio.
    } // Esegue: }

    public static void main(String[] args) { // Entry point tradizionale per avviare l'applicazione.
        launch(args); // Delegata all'infrastruttura JavaFX per inizializzare l'ambiente grafico.
    } // Esegue: }
} // Esegue: }
