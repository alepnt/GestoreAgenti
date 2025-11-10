package com.example.GestoreAgenti.fx.controller.dashboard; // Package che ospita il controller della dashboard del client.

// Import dei comandi che incapsulano le azioni attivabili dalla UI.
import com.example.GestoreAgenti.fx.command.Command; // Interfaccia base per ogni comando invocabile.
import com.example.GestoreAgenti.fx.command.LogoutCommand; // Comando che gestisce la disconnessione dell'utente.
import com.example.GestoreAgenti.fx.command.SendEmailCommand; // Comando che inoltra l'email tramite il servizio dati.
import com.example.GestoreAgenti.fx.command.SendTeamMessageCommand; // Comando per l'invio di messaggi nella chat di team.
import com.example.GestoreAgenti.fx.command.ShowPaneCommand; // Comando che cambia il pannello visibile nella dashboard.

// Import di servizi, eventi e modelli usati per popolare l'interfaccia.
import com.example.GestoreAgenti.fx.data.FxDataService; // Servizio locale che fornisce dati simulati.
import com.example.GestoreAgenti.fx.event.EmailSentEvent; // Evento emesso dopo l'invio di un'email.
import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent; // Evento relativo all'aggiornamento di una notifica.
import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent; // Evento emesso quando arriva un messaggio del team.
import com.example.GestoreAgenti.fx.model.AgendaItem; // Record che descrive un appuntamento in agenda.
import com.example.GestoreAgenti.fx.model.ChatMessage; // Record che descrive un messaggio di chat.
import com.example.GestoreAgenti.fx.model.EmailMessage; // Record che rappresenta una mail inviata o ricevuta.
import com.example.GestoreAgenti.fx.model.Employee; // Record che rappresenta il dipendente connesso.
import com.example.GestoreAgenti.fx.model.InvoiceRecord; // Record che rappresenta una fattura.
import com.example.GestoreAgenti.fx.model.MonthlyRevenue;
import com.example.GestoreAgenti.invoice.InvoiceState; // Enumerazione condivisa con gli stati delle fatture.
import com.example.GestoreAgenti.fx.model.Notification; // Record che descrive una notifica.
import com.example.GestoreAgenti.fx.model.PaymentRecord; // Record che rappresenta un pagamento.

// Import delle componenti JavaFX necessarie a manipolare la UI.
import javafx.application.Platform; // Permette di aggiornare i controlli dal thread JavaFX.
import javafx.beans.property.SimpleStringProperty; // Wrapper per esporre stringhe reattive nelle tabelle.
import javafx.collections.ObservableList; // Lista osservabile che notifica le view dei cambiamenti.
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList; // Vista filtrata che seleziona elementi per stato.
import javafx.css.PseudoClass; // Gestisce pseudo-classi CSS dinamiche.
import javafx.event.ActionEvent; // Evento generato da interazioni dell'utente.
import javafx.fxml.FXML; // Annotation per collegare i campi ai nodi FXML.
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay; // Configura come ListCell mostra testo e grafica.
import javafx.scene.control.Label; // Controllo grafico che visualizza testo.
import javafx.scene.control.ListCell; // Cella personalizzabile per ListView.
import javafx.scene.control.ListView; // Controllo che mostra liste verticali di elementi.
import javafx.scene.control.TableColumn; // Colonna di TableView configurabile per proprietà.
import javafx.scene.control.TableView; // Tabella che mostra collezioni tabellari.
import javafx.scene.control.TextArea; // Campo di input multi-riga.
import javafx.scene.control.TextField; // Campo di input a singola riga.
import javafx.scene.control.ToggleButton; // Pulsante con stato selezionabile.
import javafx.scene.control.ToggleGroup; // Collega più ToggleButton per selezione esclusiva.
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane; // Layout ancorato usato per i pannelli della dashboard.
import javafx.scene.layout.StackPane; // Layout che sovrappone i diversi pannelli.
import javafx.scene.layout.VBox; // Layout verticale usato nelle notifiche.
import javafx.stage.Stage; // Rappresenta la finestra principale di JavaFX.

// Import di utilità standard Java utilizzate dal controller.
import java.text.NumberFormat; // Gestisce la formattazione degli importi in valuta.
import java.time.format.DateTimeFormatter; // Gestisce la formattazione delle date.
import java.time.format.TextStyle;
import java.time.Month;
import java.util.Locale; // Specifica la localizzazione italiana.
import java.util.ArrayList; // Implementazione di lista usata per le sottoscrizioni.
import java.util.HashMap; // Implementazione di mappa usata per registrare i comandi.
import java.util.List; // Interfaccia List per collezioni ordinate.
import java.util.Map; // Interfaccia Map per associazioni chiave/valore.

/**
 * Controller della dashboard principale con tutte le sezioni richieste.
 */
