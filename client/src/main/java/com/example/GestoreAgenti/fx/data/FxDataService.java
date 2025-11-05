package com.example.GestoreAgenti.fx.data;

import com.example.GestoreAgenti.fx.data.adapter.EmployeeAdapter;
import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import com.example.GestoreAgenti.fx.data.remote.RemoteAgentService;
import com.example.GestoreAgenti.fx.data.remote.RemoteAgentServiceProxy;
import com.example.GestoreAgenti.fx.data.remote.RemoteChatClient;
import com.example.GestoreAgenti.fx.data.remote.RemoteEmailClient;
import com.example.GestoreAgenti.fx.event.EmailSentEvent;
import com.example.GestoreAgenti.fx.event.FxEventBus;
import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent;
import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent;
import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord;
import com.example.GestoreAgenti.invoice.InvoiceState;
import com.example.GestoreAgenti.security.UserRole;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;

/**
 * Servizio che fornisce dati dimostrativi e funzionalità minime per la GUI JavaFX.
 */
public class FxDataService {

    private record EmployeeCredential(Employee employee, String password) {}

    private static final String GENERATED_ID_PREFIX = "C";
    private static final int GENERATED_ID_DIGITS = 4;
    private static final Comparator<String> TEAM_NAME_COMPARATOR = Comparator.comparing(
            name -> name.toLowerCase(Locale.ROOT));

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
    private final ObservableList<String> availableRoles = FXCollections.observableArrayList(UserRole.getDisplayNames());
    private final ObservableList<String> availableRolesView = FXCollections.unmodifiableObservableList(availableRoles);
    private final Set<String> teamNames = new LinkedHashSet<>();
    private final RemoteChatClient remoteChatClient = new RemoteChatClient();
    private final RemoteEmailClient remoteEmailClient = new RemoteEmailClient();
    private final Map<String, AutoCloseable> chatSubscriptions = new ConcurrentHashMap<>();
    private final Set<String> desiredChatTeams = ConcurrentHashMap.newKeySet();

    private int nextEmployeeSequence;
    private RemoteAgentServiceProxy remoteAgentProxy;
    private Employee currentEmployee;

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

        String teamNord = requireTeamName("Team Nord");
        String teamCentro = requireTeamName("Team Centro");
        chatByTeam.put(teamNord, FXCollections.observableArrayList(
                new ChatMessage(teamNord, "Mario Rossi", LocalDateTime.now().minusMinutes(50), "Ricordatevi il meeting di oggi alle 14"),
                new ChatMessage(teamNord, "Lucia Bianchi", LocalDateTime.now().minusMinutes(45), "Perfetto, porterò il report clienti")));
        chatByTeam.put(teamCentro, FXCollections.observableArrayList(
                new ChatMessage(teamCentro, "Giulia Verdi", LocalDateTime.now().minusMinutes(30), "Aggiornato il backlog del progetto Gamma")));

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
        Employee employee = ensureNormalizedTeam(employeeAdapter.toModel(normalized));
        EmployeeCredential existing = credentials.get(employee.id());
        String resolvedPassword = password != null ? password : existing != null ? existing.password() : null;
        credentials.put(employee.id(), new EmployeeCredential(employee, resolvedPassword));
        ensureCollections(employee.id());
        addTeamName(employee.teamName());
        if (employee.teamName() != null) {
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
        String normalized = normalizeTeamName(teamName);
        if (normalized == null || !teamNames.add(normalized)) {
            return;
        }
        int insertionIndex = Collections.binarySearch(availableTeams, normalized, TEAM_NAME_COMPARATOR);
        if (insertionIndex < 0) {
            insertionIndex = -insertionIndex - 1;
        }
        availableTeams.add(insertionIndex, normalized);
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
        EmployeeCredential credential = credentials.get(employeeId);
        if (credential == null || !Objects.equals(credential.password(), password)) {
            currentEmployee = null;
            return Optional.empty();
        }
        currentEmployee = credential.employee();
        return Optional.of(currentEmployee);
    }

    public Optional<Employee> getCurrentEmployee() {
        return Optional.ofNullable(currentEmployee);
    }

    public void clearCurrentEmployee() {
        currentEmployee = null;
    }

