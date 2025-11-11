package com.example.GestoreAgenti.websocket; // Definisce il pacchetto com.example.GestoreAgenti.websocket che contiene questa classe.

import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.
import com.fasterxml.jackson.core.JsonProcessingException; // Importa com.fasterxml.jackson.core.JsonProcessingException per abilitare le funzionalità utilizzate nel file.
import com.fasterxml.jackson.databind.ObjectMapper; // Importa com.fasterxml.jackson.databind.ObjectMapper per abilitare le funzionalità utilizzate nel file.
import org.springframework.lang.NonNull; // Importa org.springframework.lang.NonNull per dichiarare contratti di non nullità.
import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.TextMessage; // Importa org.springframework.web.socket.TextMessage per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.WebSocketSession; // Importa org.springframework.web.socket.WebSocketSession per abilitare le funzionalità utilizzate nel file.

import java.io.IOException; // Importa java.io.IOException per abilitare le funzionalità utilizzate nel file.
import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.Set; // Importa java.util.Set per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.ConcurrentHashMap; // Importa java.util.concurrent.ConcurrentHashMap per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.CopyOnWriteArraySet; // Importa java.util.concurrent.CopyOnWriteArraySet per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per garantire il rispetto dei contratti @NonNull delle API Spring.

@Component // Applica l'annotazione @Component per configurare il componente.
public class ChatSubscriptionService { // Definisce la classe ChatSubscriptionService che incapsula la logica applicativa.

    private static final String TEAM_ATTRIBUTE = "team"; // Dichiara il campo "team" dell'oggetto.

    private final Map<String, Set<WebSocketSession>> sessionsByTeam = new ConcurrentHashMap<>(); // Definisce il metodo ConcurrentHashMap<> che supporta la logica di dominio.
    private final ObjectMapper objectMapper; // Dichiara il campo objectMapper dell'oggetto.

    public ChatSubscriptionService(ObjectMapper objectMapper) { // Costruttore della classe ChatSubscriptionService che inizializza le dipendenze necessarie.
        this.objectMapper = Objects.requireNonNull(objectMapper); // Aggiorna il campo objectMapper dell'istanza.
    } // Chiude il blocco di codice precedente.

    public void registerSession(String teamName, WebSocketSession session) { // Definisce il metodo registerSession che supporta la logica di dominio.
        String normalizedTeam = normalizeTeam(teamName); // Assegna il valore calcolato alla variabile String normalizedTeam.
        session.getAttributes().put(TEAM_ATTRIBUTE, normalizedTeam); // Esegue l'istruzione terminata dal punto e virgola.
        sessionsByTeam.computeIfAbsent(normalizedTeam, key -> new CopyOnWriteArraySet<>()) // Esegue l'istruzione necessaria alla logica applicativa.
                .add(session); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public void unregisterSession(WebSocketSession session) { // Definisce il metodo unregisterSession che supporta la logica di dominio.
        Object attribute = session.getAttributes().get(TEAM_ATTRIBUTE); // Assegna il valore calcolato alla variabile Object attribute.
        if (!(attribute instanceof String teamKey)) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        Set<WebSocketSession> sessions = sessionsByTeam.get(teamKey); // Assegna il valore calcolato alla variabile Set<WebSocketSession> sessions.
        if (sessions != null) { // Valuta la condizione per controllare il flusso applicativo.
            sessions.remove(session); // Esegue l'istruzione terminata dal punto e virgola.
            if (sessions.isEmpty()) { // Valuta la condizione per controllare il flusso applicativo.
                sessionsByTeam.remove(teamKey); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public void broadcast(ChatMessage message) { // Definisce il metodo broadcast che supporta la logica di dominio.
        if (message == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        String normalizedTeam = normalizeTeam(message.teamName()); // Assegna il valore calcolato alla variabile String normalizedTeam.
        Set<WebSocketSession> sessions = sessionsByTeam.get(normalizedTeam); // Assegna il valore calcolato alla variabile Set<WebSocketSession> sessions.
        if (sessions == null || sessions.isEmpty()) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        TextMessage payload = toTextMessage(message); // Assegna il valore calcolato alla variabile TextMessage payload.
        for (WebSocketSession session : sessions) { // Itera sugli elementi richiesti dalla logica.
            if (!session.isOpen()) { // Valuta la condizione per controllare il flusso applicativo.
                unregisterSession(session); // Esegue l'istruzione terminata dal punto e virgola.
                continue; // Passa direttamente all'iterazione successiva del ciclo.
            } // Chiude il blocco di codice precedente.
            try { // Avvia il blocco protetto per intercettare eventuali eccezioni.
                session.sendMessage(Objects.requireNonNull(payload)); // Esegue l'istruzione terminata dal punto e virgola.
            } catch (IOException e) { // Apre il blocco di codice associato alla dichiarazione.
                unregisterSession(session); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private @NonNull TextMessage toTextMessage(ChatMessage message) { // Definisce il metodo toTextMessage che supporta la logica di dominio.
        try { // Avvia il blocco protetto per intercettare eventuali eccezioni.
            return new TextMessage(Objects.requireNonNull(objectMapper.writeValueAsString(message))); // Restituisce il risultato dell'espressione new TextMessage(objectMapper.writeValueAsString(message)).
        } catch (JsonProcessingException e) { // Apre il blocco di codice associato alla dichiarazione.
            throw new IllegalStateException("Impossibile serializzare il messaggio di chat", e); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private @NonNull String normalizeTeam(String teamName) { // Definisce il metodo normalizeTeam che supporta la logica di dominio.
        return teamName == null ? "" : teamName.trim().toLowerCase(); // Restituisce il risultato dell'espressione teamName == null ? "" : teamName.trim().toLowerCase().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
