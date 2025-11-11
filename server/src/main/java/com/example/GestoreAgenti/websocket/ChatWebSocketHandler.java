package com.example.GestoreAgenti.websocket; // Definisce il pacchetto com.example.GestoreAgenti.websocket che contiene questa classe.

import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.ChatMessageRequest; // Importa com.example.GestoreAgenti.model.ChatMessageRequest per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.ChatService; // Importa com.example.GestoreAgenti.service.ChatService per abilitare le funzionalità utilizzate nel file.
import com.fasterxml.jackson.databind.ObjectMapper; // Importa com.fasterxml.jackson.databind.ObjectMapper per abilitare le funzionalità utilizzate nel file.
import org.springframework.lang.NonNull; // Importa org.springframework.lang.NonNull per dichiarare contratti di non nullità.
import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.CloseStatus; // Importa org.springframework.web.socket.CloseStatus per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.TextMessage; // Importa org.springframework.web.socket.TextMessage per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.WebSocketSession; // Importa org.springframework.web.socket.WebSocketSession per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.socket.handler.TextWebSocketHandler; // Importa org.springframework.web.socket.handler.TextWebSocketHandler per abilitare le funzionalità utilizzate nel file.

import java.io.IOException; // Importa java.io.IOException per abilitare le funzionalità utilizzate nel file.
import java.net.URI; // Importa java.net.URI per abilitare le funzionalità utilizzate nel file.
import java.net.URLDecoder; // Importa java.net.URLDecoder per abilitare le funzionalità utilizzate nel file.
import java.nio.charset.StandardCharsets; // Importa java.nio.charset.StandardCharsets per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per garantire il rispetto dei contratti @NonNull delle API Spring.

@Component // Applica l'annotazione @Component per configurare il componente.
public class ChatWebSocketHandler extends TextWebSocketHandler { // Definisce la classe ChatWebSocketHandler che incapsula la logica applicativa.

    private final ChatService chatService; // Dichiara il campo chatService dell'oggetto.
    private final ChatSubscriptionService subscriptionService; // Dichiara il campo subscriptionService dell'oggetto.
    private final ObjectMapper objectMapper; // Dichiara il campo objectMapper dell'oggetto.

    public ChatWebSocketHandler(ChatService chatService, // Costruttore della classe ChatWebSocketHandler che inizializza le dipendenze necessarie.
                                ChatSubscriptionService subscriptionService, // Esegue l'istruzione necessaria alla logica applicativa.
                                ObjectMapper objectMapper) { // Apre il blocco di codice associato alla dichiarazione.
        this.chatService = Objects.requireNonNull(chatService); // Aggiorna il campo chatService dell'istanza.
        this.subscriptionService = Objects.requireNonNull(subscriptionService); // Aggiorna il campo subscriptionService dell'istanza.
        this.objectMapper = Objects.requireNonNull(objectMapper); // Aggiorna il campo objectMapper dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception { // Definisce il metodo afterConnectionEstablished che supporta la logica di dominio.
        String teamName = resolveTeamName(session.getUri()); // Assegna il valore calcolato alla variabile String teamName.
        if (teamName == null || teamName.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            session.close(Objects.requireNonNull(CloseStatus.BAD_DATA)); // Esegue l'istruzione terminata dal punto e virgola.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        subscriptionService.registerSession(teamName, session); // Esegue l'istruzione terminata dal punto e virgola.
        sendHistory(session, teamName); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception { // Definisce il metodo handleTextMessage che supporta la logica di dominio.
        ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class); // Assegna il valore calcolato alla variabile ChatMessageRequest request.
        if (request.content() == null || request.content().isBlank() // Valuta la condizione per controllare il flusso applicativo.
                || request.sender() == null || request.sender().isBlank()) { // Apre il blocco di codice associato alla dichiarazione.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        String teamName = resolveTeamName(session.getUri()); // Assegna il valore calcolato alla variabile String teamName.
        if (teamName == null || teamName.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        ChatMessage chatMessage = chatService.appendMessage(teamName, request.sender(), request.content()); // Assegna il valore calcolato alla variabile ChatMessage chatMessage.
        subscriptionService.broadcast(chatMessage); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) { // Definisce il metodo afterConnectionClosed che supporta la logica di dominio.
        subscriptionService.unregisterSession(session); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception { // Definisce il metodo handleTransportError che supporta la logica di dominio.
        subscriptionService.unregisterSession(session); // Esegue l'istruzione terminata dal punto e virgola.
        session.close(Objects.requireNonNull(CloseStatus.SERVER_ERROR)); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void sendHistory(WebSocketSession session, String teamName) throws IOException { // Definisce il metodo sendHistory che supporta la logica di dominio.
        List<ChatMessage> history = chatService.getMessagesForTeam(teamName); // Assegna il valore calcolato alla variabile List<ChatMessage> history.
        for (ChatMessage chatMessage : history) { // Itera sugli elementi richiesti dalla logica.
            session.sendMessage(new TextMessage(Objects.requireNonNull(objectMapper.writeValueAsString(chatMessage)))); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private String resolveTeamName(URI uri) { // Definisce il metodo resolveTeamName che supporta la logica di dominio.
        if (uri == null) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        String query = uri.getQuery(); // Assegna il valore calcolato alla variabile String query.
        if (query == null || query.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        for (String parameter : query.split("&")) { // Itera sugli elementi richiesti dalla logica.
            String[] keyValue = parameter.split("=", 2); // Assegna il valore calcolato alla variabile String[] keyValue.
            if (keyValue.length == 2 && keyValue[0].equals("team")) { // Valuta la condizione per controllare il flusso applicativo.
                return URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8).trim(); // Restituisce il risultato dell'espressione URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8).trim().
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
        return null; // Restituisce il risultato dell'espressione null.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
