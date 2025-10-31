package com.example.GestoreAgenti.fx.data;

import com.example.GestoreAgenti.fx.data.remote.RemoteChatClient;
import com.example.GestoreAgenti.fx.event.EmailSentEvent;
import com.example.GestoreAgenti.fx.event.FxEventBus;
import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent;
import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent;
import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.fx.model.InvoiceState;
import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servizio che fornisce dati dimostrativi e funzionalità minime per la GUI JavaFX.
 */
public class FxDataService {

    private record EmployeeCredential(Employee employee, String password) {}

    private static final String GENERATED_ID_PREFIX = "C";
    private static final int GENERATED_ID_DIGITS = 4;

    private final Map<String, EmployeeCredential> credentials = new HashMap<>();
    private final Map<String, ObservableList<AgendaItem>> agendaByEmployee = new HashMap<>();
    private final Map<String, ObservableList<Notification>> notificationsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<InvoiceRecord>> invoicesByEmployee = new HashMap<>();
    private final Map<String, ObservableList<PaymentRecord>> paymentsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<ChatMessage>> chatByTeam = new HashMap<>();
    private final Map<String, ObservableList<EmailMessage>> emailsByEmployee = new HashMap<>();
    private final FxEventBus eventBus = new FxEventBus();
    private final EmployeeAdapter employeeAdapter = new EmployeeAdapter();
    private final ObservableList<String> availableTeams = FXCollections.observableArrayList();
    private final ObservableList<String> availableTeamsView = FXCollections.unmodifiableObservableList(availableTeams);
    private final ObservableList<String> availableRoles = FXCollections.observableArrayList("Junior", "Senior", "Responsabile");
    private final ObservableList<String> availableRolesView = FXCollections.unmodifiableObservableList(availableRoles);
    private final RemoteChatClient remoteChatClient = new RemoteChatClient();
    private final Map<String, AutoCloseable> chatSubscriptions = new ConcurrentHashMap<>();
    private final Set<String> desiredChatTeams = ConcurrentHashMap.newKeySet();

    private int nextEmployeeSequence;
    private RemoteAgentServiceProxy remoteAgentProxy;

    public FxDataService() {
        seedDemoData();
    }

    private void seedDemoData() {
        Employee mario = registerEmployee(new EmployeeDto("C0001", "Mario", "Rossi", "Account Manager", "Team Nord",
                "m.rossi@azienda.it"), "password1");
        Employee lucia = registerEmployee(new EmployeeDto("C0002", "Lucia", "Bianchi", "Consulente", "Team Nord",
                "l.bianchi@azienda.it"), "password2");
        Employee giulia = registerEmployee(new EmployeeDto("C0003", "Giulia", "Verdi", "Project Manager", "Team Centro",
                "g.verdi@azienda.it"), "password3");

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
        initializeNextEmployeeSequence();
    }

    private Employee registerEmployee(EmployeeDto dto, String password) {
        EmployeeDto normalized = dto;
        String identifier = normalized.id();
        if (identifier == null || identifier.isBlank()) {
            identifier = generateNextEmployeeId();
            normalized = new EmployeeDto(identifier, dto.firstName(), dto.lastName(), dto.role(), dto.team(), dto.email());
        }
        Employee employee = employeeAdapter.toModel(normalized);
        EmployeeCredential existing = credentials.get(employee.id());
        String resolvedPassword = password != null ? password : existing != null ? existing.password() : null;
        credentials.put(employee.id(), new EmployeeCredential(employee, resolvedPassword));
        ensureCollections(employee.id());
        addTeamName(employee.teamName());
        if (employee.teamName() != null && !employee.teamName().isBlank()) {
            chatByTeam.computeIfAbsent(employee.teamName(), key -> FXCollections.observableArrayList());
        }
        updateSequenceFromId(employee.id());
        return employee;
    }

    private void ensureCollections(String employeeId) {
        agendaByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        notificationsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        invoicesByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        paymentsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        emailsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
    }

    private void updateSequenceFromId(String id) {
        if (id == null || !id.startsWith(GENERATED_ID_PREFIX)) {
            return;
        }
        String numericPart = id.substring(GENERATED_ID_PREFIX.length());
        if (numericPart.length() != GENERATED_ID_DIGITS) {
            return;
        }
        try {
            int numericValue = Integer.parseInt(numericPart);
            nextEmployeeSequence = Math.max(nextEmployeeSequence, numericValue + 1);
        } catch (NumberFormatException ignored) {
            // Ignora identificativi non compatibili con il formato generato automaticamente.
        }
    }

    public Employee addEmployeeFromDto(EmployeeDto dto, String password) {
        return registerEmployee(dto, password);
    }

    public Optional<EmployeeDto> exportEmployee(String employeeId) {
        return Optional.ofNullable(credentials.get(employeeId))
                .map(EmployeeCredential::employee)
                .map(employeeAdapter::toDto);
    }

    public void configureRemoteAgentService(RemoteAgentService remoteAgentService) {
        this.remoteAgentProxy = remoteAgentService == null ? null : new RemoteAgentServiceProxy(remoteAgentService);
    }

    public int importFromRemote(String authToken) {
        if (remoteAgentProxy == null) {
            throw new IllegalStateException("Nessun servizio remoto configurato");
        }
        int imported = 0;
        for (EmployeeDto dto : remoteAgentProxy.fetchAgents(authToken)) {
            if (!credentials.containsKey(dto.id())) {
                registerEmployee(dto, null);
                imported++;
            }
        }
        return imported;
    }

    private void addTeamName(String teamName) {
        if (teamName == null) {
            return;
        }
        String trimmed = teamName.trim();
        if (!trimmed.isEmpty() && !availableTeams.contains(trimmed)) {
            availableTeams.add(trimmed);
        }
    }

