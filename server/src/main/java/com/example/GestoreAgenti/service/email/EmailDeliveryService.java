package com.example.GestoreAgenti.service.email; // Definisce il pacchetto com.example.GestoreAgenti.service.email che contiene questa classe.

import jakarta.mail.MessagingException; // Importa jakarta.mail.MessagingException per abilitare le funzionalità utilizzate nel file.
import jakarta.mail.internet.MimeMessage; // Importa jakarta.mail.internet.MimeMessage per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per garantire il rispetto dei contratti @NonNull delle API Spring.
import org.springframework.beans.factory.annotation.Value; // Importa org.springframework.beans.factory.annotation.Value per abilitare le funzionalità utilizzate nel file.
import org.springframework.lang.NonNull; // Importa org.springframework.lang.NonNull per dichiarare contratti di non nullità.
import org.springframework.mail.MailException; // Importa org.springframework.mail.MailException per abilitare le funzionalità utilizzate nel file.
import org.springframework.mail.javamail.JavaMailSender; // Importa org.springframework.mail.javamail.JavaMailSender per abilitare le funzionalità utilizzate nel file.
import org.springframework.mail.javamail.MimeMessageHelper; // Importa org.springframework.mail.javamail.MimeMessageHelper per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.
import org.springframework.util.StringUtils; // Importa org.springframework.util.StringUtils per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.email.EmailSentEvent; // Importa com.example.GestoreAgenti.event.email.EmailSentEvent per abilitare le funzionalità utilizzate nel file.

/**
 * Invia messaggi email utilizzando il {@link JavaMailSender} configurato in Spring Boot.
 */
@Service // Applica l'annotazione @Service per configurare il componente.
public class EmailDeliveryService { // Definisce la classe EmailDeliveryService che incapsula la logica applicativa.

    private final JavaMailSender mailSender; // Dichiara il campo mailSender dell'oggetto.
    private final boolean enabled; // Dichiara il campo enabled dell'oggetto.
    private final String overrideSender; // Dichiara il campo overrideSender dell'oggetto.
    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.

    public EmailDeliveryService(JavaMailSender mailSender, // Costruttore della classe EmailDeliveryService che inizializza le dipendenze necessarie.
                                @Value("${mail.enabled:false}") boolean enabled, // Applica l'annotazione @Value per configurare il componente.
                                @Value("${mail.override-sender:}") String overrideSender, // Applica l'annotazione @Value per configurare il componente.
                                DomainEventPublisher eventPublisher) { // Apre il blocco di codice associato alla dichiarazione.
        this.mailSender = Objects.requireNonNull(mailSender); // Aggiorna il campo mailSender dell'istanza.
        this.enabled = enabled; // Aggiorna il campo enabled dell'istanza.
        this.overrideSender = overrideSender; // Aggiorna il campo overrideSender dell'istanza.
        this.eventPublisher = Objects.requireNonNull(eventPublisher); // Aggiorna il campo eventPublisher dell'istanza.
    } // Chiude il blocco di codice precedente.

    public void send(String from, String to, String subject, String body) { // Definisce il metodo send che supporta la logica di dominio.
        if (!enabled) { // Valuta la condizione per controllare il flusso applicativo.
            throw new EmailDeliveryDisabledException("L'invio email è disabilitato. Aggiorna mail.enabled=true per abilitarlo."); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        String sanitizedFrom = resolveSender(from); // Assegna il valore calcolato alla variabile String sanitizedFrom.
        String sanitizedTo = requireNonBlank(to, "Il destinatario non può essere vuoto"); // Assegna il valore calcolato alla variabile String sanitizedTo.
        String sanitizedSubject = requireNonBlank(subject, "L'oggetto non può essere vuoto"); // Assegna il valore calcolato alla variabile String sanitizedSubject.
        String sanitizedBody = requireNonBlank(body, "Il corpo del messaggio non può essere vuoto"); // Assegna il valore calcolato alla variabile String sanitizedBody.
        try { // Avvia il blocco protetto per intercettare eventuali eccezioni.
            MimeMessage message = mailSender.createMimeMessage(); // Assegna il valore calcolato alla variabile MimeMessage message.
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // Assegna il valore calcolato alla variabile MimeMessageHelper helper.
            helper.setFrom(Objects.requireNonNull(sanitizedFrom)); // Esegue l'istruzione terminata dal punto e virgola.
            helper.setTo(Objects.requireNonNull(sanitizedTo)); // Esegue l'istruzione terminata dal punto e virgola.
            helper.setSubject(Objects.requireNonNull(sanitizedSubject)); // Esegue l'istruzione terminata dal punto e virgola.
            helper.setText(Objects.requireNonNull(sanitizedBody), false); // Esegue l'istruzione terminata dal punto e virgola.
            if (StringUtils.hasText(from) && !sanitizedFrom.equals(from.trim())) { // Valuta la condizione per controllare il flusso applicativo.
                helper.setReplyTo(Objects.requireNonNull(from).trim()); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            mailSender.send(message); // Esegue l'istruzione terminata dal punto e virgola.
            eventPublisher.publish(new EmailSentEvent(sanitizedFrom, sanitizedTo, sanitizedSubject)); // Esegue l'istruzione terminata dal punto e virgola.
        } catch (MailException | MessagingException ex) { // Apre il blocco di codice associato alla dichiarazione.
            String message = ex.getMessage(); // Assegna il valore calcolato alla variabile String message.
            if (!StringUtils.hasText(message)) { // Valuta la condizione per controllare il flusso applicativo.
                message = ex.getClass().getSimpleName(); // Assegna il valore calcolato alla variabile message.
            } // Chiude il blocco di codice precedente.
            throw new EmailDeliveryException("Invio email fallito: " + message, ex); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private @NonNull String resolveSender(String from) { // Definisce il metodo resolveSender che supporta la logica di dominio.
        if (StringUtils.hasText(overrideSender)) { // Valuta la condizione per controllare il flusso applicativo.
            String trimmedOverride = overrideSender.trim(); // Assegna il valore calcolato alla variabile trimmedOverride.
            return Objects.requireNonNull(trimmedOverride); // Restituisce il risultato dell'espressione trimmedOverride.
        } // Chiude il blocco di codice precedente.
        String resolved = requireNonBlank(from, "Il mittente non può essere vuoto").trim(); // Assegna il valore calcolato alla variabile resolved.
        return Objects.requireNonNull(resolved); // Restituisce il risultato dell'espressione resolved.
    } // Chiude il blocco di codice precedente.

    private @NonNull String requireNonBlank(String value, String errorMessage) { // Definisce il metodo requireNonBlank che supporta la logica di dominio.
        if (!StringUtils.hasText(value)) { // Valuta la condizione per controllare il flusso applicativo.
            throw new EmailDeliveryException(errorMessage); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        String trimmed = value.trim(); // Assegna il valore calcolato alla variabile trimmed.
        return Objects.requireNonNull(trimmed); // Restituisce il risultato dell'espressione trimmed.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
