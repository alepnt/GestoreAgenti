package com.example.GestoreAgenti.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.model.ChatMessage;
import com.example.GestoreAgenti.model.ChatMessageRequest;
import com.example.GestoreAgenti.service.ChatService;
import com.example.GestoreAgenti.websocket.ChatSubscriptionService;

/**
 * Gestisce la chat dei team permettendo di consultare la cronologia e inviare
 * nuovi messaggi.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatSubscriptionService subscriptionService;

    public ChatController(ChatService chatService, ChatSubscriptionService subscriptionService) {
        this.chatService = chatService;
        this.subscriptionService = subscriptionService;
    }

    /** Restituisce i messaggi associati al team richiesto. */
    @GetMapping("/{teamName}")
    public ResponseEntity<List<ChatMessage>> loadMessages(@PathVariable String teamName) {
        return ResponseEntity.ok(chatService.getMessagesForTeam(teamName));
    }

    /** Pubblica un nuovo messaggio nella chat del team e lo notifica agli iscritti. */
    @PostMapping("/{teamName}")
    public ResponseEntity<ChatMessage> sendMessage(@PathVariable String teamName,
            @RequestBody ChatMessageRequest request) {
        if (request == null
                || request.sender() == null || request.sender().isBlank()
                || request.content() == null || request.content().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        ChatMessage message = chatService.appendMessage(teamName, request.sender(), request.content());
        subscriptionService.broadcast(message);
        return ResponseEntity.ok(message);
    }
}
