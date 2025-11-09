package com.example.GestoreAgenti.fx.data; // Esegue: package com.example.GestoreAgenti.fx.data;

import com.example.GestoreAgenti.fx.data.adapter.EmployeeAdapter; // Esegue: import com.example.GestoreAgenti.fx.data.adapter.EmployeeAdapter;
import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Esegue: import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import com.example.GestoreAgenti.fx.data.remote.RemoteAgentService; // Esegue: import com.example.GestoreAgenti.fx.data.remote.RemoteAgentService;
import com.example.GestoreAgenti.fx.data.remote.RemoteAgentServiceProxy; // Esegue: import com.example.GestoreAgenti.fx.data.remote.RemoteAgentServiceProxy;
import com.example.GestoreAgenti.fx.data.remote.RemoteChatClient; // Esegue: import com.example.GestoreAgenti.fx.data.remote.RemoteChatClient;
import com.example.GestoreAgenti.fx.data.remote.RemoteEmailClient; // Esegue: import com.example.GestoreAgenti.fx.data.remote.RemoteEmailClient;
import com.example.GestoreAgenti.fx.data.remote.RemoteTaskScheduler; // Esegue: import com.example.GestoreAgenti.fx.data.remote.RemoteTaskScheduler;
import com.example.GestoreAgenti.fx.event.EmailSentEvent; // Esegue: import com.example.GestoreAgenti.fx.event.EmailSentEvent;
import com.example.GestoreAgenti.fx.event.FxEventBus; // Esegue: import com.example.GestoreAgenti.fx.event.FxEventBus;
import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent; // Esegue: import com.example.GestoreAgenti.fx.event.NotificationUpdatedEvent;
import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent; // Esegue: import com.example.GestoreAgenti.fx.event.TeamMessageSentEvent;
import com.example.GestoreAgenti.fx.model.AgendaItem; // Esegue: import com.example.GestoreAgenti.fx.model.AgendaItem;
import com.example.GestoreAgenti.fx.model.ChatMessage; // Esegue: import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.EmailMessage; // Esegue: import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.InvoiceRecord; // Esegue: import com.example.GestoreAgenti.fx.model.InvoiceRecord;
import com.example.GestoreAgenti.fx.model.Notification; // Esegue: import com.example.GestoreAgenti.fx.model.Notification;
import com.example.GestoreAgenti.fx.model.PaymentRecord; // Esegue: import com.example.GestoreAgenti.fx.model.PaymentRecord;
import com.example.GestoreAgenti.invoice.InvoiceState; // Esegue: import com.example.GestoreAgenti.invoice.InvoiceState;
import com.example.GestoreAgenti.security.UserRole; // Esegue: import com.example.GestoreAgenti.security.UserRole;
import javafx.application.Platform; // Esegue: import javafx.application.Platform;
import javafx.collections.FXCollections; // Esegue: import javafx.collections.FXCollections;
import javafx.collections.ObservableList; // Esegue: import javafx.collections.ObservableList;

import java.math.BigDecimal; // Esegue: import java.math.BigDecimal;
import java.time.LocalDate; // Esegue: import java.time.LocalDate;
import java.time.LocalDateTime; // Esegue: import java.time.LocalDateTime;
import java.time.Month; // Esegue: import java.time.Month;
import java.util.Collections; // Esegue: import java.util.Collections;
import java.util.Comparator; // Esegue: import java.util.Comparator;
import java.util.HashMap; // Esegue: import java.util.HashMap;
import java.util.LinkedHashSet; // Esegue: import java.util.LinkedHashSet;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.Optional; // Esegue: import java.util.Optional;
import java.util.Set; // Esegue: import java.util.Set;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap; // Esegue: import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale; // Esegue: import java.util.Locale;

/**
 * Servizio che fornisce dati dimostrativi e funzionalità minime per la GUI JavaFX.
 */
public class FxDataService { // Esegue: public class FxDataService {

    private record EmployeeCredential(Employee employee, String password) {} // Esegue: private record EmployeeCredential(Employee employee, String password) {}

    private static final String GENERATED_ID_PREFIX = "C"; // Esegue: private static final String GENERATED_ID_PREFIX = "C";
    private static final int GENERATED_ID_DIGITS = 4; // Esegue: private static final int GENERATED_ID_DIGITS = 4;
    private static final Comparator<String> TEAM_NAME_COMPARATOR = Comparator.comparing( // Esegue: private static final Comparator<String> TEAM_NAME_COMPARATOR = Comparator.comparing(
            name -> name.toLowerCase(Locale.ROOT)); // Esegue: name -> name.toLowerCase(Locale.ROOT));

