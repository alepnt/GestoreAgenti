package com.example.GestoreAgenti.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.email.EmailSentEvent;

/**
 * Invia messaggi email utilizzando il {@link JavaMailSender} configurato in Spring Boot.
 */
@Service
public class EmailDeliveryService {

    private final JavaMailSender mailSender;
    private final boolean enabled;
    private final String overrideSender;
    private final DomainEventPublisher eventPublisher;

    public EmailDeliveryService(JavaMailSender mailSender,
                                @Value("${mail.enabled:false}") boolean enabled,
                                @Value("${mail.override-sender:}") String overrideSender,
                                DomainEventPublisher eventPublisher) {
        this.mailSender = mailSender;
        this.enabled = enabled;
        this.overrideSender = overrideSender;
        this.eventPublisher = eventPublisher;
    }

    public void send(String from, String to, String subject, String body) {
        if (!enabled) {
            throw new EmailDeliveryDisabledException("L'invio email è disabilitato. Aggiorna mail.enabled=true per abilitarlo.");
        }
        String sanitizedFrom = resolveSender(from);
        String sanitizedTo = requireNonBlank(to, "Il destinatario non può essere vuoto");
        String sanitizedSubject = requireNonBlank(subject, "L'oggetto non può essere vuoto");
        String sanitizedBody = requireNonBlank(body, "Il corpo del messaggio non può essere vuoto");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(sanitizedFrom);
            helper.setTo(sanitizedTo);
            helper.setSubject(sanitizedSubject);
            helper.setText(sanitizedBody, false);
            if (StringUtils.hasText(from) && !sanitizedFrom.equals(from.trim())) {
                helper.setReplyTo(from.trim());
            }
            mailSender.send(message);
            eventPublisher.publish(new EmailSentEvent(sanitizedFrom, sanitizedTo, sanitizedSubject));
        } catch (MailException | MessagingException ex) {
            String message = ex.getMessage();
            if (!StringUtils.hasText(message)) {
                message = ex.getClass().getSimpleName();
            }
            throw new EmailDeliveryException("Invio email fallito: " + message, ex);
        }
    }

    private String resolveSender(String from) {
        if (StringUtils.hasText(overrideSender)) {
            return overrideSender.trim();
        }
        return requireNonBlank(from, "Il mittente non può essere vuoto").trim();
    }

    private String requireNonBlank(String value, String errorMessage) {
        if (!StringUtils.hasText(value)) {
            throw new EmailDeliveryException(errorMessage);
        }
        return value.trim();
    }
}
