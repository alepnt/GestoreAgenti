package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service che contiene questa classe.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent; // Importa com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import java.time.LocalDateTime; // Importa java.time.LocalDateTime per abilitare le funzionalità utilizzate nel file.
import java.util.ArrayList; // Importa java.util.ArrayList per abilitare le funzionalità utilizzate nel file.
import java.util.Comparator; // Importa java.util.Comparator per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Locale; // Importa java.util.Locale per abilitare le funzionalità utilizzate nel file.
import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.ConcurrentHashMap; // Importa java.util.concurrent.ConcurrentHashMap per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.CopyOnWriteArrayList; // Importa java.util.concurrent.CopyOnWriteArrayList per abilitare le funzionalità utilizzate nel file.

/**
 * Gestisce la memorizzazione in memoria dei messaggi di chat.
 */
@Service // Applica l'annotazione @Service per configurare il componente.
public class ChatService { // Definisce la classe ChatService che incapsula la logica applicativa.

    private final Map<String, CopyOnWriteArrayList<ChatMessage>> messagesByTeam = new ConcurrentHashMap<>(); // Definisce il metodo ConcurrentHashMap<> che supporta la logica di dominio.
    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.

    public ChatService(DomainEventPublisher eventPublisher) { // Costruttore della classe ChatService che inizializza le dipendenze necessarie.
        this.eventPublisher = eventPublisher; // Aggiorna il campo eventPublisher dell'istanza.
        seedDemoMessages(); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public List<ChatMessage> getMessagesForTeam(String teamName) { // Definisce il metodo getMessagesForTeam che supporta la logica di dominio.
        if (teamName == null || teamName.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            return List.of(); // Restituisce il risultato dell'espressione List.of().
        } // Chiude il blocco di codice precedente.
        var normalizedKey = normalizeKey(teamName); // Assegna il valore calcolato alla variabile var normalizedKey.
        var messages = messagesByTeam.get(normalizedKey); // Assegna il valore calcolato alla variabile var messages.
        if (messages == null) { // Valuta la condizione per controllare il flusso applicativo.
            return List.of(); // Restituisce il risultato dell'espressione List.of().
        } // Chiude il blocco di codice precedente.
        var sorted = new ArrayList<>(messages); // Assegna il valore calcolato alla variabile var sorted.
        sorted.sort(Comparator.comparing(ChatMessage::timestamp)); // Esegue l'istruzione terminata dal punto e virgola.
        return sorted; // Restituisce il risultato dell'espressione sorted.
    } // Chiude il blocco di codice precedente.

    public ChatMessage appendMessage(String teamName, String sender, String content) { // Definisce il metodo appendMessage che supporta la logica di dominio.
        if (teamName == null || teamName.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il team non può essere vuoto"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        if (sender == null || sender.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il mittente non può essere vuoto"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        if (content == null || content.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il contenuto non può essere vuoto"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        String trimmedTeam = teamName.trim(); // Assegna il valore calcolato alla variabile String trimmedTeam.
        String trimmedSender = sender.trim(); // Assegna il valore calcolato alla variabile String trimmedSender.
        String trimmedContent = content.trim(); // Assegna il valore calcolato alla variabile String trimmedContent.
        ChatMessage message = new ChatMessage(trimmedTeam, trimmedSender, LocalDateTime.now(), trimmedContent); // Assegna il valore calcolato alla variabile ChatMessage message.
        messagesByTeam.computeIfAbsent(normalizeKey(trimmedTeam), key -> new CopyOnWriteArrayList<>()) // Esegue l'istruzione necessaria alla logica applicativa.
                .add(message); // Esegue l'istruzione terminata dal punto e virgola.
        eventPublisher.publish(new ChatMessageCreatedEvent(message)); // Esegue l'istruzione terminata dal punto e virgola.
        return message; // Restituisce il risultato dell'espressione message.
    } // Chiude il blocco di codice precedente.

    private void seedDemoMessages() { // Definisce il metodo seedDemoMessages che supporta la logica di dominio.
        LocalDateTime now = LocalDateTime.now(); // Assegna il valore calcolato alla variabile LocalDateTime now.
        var nordMessages = messagesByTeam.computeIfAbsent(normalizeKey("Team Nord"), key -> new CopyOnWriteArrayList<>()); // Assegna il valore calcolato alla variabile var nordMessages.
        nordMessages.add(new ChatMessage("Team Nord", "Mario Rossi", now.minusMinutes(50), // Esegue l'istruzione necessaria alla logica applicativa.
                "Ricordatevi il meeting di oggi alle 14")); // Esegue l'istruzione terminata dal punto e virgola.
        nordMessages.add(new ChatMessage("Team Nord", "Lucia Bianchi", now.minusMinutes(45), // Esegue l'istruzione necessaria alla logica applicativa.
                "Perfetto, porterò il report clienti")); // Esegue l'istruzione terminata dal punto e virgola.

        var centroMessages = messagesByTeam.computeIfAbsent(normalizeKey("Team Centro"), key -> new CopyOnWriteArrayList<>()); // Assegna il valore calcolato alla variabile var centroMessages.
        centroMessages.add(new ChatMessage("Team Centro", "Giulia Verdi", now.minusMinutes(30), // Esegue l'istruzione necessaria alla logica applicativa.
                "Aggiornato il backlog del progetto Gamma")); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private String normalizeKey(String teamName) { // Definisce il metodo normalizeKey che supporta la logica di dominio.
        return teamName.trim().toLowerCase(Locale.ITALIAN); // Restituisce il risultato dell'espressione teamName.trim().toLowerCase(Locale.ITALIAN).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
