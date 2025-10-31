package com.example.GestoreAgenti.controller;

import com.example.GestoreAgenti.model.EmailSendRequest;
import com.example.GestoreAgenti.service.email.EmailDeliveryDisabledException;
import com.example.GestoreAgenti.service.email.EmailDeliveryException;
import com.example.GestoreAgenti.service.email.EmailDeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Espone l'endpoint REST che permette al client JavaFX di inviare email reali tramite SMTP.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailDeliveryService emailDeliveryService;

    public EmailController(EmailDeliveryService emailDeliveryService) {
        this.emailDeliveryService = emailDeliveryService;
    }

    @PostMapping
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSendRequest request) {
        emailDeliveryService.send(request.from(), request.to(), request.subject(), request.body());
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler(EmailDeliveryDisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(EmailDeliveryDisabledException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(EmailDeliveryException.class)
    public ResponseEntity<Map<String, String>> handleDeliveryError(EmailDeliveryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("message", ex.getMessage()));
    }

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