    private final Map<String, EmployeeCredential> credentials = new HashMap<>(); // Esegue: private final Map<String, EmployeeCredential> credentials = new HashMap<>();
    private final Map<String, ObservableList<AgendaItem>> agendaByEmployee = new HashMap<>(); // Esegue: private final Map<String, ObservableList<AgendaItem>> agendaByEmployee = new HashMap<>();
    private final Map<String, ObservableList<Notification>> notificationsByEmployee = new HashMap<>(); // Esegue: private final Map<String, ObservableList<Notification>> notificationsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<InvoiceRecord>> invoicesByEmployee = new HashMap<>(); // Esegue: private final Map<String, ObservableList<InvoiceRecord>> invoicesByEmployee = new HashMap<>();
    private final Map<String, ObservableList<PaymentRecord>> paymentsByEmployee = new HashMap<>(); // Esegue: private final Map<String, ObservableList<PaymentRecord>> paymentsByEmployee = new HashMap<>();
    private final Map<String, ObservableList<ChatMessage>> chatByTeam = new HashMap<>(); // Esegue: private final Map<String, ObservableList<ChatMessage>> chatByTeam = new HashMap<>();
    private final Map<String, ObservableList<EmailMessage>> emailsByEmployee = new HashMap<>(); // Esegue: private final Map<String, ObservableList<EmailMessage>> emailsByEmployee = new HashMap<>();
    private final FxEventBus eventBus = new FxEventBus(); // Esegue: private final FxEventBus eventBus = new FxEventBus();
    private final EmployeeAdapter employeeAdapter = new EmployeeAdapter(); // Esegue: private final EmployeeAdapter employeeAdapter = new EmployeeAdapter();
    private final ObservableList<String> availableTeams = FXCollections.observableArrayList(); // Esegue: private final ObservableList<String> availableTeams = FXCollections.observableArrayList();
    private final ObservableList<String> availableTeamsView = FXCollections.unmodifiableObservableList(availableTeams); // Esegue: private final ObservableList<String> availableTeamsView = FXCollections.unmodifiableObservableList(availableTeams);
    private final ObservableList<String> availableRoles = FXCollections.observableArrayList(UserRole.getDisplayNames()); // Esegue: private final ObservableList<String> availableRoles = FXCollections.observableArrayList(UserRole.getDisplayNames());
    private final ObservableList<String> availableRolesView = FXCollections.unmodifiableObservableList(availableRoles); // Esegue: private final ObservableList<String> availableRolesView = FXCollections.unmodifiableObservableList(availableRoles);
    private final Set<String> teamNames = new LinkedHashSet<>(); // Esegue: private final Set<String> teamNames = new LinkedHashSet<>();
    private static final int REMOTE_MAX_CONCURRENCY = 4; // Esegue: private static final int REMOTE_MAX_CONCURRENCY = 4;
    private final RemoteTaskScheduler remoteTaskScheduler = new RemoteTaskScheduler(REMOTE_MAX_CONCURRENCY); // Esegue: private final RemoteTaskScheduler remoteTaskScheduler = new RemoteTaskScheduler(REMOTE_MAX_CONCURRENCY);
    private final RemoteChatClient remoteChatClient = new RemoteChatClient(remoteTaskScheduler); // Esegue: private final RemoteChatClient remoteChatClient = new RemoteChatClient(remoteTaskScheduler);
    private final RemoteEmailClient remoteEmailClient = new RemoteEmailClient(remoteTaskScheduler); // Esegue: private final RemoteEmailClient remoteEmailClient = new RemoteEmailClient(remoteTaskScheduler);
    private final Map<String, AutoCloseable> chatSubscriptions = new ConcurrentHashMap<>(); // Esegue: private final Map<String, AutoCloseable> chatSubscriptions = new ConcurrentHashMap<>();
    private final Set<String> desiredChatTeams = ConcurrentHashMap.newKeySet(); // Esegue: private final Set<String> desiredChatTeams = ConcurrentHashMap.newKeySet();

    private int nextEmployeeSequence; // Esegue: private int nextEmployeeSequence;
    private RemoteAgentServiceProxy remoteAgentProxy; // Esegue: private RemoteAgentServiceProxy remoteAgentProxy;
    private Employee currentEmployee; // Esegue: private Employee currentEmployee;

    public FxDataService() { // Esegue: public FxDataService() {
        seedDemoData(); // Esegue: seedDemoData();
    } // Esegue: }

