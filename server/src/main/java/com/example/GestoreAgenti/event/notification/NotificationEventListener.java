package com.example.GestoreAgenti.event.notification;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent;
import com.example.GestoreAgenti.event.email.EmailSentEvent;
import com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent;
import com.example.GestoreAgenti.event.team.TeamMemberAddedEvent;
import com.example.GestoreAgenti.model.ChatMessage;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.model.NotificationType;
import com.example.GestoreAgenti.model.Pagamento;
import com.example.GestoreAgenti.repository.DipendenteRepository;
import com.example.GestoreAgenti.service.NotificationService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class NotificationEventListener {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final NotificationService notificationService;
    private final DipendenteRepository dipendenteRepository;
    private final DomainEventPublisher eventPublisher;
    private final List<AutoCloseable> subscriptions = new CopyOnWriteArrayList<>();

    public NotificationEventListener(NotificationService notificationService,
                                     DipendenteRepository dipendenteRepository,
                                     DomainEventPublisher eventPublisher) {
        this.notificationService = notificationService;
        this.dipendenteRepository = dipendenteRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    void subscribe() {
        subscriptions.add(eventPublisher.subscribe(TeamMemberAddedEvent.class, this::onTeamMemberAdded));
        subscriptions.add(eventPublisher.subscribe(ChatMessageCreatedEvent.class, this::onChatMessageCreated));
        subscriptions.add(eventPublisher.subscribe(EmailSentEvent.class, this::onEmailSent));
        subscriptions.add(eventPublisher.subscribe(FatturaCreatedEvent.class, this::onFatturaCreated));
        subscriptions.add(eventPublisher.subscribe(PagamentoCreatedEvent.class, this::onPagamentoCreated));
        subscriptions.add(eventPublisher.subscribe(PagamentoStatusChangedEvent.class, this::onPagamentoStatusChanged));
    }

    @PreDestroy
    void cleanup() {
        for (AutoCloseable subscription : new ArrayList<>(subscriptions)) {
            try {
                subscription.close();
            } catch (Exception ignored) {
            }
        }
        subscriptions.clear();
    }

    private void onTeamMemberAdded(TeamMemberAddedEvent event) {
        Dipendente newMember = event.member();
        if (newMember == null || !StringUtils.hasText(newMember.getTeam())) {
            return;
        }
        List<Dipendente> teammates = dipendenteRepository.findByTeamIgnoreCase(newMember.getTeam());
        for (Dipendente teammate : teammates) {
            if (teammate.getId() != null && teammate.getId().equals(newMember.getId())) {
                continue;
            }
            String title = "Nuovo membro nel tuo team";
            String message = String.format(Locale.ITALIAN,
                    "%s %s si è unito al team %s",
                    safe(newMember.getNome()),
                    safe(newMember.getCognome()),
                    newMember.getTeam());
            notificationService.notifyRecipient(teammate, NotificationType.TEAM_MEMBER_ADDED, title, message);
        }
    }

    private void onChatMessageCreated(ChatMessageCreatedEvent event) {
        ChatMessage message = event.message();
        if (message == null || !StringUtils.hasText(message.teamName())) {
            return;
        }
        List<Dipendente> recipients = dipendenteRepository.findByTeamIgnoreCase(message.teamName());
        for (Dipendente recipient : recipients) {
            if (isSender(recipient, message.sender())) {
                continue;
            }
            String title = "Nuovo messaggio in chat";
            String preview = message.content().length() > 120
                    ? message.content().substring(0, 117).concat("...")
                    : message.content();
            String body = String.format(Locale.ITALIAN,
                    "%s ha scritto: %s",
                    message.sender(),
                    preview);
            notificationService.notifyRecipient(recipient, NotificationType.CHAT_MESSAGE, title, body);
        }
    }

    private void onEmailSent(EmailSentEvent event) {
        if (!StringUtils.hasText(event.to())) {
            return;
        }
        dipendenteRepository.findByEmailIgnoreCase(event.to())
                .ifPresent(recipient -> {
                    String title = "Nuova mail ricevuta";
                    String body = String.format(Locale.ITALIAN,
                            "Hai ricevuto una mail da %s con oggetto '%s'",
                            safe(event.from()),
                            safe(event.subject()));
                    notificationService.notifyRecipient(recipient, NotificationType.EMAIL_RECEIVED, title, body);
                });
    }

    private void onFatturaCreated(FatturaCreatedEvent event) {
        Fattura fattura = event.fattura();
        if (fattura == null) {
            return;
        }
        Dipendente agente = resolveAgent(fattura.getContratto());
        if (agente == null) {
            return;
        }
        String title = "Nuova fattura per i tuoi clienti";
        String message = String.format(Locale.ITALIAN,
                "Fattura %s emessa il %s per il cliente %s",
                safe(fattura.getNumeroFattura()),
                fattura.getDataEmissione() != null ? DATE_FORMATTER.format(fattura.getDataEmissione()) : "data da definire",
                fattura.getCliente() != null ? safe(fattura.getCliente().getNome()) : "cliente sconosciuto");
        notificationService.notifyRecipient(agente, NotificationType.INVOICE_CREATED, title, message);
    }

    private void onPagamentoCreated(PagamentoCreatedEvent event) {
        Pagamento pagamento = event.pagamento();
        if (pagamento == null) {
            return;
        }
        Dipendente agente = resolveAgentFromPagamento(pagamento);
        if (agente == null) {
            return;
        }
        String title = "Nuovo pagamento registrato";
        String message = String.format(Locale.ITALIAN,
                "Registrato pagamento #%d di %s",
                pagamento.getIdPagamento(),
                formatCurrency(pagamento.getImporto()));
        notificationService.notifyRecipient(agente, NotificationType.PAYMENT_EVENT, title, message);
    }

    private void onPagamentoStatusChanged(PagamentoStatusChangedEvent event) {
        Pagamento pagamento = event.pagamento();
        if (pagamento == null) {
            return;
        }
        Dipendente agente = resolveAgentFromPagamento(pagamento);
        if (agente == null) {
            return;
        }
        String title = "Aggiornamento stato pagamento";
        String message = String.format(Locale.ITALIAN,
                "Il pagamento #%d è passato da %s a %s",
                pagamento.getIdPagamento(),
                safe(event.previousStatus()),
                safe(event.newStatus()));
        notificationService.notifyRecipient(agente, NotificationType.PAYMENT_EVENT, title, message);
    }

    private Dipendente resolveAgentFromPagamento(Pagamento pagamento) {
        if (pagamento == null || pagamento.getFattura() == null) {
            return null;
        }
        return resolveAgent(pagamento.getFattura().getContratto());
    }

    private Dipendente resolveAgent(Contratto contratto) {
        if (contratto == null) {
            return null;
        }
        return contratto.getDipendente();
    }

    private boolean isSender(Dipendente candidate, String sender) {
        if (candidate == null || !StringUtils.hasText(sender)) {
            return false;
        }
        String normalizedSender = sender.trim().toLowerCase(Locale.ITALIAN);
        String fullName = (safe(candidate.getNome()) + " " + safe(candidate.getCognome())).trim().toLowerCase(Locale.ITALIAN);
        return !fullName.isEmpty() && fullName.equals(normalizedSender);
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "importo non indicato";
        }
        return String.format(Locale.ITALIAN, "€ %.2f", value);
    }
}
