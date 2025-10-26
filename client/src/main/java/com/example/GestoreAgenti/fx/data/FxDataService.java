package com.example.GestoreAgenti.fx.data;

import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.fx.model.InvoiceState;
import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Servizio che fornisce dati dimostrativi e funzionalità minime per la GUI JavaFX.
 */
public class FxDataService {

    private record EmployeeCredential(Employee employee, String password) {}

    private final Map<String, EmployeeCredential> credentials = new HashMap<>();
    private final Map<String, ObservableList<AgendaItem>> agendaByEmployee = new HashMap<>();
    private final Map<String, ObservableList<Notification>> notificationsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<InvoiceRecord>> invoicesByEmployee = new HashMap<>();
    private final Map<String, ObservableList<PaymentRecord>> paymentsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<ChatMessage>> chatByTeam = new HashMap<>();
    private final Map<String, ObservableList<EmailMessage>> emailsByEmployee = new HashMap<>();

    public FxDataService() {
        seedDemoData();
    }

    private void seedDemoData() {
        Employee mario = new Employee("A001", "Mario Rossi", "Account Manager", "Team Nord", "m.rossi@azienda.it");
        Employee lucia = new Employee("A002", "Lucia Bianchi", "Consulente", "Team Nord", "l.bianchi@azienda.it");
        Employee giulia = new Employee("A003", "Giulia Verdi", "Project Manager", "Team Centro", "g.verdi@azienda.it");

        credentials.put(mario.id(), new EmployeeCredential(mario, "password1"));
        credentials.put(lucia.id(), new EmployeeCredential(lucia, "password2"));
        credentials.put(giulia.id(), new EmployeeCredential(giulia, "password3"));

        agendaByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 9, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 12, 10, 0),
                        "Call con cliente Alfa per rinnovo contratto", "Sala riunioni 1"),
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 14, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 12, 15, 30),
                        "Pianificazione trimestrale con il team", "Sala Nord")));
        agendaByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 13, 11, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 13, 12, 0),
                        "Onboarding nuovo cliente Beta", "Sala riunioni 2")));
        agendaByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 11, 16, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 11, 17, 0),
                        "Revisione avanzamento progetto Gamma", "Sala Centro")));

        notificationsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new Notification("Fattura in scadenza", "Il cliente Alfa ha una fattura da sollecitare", LocalDateTime.now().minusHours(5), false),
                new Notification("Nuovo documento", "È stato caricato il contratto firmato dal cliente Beta", LocalDateTime.now().minusDays(1), true)));
        notificationsByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new Notification("Aggiornamento agenda", "È stata aggiunta una visita sul territorio", LocalDateTime.now().minusHours(2), false)));
        notificationsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new Notification("Nuovo messaggio", "Mario Rossi ha inviato un aggiornamento", LocalDateTime.now().minusMinutes(30), false)));

        invoicesByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-001", LocalDate.of(2024, Month.NOVEMBER, 30), "Alfa S.p.A.", InvoiceState.EMESSA, new BigDecimal("1250.00")),
                new InvoiceRecord("FT-2024-002", LocalDate.of(2024, Month.OCTOBER, 15), "Gamma SRL", InvoiceState.IN_SOLLECITO, new BigDecimal("3200.00")),
                new InvoiceRecord("FT-2024-003", LocalDate.of(2024, Month.SEPTEMBER, 10), "Delta Consulting", InvoiceState.SALDATA, new BigDecimal("890.00"))));
        invoicesByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-004", LocalDate.of(2024, Month.NOVEMBER, 12), "Sigma Industries", InvoiceState.EMESSA, new BigDecimal("2100.00"))));
        invoicesByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-005", LocalDate.of(2024, Month.AUGUST, 2), "Omega Spa", InvoiceState.SALDATA, new BigDecimal("5100.00"))));

        paymentsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new PaymentRecord("FT-2024-003", new BigDecimal("890.00"), LocalDate.of(2024, Month.OCTOBER, 5), "Bonifico")));
        paymentsByEmployee.put(lucia.id(), FXCollections.observableArrayList());
        paymentsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new PaymentRecord("FT-2024-005", new BigDecimal("5100.00"), LocalDate.of(2024, Month.SEPTEMBER, 1), "Carta")));

        chatByTeam.put("Team Nord", FXCollections.observableArrayList(
                new ChatMessage("Team Nord", "Mario Rossi", LocalDateTime.now().minusMinutes(50), "Ricordatevi il meeting di oggi alle 14"),
                new ChatMessage("Team Nord", "Lucia Bianchi", LocalDateTime.now().minusMinutes(45), "Perfetto, porterò il report clienti")));
        chatByTeam.put("Team Centro", FXCollections.observableArrayList(
                new ChatMessage("Team Centro", "Giulia Verdi", LocalDateTime.now().minusMinutes(30), "Aggiornato il backlog del progetto Gamma")));

        emailsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new EmailMessage("clienti@alfa.it", mario.email(), "Richiesta aggiornamento",
                        "Buongiorno, potremmo avere un aggiornamento sullo stato del contratto?",
                        LocalDateTime.now().minusHours(6), true),
                new EmailMessage(mario.email(), "clienti@alfa.it", "Re: Richiesta aggiornamento",
                        "Certamente, vi invio il report entro oggi.", LocalDateTime.now().minusHours(5), false)));
        emailsByEmployee.put(lucia.id(), FXCollections.observableArrayList());
        emailsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new EmailMessage("amministrazione@omega.it", giulia.email(), "Fattura pagata",
                        "Buongiorno, vi confermiamo il pagamento della fattura FT-2024-005.",
                        LocalDateTime.now().minusDays(2), true)));
    }

    public Optional<Employee> authenticate(String employeeId, String password) {
        return Optional.ofNullable(credentials.get(employeeId))
                .filter(credential -> credential.password().equals(password))
                .map(EmployeeCredential::employee);
    }

    public Optional<Employee> registerEmployee(String id, String fullName, String role, String teamName, String email, String password) {
        String trimmedId = id == null ? "" : id.trim();
        String trimmedName = fullName == null ? "" : fullName.trim();
        String trimmedRole = role == null ? "" : role.trim();
        String trimmedTeam = teamName == null ? "" : teamName.trim();
        String trimmedEmail = email == null ? "" : email.trim();
        String trimmedPassword = password == null ? "" : password.trim();

        if (trimmedId.isEmpty() || trimmedName.isEmpty() || trimmedRole.isEmpty()
                || trimmedTeam.isEmpty() || trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            return Optional.empty();
        }

        if (credentials.containsKey(trimmedId)) {
            return Optional.empty();
        }

        Employee employee = new Employee(trimmedId, trimmedName, trimmedRole, trimmedTeam, trimmedEmail);
        credentials.put(trimmedId, new EmployeeCredential(employee, trimmedPassword));

        agendaByEmployee.computeIfAbsent(trimmedId, key -> FXCollections.observableArrayList());
        notificationsByEmployee.computeIfAbsent(trimmedId, key -> FXCollections.observableArrayList());
        invoicesByEmployee.computeIfAbsent(trimmedId, key -> FXCollections.observableArrayList());
        paymentsByEmployee.computeIfAbsent(trimmedId, key -> FXCollections.observableArrayList());
        emailsByEmployee.computeIfAbsent(trimmedId, key -> FXCollections.observableArrayList());
        chatByTeam.computeIfAbsent(trimmedTeam, key -> FXCollections.observableArrayList());

        return Optional.of(employee);
    }

    public ObservableList<AgendaItem> getAgendaFor(Employee employee) {
        return agendaByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public ObservableList<Notification> getNotificationsFor(Employee employee) {
        return notificationsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public ObservableList<InvoiceRecord> getInvoicesFor(Employee employee) {
        return invoicesByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public ObservableList<PaymentRecord> getPaymentsFor(Employee employee) {
        return paymentsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public ObservableList<ChatMessage> getTeamChat(Employee employee) {
        return chatByTeam.computeIfAbsent(employee.teamName(), key -> FXCollections.observableArrayList());
    }

    public void sendTeamMessage(Employee employee, String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        ObservableList<ChatMessage> chat = getTeamChat(employee);
        chat.add(new ChatMessage(employee.teamName(), employee.fullName(), LocalDateTime.now(), message.trim()));
    }

    public ObservableList<EmailMessage> getEmailsFor(Employee employee) {
        return emailsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public void sendEmail(Employee employee, String recipient, String subject, String body) {
        if (recipient == null || recipient.isBlank() || subject == null || subject.isBlank() || body == null || body.isBlank()) {
            return;
        }
        ObservableList<EmailMessage> emails = getEmailsFor(employee);
        EmailMessage outgoing = new EmailMessage(employee.email(), recipient.trim(), subject.trim(), body.trim(), LocalDateTime.now(), false);
        emails.add(outgoing);
        // Genera automaticamente una risposta del cliente per dimostrazione.
        emails.add(new EmailMessage(recipient.trim(), employee.email(), "Re: " + subject.trim(),
                "Grazie per l'aggiornamento, vi ricontatteremo a breve.", LocalDateTime.now(), true));
    }

    public void markNotificationAsRead(Employee employee, Notification notification) {
        ObservableList<Notification> notifications = getNotificationsFor(employee);
        int index = notifications.indexOf(notification);
        if (index >= 0) {
            notifications.set(index, new Notification(notification.title(), notification.message(), notification.createdAt(), true));
        }
    }
}