    private void initializeNextEmployeeSequence() {
        nextEmployeeSequence = 1;
        credentials.keySet().forEach(this::updateSequenceFromId);
    }

    public String peekNextEmployeeId() {
        int sequence = nextEmployeeSequence;
        String candidate = formatGeneratedId(sequence);
        while (credentials.containsKey(candidate)) {
            sequence++;
            candidate = formatGeneratedId(sequence);
        }
        return candidate;
    }

    public synchronized String generateNextEmployeeId() {
        String nextId = peekNextEmployeeId();
        updateSequenceFromId(nextId);
        return nextId;
    }

    public ObservableList<String> getAvailableTeams() {
        return availableTeamsView;
    }

    public ObservableList<String> getAvailableRoles() {
        return availableRolesView;
    }

    private String formatGeneratedId(int sequence) {
        return GENERATED_ID_PREFIX + String.format("%0" + GENERATED_ID_DIGITS + "d", sequence);
    }

    public Optional<Employee> authenticate(String employeeId, String password) {
        return Optional.ofNullable(credentials.get(employeeId))
                .filter(credential -> credential.password().equals(password))
                .map(EmployeeCredential::employee);
    }

    public Optional<Employee> registerEmployee(String fullName, String role, String teamName, String email, String password) {
        String trimmedName = fullName == null ? "" : fullName.trim();
        String trimmedRole = role == null ? "" : role.trim();
        String trimmedTeam = teamName == null ? "" : teamName.trim();
        String trimmedEmail = email == null ? "" : email.trim();
        String trimmedPassword = password == null ? "" : password.trim();

        if (trimmedName.isEmpty() || trimmedRole.isEmpty()
                || trimmedTeam.isEmpty() || trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            return Optional.empty();
        }

        String generatedId = generateNextEmployeeId();
        Employee employee = new Employee(generatedId, trimmedName, trimmedRole, trimmedTeam, trimmedEmail);
        credentials.put(generatedId, new EmployeeCredential(employee, trimmedPassword));

        ensureCollections(generatedId);
        chatByTeam.computeIfAbsent(trimmedTeam, key -> FXCollections.observableArrayList());
        addTeamName(trimmedTeam);

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
        String trimmedMessage = message.trim();
        remoteChatClient.sendTeamMessage(employee.teamName(), employee.fullName(), trimmedMessage)
                .exceptionally(error -> {
                    System.err.println("Invio messaggio chat fallito: " + error.getMessage());
                    Platform.runLater(() -> {
                        ChatMessage fallback = new ChatMessage(employee.teamName(), employee.fullName(),
                                LocalDateTime.now(), trimmedMessage);
                        ObservableList<ChatMessage> chat = getTeamChat(employee);
                        chat.add(fallback);
                        eventBus.publish(new TeamMessageSentEvent(employee, fallback));
                    });
                    return null;
                });
    }

    public void connectTeamChat(Employee employee) {
        if (employee == null || employee.teamName() == null || employee.teamName().isBlank()) {
            return;
        }
        String teamName = employee.teamName().trim();
        disconnectTeamChat(employee);
        desiredChatTeams.add(teamName);
        remoteChatClient.subscribeToTeam(teamName,
                        message -> Platform.runLater(() -> {
                            ObservableList<ChatMessage> chat = getTeamChat(employee);
                            if (!chat.contains(message)) {
                                chat.add(message);
                                chat.sort(Comparator.comparing(ChatMessage::timestamp));
                                eventBus.publish(new TeamMessageSentEvent(employee, message));
                            }
                        }),
                        error -> System.err.println("Aggiornamento chat in tempo reale fallito: " + error.getMessage()))
                .thenAccept(subscription -> {
                    if (!desiredChatTeams.contains(teamName)) {
                        try {
                            subscription.close();
                        } catch (Exception ignored) {
                            // Ignored
                        }
                        return;
                    }
                    chatSubscriptions.put(teamName, subscription);
                })
                .exceptionally(error -> {
                    System.err.println("Connessione chat fallita: " + error.getMessage());
                    desiredChatTeams.remove(teamName);
                    return null;
                });
    }

    public void disconnectTeamChat(Employee employee) {
        if (employee == null || employee.teamName() == null || employee.teamName().isBlank()) {
            return;
        }
        String teamName = employee.teamName().trim();
        desiredChatTeams.remove(teamName);
        AutoCloseable subscription = chatSubscriptions.remove(teamName);
        if (subscription != null) {
            try {
                subscription.close();
            } catch (Exception ignored) {
                // Ignored
            }
        }
        remoteChatClient.disconnectFromTeam(teamName);
    }

    public void refreshTeamChat(Employee employee) {
        if (employee == null) {
            return;
        }
        remoteChatClient.fetchTeamMessages(employee.teamName())
                .thenAccept(messages -> Platform.runLater(() -> {
                    ObservableList<ChatMessage> chat = getTeamChat(employee);
                    chat.setAll(messages);
                    chat.sort(Comparator.comparing(ChatMessage::timestamp));
                }))
                .exceptionally(error -> {
                    System.err.println("Aggiornamento chat fallito: " + error.getMessage());
                    return null;
                });
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
        eventBus.publish(new EmailSentEvent(employee, outgoing));
    }

    public void markNotificationAsRead(Employee employee, Notification notification) {
        ObservableList<Notification> notifications = getNotificationsFor(employee);
        int index = notifications.indexOf(notification);
        if (index >= 0) {
            Notification updated = new Notification(notification.title(), notification.message(), notification.createdAt(), true);
            notifications.set(index, updated);
            eventBus.publish(new NotificationUpdatedEvent(employee, updated));
        }
    }

    public FxEventBus getEventBus() {
        return eventBus;
    }
}
