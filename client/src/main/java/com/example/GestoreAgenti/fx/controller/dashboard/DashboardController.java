package com.example.GestoreAgenti.fx.controller.dashboard;

import com.example.GestoreAgenti.fx.command.Command;
import com.example.GestoreAgenti.fx.command.LogoutCommand;
import com.example.GestoreAgenti.fx.command.SendEmailCommand;
import com.example.GestoreAgenti.fx.command.SendTeamMessageCommand;
import com.example.GestoreAgenti.fx.command.ShowPaneCommand;
import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.event.EmailSentEvent;
import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent;
import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent;
import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.invoice.InvoiceState;
import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller della dashboard principale con tutte le sezioni richieste.
 */
public class DashboardController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter TIME_RANGE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("it", "IT"));
    private static final PseudoClass UNREAD_PSEUDO_CLASS = PseudoClass.getPseudoClass("unread");

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label teamLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label userTeamLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private ToggleGroup navigationGroup;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton utenteButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton agendaButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton chatInternaButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton chatEsternaButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton notificheButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton fattureButton;

    @SuppressWarnings("unused")
    @FXML
    private ToggleButton pagamentiButton;

    @FXML
    private StackPane contentStack;

    @FXML
    private AnchorPane utentePane;

    @FXML
    private AnchorPane agendaPane;

    @FXML
    private AnchorPane chatInternaPane;

    @FXML
    private AnchorPane chatEsternaPane;

    @FXML
    private AnchorPane notifichePane;

    @FXML
    private AnchorPane fatturePane;

    @FXML
    private AnchorPane pagamentiPane;

    @FXML
    private TableView<AgendaItem> agendaTable;

    @FXML
    private TableColumn<AgendaItem, String> agendaDateColumn;

    @FXML
    private TableColumn<AgendaItem, String> agendaTimeColumn;

    @FXML
    private TableColumn<AgendaItem, String> agendaDescriptionColumn;

    @FXML
    private TableColumn<AgendaItem, String> agendaLocationColumn;

    @FXML
    private ListView<ChatMessage> teamChatList;

    @FXML
    private TextArea teamChatInput;

    @FXML
    private ListView<EmailMessage> emailList;

    @FXML
    private Label emailSenderLabel;

    @FXML
    private Label emailSubjectLabel;

    @FXML
    private TextArea emailBodyPreview;

    @FXML
    private TextField emailRecipientField;

    @FXML
    private TextField emailSubjectField;

    @FXML
    private TextArea emailBodyArea;

    @FXML
    private Label emailStatusLabel;

    @FXML
    private ListView<Notification> notificationsList;

    @FXML
    private Label notificationTitleLabel;

    @FXML
    private Label notificationDateLabel;

    @FXML
    private Label notificationStatusLabel;

    @FXML
    private TextArea notificationBodyArea;

    @FXML
    private TableView<InvoiceRecord> invoicesIssuedTable;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedNumberColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedDateColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedCustomerColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceIssuedTotalColumn;

    @FXML
    private TableView<InvoiceRecord> invoicesDueTable;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueNumberColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueDateColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueCustomerColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoiceDueTotalColumn;

    @FXML
    private TableView<InvoiceRecord> invoicesPaidTable;

    @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidNumberColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidDateColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidCustomerColumn;

    @FXML
    private TableColumn<InvoiceRecord, String> invoicePaidTotalColumn;

    @FXML
    private TableView<PaymentRecord> paymentsTable;

    @FXML
    private TableColumn<PaymentRecord, String> paymentInvoiceColumn;

    @FXML
    private TableColumn<PaymentRecord, String> paymentAmountColumn;

    @FXML
    private TableColumn<PaymentRecord, String> paymentDateColumn;

    @FXML
    private TableColumn<PaymentRecord, String> paymentMethodColumn;

    private FxDataService dataService;
    private Employee employee;
    private final Map<String, Command> commands = new HashMap<>();
    private final List<AutoCloseable> eventSubscriptions = new ArrayList<>();

    public void initializeData(FxDataService dataService, Employee employee) {
        cleanupSubscriptions();
        this.dataService = dataService;
        this.employee = employee;

        welcomeLabel.setText("Benvenuto, " + employee.fullName());
        teamLabel.setText("Team " + employee.teamName());
        userNameLabel.setText(employee.fullName());
        userRoleLabel.setText(employee.role());
        userTeamLabel.setText(employee.teamName());
        userEmailLabel.setText(employee.email());

        configureAgenda();
        configureTeamChat();
        configureEmailSection();
        configureNotifications();
        configureInvoices();
        configurePayments();

        registerCommands();
        subscribeToEvents();

        showPane(utentePane);
        navigationGroup.selectToggle(utenteButton);
        emailStatusLabel.setText("");
    }

    private void configureAgenda() {
        agendaDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().start().toLocalDate().format(DATE_FORMAT)));
        agendaTimeColumn.setCellValueFactory(data -> {
            String start = data.getValue().start().format(TIME_RANGE_FORMAT);
            String end = data.getValue().end().format(TIME_RANGE_FORMAT);
            return new SimpleStringProperty(start + " - " + end);
        });
        agendaDescriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().description()));
        agendaLocationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().location()));
        agendaTable.setItems(dataService.getAgendaFor(employee));
    }

    private void configureTeamChat() {
        teamChatList.setItems(dataService.getTeamChat(employee));
        teamChatList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                } else {
                    setText("[" + message.timestamp().format(DATE_TIME_FORMAT) + "] " + message.sender() + ": " + message.content());
                }
            }
        });
        dataService.refreshTeamChat(employee);
        dataService.connectTeamChat(employee);
    }

    private void configureEmailSection() {
        ObservableList<EmailMessage> emails = dataService.getEmailsFor(employee);
        emailList.setItems(emails);
        emailList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(EmailMessage email, boolean empty) {
                super.updateItem(email, empty);
                if (empty || email == null) {
                    setText(null);
                } else {
                    String prefix = email.incoming() ? "⬅" : "➡";
                    setText(prefix + " " + email.subject() + " (" + email.timestamp().format(DATE_TIME_FORMAT) + ")");
                }
            }
        });
        emailList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showEmailDetail(newValue));
        if (!emails.isEmpty()) {
            emailList.getSelectionModel().selectFirst();
        }
    }

    private void configureNotifications() {
        ObservableList<Notification> notifications = dataService.getNotificationsFor(employee);
        notificationsList.setItems(notifications);
        notificationsList.setCellFactory(listView -> new ListCell<>() {
            private final VBox container = new VBox(4);
            private final Label titleLabel = new Label();
            private final Label messageLabel = new Label();
            private final Label dateLabel = new Label();

            {
                container.getStyleClass().add("notification-item");
                container.prefWidthProperty().bind(listView.widthProperty().subtract(32));
                titleLabel.getStyleClass().add("notification-item-title");
                titleLabel.setWrapText(true);
                titleLabel.setMaxWidth(Double.MAX_VALUE);
                messageLabel.getStyleClass().add("notification-item-message");
                messageLabel.setWrapText(true);
                messageLabel.setMaxWidth(Double.MAX_VALUE);
                dateLabel.getStyleClass().add("notification-item-date");
                container.getChildren().addAll(titleLabel, messageLabel, dateLabel);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty);
                if (empty || notification == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(notification.title());
                    messageLabel.setText(notification.message());
                    dateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT));
                    container.pseudoClassStateChanged(UNREAD_PSEUDO_CLASS, !notification.read());
                    setGraphic(container);
                }
            }
        });
        notificationsList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showNotificationDetail(newValue));
        if (!notifications.isEmpty()) {
            notificationsList.getSelectionModel().selectFirst();
        }
    }

    private void configureInvoices() {
        ObservableList<InvoiceRecord> invoices = dataService.getInvoicesFor(employee);
        FilteredList<InvoiceRecord> issued = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.EMESSA);
        FilteredList<InvoiceRecord> due = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.IN_SOLLECITO);
        FilteredList<InvoiceRecord> paid = new FilteredList<>(invoices, invoice -> invoice.state() == InvoiceState.SALDATA);

        configureInvoiceColumns(invoiceIssuedNumberColumn, invoiceIssuedDateColumn, invoiceIssuedCustomerColumn, invoiceIssuedTotalColumn);
        configureInvoiceColumns(invoiceDueNumberColumn, invoiceDueDateColumn, invoiceDueCustomerColumn, invoiceDueTotalColumn);
        configureInvoiceColumns(invoicePaidNumberColumn, invoicePaidDateColumn, invoicePaidCustomerColumn, invoicePaidTotalColumn);

        invoicesIssuedTable.setItems(issued);
        invoicesDueTable.setItems(due);
        invoicesPaidTable.setItems(paid);
    }

    private void configureInvoiceColumns(TableColumn<InvoiceRecord, String> numberColumn,
                                         TableColumn<InvoiceRecord, String> dateColumn,
                                         TableColumn<InvoiceRecord, String> customerColumn,
                                         TableColumn<InvoiceRecord, String> totalColumn) {
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().number()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().issueDate().format(DATE_FORMAT)));
        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().customer()));
        totalColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().total())));
    }

    private void configurePayments() {
        paymentsTable.setItems(dataService.getPaymentsFor(employee));
        paymentInvoiceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().invoiceNumber()));
        paymentAmountColumn.setCellValueFactory(data -> new SimpleStringProperty(CURRENCY_FORMAT.format(data.getValue().amount())));
        paymentDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().paymentDate().format(DATE_FORMAT)));
        paymentMethodColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().method()));
    }

    private void registerCommands() {
        commands.clear();
        commands.put("showUtente", new ShowPaneCommand(this::showPane, utentePane));
        commands.put("showAgenda", new ShowPaneCommand(this::showPane, agendaPane));
        commands.put("showChatInterna", new ShowPaneCommand(this::showPane, chatInternaPane));
        commands.put("showChatEsterna", new ShowPaneCommand(this::showPane, chatEsternaPane));
        commands.put("showNotifiche", new ShowPaneCommand(this::showPane, notifichePane));
        commands.put("showFatture", new ShowPaneCommand(this::showPane, fatturePane));
        commands.put("showPagamenti", new ShowPaneCommand(this::showPane, pagamentiPane));
        commands.put("sendTeamMessage", new SendTeamMessageCommand(() -> teamChatInput.getText(), dataService, employee, teamChatInput, teamChatList));
        commands.put("sendEmail", new SendEmailCommand(() -> emailRecipientField.getText(),
                () -> emailSubjectField.getText(),
                () -> emailBodyArea.getText(),
                dataService,
                employee,
                emailStatusLabel,
                emailRecipientField,
                emailSubjectField,
                emailBodyArea,
                emailList));
        commands.put("logout", new LogoutCommand(() -> (Stage) contentStack.getScene().getWindow(), dataService, this::cleanupSubscriptions));
    }

    private void subscribeToEvents() {
        if (dataService == null) {
            return;
        }
        var bus = dataService.getEventBus();
        eventSubscriptions.add(bus.subscribe(TeamMessageSentEvent.class, this::onTeamMessageSent));
        eventSubscriptions.add(bus.subscribe(EmailSentEvent.class, this::onEmailSent));
        eventSubscriptions.add(bus.subscribe(NotificationUpdatedEvent.class, this::onNotificationUpdated));
    }

    private void onTeamMessageSent(TeamMessageSentEvent event) {
        if (!event.message().teamName().equals(employee.teamName())) {
            return;
        }
        Platform.runLater(() -> {
            int lastIndex = teamChatList.getItems().size() - 1;
            if (lastIndex >= 0) {
                teamChatList.scrollTo(lastIndex);
            }
        });
    }

    private void onEmailSent(EmailSentEvent event) {
        if (!event.employee().id().equals(employee.id())) {
            return;
        }
        Platform.runLater(() -> {
            emailStatusLabel.setText("Email inviata");
            if (!emailList.getItems().isEmpty()) {
                emailList.getSelectionModel().selectLast();
            }
        });
    }

    private void onNotificationUpdated(NotificationUpdatedEvent event) {
        if (!event.employee().id().equals(employee.id())) {
            return;
        }
        Platform.runLater(notificationsList::refresh);
    }

    private void cleanupSubscriptions() {
        if (dataService != null && employee != null) {
            dataService.disconnectTeamChat(employee);
        }
        for (AutoCloseable subscription : eventSubscriptions) {
            try {
                subscription.close();
            } catch (Exception ignored) {
                // nothing to do
            }
        }
        eventSubscriptions.clear();
    }

    private void executeCommand(String key) {
        Command command = commands.get(key);
        if (command != null) {
            command.execute();
        }
    }

    private void showEmailDetail(EmailMessage email) {
        if (email == null) {
            emailSenderLabel.setText("");
            emailSubjectLabel.setText("");
            emailBodyPreview.clear();
            return;
        }
        String direction = email.incoming() ? "Da: " : "A: ";
        emailSenderLabel.setText(direction + (email.incoming() ? email.sender() : email.recipient()));
        emailSubjectLabel.setText(email.subject());
        emailBodyPreview.setText(email.body());
    }

    private void showNotificationDetail(Notification notification) {
        if (notification == null) {
            notificationTitleLabel.setText("");
            notificationDateLabel.setText("");
            notificationStatusLabel.setText("");
            notificationBodyArea.clear();
            return;
        }
        notificationTitleLabel.setText(notification.title());
        notificationDateLabel.setText(notification.createdAt().format(DATE_TIME_FORMAT));
        notificationStatusLabel.setText(notification.read() ? "Letta" : "Nuova");
        notificationBodyArea.setText(notification.message());
        if (!notification.read()) {
            dataService.markNotificationAsRead(employee, notification);
            notificationStatusLabel.setText("Letta");
        }
    }

    private void showPane(AnchorPane paneToShow) {
        contentStack.getChildren().forEach(node -> {
            node.setVisible(node == paneToShow);
            node.setManaged(node == paneToShow);
        });
    }

    @SuppressWarnings("unused")
    @FXML
    private void showUtente(ActionEvent event) {
        executeCommand("showUtente");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showAgenda(ActionEvent event) {
        executeCommand("showAgenda");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showChatInterna(ActionEvent event) {
        executeCommand("showChatInterna");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showChatEsterna(ActionEvent event) {
        executeCommand("showChatEsterna");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showNotifiche(ActionEvent event) {
        executeCommand("showNotifiche");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showFatture(ActionEvent event) {
        executeCommand("showFatture");
    }

    @SuppressWarnings("unused")
    @FXML
    private void showPagamenti(ActionEvent event) {
        executeCommand("showPagamenti");
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleSendTeamMessage(ActionEvent event) {
        executeCommand("sendTeamMessage");
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleSendEmail(ActionEvent event) {
        executeCommand("sendEmail");
    }

    @SuppressWarnings("unused")
    @FXML
    private void handleLogout(ActionEvent event) {
        executeCommand("logout");
    }
}