    private void seedDemoData() { // Esegue: private void seedDemoData() {
        Employee mario = registerEmployee(new EmployeeDto("C0001", "Mario", "Rossi", "Account Manager", "Team Nord", // Esegue: Employee mario = registerEmployee(new EmployeeDto("C0001", "Mario", "Rossi", "Account Manager", "Team Nord",
                "m.rossi@azienda.it"), "password1"); // Esegue: "m.rossi@azienda.it"), "password1");
        Employee lucia = registerEmployee(new EmployeeDto("C0002", "Lucia", "Bianchi", "Consulente", "Team Nord", // Esegue: Employee lucia = registerEmployee(new EmployeeDto("C0002", "Lucia", "Bianchi", "Consulente", "Team Nord",
                "l.bianchi@azienda.it"), "password2"); // Esegue: "l.bianchi@azienda.it"), "password2");
        Employee giulia = registerEmployee(new EmployeeDto("C0003", "Giulia", "Verdi", "Project Manager", "Team Centro", // Esegue: Employee giulia = registerEmployee(new EmployeeDto("C0003", "Giulia", "Verdi", "Project Manager", "Team Centro",
                "g.verdi@azienda.it"), "password3"); // Esegue: "g.verdi@azienda.it"), "password3");

        agendaByEmployee.put(mario.id(), FXCollections.observableArrayList( // Esegue: agendaByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 9, 0), // Esegue: new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 9, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 12, 10, 0), // Esegue: LocalDateTime.of(2024, Month.DECEMBER, 12, 10, 0),
                        "Call con cliente Alfa per rinnovo contratto", "Sala riunioni 1"), // Esegue: "Call con cliente Alfa per rinnovo contratto", "Sala riunioni 1"),
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 14, 0), // Esegue: new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 12, 14, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 12, 15, 30), // Esegue: LocalDateTime.of(2024, Month.DECEMBER, 12, 15, 30),
                        "Pianificazione trimestrale con il team", "Sala Nord"))); // Esegue: "Pianificazione trimestrale con il team", "Sala Nord")));
        agendaByEmployee.put(lucia.id(), FXCollections.observableArrayList( // Esegue: agendaByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 13, 11, 0), // Esegue: new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 13, 11, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 13, 12, 0), // Esegue: LocalDateTime.of(2024, Month.DECEMBER, 13, 12, 0),
                        "Onboarding nuovo cliente Beta", "Sala riunioni 2"))); // Esegue: "Onboarding nuovo cliente Beta", "Sala riunioni 2")));
        agendaByEmployee.put(giulia.id(), FXCollections.observableArrayList( // Esegue: agendaByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 11, 16, 0), // Esegue: new AgendaItem(LocalDateTime.of(2024, Month.DECEMBER, 11, 16, 0),
                        LocalDateTime.of(2024, Month.DECEMBER, 11, 17, 0), // Esegue: LocalDateTime.of(2024, Month.DECEMBER, 11, 17, 0),
                        "Revisione avanzamento progetto Gamma", "Sala Centro"))); // Esegue: "Revisione avanzamento progetto Gamma", "Sala Centro")));

        notificationsByEmployee.put(mario.id(), FXCollections.observableArrayList( // Esegue: notificationsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new Notification("Fattura in scadenza", "Il cliente Alfa ha una fattura da sollecitare", LocalDateTime.now().minusHours(5), false), // Esegue: new Notification("Fattura in scadenza", "Il cliente Alfa ha una fattura da sollecitare", LocalDateTime.now().minusHours(5), false),
                new Notification("Nuovo documento", "È stato caricato il contratto firmato dal cliente Beta", LocalDateTime.now().minusDays(1), true))); // Esegue: new Notification("Nuovo documento", "È stato caricato il contratto firmato dal cliente Beta", LocalDateTime.now().minusDays(1), true)));
        notificationsByEmployee.put(lucia.id(), FXCollections.observableArrayList( // Esegue: notificationsByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new Notification("Aggiornamento agenda", "È stata aggiunta una visita sul territorio", LocalDateTime.now().minusHours(2), false))); // Esegue: new Notification("Aggiornamento agenda", "È stata aggiunta una visita sul territorio", LocalDateTime.now().minusHours(2), false)));
        notificationsByEmployee.put(giulia.id(), FXCollections.observableArrayList( // Esegue: notificationsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new Notification("Nuovo messaggio", "Mario Rossi ha inviato un aggiornamento", LocalDateTime.now().minusMinutes(30), false))); // Esegue: new Notification("Nuovo messaggio", "Mario Rossi ha inviato un aggiornamento", LocalDateTime.now().minusMinutes(30), false)));

        invoicesByEmployee.put(mario.id(), FXCollections.observableArrayList( // Esegue: invoicesByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-001", LocalDate.of(2024, Month.NOVEMBER, 30), "Alfa S.p.A.", InvoiceState.EMESSA, new BigDecimal("1250.00")), // Esegue: new InvoiceRecord("FT-2024-001", LocalDate.of(2024, Month.NOVEMBER, 30), "Alfa S.p.A.", InvoiceState.EMESSA, new BigDecimal("1250.00")),
                new InvoiceRecord("FT-2024-002", LocalDate.of(2024, Month.OCTOBER, 15), "Gamma SRL", InvoiceState.IN_SOLLECITO, new BigDecimal("3200.00")), // Esegue: new InvoiceRecord("FT-2024-002", LocalDate.of(2024, Month.OCTOBER, 15), "Gamma SRL", InvoiceState.IN_SOLLECITO, new BigDecimal("3200.00")),
                new InvoiceRecord("FT-2024-003", LocalDate.of(2024, Month.SEPTEMBER, 10), "Delta Consulting", InvoiceState.SALDATA, new BigDecimal("890.00")))); // Esegue: new InvoiceRecord("FT-2024-003", LocalDate.of(2024, Month.SEPTEMBER, 10), "Delta Consulting", InvoiceState.SALDATA, new BigDecimal("890.00"))));
        invoicesByEmployee.put(lucia.id(), FXCollections.observableArrayList( // Esegue: invoicesByEmployee.put(lucia.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-004", LocalDate.of(2024, Month.NOVEMBER, 12), "Sigma Industries", InvoiceState.EMESSA, new BigDecimal("2100.00")))); // Esegue: new InvoiceRecord("FT-2024-004", LocalDate.of(2024, Month.NOVEMBER, 12), "Sigma Industries", InvoiceState.EMESSA, new BigDecimal("2100.00"))));
        invoicesByEmployee.put(giulia.id(), FXCollections.observableArrayList( // Esegue: invoicesByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new InvoiceRecord("FT-2024-005", LocalDate.of(2024, Month.AUGUST, 2), "Omega Spa", InvoiceState.SALDATA, new BigDecimal("5100.00")))); // Esegue: new InvoiceRecord("FT-2024-005", LocalDate.of(2024, Month.AUGUST, 2), "Omega Spa", InvoiceState.SALDATA, new BigDecimal("5100.00"))));

        paymentsByEmployee.put(mario.id(), FXCollections.observableArrayList( // Esegue: paymentsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new PaymentRecord("FT-2024-003", new BigDecimal("890.00"), LocalDate.of(2024, Month.OCTOBER, 5), "Bonifico"))); // Esegue: new PaymentRecord("FT-2024-003", new BigDecimal("890.00"), LocalDate.of(2024, Month.OCTOBER, 5), "Bonifico")));
        paymentsByEmployee.put(lucia.id(), FXCollections.observableArrayList()); // Esegue: paymentsByEmployee.put(lucia.id(), FXCollections.observableArrayList());
        paymentsByEmployee.put(giulia.id(), FXCollections.observableArrayList( // Esegue: paymentsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new PaymentRecord("FT-2024-005", new BigDecimal("5100.00"), LocalDate.of(2024, Month.SEPTEMBER, 1), "Carta"))); // Esegue: new PaymentRecord("FT-2024-005", new BigDecimal("5100.00"), LocalDate.of(2024, Month.SEPTEMBER, 1), "Carta")));

        String teamNord = requireTeamName("Team Nord"); // Esegue: String teamNord = requireTeamName("Team Nord");
        String teamCentro = requireTeamName("Team Centro"); // Esegue: String teamCentro = requireTeamName("Team Centro");
        chatByTeam.put(teamNord, FXCollections.observableArrayList( // Esegue: chatByTeam.put(teamNord, FXCollections.observableArrayList(
                new ChatMessage(teamNord, "Mario Rossi", LocalDateTime.now().minusMinutes(50), "Ricordatevi il meeting di oggi alle 14"), // Esegue: new ChatMessage(teamNord, "Mario Rossi", LocalDateTime.now().minusMinutes(50), "Ricordatevi il meeting di oggi alle 14"),
                new ChatMessage(teamNord, "Lucia Bianchi", LocalDateTime.now().minusMinutes(45), "Perfetto, porterò il report clienti"))); // Esegue: new ChatMessage(teamNord, "Lucia Bianchi", LocalDateTime.now().minusMinutes(45), "Perfetto, porterò il report clienti")));
        chatByTeam.put(teamCentro, FXCollections.observableArrayList( // Esegue: chatByTeam.put(teamCentro, FXCollections.observableArrayList(
                new ChatMessage(teamCentro, "Giulia Verdi", LocalDateTime.now().minusMinutes(30), "Aggiornato il backlog del progetto Gamma"))); // Esegue: new ChatMessage(teamCentro, "Giulia Verdi", LocalDateTime.now().minusMinutes(30), "Aggiornato il backlog del progetto Gamma")));

        emailsByEmployee.put(mario.id(), FXCollections.observableArrayList( // Esegue: emailsByEmployee.put(mario.id(), FXCollections.observableArrayList(
                new EmailMessage("clienti@alfa.it", mario.email(), "Richiesta aggiornamento", // Esegue: new EmailMessage("clienti@alfa.it", mario.email(), "Richiesta aggiornamento",
                        "Buongiorno, potremmo avere un aggiornamento sullo stato del contratto?", // Esegue: "Buongiorno, potremmo avere un aggiornamento sullo stato del contratto?",
                        LocalDateTime.now().minusHours(6), true), // Esegue: LocalDateTime.now().minusHours(6), true),
                new EmailMessage(mario.email(), "clienti@alfa.it", "Re: Richiesta aggiornamento", // Esegue: new EmailMessage(mario.email(), "clienti@alfa.it", "Re: Richiesta aggiornamento",
                        "Certamente, vi invio il report entro oggi.", LocalDateTime.now().minusHours(5), false))); // Esegue: "Certamente, vi invio il report entro oggi.", LocalDateTime.now().minusHours(5), false)));
        emailsByEmployee.put(lucia.id(), FXCollections.observableArrayList()); // Esegue: emailsByEmployee.put(lucia.id(), FXCollections.observableArrayList());
        emailsByEmployee.put(giulia.id(), FXCollections.observableArrayList( // Esegue: emailsByEmployee.put(giulia.id(), FXCollections.observableArrayList(
                new EmailMessage("amministrazione@omega.it", giulia.email(), "Fattura pagata", // Esegue: new EmailMessage("amministrazione@omega.it", giulia.email(), "Fattura pagata",
                        "Buongiorno, vi confermiamo il pagamento della fattura FT-2024-005.", // Esegue: "Buongiorno, vi confermiamo il pagamento della fattura FT-2024-005.",
                        LocalDateTime.now().minusDays(2), true))); // Esegue: LocalDateTime.now().minusDays(2), true)));
        initializeNextEmployeeSequence(); // Esegue: initializeNextEmployeeSequence();
    } // Esegue: }

    private Employee registerEmployee(EmployeeDto dto, String password) { // Esegue: private Employee registerEmployee(EmployeeDto dto, String password) {
        EmployeeDto normalized = dto; // Esegue: EmployeeDto normalized = dto;
        String identifier = normalized.id(); // Esegue: String identifier = normalized.id();
        if (identifier == null || identifier.isBlank()) { // Esegue: if (identifier == null || identifier.isBlank()) {
            identifier = generateNextEmployeeId(); // Esegue: identifier = generateNextEmployeeId();
            normalized = new EmployeeDto(identifier, dto.firstName(), dto.lastName(), dto.role(), dto.team(), dto.email()); // Esegue: normalized = new EmployeeDto(identifier, dto.firstName(), dto.lastName(), dto.role(), dto.team(), dto.email());
        } // Esegue: }
        Employee employee = ensureNormalizedTeam(employeeAdapter.toModel(normalized)); // Esegue: Employee employee = ensureNormalizedTeam(employeeAdapter.toModel(normalized));
        EmployeeCredential existing = credentials.get(employee.id()); // Esegue: EmployeeCredential existing = credentials.get(employee.id());
        String resolvedPassword = password != null ? password : existing != null ? existing.password() : null; // Esegue: String resolvedPassword = password != null ? password : existing != null ? existing.password() : null;
        credentials.put(employee.id(), new EmployeeCredential(employee, resolvedPassword)); // Esegue: credentials.put(employee.id(), new EmployeeCredential(employee, resolvedPassword));
        ensureCollections(employee.id()); // Esegue: ensureCollections(employee.id());
        addTeamName(employee.teamName()); // Esegue: addTeamName(employee.teamName());
        if (employee.teamName() != null) { // Esegue: if (employee.teamName() != null) {
            chatByTeam.computeIfAbsent(employee.teamName(), key -> FXCollections.observableArrayList()); // Esegue: chatByTeam.computeIfAbsent(employee.teamName(), key -> FXCollections.observableArrayList());
        } // Esegue: }
        updateSequenceFromId(employee.id()); // Esegue: updateSequenceFromId(employee.id());
        return employee; // Esegue: return employee;
    } // Esegue: }

    private void ensureCollections(String employeeId) { // Esegue: private void ensureCollections(String employeeId) {
        agendaByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList()); // Esegue: agendaByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        notificationsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList()); // Esegue: notificationsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        invoicesByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList()); // Esegue: invoicesByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        paymentsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList()); // Esegue: paymentsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
        emailsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList()); // Esegue: emailsByEmployee.computeIfAbsent(employeeId, key -> FXCollections.observableArrayList());
    } // Esegue: }

    private void updateSequenceFromId(String id) { // Esegue: private void updateSequenceFromId(String id) {
        if (id == null || !id.startsWith(GENERATED_ID_PREFIX)) { // Esegue: if (id == null || !id.startsWith(GENERATED_ID_PREFIX)) {
            return; // Esegue: return;
        } // Esegue: }
        String numericPart = id.substring(GENERATED_ID_PREFIX.length()); // Esegue: String numericPart = id.substring(GENERATED_ID_PREFIX.length());
        if (numericPart.length() != GENERATED_ID_DIGITS) { // Esegue: if (numericPart.length() != GENERATED_ID_DIGITS) {
            return; // Esegue: return;
        } // Esegue: }
        try { // Esegue: try {
            int numericValue = Integer.parseInt(numericPart); // Esegue: int numericValue = Integer.parseInt(numericPart);
            nextEmployeeSequence = Math.max(nextEmployeeSequence, numericValue + 1); // Esegue: nextEmployeeSequence = Math.max(nextEmployeeSequence, numericValue + 1);
        } catch (NumberFormatException ignored) { // Esegue: } catch (NumberFormatException ignored) {
            // Ignora identificativi non compatibili con il formato generato automaticamente.
        } // Esegue: }
    } // Esegue: }

    public Employee addEmployeeFromDto(EmployeeDto dto, String password) { // Esegue: public Employee addEmployeeFromDto(EmployeeDto dto, String password) {
        return registerEmployee(dto, password); // Esegue: return registerEmployee(dto, password);
    } // Esegue: }

    public Optional<EmployeeDto> exportEmployee(String employeeId) { // Esegue: public Optional<EmployeeDto> exportEmployee(String employeeId) {
        return Optional.ofNullable(credentials.get(employeeId)) // Esegue: return Optional.ofNullable(credentials.get(employeeId))
                .map(EmployeeCredential::employee) // Esegue: .map(EmployeeCredential::employee)
                .map(employeeAdapter::toDto); // Esegue: .map(employeeAdapter::toDto);
    } // Esegue: }

    public void configureRemoteAgentService(RemoteAgentService remoteAgentService) { // Esegue: public void configureRemoteAgentService(RemoteAgentService remoteAgentService) {
        this.remoteAgentProxy = remoteAgentService == null ? null : new RemoteAgentServiceProxy(remoteAgentService); // Esegue: this.remoteAgentProxy = remoteAgentService == null ? null : new RemoteAgentServiceProxy(remoteAgentService);
    } // Esegue: }

    public int importFromRemote(String authToken) { // Esegue: public int importFromRemote(String authToken) {
        if (remoteAgentProxy == null) { // Esegue: if (remoteAgentProxy == null) {
            throw new IllegalStateException("Nessun servizio remoto configurato"); // Esegue: throw new IllegalStateException("Nessun servizio remoto configurato");
        } // Esegue: }
        int imported = 0; // Esegue: int imported = 0;
        for (EmployeeDto dto : remoteAgentProxy.fetchAgents(authToken)) { // Esegue: for (EmployeeDto dto : remoteAgentProxy.fetchAgents(authToken)) {
            if (!credentials.containsKey(dto.id())) { // Esegue: if (!credentials.containsKey(dto.id())) {
                registerEmployee(dto, null); // Esegue: registerEmployee(dto, null);
                imported++; // Esegue: imported++;
            } // Esegue: }
        } // Esegue: }
        return imported; // Esegue: return imported;
    } // Esegue: }

    private void addTeamName(String teamName) { // Esegue: private void addTeamName(String teamName) {
        String normalized = normalizeTeamName(teamName); // Esegue: String normalized = normalizeTeamName(teamName);
        if (normalized == null || !teamNames.add(normalized)) { // Esegue: if (normalized == null || !teamNames.add(normalized)) {
            return; // Esegue: return;
        } // Esegue: }
        int insertionIndex = Collections.binarySearch(availableTeams, normalized, TEAM_NAME_COMPARATOR); // Esegue: int insertionIndex = Collections.binarySearch(availableTeams, normalized, TEAM_NAME_COMPARATOR);
        if (insertionIndex < 0) { // Esegue: if (insertionIndex < 0) {
            insertionIndex = -insertionIndex - 1; // Esegue: insertionIndex = -insertionIndex - 1;
        } // Esegue: }
        availableTeams.add(insertionIndex, normalized); // Esegue: availableTeams.add(insertionIndex, normalized);
    } // Esegue: }

    private void initializeNextEmployeeSequence() { // Esegue: private void initializeNextEmployeeSequence() {
        nextEmployeeSequence = 1; // Esegue: nextEmployeeSequence = 1;
        credentials.keySet().forEach(this::updateSequenceFromId); // Esegue: credentials.keySet().forEach(this::updateSequenceFromId);
    } // Esegue: }

    public String peekNextEmployeeId() { // Esegue: public String peekNextEmployeeId() {
        int sequence = nextEmployeeSequence; // Esegue: int sequence = nextEmployeeSequence;
        String candidate = formatGeneratedId(sequence); // Esegue: String candidate = formatGeneratedId(sequence);
        while (credentials.containsKey(candidate)) { // Esegue: while (credentials.containsKey(candidate)) {
            sequence++; // Esegue: sequence++;
            candidate = formatGeneratedId(sequence); // Esegue: candidate = formatGeneratedId(sequence);
        } // Esegue: }
        return candidate; // Esegue: return candidate;
    } // Esegue: }

    public synchronized String generateNextEmployeeId() { // Esegue: public synchronized String generateNextEmployeeId() {
        String nextId = peekNextEmployeeId(); // Esegue: String nextId = peekNextEmployeeId();
        updateSequenceFromId(nextId); // Esegue: updateSequenceFromId(nextId);
        return nextId; // Esegue: return nextId;
    } // Esegue: }

    public ObservableList<String> getAvailableTeams() { // Esegue: public ObservableList<String> getAvailableTeams() {
        return availableTeamsView; // Esegue: return availableTeamsView;
    } // Esegue: }

    public ObservableList<String> getAvailableRoles() { // Esegue: public ObservableList<String> getAvailableRoles() {
        return availableRolesView; // Esegue: return availableRolesView;
    } // Esegue: }

    private String formatGeneratedId(int sequence) { // Esegue: private String formatGeneratedId(int sequence) {
        return GENERATED_ID_PREFIX + String.format("%0" + GENERATED_ID_DIGITS + "d", sequence); // Esegue: return GENERATED_ID_PREFIX + String.format("%0" + GENERATED_ID_DIGITS + "d", sequence);
    } // Esegue: }

    public Optional<Employee> authenticate(String employeeId, String password) { // Esegue: public Optional<Employee> authenticate(String employeeId, String password) {
        EmployeeCredential credential = credentials.get(employeeId); // Esegue: EmployeeCredential credential = credentials.get(employeeId);
        if (credential == null || !Objects.equals(credential.password(), password)) { // Esegue: if (credential == null || !Objects.equals(credential.password(), password)) {
            currentEmployee = null; // Esegue: currentEmployee = null;
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }
        currentEmployee = credential.employee(); // Esegue: currentEmployee = credential.employee();
        return Optional.of(currentEmployee); // Esegue: return Optional.of(currentEmployee);
    } // Esegue: }

    public Optional<Employee> getCurrentEmployee() { // Esegue: public Optional<Employee> getCurrentEmployee() {
        return Optional.ofNullable(currentEmployee); // Esegue: return Optional.ofNullable(currentEmployee);
    } // Esegue: }

    public void clearCurrentEmployee() { // Esegue: public void clearCurrentEmployee() {
        currentEmployee = null; // Esegue: currentEmployee = null;
    } // Esegue: }

    public Optional<Employee> registerEmployee(String fullName, String role, String teamName, String email, String password) { // Esegue: public Optional<Employee> registerEmployee(String fullName, String role, String teamName, String email, String password) {
        String normalizedName = normalizeRequired(fullName); // Esegue: String normalizedName = normalizeRequired(fullName);
        UserRole resolvedRole; // Esegue: UserRole resolvedRole;
        try { // Esegue: try {
            resolvedRole = UserRole.resolve(role); // Esegue: resolvedRole = UserRole.resolve(role);
        } catch (IllegalArgumentException ex) { // Esegue: } catch (IllegalArgumentException ex) {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }
        String normalizedRole = resolvedRole.getDisplayName(); // Esegue: String normalizedRole = resolvedRole.getDisplayName();
        String normalizedTeam = normalizeTeamName(teamName); // Esegue: String normalizedTeam = normalizeTeamName(teamName);
        String normalizedEmail = normalizeRequired(email); // Esegue: String normalizedEmail = normalizeRequired(email);
        String normalizedPassword = normalizeRequired(password); // Esegue: String normalizedPassword = normalizeRequired(password);

        if (normalizedName.isEmpty() || normalizedRole.isEmpty() // Esegue: if (normalizedName.isEmpty() || normalizedRole.isEmpty()
                || normalizedTeam == null || normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) { // Esegue: || normalizedTeam == null || normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        String generatedId = generateNextEmployeeId(); // Esegue: String generatedId = generateNextEmployeeId();
        Employee employee = new Employee(generatedId, normalizedName, normalizedRole, normalizedTeam, normalizedEmail); // Esegue: Employee employee = new Employee(generatedId, normalizedName, normalizedRole, normalizedTeam, normalizedEmail);
        credentials.put(generatedId, new EmployeeCredential(employee, normalizedPassword)); // Esegue: credentials.put(generatedId, new EmployeeCredential(employee, normalizedPassword));

        ensureCollections(generatedId); // Esegue: ensureCollections(generatedId);
        chatByTeam.computeIfAbsent(normalizedTeam, key -> FXCollections.observableArrayList()); // Esegue: chatByTeam.computeIfAbsent(normalizedTeam, key -> FXCollections.observableArrayList());
        addTeamName(normalizedTeam); // Esegue: addTeamName(normalizedTeam);

        return Optional.of(employee); // Esegue: return Optional.of(employee);
    } // Esegue: }

    public ObservableList<AgendaItem> getAgendaFor(Employee employee) { // Esegue: public ObservableList<AgendaItem> getAgendaFor(Employee employee) {
        return agendaByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList()); // Esegue: return agendaByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    } // Esegue: }

    public ObservableList<Notification> getNotificationsFor(Employee employee) { // Esegue: public ObservableList<Notification> getNotificationsFor(Employee employee) {
        return notificationsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList()); // Esegue: return notificationsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    } // Esegue: }

    public ObservableList<InvoiceRecord> getInvoicesFor(Employee employee) { // Esegue: public ObservableList<InvoiceRecord> getInvoicesFor(Employee employee) {
        return invoicesByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList()); // Esegue: return invoicesByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    } // Esegue: }

    public ObservableList<PaymentRecord> getPaymentsFor(Employee employee) { // Esegue: public ObservableList<PaymentRecord> getPaymentsFor(Employee employee) {
        return paymentsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList()); // Esegue: return paymentsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    } // Esegue: }

    public ObservableList<ChatMessage> getTeamChat(Employee employee) { // Esegue: public ObservableList<ChatMessage> getTeamChat(Employee employee) {
        Objects.requireNonNull(employee, "employee"); // Esegue: Objects.requireNonNull(employee, "employee");
        String teamName = requireTeamName(employee.teamName()); // Esegue: String teamName = requireTeamName(employee.teamName());
        return chatByTeam.computeIfAbsent(teamName, key -> FXCollections.observableArrayList()); // Esegue: return chatByTeam.computeIfAbsent(teamName, key -> FXCollections.observableArrayList());
    } // Esegue: }

    public void sendTeamMessage(Employee employee, String message) { // Esegue: public void sendTeamMessage(Employee employee, String message) {
        Objects.requireNonNull(employee, "employee"); // Esegue: Objects.requireNonNull(employee, "employee");
        if (message == null || message.isBlank()) { // Esegue: if (message == null || message.isBlank()) {
            return; // Esegue: return;
        } // Esegue: }
        String trimmedMessage = message.trim(); // Esegue: String trimmedMessage = message.trim();
        String teamName = requireTeamName(employee.teamName()); // Esegue: String teamName = requireTeamName(employee.teamName());
        remoteChatClient.sendTeamMessage(teamName, employee.fullName(), trimmedMessage) // Esegue: remoteChatClient.sendTeamMessage(teamName, employee.fullName(), trimmedMessage)
                .exceptionally(error -> { // Esegue: .exceptionally(error -> {
                    System.err.println("Invio messaggio chat fallito: " + error.getMessage()); // Esegue: System.err.println("Invio messaggio chat fallito: " + error.getMessage());
                    Platform.runLater(() -> { // Esegue: Platform.runLater(() -> {
                        ChatMessage fallback = new ChatMessage(teamName, employee.fullName(), // Esegue: ChatMessage fallback = new ChatMessage(teamName, employee.fullName(),
                                LocalDateTime.now(), trimmedMessage); // Esegue: LocalDateTime.now(), trimmedMessage);
                        ObservableList<ChatMessage> chat = getTeamChat(employee); // Esegue: ObservableList<ChatMessage> chat = getTeamChat(employee);
                        chat.add(fallback); // Esegue: chat.add(fallback);
                        eventBus.publish(new TeamMessageSentEvent(employee, fallback)); // Esegue: eventBus.publish(new TeamMessageSentEvent(employee, fallback));
                    }); // Esegue: });
                    return null; // Esegue: return null;
                }); // Esegue: });
    } // Esegue: }

    public void connectTeamChat(Employee employee) { // Esegue: public void connectTeamChat(Employee employee) {
        if (employee == null) { // Esegue: if (employee == null) {
            return; // Esegue: return;
        } // Esegue: }
        String teamName = normalizeTeamName(employee.teamName()); // Esegue: String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) { // Esegue: if (teamName == null) {
            return; // Esegue: return;
        } // Esegue: }
        disconnectTeamChat(employee); // Esegue: disconnectTeamChat(employee);
        desiredChatTeams.add(teamName); // Esegue: desiredChatTeams.add(teamName);
        remoteChatClient.subscribeToTeam(teamName, // Esegue: remoteChatClient.subscribeToTeam(teamName,
                        message -> Platform.runLater(() -> { // Esegue: message -> Platform.runLater(() -> {
                            ObservableList<ChatMessage> chat = getTeamChat(employee); // Esegue: ObservableList<ChatMessage> chat = getTeamChat(employee);
                            if (!chat.contains(message)) { // Esegue: if (!chat.contains(message)) {
                                chat.add(message); // Esegue: chat.add(message);
                                chat.sort(Comparator.comparing(ChatMessage::timestamp)); // Esegue: chat.sort(Comparator.comparing(ChatMessage::timestamp));
                                eventBus.publish(new TeamMessageSentEvent(employee, message)); // Esegue: eventBus.publish(new TeamMessageSentEvent(employee, message));
                            } // Esegue: }
                        }), // Esegue: }),
                        error -> System.err.println("Aggiornamento chat in tempo reale fallito: " + error.getMessage())) // Esegue: error -> System.err.println("Aggiornamento chat in tempo reale fallito: " + error.getMessage()))
                .thenAccept(subscription -> { // Esegue: .thenAccept(subscription -> {
                    if (!desiredChatTeams.contains(teamName)) { // Esegue: if (!desiredChatTeams.contains(teamName)) {
                        try { // Esegue: try {
                            subscription.close(); // Esegue: subscription.close();
                        } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                            // Ignored
                        } // Esegue: }
                        return; // Esegue: return;
                    } // Esegue: }
                    chatSubscriptions.put(teamName, subscription); // Esegue: chatSubscriptions.put(teamName, subscription);
                }) // Esegue: })
                .exceptionally(error -> { // Esegue: .exceptionally(error -> {
                    System.err.println("Connessione chat fallita: " + error.getMessage()); // Esegue: System.err.println("Connessione chat fallita: " + error.getMessage());
                    desiredChatTeams.remove(teamName); // Esegue: desiredChatTeams.remove(teamName);
                    return null; // Esegue: return null;
                }); // Esegue: });
    } // Esegue: }

    public void disconnectTeamChat(Employee employee) { // Esegue: public void disconnectTeamChat(Employee employee) {
        if (employee == null) { // Esegue: if (employee == null) {
            return; // Esegue: return;
        } // Esegue: }
        String teamName = normalizeTeamName(employee.teamName()); // Esegue: String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) { // Esegue: if (teamName == null) {
            return; // Esegue: return;
        } // Esegue: }
        desiredChatTeams.remove(teamName); // Esegue: desiredChatTeams.remove(teamName);
        AutoCloseable subscription = chatSubscriptions.remove(teamName); // Esegue: AutoCloseable subscription = chatSubscriptions.remove(teamName);
        if (subscription != null) { // Esegue: if (subscription != null) {
            try { // Esegue: try {
                subscription.close(); // Esegue: subscription.close();
            } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                // Ignored
            } // Esegue: }
        } // Esegue: }
        remoteChatClient.disconnectFromTeam(teamName); // Esegue: remoteChatClient.disconnectFromTeam(teamName);
    } // Esegue: }

    public void refreshTeamChat(Employee employee) { // Esegue: public void refreshTeamChat(Employee employee) {
        if (employee == null) { // Esegue: if (employee == null) {
            return; // Esegue: return;
        } // Esegue: }
        String teamName = normalizeTeamName(employee.teamName()); // Esegue: String teamName = normalizeTeamName(employee.teamName());
        if (teamName == null) { // Esegue: if (teamName == null) {
            return; // Esegue: return;
        } // Esegue: }
        remoteChatClient.fetchTeamMessages(teamName) // Esegue: remoteChatClient.fetchTeamMessages(teamName)
                .thenAccept(messages -> Platform.runLater(() -> { // Esegue: .thenAccept(messages -> Platform.runLater(() -> {
                    ObservableList<ChatMessage> chat = getTeamChat(employee); // Esegue: ObservableList<ChatMessage> chat = getTeamChat(employee);
                    chat.setAll(messages); // Esegue: chat.setAll(messages);
                    chat.sort(Comparator.comparing(ChatMessage::timestamp)); // Esegue: chat.sort(Comparator.comparing(ChatMessage::timestamp));
                })) // Esegue: }))
                .exceptionally(error -> { // Esegue: .exceptionally(error -> {
                    System.err.println("Aggiornamento chat fallito: " + error.getMessage()); // Esegue: System.err.println("Aggiornamento chat fallito: " + error.getMessage());
                    return null; // Esegue: return null;
                }); // Esegue: });
    } // Esegue: }

    private Employee ensureNormalizedTeam(Employee employee) { // Esegue: private Employee ensureNormalizedTeam(Employee employee) {
        if (employee == null) { // Esegue: if (employee == null) {
            return null; // Esegue: return null;
        } // Esegue: }
        String normalizedTeam = normalizeTeamName(employee.teamName()); // Esegue: String normalizedTeam = normalizeTeamName(employee.teamName());
        if (Objects.equals(employee.teamName(), normalizedTeam)) { // Esegue: if (Objects.equals(employee.teamName(), normalizedTeam)) {
            return employee; // Esegue: return employee;
        } // Esegue: }
        return new Employee(employee.id(), employee.fullName(), employee.role(), normalizedTeam, employee.email()); // Esegue: return new Employee(employee.id(), employee.fullName(), employee.role(), normalizedTeam, employee.email());
    } // Esegue: }

    private static String normalizeRequired(String value) { // Esegue: private static String normalizeRequired(String value) {
        return value == null ? "" : value.trim(); // Esegue: return value == null ? "" : value.trim();
    } // Esegue: }

    private static String normalizeTeamName(String teamName) { // Esegue: private static String normalizeTeamName(String teamName) {
        if (teamName == null) { // Esegue: if (teamName == null) {
            return null; // Esegue: return null;
        } // Esegue: }
        String trimmed = teamName.trim(); // Esegue: String trimmed = teamName.trim();
        return trimmed.isEmpty() ? null : trimmed; // Esegue: return trimmed.isEmpty() ? null : trimmed;
    } // Esegue: }

    private static String requireTeamName(String teamName) { // Esegue: private static String requireTeamName(String teamName) {
        String normalized = normalizeTeamName(teamName); // Esegue: String normalized = normalizeTeamName(teamName);
        if (normalized == null) { // Esegue: if (normalized == null) {
            throw new IllegalArgumentException("Il nome del team non può essere vuoto"); // Esegue: throw new IllegalArgumentException("Il nome del team non può essere vuoto");
        } // Esegue: }
        return normalized; // Esegue: return normalized;
    } // Esegue: }

    public ObservableList<EmailMessage> getEmailsFor(Employee employee) { // Esegue: public ObservableList<EmailMessage> getEmailsFor(Employee employee) {
        return emailsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList()); // Esegue: return emailsByEmployee.computeIfAbsent(employee.id(), key -> FXCollections.observableArrayList());
    } // Esegue: }

    public CompletableFuture<Void> sendEmail(Employee employee, String recipient, String subject, String body) { // Esegue: public CompletableFuture<Void> sendEmail(Employee employee, String recipient, String subject, String body) {
        if (employee == null) { // Esegue: if (employee == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Dipendente non valido")); // Esegue: return CompletableFuture.failedFuture(new IllegalArgumentException("Dipendente non valido"));
        } // Esegue: }
        if (recipient == null || recipient.isBlank() || subject == null || subject.isBlank() || body == null || body.isBlank()) { // Esegue: if (recipient == null || recipient.isBlank() || subject == null || subject.isBlank() || body == null || body.isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Compila destinatario, oggetto e testo")); // Esegue: return CompletableFuture.failedFuture(new IllegalArgumentException("Compila destinatario, oggetto e testo"));
        } // Esegue: }
        String trimmedRecipient = recipient.trim(); // Esegue: String trimmedRecipient = recipient.trim();
        String trimmedSubject = subject.trim(); // Esegue: String trimmedSubject = subject.trim();
        String trimmedBody = body.trim(); // Esegue: String trimmedBody = body.trim();
        EmailMessage outgoing = new EmailMessage(employee.email(), trimmedRecipient, trimmedSubject, trimmedBody, LocalDateTime.now(), false); // Esegue: EmailMessage outgoing = new EmailMessage(employee.email(), trimmedRecipient, trimmedSubject, trimmedBody, LocalDateTime.now(), false);
        return remoteEmailClient.sendEmail(outgoing.sender(), outgoing.recipient(), outgoing.subject(), outgoing.body()) // Esegue: return remoteEmailClient.sendEmail(outgoing.sender(), outgoing.recipient(), outgoing.subject(), outgoing.body())
                .thenRun(() -> Platform.runLater(() -> { // Esegue: .thenRun(() -> Platform.runLater(() -> {
                    ObservableList<EmailMessage> emails = getEmailsFor(employee); // Esegue: ObservableList<EmailMessage> emails = getEmailsFor(employee);
                    emails.add(outgoing); // Esegue: emails.add(outgoing);
                    eventBus.publish(new EmailSentEvent(employee, outgoing)); // Esegue: eventBus.publish(new EmailSentEvent(employee, outgoing));
                })); // Esegue: }));
    } // Esegue: }

    public void markNotificationAsRead(Employee employee, Notification notification) { // Esegue: public void markNotificationAsRead(Employee employee, Notification notification) {
        ObservableList<Notification> notifications = getNotificationsFor(employee); // Esegue: ObservableList<Notification> notifications = getNotificationsFor(employee);
        int index = notifications.indexOf(notification); // Esegue: int index = notifications.indexOf(notification);
        if (index >= 0) { // Esegue: if (index >= 0) {
            Notification updated = new Notification(notification.title(), notification.message(), notification.createdAt(), true); // Esegue: Notification updated = new Notification(notification.title(), notification.message(), notification.createdAt(), true);
            notifications.set(index, updated); // Esegue: notifications.set(index, updated);
            eventBus.publish(new NotificationUpdatedEvent(employee, updated)); // Esegue: eventBus.publish(new NotificationUpdatedEvent(employee, updated));
        } // Esegue: }
    } // Esegue: }

    public FxEventBus getEventBus() { // Esegue: public FxEventBus getEventBus() {
        return eventBus; // Esegue: return eventBus;
    } // Esegue: }

    public void shutdownAsyncOperations() { // Esegue: public void shutdownAsyncOperations() {
        remoteTaskScheduler.stop(); // Esegue: remoteTaskScheduler.stop();
        chatSubscriptions.values().forEach(subscription -> { // Esegue: chatSubscriptions.values().forEach(subscription -> {
            try { // Esegue: try {
                subscription.close(); // Esegue: subscription.close();
            } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                // Ignored
            } // Esegue: }
        }); // Esegue: });
        chatSubscriptions.clear(); // Esegue: chatSubscriptions.clear();
        desiredChatTeams.clear(); // Esegue: desiredChatTeams.clear();
        remoteChatClient.disconnectAll(); // Esegue: remoteChatClient.disconnectAll();
    } // Esegue: }

} // Esegue: }
