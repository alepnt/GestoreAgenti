package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller che contiene questa classe.

import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.ChatMessageRequest; // Importa com.example.GestoreAgenti.model.ChatMessageRequest per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.ChatService; // Importa com.example.GestoreAgenti.service.ChatService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.websocket.ChatSubscriptionService; // Importa com.example.GestoreAgenti.websocket.ChatSubscriptionService per abilitare le funzionalità utilizzate nel file.
import org.springframework.http.ResponseEntity; // Importa org.springframework.http.ResponseEntity per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.GetMapping; // Importa org.springframework.web.bind.annotation.GetMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.PathVariable; // Importa org.springframework.web.bind.annotation.PathVariable per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.PostMapping; // Importa org.springframework.web.bind.annotation.PostMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestBody; // Importa org.springframework.web.bind.annotation.RequestBody per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestMapping; // Importa org.springframework.web.bind.annotation.RequestMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RestController; // Importa org.springframework.web.bind.annotation.RestController per abilitare le funzionalità utilizzate nel file.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

/**
 * Espone le API REST per leggere e pubblicare messaggi di chat.
 */
@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/chat") // Applica l'annotazione @RequestMapping per configurare il componente.
public class ChatController { // Definisce la classe ChatController che incapsula la logica applicativa.

    private final ChatService chatService; // Dichiara il campo chatService dell'oggetto.
    private final ChatSubscriptionService subscriptionService; // Dichiara il campo subscriptionService dell'oggetto.

    public ChatController(ChatService chatService, ChatSubscriptionService subscriptionService) { // Costruttore della classe ChatController che inizializza le dipendenze necessarie.
        this.chatService = chatService; // Aggiorna il campo chatService dell'istanza.
        this.subscriptionService = subscriptionService; // Aggiorna il campo subscriptionService dell'istanza.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{teamName}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<List<ChatMessage>> loadMessages(@PathVariable String teamName) { // Definisce il metodo loadMessages che supporta la logica di dominio.
        return ResponseEntity.ok(chatService.getMessagesForTeam(teamName)); // Restituisce il risultato dell'espressione ResponseEntity.ok(chatService.getMessagesForTeam(teamName)).
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{teamName}") // Applica l'annotazione @PostMapping per configurare il componente.
    public ResponseEntity<ChatMessage> sendMessage(@PathVariable String teamName, // Definisce il metodo sendMessage che supporta la logica di dominio.
                                                   @RequestBody ChatMessageRequest request) { // Applica l'annotazione @RequestBody ChatMessageRequest request) { per configurare il componente.
        if (request == null // Valuta la condizione per controllare il flusso applicativo.
                || request.sender() == null || request.sender().isBlank() // Esegue l'istruzione necessaria alla logica applicativa.
                || request.content() == null || request.content().isBlank()) { // Apre il blocco di codice associato alla dichiarazione.
            return ResponseEntity.badRequest().build(); // Restituisce il risultato dell'espressione ResponseEntity.badRequest().build().
        } // Chiude il blocco di codice precedente.
        ChatMessage message = chatService.appendMessage(teamName, request.sender(), request.content()); // Assegna il valore calcolato alla variabile ChatMessage message.
        subscriptionService.broadcast(message); // Esegue l'istruzione terminata dal punto e virgola.
        return ResponseEntity.ok(message); // Restituisce il risultato dell'espressione ResponseEntity.ok(message).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