public class DashboardController { // Esegue: public class DashboardController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato per le sole date.
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Formato data e ora.
    private static final DateTimeFormatter TIME_RANGE_FORMAT = DateTimeFormatter.ofPattern("HH:mm"); // Formato per orari di inizio/fine.
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("it", "IT")); // Formattatore monetario locale.
    private static final PseudoClass UNREAD_PSEUDO_CLASS = PseudoClass.getPseudoClass("unread"); // Pseudo-classe usata per notifiche non lette.

    @FXML // Esegue: @FXML
    private Label welcomeLabel; // Etichetta con il testo di benvenuto personalizzato.

    @FXML // Esegue: @FXML
    private Label teamLabel; // Etichetta che mostra il nome del team dell'utente.

    @FXML // Esegue: @FXML
    private Label userNameLabel; // Etichetta con il nome e cognome del dipendente.

    @FXML // Esegue: @FXML
    private Label userRoleLabel; // Etichetta che evidenzia il ruolo professionale.

    @FXML // Esegue: @FXML
    private Label userTeamLabel; // Etichetta che riepiloga il team di appartenenza.

    @FXML // Esegue: @FXML
    private Label userEmailLabel; // Etichetta con l'indirizzo email aziendale.

    private final ToggleGroup navigationGroup = new ToggleGroup(); // Gruppo che coordina la selezione dei pulsanti di navigazione.

    @FXML // Esegue: @FXML
    private ToggleButton utenteButton; // Pulsante che porta alla scheda utente.

    @FXML // Esegue: @FXML
    private ToggleButton agendaButton; // Pulsante che apre la sezione agenda.

    @FXML // Esegue: @FXML
    private ToggleButton chatInternaButton; // Pulsante dedicato alla chat interna del team.

    @FXML // Esegue: @FXML
    private ToggleButton chatEsternaButton; // Pulsante che mostra il pannello email/chat esterna.

    @FXML // Esegue: @FXML
    private ToggleButton notificheButton; // Pulsante che porta alla sezione notifiche.

    @FXML // Esegue: @FXML
    private ToggleButton fattureButton; // Pulsante che apre la sezione fatture.

    @FXML // Esegue: @FXML
    private ToggleButton pagamentiButton; // Pulsante che mostra la sezione pagamenti.

    @FXML // Esegue: @FXML
    private StackPane contentStack; // Contenitore che ospita tutti i pannelli della dashboard.

    @FXML // Esegue: @FXML
    private AnchorPane utentePane; // Pannello con le informazioni dell'utente.

    @FXML // Esegue: @FXML
    private AnchorPane agendaPane; // Pannello con la tabella degli appuntamenti.

    @FXML // Esegue: @FXML
    private AnchorPane chatInternaPane; // Pannello che visualizza la chat di team.

    @FXML // Esegue: @FXML
    private AnchorPane chatEsternaPane; // Pannello con email e messaggistica esterna.

    @FXML // Esegue: @FXML
    private AnchorPane notifichePane; // Pannello che mostra la lista notifiche.

    @FXML // Esegue: @FXML
    private AnchorPane fatturePane; // Pannello dedicato alla gestione delle fatture.

    @FXML // Esegue: @FXML
    private AnchorPane pagamentiPane; // Pannello che riepiloga i pagamenti.

    @FXML // Esegue: @FXML
    private TableView<AgendaItem> agendaTable; // Tabella che elenca tutti gli appuntamenti in agenda.

    @FXML // Esegue: @FXML
    private TableColumn<AgendaItem, String> agendaDateColumn; // Colonna che mostra la data dell'evento.

    @FXML // Esegue: @FXML
    private TableColumn<AgendaItem, String> agendaTimeColumn; // Colonna che mostra l'intervallo orario.

    @FXML // Esegue: @FXML
    private TableColumn<AgendaItem, String> agendaDescriptionColumn; // Colonna che visualizza la descrizione dell'evento.

    @FXML // Esegue: @FXML
    private TableColumn<AgendaItem, String> agendaLocationColumn; // Colonna che riporta il luogo dell'incontro.

    @FXML // Esegue: @FXML
    private ListView<ChatMessage> teamChatList; // Elenco dei messaggi scambiati nel team.

    @FXML // Esegue: @FXML
    private TextArea teamChatInput; // Area di input per scrivere un nuovo messaggio di team.

    @FXML
    private Button sendTeamMessageButton;

    @FXML // Esegue: @FXML
    private ListView<EmailMessage> emailList; // Lista che mostra le email ordinate cronologicamente.

    @FXML // Esegue: @FXML
    private Label emailSenderLabel; // Etichetta con mittente/destinatario della mail selezionata.

    @FXML // Esegue: @FXML
    private Label emailSubjectLabel; // Etichetta con l'oggetto della mail selezionata.

    @FXML // Esegue: @FXML
    private TextArea emailBodyPreview; // Area che visualizza il corpo della mail selezionata.

    @FXML // Esegue: @FXML
    private TextField emailRecipientField; // Campo per inserire il destinatario di una nuova email.

    @FXML // Esegue: @FXML
    private TextField emailSubjectField; // Campo per definire l'oggetto della nuova email.

    @FXML // Esegue: @FXML
    private TextArea emailBodyArea; // Area di testo per scrivere il contenuto dell'email.

    @FXML // Esegue: @FXML
    private Label emailStatusLabel; // Etichetta che comunica lo stato di invio dell'email.

    @FXML
    private Button sendEmailButton;

    @FXML // Esegue: @FXML
    private ListView<Notification> notificationsList; // Lista che raccoglie tutte le notifiche dell'utente.

    @FXML // Esegue: @FXML
    private Label notificationTitleLabel; // Etichetta che mostra il titolo della notifica selezionata.

    @FXML // Esegue: @FXML
    private Label notificationDateLabel; // Etichetta con la data e ora della notifica selezionata.

    @FXML // Esegue: @FXML
    private Label notificationStatusLabel; // Etichetta che segnala se la notifica è letta o meno.

    @FXML // Esegue: @FXML
    private TextArea notificationBodyArea; // Area di testo che contiene il messaggio della notifica.

    @FXML // Esegue: @FXML
    private TableView<InvoiceRecord> invoicesIssuedTable; // Tabella che elenca le fatture già emesse.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedNumberColumn; // Colonna con il numero della fattura emessa.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedDateColumn; // Colonna con la data di emissione.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedCustomerColumn; // Colonna con il nome del cliente fatturato.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedTotalColumn; // Colonna con l'importo della fattura emessa.

    @FXML // Esegue: @FXML
    private TableView<InvoiceRecord> invoicesDueTable; // Tabella che elenca le fatture in scadenza o sollecito.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueNumberColumn; // Colonna con il numero della fattura in sospeso.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueDateColumn; // Colonna con la data di scadenza della fattura.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueCustomerColumn; // Colonna con il nome del cliente debitore.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueTotalColumn; // Colonna con l'importo ancora dovuto.

    @FXML // Esegue: @FXML
    private TableView<InvoiceRecord> invoicesPaidTable; // Tabella che elenca le fatture già saldate.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidNumberColumn; // Colonna con il numero della fattura saldata.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidDateColumn; // Colonna con la data di saldo.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidCustomerColumn; // Colonna con il cliente che ha pagato.

    @FXML // Esegue: @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidTotalColumn; // Colonna con l'importo pagato.

    @FXML
    private TableView<InvoiceRecord> invoicesRegisteredTable;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceRegisteredNumberColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceRegisteredDateColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceRegisteredCustomerColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceRegisteredTotalColumn;

    @FXML
    private LineChart<String, Number> revenueTrendChart;

    @FXML // Esegue: @FXML
    private TableView<PaymentRecord> paymentsTable; // Tabella che riepiloga i pagamenti ricevuti.

    @FXML // Esegue: @FXML
    private TableColumn<PaymentRecord, String> paymentInvoiceColumn; // Colonna con il numero della fattura collegata.

    @FXML // Esegue: @FXML
    private TableColumn<PaymentRecord, String> paymentAmountColumn; // Colonna con l'importo del pagamento.

    @FXML // Esegue: @FXML
    private TableColumn<PaymentRecord, String> paymentDateColumn; // Colonna con la data di incasso.

    @FXML // Esegue: @FXML
    private TableColumn<PaymentRecord, String> paymentMethodColumn; // Colonna con il metodo di pagamento usato.

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        ToggleButton[] navigationButtons = {
                utenteButton,
                agendaButton,
                chatInternaButton,
                chatEsternaButton,
                notificheButton,
                fattureButton,
                pagamentiButton
        };
        for (ToggleButton button : navigationButtons) {
            if (button != null) {
                button.setToggleGroup(navigationGroup);
            }
        }
    }

    private FxDataService dataService; // Servizio dati condiviso dai controller.
    private Employee employee; // Dipendente attualmente visualizzato.
    private final Map<String, Command> commands = new HashMap<>(); // Mappa dei comandi invocabili tramite UI.
    private final List<AutoCloseable> eventSubscriptions = new ArrayList<>(); // Elenco delle sottoscrizioni registrate sul bus eventi.

    public void initializeData(FxDataService dataService, Employee employee) { // Inietta il servizio e il dipendente da mostrare.
        cleanupSubscriptions(); // Libera eventuali precedenti sottoscrizioni alla chat e al bus eventi.
        this.dataService = dataService; // Memorizza il servizio dati per usi futuri.
        this.employee = employee; // Memorizza il dipendente corrente.

        welcomeLabel.setText("Benvenuto, " + employee.fullName()); // Aggiorna il messaggio di benvenuto.
        teamLabel.setText("Team " + employee.teamName()); // Mostra il team del dipendente nell'intestazione.
        userNameLabel.setText(employee.fullName()); // Riporta il nome nella scheda profilo.
        userRoleLabel.setText(employee.role()); // Riporta il ruolo aziendale.
        userTeamLabel.setText(employee.teamName()); // Riporta il team di appartenenza.
        userEmailLabel.setText(employee.email()); // Riporta l'indirizzo email aziendale.

        configureAgenda(); // Popola la sezione agenda.
        configureTeamChat(); // Popola la sezione chat.
        configureEmailSection(); // Prepara la sezione email.
        configureNotifications(); // Prepara la sezione notifiche.
        configureInvoices(); // Prepara la sezione fatture.
        configurePayments(); // Prepara la sezione pagamenti.
        observeRevenueTrend();

        registerCommands(); // Registra i comandi necessari alla UI.
        configureNavigationButtons();
        configureActionButtons();
        subscribeToEvents(); // Sottoscrive gli handler agli eventi di dominio.

        showPane(utentePane); // Mostra il pannello utente come predefinito.
        navigationGroup.selectToggle(utenteButton); // Seleziona il pulsante di navigazione corrispondente.
        emailStatusLabel.setText(""); // Ripulisce lo stato di invio email.
    } // Esegue: }

    private void configureAgenda() { // Configura la tabella degli appuntamenti.
        agendaDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().start().toLocalDate().format(DATE_FORMAT))); // Mostra la data dell'impegno.
        agendaTimeColumn.setCellValueFactory(data -> { // Calcola l'intervallo orario da visualizzare.
            String start = data.getValue().start().format(TIME_RANGE_FORMAT); // Formatta l'orario di inizio.
            String end = data.getValue().end().format(TIME_RANGE_FORMAT); // Formatta l'orario di fine.
            return new SimpleStringProperty(start + " - " + end); // Restituisce l'intervallo come stringa.
        }); // Esegue: });
        agendaDescriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().description())); // Mostra la descrizione dell'impegno.
        agendaLocationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().location())); // Mostra la sede dell'incontro.
        agendaTable.setItems(dataService.getAgendaFor(employee)); // Carica gli appuntamenti dal servizio dati.
    } // Esegue: }

    private void configureTeamChat() { // Configura la ListView della chat interna.
        teamChatList.setItems(dataService.getTeamChat(employee)); // Recupera la lista osservabile dei messaggi di team.
        teamChatList.setCellFactory(listView -> new ListCell<>() { // Definisce una cella personalizzata per ogni messaggio.
            @Override // Esegue: @Override
            protected void updateItem(ChatMessage message, boolean empty) { // Aggiorna la rappresentazione del singolo messaggio.
                super.updateItem(message, empty); // Chiama la logica base di ListCell.
                if (empty || message == null) { // Se la cella non contiene un messaggio valido.
                    setText(null); // Rimuove eventuale testo residuo.
                } else { // Esegue: } else {
                    setText("[" + message.timestamp().format(DATE_TIME_FORMAT) + "] " + message.sender() + ": " + message.content()); // Mostra timestamp, autore e contenuto.
                } // Esegue: }
            } // Esegue: }
        }); // Esegue: });
        dataService.refreshTeamChat(employee); // Richiede un aggiornamento iniziale dei messaggi.
        dataService.connectTeamChat(employee); // Simula la connessione al canale per ricevere nuovi messaggi.
    } // Esegue: }

    private void configureEmailSection() { // Configura la sezione dedicata alle email.
        ObservableList<EmailMessage> emails = dataService.getEmailsFor(employee); // Recupera la lista di messaggi email dal servizio.
        emailList.setItems(emails); // Associa la lista alla ListView.
        emailList.setCellFactory(listView -> new ListCell<>() { // Personalizza la rappresentazione di ciascuna email.
            @Override // Esegue: @Override
            protected void updateItem(EmailMessage email, boolean empty) { // Aggiorna la cella quando cambia il contenuto.
                super.updateItem(email, empty); // Mantiene la logica predefinita.
                if (empty || email == null) { // Se la cella non contiene dati validi.
                    setText(null); // Svuota il testo della cella.
                } else { // Esegue: } else {
                    String prefix = email.incoming() ? "⬅" : "➡"; // Determina se l'email è in entrata o in uscita.
                    setText(prefix + " " + email.subject() + " (" + email.timestamp().format(DATE_TIME_FORMAT) + ")"); // Mostra direzione, oggetto e data.
                } // Esegue: }
            } // Esegue: }
        }); // Esegue: });
        emailList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showEmailDetail(newValue)); // Aggiorna i dettagli quando cambia la selezione.
        if (!emails.isEmpty()) { // Se sono presenti email.
            emailList.getSelectionModel().selectFirst(); // Seleziona automaticamente la prima email per mostrarne il dettaglio.
        } // Esegue: }
    } // Esegue: }

    private void configureNotifications() { // Esegue: private void configureNotifications() {
        ObservableList<Notification> notifications = dataService.getNotificationsFor(employee); // Esegue: ObservableList<Notification> notifications = dataService.getNotificationsFor(employee);
        notificationsList.setItems(notifications); // Esegue: notificationsList.setItems(notifications);
        notificationsList.setCellFactory(listView -> new ListCell<>() { // Esegue: notificationsList.setCellFactory(listView -> new ListCell<>() {
            private final VBox container = new VBox(4); // Esegue: private final VBox container = new VBox(4);
            private final Label titleLabel = new Label(); // Esegue: private final Label titleLabel = new Label();
            private final Label messageLabel = new Label(); // Esegue: private final Label messageLabel = new Label();
            private final Label dateLabel = new Label(); // Esegue: private final Label dateLabel = new Label();

            { // Esegue: {
                container.getStyleClass().add("notification-item"); // Esegue: container.getStyleClass().add("notification-item");
                container.prefWidthProperty().bind(listView.widthProperty().subtract(32)); // Esegue: container.prefWidthProperty().bind(listView.widthProperty().subtract(32));
                titleLabel.getStyleClass().add("notification-item-title"); // Esegue: titleLabel.getStyleClass().add("notification-item-title");
                titleLabel.setWrapText(true); // Esegue: titleLabel.setWrapText(true);
                titleLabel.setMaxWidth(Double.MAX_VALUE); // Esegue: titleLabel.setMaxWidth(Double.MAX_VALUE);
                messageLabel.getStyleClass().add("notification-item-message"); // Esegue: messageLabel.getStyleClass().add("notification-item-message");
                messageLabel.setWrapText(true); // Esegue: messageLabel.setWrapText(true);
                messageLabel.setMaxWidth(Double.MAX_VALUE); // Esegue: messageLabel.setMaxWidth(Double.MAX_VALUE);
                dateLabel.getStyleClass().add("notification-item-date"); // Esegue: dateLabel.getStyleClass().add("notification-item-date");
                container.getChildren().addAll(titleLabel, messageLabel, dateLabel); // Esegue: container.getChildren().addAll(titleLabel, messageLabel, dateLabel);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Esegue: setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } // Esegue: }

            @Override // Esegue: @Override
            protected void updateItem(Notification notification, boolean empty) { // Esegue: protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty); // Esegue: super.updateItem(notification, empty);
                if (empty || notification == null) { // Esegue: if (empty || notification == null) {
                    setGraphic(null); // Esegue: setGraphic(null);
                } else { // Esegue: } else {
                    titleLabel.setText(notification.title()); // Esegue: titleLabel.setText(notification.title());
                    messageLabel.setText(notification.message()); // Esegue: messageLabel.setText(notification.message());
                    dateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT)); // Esegue: dateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT));
                    container.pseudoClassStateChanged(UNREAD_PSEUDO_CLASS, !notification.read()); // Esegue: container.pseudoClassStateChanged(UNREAD_PSEUDO_CLASS, !notification.read());
                    setGraphic(container); // Esegue: setGraphic(container);
                } // Esegue: }
            } // Esegue: }
        }); // Esegue: });
        notificationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showNotificationDetail(newValue)); // Esegue: notificationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showNotificationDetail(newValue));
        if (!notifications.isEmpty()) { // Esegue: if (!notifications.isEmpty()) {
            notificationsList.getSelectionModel().selectFirst(); // Esegue: notificationsList.getSelectionModel().selectFirst();
        } // Esegue: }
    } // Esegue: }

    private void configureInvoices() { // Esegue: private void configureInvoices() {
        ObservableList<InvoiceRecord> invoices = dataService.getInvoicesFor(employee); // Esegue: ObservableList<InvoiceRecord> invoices = dataService.getInvoicesFor(employee);
        FilteredList<InvoiceRecord> issued = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.EMESSA); // Esegue: FilteredList<InvoiceRecord> issued = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.EMESSA);
        FilteredList<InvoiceRecord> due = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.IN_SOLLECITO); // Esegue: FilteredList<InvoiceRecord> due = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.IN_SOLLECITO);
        FilteredList<InvoiceRecord> paid = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.SALDATA); // Esegue: FilteredList<InvoiceRecord> paid = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.SALDATA);
        FilteredList<InvoiceRecord> registered = new FilteredList<>(invoices, InvoiceRecord::registered);

        configureInvoiceColumns(invoiceIssuedNumberColumn, invoiceIssuedDateColumn, invoiceIssuedCustomerColumn, invoiceIssuedTotalColumn); // Esegue: configureInvoiceColumns(invoiceIssuedNumberColumn, invoiceIssuedDateColumn, invoiceIssuedCustomerColumn, invoiceIssuedTotalColumn);
        configureInvoiceColumns(invoiceDueNumberColumn, invoiceDueDateColumn, invoiceDueCustomerColumn, invoiceDueTotalColumn); // Esegue: configureInvoiceColumns(invoiceDueNumberColumn, invoiceDueDateColumn, invoiceDueCustomerColumn, invoiceDueTotalColumn);
        configureInvoiceColumns(invoicePaidNumberColumn, invoicePaidDateColumn, invoicePaidCustomerColumn, invoicePaidTotalColumn); // Esegue: configureInvoiceColumns(invoicePaidNumberColumn, invoicePaidDateColumn, invoicePaidCustomerColumn, invoicePaidTotalColumn);
        configureInvoiceColumns(invoiceRegisteredNumberColumn, invoiceRegisteredDateColumn, invoiceRegisteredCustomerColumn, invoiceRegisteredTotalColumn);

        invoicesIssuedTable.setItems(issued); // Esegue: invoicesIssuedTable.setItems(issued);
        invoicesDueTable.setItems(due); // Esegue: invoicesDueTable.setItems(due);
        invoicesPaidTable.setItems(paid); // Esegue: invoicesPaidTable.setItems(paid);
        invoicesRegisteredTable.setItems(registered);
    } // Esegue: }

    private void configureInvoiceColumns(TableColumn<InvoiceRecord, String> numberColumn, // Esegue: private void configureInvoiceColumns(TableColumn<InvoiceRecord, String> numberColumn,
                                         TableColumn<InvoiceRecord, String> dateColumn, // Esegue: TableColumn<InvoiceRecord, String> dateColumn,
                                         TableColumn<InvoiceRecord, String> customerColumn, // Esegue: TableColumn<InvoiceRecord, String> customerColumn,
                                         TableColumn<InvoiceRecord, String> totalColumn) { // Esegue: TableColumn<InvoiceRecord, String> totalColumn) {
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().number())); // Esegue: numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().number()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().issueDate().format(DATE_FORMAT))); // Esegue: dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().issueDate().format(DATE_FORMAT)));
        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().customer())); // Esegue: customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().customer()));
        totalColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().total()))); // Esegue: totalColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().total())));
    } // Esegue: }

    private void observeRevenueTrend() {
        ObservableList<MonthlyRevenue> revenues = dataService.getRevenueTrend();
        revenues.addListener((ListChangeListener<MonthlyRevenue>) change -> updateRevenueChart(revenues));
        updateRevenueChart(revenues);
    }

    private void updateRevenueChart(List<? extends MonthlyRevenue> revenues) {
        if (revenueTrendChart == null) {
            return;
        }
        Runnable updateTask = () -> {
            revenueTrendChart.getData().clear();
            if (revenues == null || revenues.isEmpty()) {
                return;
            }
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (MonthlyRevenue revenue : revenues) {
                if (revenue != null) {
                    series.getData().add(new XYChart.Data<>(formatMonthLabel(revenue), revenue.total()));
                }
            }
            revenueTrendChart.getData().add(series);
        };
        if (Platform.isFxApplicationThread()) {
            updateTask.run();
        } else {
            Platform.runLater(updateTask);
        }
    }

    private String formatMonthLabel(MonthlyRevenue revenue) {
        if (revenue == null) {
            return "";
        }
        int monthValue = revenue.month();
        String monthName = monthValue >= 1 && monthValue <= 12
                ? Month.of(monthValue).getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
                : String.valueOf(monthValue);
        return monthName + " " + revenue.year();
    }

    private void configurePayments() { // Esegue: private void configurePayments() {
        paymentsTable.setItems(dataService.getPaymentsFor(employee)); // Esegue: paymentsTable.setItems(dataService.getPaymentsFor(employee));
        paymentInvoiceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().invoiceNumber())); // Esegue: paymentInvoiceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().invoiceNumber()));
        paymentAmountColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().amount()))); // Esegue: paymentAmountColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().amount())));
        paymentDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().paymentDate().format(DATE_FORMAT))); // Esegue: paymentDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().paymentDate().format(DATE_FORMAT)));
        paymentMethodColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().method())); // Esegue: paymentMethodColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().method()));
    } // Esegue: }

    private void registerCommands() { // Esegue: private void registerCommands() {
        commands.clear(); // Esegue: commands.clear();
        commands.put("showUtente", new ShowPaneCommand(this::showPane, utentePane)); // Esegue: commands.put("showUtente", new ShowPaneCommand(this::showPane, utentePane));
        commands.put("showAgenda", new ShowPaneCommand(this::showPane, agendaPane)); // Esegue: commands.put("showAgenda", new ShowPaneCommand(this::showPane, agendaPane));
        commands.put("showChatInterna", new ShowPaneCommand(this::showPane, chatInternaPane)); // Esegue: commands.put("showChatInterna", new ShowPaneCommand(this::showPane, chatInternaPane));
        commands.put("showChatEsterna", new ShowPaneCommand(this::showPane, chatEsternaPane)); // Esegue: commands.put("showChatEsterna", new ShowPaneCommand(this::showPane, chatEsternaPane));
        commands.put("showNotifiche", new ShowPaneCommand(this::showPane, notifichePane)); // Esegue: commands.put("showNotifiche", new ShowPaneCommand(this::showPane, notifichePane));
        commands.put("showFatture", new ShowPaneCommand(this::showPane, fatturePane)); // Esegue: commands.put("showFatture", new ShowPaneCommand(this::showPane, fatturePane));
        commands.put("showPagamenti", new ShowPaneCommand(this::showPane, pagamentiPane)); // Esegue: commands.put("showPagamenti", new ShowPaneCommand(this::showPane, pagamentiPane));
        commands.put("sendTeamMessage", new SendTeamMessageCommand(() -> teamChatInput.getText(), dataService, employee, teamChatInput, teamChatList)); // Esegue: commands.put("sendTeamMessage", new SendTeamMessageCommand(() -> teamChatInput.getText(), dataService, employee, teamChatInput, teamChatList));
        commands.put("sendEmail", new SendEmailCommand(() -> emailRecipientField.getText(), // Esegue: commands.put("sendEmail", new SendEmailCommand(() -> emailRecipientField.getText(),
                () -> emailSubjectField.getText(), // Esegue: () -> emailSubjectField.getText(),
                () -> emailBodyArea.getText(), // Esegue: () -> emailBodyArea.getText(),
                dataService, // Esegue: dataService,
                employee, // Esegue: employee,
                emailStatusLabel, // Esegue: emailStatusLabel,
                emailRecipientField, // Esegue: emailRecipientField,
                emailSubjectField, // Esegue: emailSubjectField,
                emailBodyArea)); // Esegue: emailBodyArea));
        commands.put("logout", new LogoutCommand(() -> (Stage) contentStack.getScene().getWindow(), dataService, this::cleanupSubscriptions)); // Esegue: commands.put("logout", new LogoutCommand(() -> (Stage) contentStack.getScene().getWindow(), dataService, this::cleanupSubscriptions));
    } // Esegue: }

    private void subscribeToEvents() { // Esegue: private void subscribeToEvents() {
        if (dataService == null) { // Esegue: if (dataService == null) {
            return; // Esegue: return;
        } // Esegue: }
        var bus = dataService.getEventBus(); // Esegue: var bus = dataService.getEventBus();
        eventSubscriptions.add(bus.subscribe(TeamMessageSentEvent.class, this::onTeamMessageSent)); // Esegue: eventSubscriptions.add(bus.subscribe(TeamMessageSentEvent.class, this::onTeamMessageSent));
        eventSubscriptions.add(bus.subscribe(EmailSentEvent.class, this::onEmailSent)); // Esegue: eventSubscriptions.add(bus.subscribe(EmailSentEvent.class, this::onEmailSent));
        eventSubscriptions.add(bus.subscribe(NotificationUpdatedEvent.class, this::onNotificationUpdated)); // Esegue: eventSubscriptions.add(bus.subscribe(NotificationUpdatedEvent.class, this::onNotificationUpdated));
    } // Esegue: }

    private void onTeamMessageSent(TeamMessageSentEvent event) { // Esegue: private void onTeamMessageSent(TeamMessageSentEvent event) {
        if (!event.message().teamName().equals(employee.teamName())) { // Esegue: if (!event.message().teamName().equals(employee.teamName())) {
            return; // Esegue: return;
        } // Esegue: }
        Platform.runLater(() -> { // Esegue: Platform.runLater(() -> {
            int lastIndex = teamChatList.getItems().size() - 1; // Esegue: int lastIndex = teamChatList.getItems().size() - 1;
            if (lastIndex >= 0) { // Esegue: if (lastIndex >= 0) {
                teamChatList.scrollTo(lastIndex); // Esegue: teamChatList.scrollTo(lastIndex);
            } // Esegue: }
        }); // Esegue: });
    } // Esegue: }

    private void onEmailSent(EmailSentEvent event) { // Esegue: private void onEmailSent(EmailSentEvent event) {
        if (!event.employee().id().equals(employee.id())) { // Esegue: if (!event.employee().id().equals(employee.id())) {
            return; // Esegue: return;
        } // Esegue: }
        Platform.runLater(() -> { // Esegue: Platform.runLater(() -> {
            emailStatusLabel.setText("Email inviata"); // Esegue: emailStatusLabel.setText("Email inviata");
            if (!emailList.getItems().isEmpty()) { // Esegue: if (!emailList.getItems().isEmpty()) {
                emailList.getSelectionModel().selectLast(); // Esegue: emailList.getSelectionModel().selectLast();
            } // Esegue: }
        }); // Esegue: });
    } // Esegue: }

    private void onNotificationUpdated(NotificationUpdatedEvent event) { // Esegue: private void onNotificationUpdated(NotificationUpdatedEvent event) {
        if (!event.employee().id().equals(employee.id())) { // Esegue: if (!event.employee().id().equals(employee.id())) {
            return; // Esegue: return;
        } // Esegue: }
        Platform.runLater(notificationsList::refresh); // Esegue: Platform.runLater(notificationsList::refresh);
    } // Esegue: }

    private void cleanupSubscriptions() { // Esegue: private void cleanupSubscriptions() {
        if (dataService != null && employee != null) { // Esegue: if (dataService != null && employee != null) {
            dataService.disconnectTeamChat(employee); // Esegue: dataService.disconnectTeamChat(employee);
        } // Esegue: }
        for (AutoCloseable subscription : eventSubscriptions) { // Esegue: for (AutoCloseable subscription : eventSubscriptions) {
            try { // Esegue: try {
                subscription.close(); // Esegue: subscription.close();
            } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                // nothing to do
            } // Esegue: }
        } // Esegue: }
        eventSubscriptions.clear(); // Esegue: eventSubscriptions.clear();
    } // Esegue: }

    private void executeCommand(String key) { // Esegue: private void executeCommand(String key) {
        Command command = commands.get(key); // Esegue: Command command = commands.get(key);
        if (command != null) { // Esegue: if (command != null) {
            command.execute(); // Esegue: command.execute();
        } // Esegue: }
    } // Esegue: }

    private void showEmailDetail(EmailMessage email) { // Esegue: private void showEmailDetail(EmailMessage email) {
        if (email == null) { // Esegue: if (email == null) {
            emailSenderLabel.setText(""); // Esegue: emailSenderLabel.setText("");
            emailSubjectLabel.setText(""); // Esegue: emailSubjectLabel.setText("");
            emailBodyPreview.clear(); // Esegue: emailBodyPreview.clear();
            return; // Esegue: return;
        } // Esegue: }
        String direction = email.incoming() ? "Da: " : "A: "; // Esegue: String direction = email.incoming() ? "Da: " : "A: ";
        emailSenderLabel.setText(direction + (email.incoming() ? email.sender() : email.recipient())); // Esegue: emailSenderLabel.setText(direction + (email.incoming() ? email.sender() : email.recipient()));
        emailSubjectLabel.setText(email.subject()); // Esegue: emailSubjectLabel.setText(email.subject());
        emailBodyPreview.setText(email.body()); // Esegue: emailBodyPreview.setText(email.body());
    } // Esegue: }

    private void showNotificationDetail(Notification notification) { // Esegue: private void showNotificationDetail(Notification notification) {
        if (notification == null) { // Esegue: if (notification == null) {
            notificationTitleLabel.setText(""); // Esegue: notificationTitleLabel.setText("");
            notificationDateLabel.setText(""); // Esegue: notificationDateLabel.setText("");
            notificationStatusLabel.setText(""); // Esegue: notificationStatusLabel.setText("");
            notificationBodyArea.clear(); // Esegue: notificationBodyArea.clear();
            return; // Esegue: return;
        } // Esegue: }
        notificationTitleLabel.setText(notification.title()); // Esegue: notificationTitleLabel.setText(notification.title());
        notificationDateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT)); // Esegue: notificationDateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT));
        notificationStatusLabel.setText(notification.read() ? "Letta" : "Nuova"); // Esegue: notificationStatusLabel.setText(notification.read() ? "Letta" : "Nuova");
        notificationBodyArea.setText(notification.message()); // Esegue: notificationBodyArea.setText(notification.message());
        if (!notification.read()) { // Esegue: if (!notification.read()) {
            dataService.markNotificationAsRead(employee, notification); // Esegue: dataService.markNotificationAsRead(employee, notification);
            notificationStatusLabel.setText("Letta"); // Esegue: notificationStatusLabel.setText("Letta");
        } // Esegue: }
    } // Esegue: }

    private void showPane(AnchorPane paneToShow) { // Esegue: private void showPane(AnchorPane paneToShow) {
        contentStack.getChildren().forEach(node -> { // Esegue: contentStack.getChildren().forEach(node -> {
            node.setVisible(node == paneToShow); // Esegue: node.setVisible(node == paneToShow);
            node.setManaged(node == paneToShow); // Esegue: node.setManaged(node == paneToShow);
        }); // Esegue: });
    } // Esegue: }

    @FXML
    private void handleNavigation(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof ToggleButton toggle) {
            Object commandKey = toggle.getUserData();
            if (commandKey instanceof String key) {
                executeCommand(key);
            }
        }
    }

    @FXML // Esegue: @FXML
    private void handleSendTeamMessage(ActionEvent event) { // Esegue: private void handleSendTeamMessage(ActionEvent event) {
        executeCommand("sendTeamMessage"); // Esegue: executeCommand("sendTeamMessage");
    } // Esegue: }

    @FXML // Esegue: @FXML
    private void handleSendEmail(ActionEvent event) { // Esegue: private void handleSendEmail(ActionEvent event) {
        executeCommand("sendEmail"); // Esegue: executeCommand("sendEmail");
    } // Esegue: }

    @FXML // Esegue: @FXML
    private void handleLogout(ActionEvent event) { // Esegue: private void handleLogout(ActionEvent event) {
        executeCommand("logout"); // Esegue: executeCommand("logout");
    } // Esegue: }

    private void configureNavigationButtons() {
        Map<ToggleButton, String> navigationCommands = Map.of(
                utenteButton, "showUtente",
                agendaButton, "showAgenda",
                chatInternaButton, "showChatInterna",
                chatEsternaButton, "showChatEsterna",
                notificheButton, "showNotifiche",
                fattureButton, "showFatture",
                pagamentiButton, "showPagamenti"
        );
        navigationCommands.forEach((button, commandKey) -> {
            if (button != null) {
                button.setUserData(commandKey);
                button.setOnAction(this::handleNavigation);
            }
        });
    }

    private void configureActionButtons() {
        if (sendTeamMessageButton != null) {
            sendTeamMessageButton.setOnAction(this::handleSendTeamMessage);
        }
        if (sendEmailButton != null) {
            sendEmailButton.setOnAction(this::handleSendEmail);
        }
        if (logoutButton != null) {
            logoutButton.setOnAction(this::handleLogout);
        }
    }
} // Esegue: }
