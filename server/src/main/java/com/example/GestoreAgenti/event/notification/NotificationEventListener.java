package com.example.GestoreAgenti.event.notification; // Definisce il pacchetto com.example.GestoreAgenti.event.notification che contiene questa classe.

import java.math.BigDecimal; // Importa java.math.BigDecimal per abilitare le funzionalità utilizzate nel file.
import java.time.format.DateTimeFormatter; // Importa java.time.format.DateTimeFormatter per abilitare le funzionalità utilizzate nel file.
import java.util.ArrayList; // Importa java.util.ArrayList per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Locale; // Importa java.util.Locale per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.CopyOnWriteArrayList; // Importa java.util.concurrent.CopyOnWriteArrayList per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.
import org.springframework.util.StringUtils; // Importa org.springframework.util.StringUtils per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent; // Importa com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.email.EmailSentEvent; // Importa com.example.GestoreAgenti.event.email.EmailSentEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent; // Importa com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.team.TeamMemberAddedEvent; // Importa com.example.GestoreAgenti.event.team.TeamMemberAddedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.NotificationType; // Importa com.example.GestoreAgenti.model.NotificationType per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.DipendenteRepository; // Importa com.example.GestoreAgenti.repository.DipendenteRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.NotificationService; // Importa com.example.GestoreAgenti.service.NotificationService per abilitare le funzionalità utilizzate nel file.

import jakarta.annotation.PostConstruct; // Importa jakarta.annotation.PostConstruct per abilitare le funzionalità utilizzate nel file.
import jakarta.annotation.PreDestroy; // Importa jakarta.annotation.PreDestroy per abilitare le funzionalità utilizzate nel file.

@Component // Applica l'annotazione @Component per configurare il componente.
public class NotificationEventListener { // Definisce la classe NotificationEventListener che incapsula la logica applicativa.

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Definisce il metodo DateTimeFormatter.ofPattern che supporta la logica di dominio.

    private final NotificationService notificationService; // Dichiara il campo notificationService dell'oggetto.
    private final DipendenteRepository dipendenteRepository; // Dichiara il campo dipendenteRepository dell'oggetto.
    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.
    private final List<AutoCloseable> subscriptions = new CopyOnWriteArrayList<>(); // Definisce il metodo CopyOnWriteArrayList<> che supporta la logica di dominio.

    public NotificationEventListener(NotificationService notificationService, // Costruttore della classe NotificationEventListener che inizializza le dipendenze necessarie.
                                     DipendenteRepository dipendenteRepository, // Esegue l'istruzione necessaria alla logica applicativa.
                                     DomainEventPublisher eventPublisher) { // Apre il blocco di codice associato alla dichiarazione.
        this.notificationService = notificationService; // Aggiorna il campo notificationService dell'istanza.
        this.dipendenteRepository = dipendenteRepository; // Aggiorna il campo dipendenteRepository dell'istanza.
        this.eventPublisher = eventPublisher; // Aggiorna il campo eventPublisher dell'istanza.
    } // Chiude il blocco di codice precedente.

