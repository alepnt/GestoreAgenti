package com.example.GestoreAgenti.fx.controller.dashboard;

import com.example.GestoreAgenti.fx.controller.LoginController;
import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.fx.model.InvoiceState;
import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller della dashboard principale con tutte le sezioni richieste.
 */
public class DashboardController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter TIME_RANGE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("it", "IT"));

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

    @FXML
    private ToggleButton utenteButton;

    @FXML
    private ToggleButton agendaButton;

    @FXML
    private ToggleButton chatInternaButton;

    @FXML
    private ToggleButton chatEsternaButton;

    @FXML
    private ToggleButton notificheButton;

    @FXML
    private ToggleButton fattureButton;

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

    public void initializeData(FxDataService dataService, Employee employee) {
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
            @Override
            protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty);
                if (empty || notification == null) {
                    setText(null);
                } else {
                    String status = notification.read() ? "(letto)" : "(nuovo)";
                    setText(notification.title() + " " + status);
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
            notificationBodyArea.clear();
            return;
        }
        notificationTitleLabel.setText(notification.title());
        notificationBodyArea.setText(notification.message());
        if (!notification.read()) {
            dataService.markNotificationAsRead(employee, notification);
        }
    }

    private void showPane(AnchorPane paneToShow) {
        contentStack.getChildren().forEach(node -> {
            node.setVisible(node == paneToShow);
            node.setManaged(node == paneToShow);
        });
    }

    @FXML
    private void showUtente(ActionEvent event) {
        showPane(utentePane);
    }

    @FXML
    private void showAgenda(ActionEvent event) {
        showPane(agendaPane);
    }

    @FXML
    private void showChatInterna(ActionEvent event) {
        showPane(chatInternaPane);
    }

    @FXML
    private void showChatEsterna(ActionEvent event) {
        showPane(chatEsternaPane);
    }

    @FXML
    private void showNotifiche(ActionEvent event) {
        showPane(notifichePane);
    }

    @FXML
    private void showFatture(ActionEvent event) {
        showPane(fatturePane);
    }

    @FXML
    private void showPagamenti(ActionEvent event) {
        showPane(pagamentiPane);
    }

    @FXML
    private void handleSendTeamMessage(ActionEvent event) {
        String message = teamChatInput.getText();
        if (message == null || message.isBlank()) {
            return;
        }
        dataService.sendTeamMessage(employee, message);
        teamChatInput.clear();
        int lastIndex = teamChatList.getItems().size() - 1;
        if (lastIndex >= 0) {
            teamChatList.scrollTo(lastIndex);
        }
    }

    @FXML
    private void handleSendEmail(ActionEvent event) {
        String recipient = emailRecipientField.getText();
        String subject = emailSubjectField.getText();
        String body = emailBodyArea.getText();
        if (recipient == null || recipient.isBlank() || subject == null || subject.isBlank() || body == null || body.isBlank()) {
            emailStatusLabel.setText("Compila destinatario, oggetto e testo");
            return;
        }
        dataService.sendEmail(employee, recipient, subject, body);
        emailStatusLabel.setText("Email inviata");
        emailRecipientField.clear();
        emailSubjectField.clear();
        emailBodyArea.clear();
        if (!emailList.getItems().isEmpty()) {
            emailList.getSelectionModel().selectLast();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/login-view.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            Stage stage = (Stage) contentStack.getScene().getWindow();
            loginController.setPrimaryStage(stage);
            loginController.setDataService(dataService);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fx/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Gestore Agenti - Accesso dipendenti");
        } catch (IOException e) {
            // In un caso reale si mostrerebbe un avviso all'utente
        }
    }
}