    public Optional<Employee> registerEmployee(String fullName, String role, String teamName, String email, String password) {
        String normalizedName = normalizeRequired(fullName);
        UserRole resolvedRole;
        try {
            resolvedRole = UserRole.resolve(role);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
        String normalizedRole = resolvedRole.getDisplayName();
        String normalizedTeam = normalizeTeamName(teamName);
        String normalizedEmail = normalizeRequired(email);
        String normalizedPassword = normalizeRequired(password);

        if (normalizedName.isEmpty() || normalizedRole.isEmpty()
                || normalizedTeam == null || normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) {
            return Optional.empty();
        }

        String generatedId = generateNextEmployeeId();
        Employee employee = new Employee(generatedId, normalizedName, normalizedRole, normalizedTeam, normalizedEmail);
        credentials.put(generatedId, new EmployeeCredential(employee, normalizedPassword));

        ensureCollections(generatedId);
        chatByTeam.computeIfAbsent(normalizedTeam, key -> FXCollections.observableArrayList());
        addTeamName(normalizedTeam);

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
        Objects.requireNonNull(employee, "employee");
        String teamName = requireTeamName(employee.teamName());
        return chatByTeam.computeIfAbsent(teamName, key -> FXCollections.observableArrayList());
    }

    public void sendTeamMessage(Employee employee, String message) {
        Objects.requireNonNull(employee, "employee");
        if (message == null || message.isBlank()) {
            return;
        }
        String trimmedMessage = message.trim();
        String teamName = requireTeamName(employee.teamName());
        remoteChatClient.sendTeamMessage(teamName, employee.fullName(), trimmedMessage)
                .exceptionally(error -> {
                    System.err.println("Invio messaggio chat fallito: " + error.getMessage());
                    Platform.runLater(() -> {
                        ChatMessage fallback = new ChatMessage(teamName, employee.fullName(),
                                LocalDateTime.now(), trimmedMessage);
                        ObservableList<ChatMessage> chat = getTeamChat(employee);
                        chat.add(fallback);
                        eventBus.publish(new TeamMessageSentEvent(employee, fallback));
                    });
                    return null;
                });
    }

    public void connectTeamChat(Employee employee) {
        if (employee == null) {
            return;
        }
        String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) {
            return;
        }
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
        if (employee == null) {
            return;
        }
        String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) {
            return;
        }
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
        String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) {
            return;
        }
        remoteChatClient.fetchTeamMessages(teamName)
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

    private Employee ensureNormalizedTeam(Employee employee) {
        if (employee == null) {
            return null;
        }
        String normalizedTeam = normalizeTeamName(employee.teamName());
        if (Objects.equals(employee.teamName(), normalizedTeam)) {
            return employee;
        }
        return new Employee(employee.id(), employee.fullName(), employee.role(), normalizedTeam, employee.email());
    }

    private static String normalizeRequired(String value) {
        return value == null ? "" : value.trim();
    }

    private static String normalizeTeamName(String teamName) {
        if (teamName == null) {
            return null;
        }
        String trimmed = teamName.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String requireTeamName(String teamName) {
        String normalized = normalizeTeamName(teamName);
        if (normalized == null) {
            throw new IllegalArgumentException("Il nome del team non può essere vuoto");
        }
        return normalized;
    }

    public ObservableList<EmailMessage> getEmailsFor(Employee employee) {
        return emailsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    }

    public CompletableFuture<Void> sendEmail(Employee employee, String recipient, String subject, String body) {
        if (employee == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Dipendente non valido"));
        }
        if (recipient == null || recipient.isBlank() || subject == null || subject.isBlank() || body == null || body.isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Compila destinatario, oggetto e testo"));
        }
        String trimmedRecipient = recipient.trim();
        String trimmedSubject = subject.trim();
        String trimmedBody = body.trim();
        EmailMessage outgoing = new EmailMessage(employee.email(), trimmedRecipient, trimmedSubject, trimmedBody, LocalDateTime.now(), false);
        return remoteEmailClient.sendEmail(outgoing.sender(), outgoing.recipient(), outgoing.subject(), outgoing.body())
                .thenRun(() -> Platform.runLater(() -> {
                    ObservableList<EmailMessage> emails = getEmailsFor(employee);
                    emails.add(outgoing);
                    eventBus.publish(new EmailSentEvent(employee, outgoing));
                }));
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