    @PostConstruct // Applica l'annotazione @PostConstruct per configurare il componente.
    void subscribe() { // Apre il blocco di codice associato alla dichiarazione.
        subscriptions.add(eventPublisher.subscribe(TeamMemberAddedEvent.class, this::onTeamMemberAdded)); // Esegue l'istruzione terminata dal punto e virgola.
        subscriptions.add(eventPublisher.subscribe(ChatMessageCreatedEvent.class, this::onChatMessageCreated)); // Esegue l'istruzione terminata dal punto e virgola.
        subscriptions.add(eventPublisher.subscribe(EmailSentEvent.class, this::onEmailSent)); // Esegue l'istruzione terminata dal punto e virgola.
        subscriptions.add(eventPublisher.subscribe(FatturaCreatedEvent.class, this::onFatturaCreated)); // Esegue l'istruzione terminata dal punto e virgola.
        subscriptions.add(eventPublisher.subscribe(PagamentoCreatedEvent.class, this::onPagamentoCreated)); // Esegue l'istruzione terminata dal punto e virgola.
        subscriptions.add(eventPublisher.subscribe(PagamentoStatusChangedEvent.class, this::onPagamentoStatusChanged)); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @PreDestroy // Applica l'annotazione @PreDestroy per configurare il componente.
    void cleanup() { // Apre il blocco di codice associato alla dichiarazione.
        for (AutoCloseable subscription : new ArrayList<>(subscriptions)) { // Itera sugli elementi richiesti dalla logica.
            try { // Avvia il blocco protetto per intercettare eventuali eccezioni.
                subscription.close(); // Esegue l'istruzione terminata dal punto e virgola.
            } catch (Exception ignored) { // Apre il blocco di codice associato alla dichiarazione.
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
        subscriptions.clear(); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void onTeamMemberAdded(TeamMemberAddedEvent event) { // Definisce il metodo onTeamMemberAdded che supporta la logica di dominio.
        Dipendente newMember = event.member(); // Assegna il valore calcolato alla variabile Dipendente newMember.
        if (newMember == null || !StringUtils.hasText(newMember.getTeam())) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        List<Dipendente> teammates = dipendenteRepository.findByTeamIgnoreCase(newMember.getTeam()); // Assegna il valore calcolato alla variabile List<Dipendente> teammates.
        for (Dipendente teammate : teammates) { // Itera sugli elementi richiesti dalla logica.
            if (teammate.getId() != null && teammate.getId().equals(newMember.getId())) { // Valuta la condizione per controllare il flusso applicativo.
                continue; // Passa direttamente all'iterazione successiva del ciclo.
            } // Chiude il blocco di codice precedente.
            String title = "Nuovo membro nel tuo team"; // Assegna il valore calcolato alla variabile String title.
            String message = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                    "%s %s si è unito al team %s", // Esegue l'istruzione necessaria alla logica applicativa.
                    safe(newMember.getNome()), // Esegue l'istruzione necessaria alla logica applicativa.
                    safe(newMember.getCognome()), // Esegue l'istruzione necessaria alla logica applicativa.
                    newMember.getTeam()); // Esegue l'istruzione terminata dal punto e virgola.
            notificationService.notifyRecipient(teammate, NotificationType.TEAM_MEMBER_ADDED, title, message); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private void onChatMessageCreated(ChatMessageCreatedEvent event) { // Definisce il metodo onChatMessageCreated che supporta la logica di dominio.
        ChatMessage message = event.message(); // Assegna il valore calcolato alla variabile ChatMessage message.
        if (message == null || !StringUtils.hasText(message.teamName())) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        List<Dipendente> recipients = dipendenteRepository.findByTeamIgnoreCase(message.teamName()); // Assegna il valore calcolato alla variabile List<Dipendente> recipients.
        for (Dipendente recipient : recipients) { // Itera sugli elementi richiesti dalla logica.
            if (isSender(recipient, message.sender())) { // Valuta la condizione per controllare il flusso applicativo.
                continue; // Passa direttamente all'iterazione successiva del ciclo.
            } // Chiude il blocco di codice precedente.
            String title = "Nuovo messaggio in chat"; // Assegna il valore calcolato alla variabile String title.
            String preview = message.content().length() > 120 // Esegue l'istruzione necessaria alla logica applicativa.
                    ? message.content().substring(0, 117).concat("...") // Esegue l'istruzione necessaria alla logica applicativa.
                    : message.content(); // Esegue l'istruzione terminata dal punto e virgola.
            String body = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                    "%s ha scritto: %s", // Esegue l'istruzione necessaria alla logica applicativa.
                    message.sender(), // Esegue l'istruzione necessaria alla logica applicativa.
                    preview); // Esegue l'istruzione terminata dal punto e virgola.
            notificationService.notifyRecipient(recipient, NotificationType.CHAT_MESSAGE, title, body); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private void onEmailSent(EmailSentEvent event) { // Definisce il metodo onEmailSent che supporta la logica di dominio.
        if (!StringUtils.hasText(event.to())) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        dipendenteRepository.findByEmailIgnoreCase(event.to()) // Esegue l'istruzione necessaria alla logica applicativa.
                .ifPresent(recipient -> { // Apre il blocco di codice associato alla dichiarazione.
                    String title = "Nuova mail ricevuta"; // Assegna il valore calcolato alla variabile String title.
                    String body = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                            "Hai ricevuto una mail da %s con oggetto '%s'", // Esegue l'istruzione necessaria alla logica applicativa.
                            safe(event.from()), // Esegue l'istruzione necessaria alla logica applicativa.
                            safe(event.subject())); // Esegue l'istruzione terminata dal punto e virgola.
                    notificationService.notifyRecipient(recipient, NotificationType.EMAIL_RECEIVED, title, body); // Esegue l'istruzione terminata dal punto e virgola.
                }); // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private void onFatturaCreated(FatturaCreatedEvent event) { // Definisce il metodo onFatturaCreated che supporta la logica di dominio.
        Fattura fattura = event.fattura(); // Assegna il valore calcolato alla variabile Fattura fattura.
        if (fattura == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        Dipendente agente = resolveAgent(fattura.getContratto()); // Assegna il valore calcolato alla variabile Dipendente agente.
        if (agente == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        String title = "Nuova fattura per i tuoi clienti"; // Assegna il valore calcolato alla variabile String title.
        String message = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                "Fattura %s emessa il %s per il cliente %s", // Esegue l'istruzione necessaria alla logica applicativa.
                safe(fattura.getNumeroFattura()), // Esegue l'istruzione necessaria alla logica applicativa.
                fattura.getDataEmissione() != null ? DATE_FORMATTER.format(fattura.getDataEmissione()) : "data da definire", // Esegue l'istruzione necessaria alla logica applicativa.
                fattura.getCliente() != null ? safe(fattura.getCliente().getNome()) : "cliente sconosciuto"); // Esegue l'istruzione terminata dal punto e virgola.
        notificationService.notifyRecipient(agente, NotificationType.INVOICE_CREATED, title, message); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void onPagamentoCreated(PagamentoCreatedEvent event) { // Definisce il metodo onPagamentoCreated che supporta la logica di dominio.
        Pagamento pagamento = event.pagamento(); // Assegna il valore calcolato alla variabile Pagamento pagamento.
        if (pagamento == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        Dipendente agente = resolveAgentFromPagamento(pagamento); // Assegna il valore calcolato alla variabile Dipendente agente.
        if (agente == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        String title = "Nuovo pagamento registrato"; // Assegna il valore calcolato alla variabile String title.
        String message = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                "Registrato pagamento #%d di %s", // Esegue l'istruzione necessaria alla logica applicativa.
                pagamento.getIdPagamento(), // Esegue l'istruzione necessaria alla logica applicativa.
                formatCurrency(pagamento.getImporto())); // Esegue l'istruzione terminata dal punto e virgola.
        notificationService.notifyRecipient(agente, NotificationType.PAYMENT_EVENT, title, message); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void onPagamentoStatusChanged(PagamentoStatusChangedEvent event) { // Definisce il metodo onPagamentoStatusChanged che supporta la logica di dominio.
        Pagamento pagamento = event.pagamento(); // Assegna il valore calcolato alla variabile Pagamento pagamento.
        if (pagamento == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        Dipendente agente = resolveAgentFromPagamento(pagamento); // Assegna il valore calcolato alla variabile Dipendente agente.
        if (agente == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        String title = "Aggiornamento stato pagamento"; // Assegna il valore calcolato alla variabile String title.
        String message = String.format(Locale.ITALIAN, // Esegue l'istruzione necessaria alla logica applicativa.
                "Il pagamento #%d è passato da %s a %s", // Esegue l'istruzione necessaria alla logica applicativa.
                pagamento.getIdPagamento(), // Esegue l'istruzione necessaria alla logica applicativa.
                safe(event.previousStatus()), // Esegue l'istruzione necessaria alla logica applicativa.
                safe(event.newStatus())); // Esegue l'istruzione terminata dal punto e virgola.
        notificationService.notifyRecipient(agente, NotificationType.PAYMENT_EVENT, title, message); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private Dipendente resolveAgentFromPagamento(Pagamento pagamento) { // Definisce il metodo resolveAgentFromPagamento che supporta la logica di dominio.
        if (pagamento == null || pagamento.getFattura() == null) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        return resolveAgent(pagamento.getFattura().getContratto()); // Restituisce il risultato dell'espressione resolveAgent(pagamento.getFattura().getContratto()).
    } // Chiude il blocco di codice precedente.

    private Dipendente resolveAgent(Contratto contratto) { // Definisce il metodo resolveAgent che supporta la logica di dominio.
        if (contratto == null) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        return contratto.getDipendente(); // Restituisce il risultato dell'espressione contratto.getDipendente().
    } // Chiude il blocco di codice precedente.

    private boolean isSender(Dipendente candidate, String sender) { // Definisce il metodo isSender che supporta la logica di dominio.
        if (candidate == null || !StringUtils.hasText(sender)) { // Valuta la condizione per controllare il flusso applicativo.
            return false; // Restituisce il risultato dell'espressione false.
        } // Chiude il blocco di codice precedente.
        String normalizedSender = sender.trim().toLowerCase(Locale.ITALIAN); // Assegna il valore calcolato alla variabile String normalizedSender.
        String fullName = (safe(candidate.getNome()) + " " + safe(candidate.getCognome())).trim().toLowerCase(Locale.ITALIAN); // Assegna il valore calcolato alla variabile String fullName.
        return !fullName.isEmpty() && fullName.equals(normalizedSender); // Restituisce il risultato dell'espressione !fullName.isEmpty() && fullName.equals(normalizedSender).
    } // Chiude il blocco di codice precedente.

    private String safe(String value) { // Definisce il metodo safe che supporta la logica di dominio.
        return value == null ? "" : value.trim(); // Restituisce il risultato dell'espressione value == null ? "" : value.trim().
    } // Chiude il blocco di codice precedente.

    private String formatCurrency(BigDecimal value) { // Definisce il metodo formatCurrency che supporta la logica di dominio.
        if (value == null) { // Valuta la condizione per controllare il flusso applicativo.
            return "importo non indicato"; // Restituisce il risultato dell'espressione "importo non indicato".
        } // Chiude il blocco di codice precedente.
        return String.format(Locale.ITALIAN, "€ %.2f", value); // Restituisce il risultato dell'espressione String.format(Locale.ITALIAN, "€ %.2f", value).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
