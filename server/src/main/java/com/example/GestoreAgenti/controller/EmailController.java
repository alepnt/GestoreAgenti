package com.example.GestoreAgenti.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.model.EmailSendRequest;
import com.example.GestoreAgenti.service.email.EmailDeliveryDisabledException;
import com.example.GestoreAgenti.service.email.EmailDeliveryException;
import com.example.GestoreAgenti.service.email.EmailDeliveryService;

import jakarta.validation.Valid;

/**
 * Offre un endpoint REST per inviare email e gestisce gli errori comuni di
 * consegna.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailDeliveryService emailDeliveryService;

    public EmailController(EmailDeliveryService emailDeliveryService) {
        this.emailDeliveryService = emailDeliveryService;
    }

    /** Invia un'email utilizzando il canale SMTP configurato. */
    @PostMapping
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSendRequest request) {
        emailDeliveryService.send(request.from(), request.to(), request.subject(), request.body());
        return ResponseEntity.accepted().build();
    }

    /** Segnala che l'invio email è disabilitato nell'ambiente corrente. */
    @ExceptionHandler(EmailDeliveryDisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(EmailDeliveryDisabledException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", ex.getMessage()));
    }

    /** Gestisce errori applicativi emersi durante la consegna dell'email. */
    @ExceptionHandler(EmailDeliveryException.class)
    public ResponseEntity<Map<String, String>> handleDeliveryError(EmailDeliveryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("message", ex.getMessage()));
    }

    /** Riassume gli errori di validazione del payload in un messaggio leggibile. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            message = "Dati non validi";
        }
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}
