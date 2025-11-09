package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller che contiene questa classe.

import com.example.GestoreAgenti.model.EmailSendRequest; // Importa com.example.GestoreAgenti.model.EmailSendRequest per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.email.EmailDeliveryDisabledException; // Importa com.example.GestoreAgenti.service.email.EmailDeliveryDisabledException per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.email.EmailDeliveryException; // Importa com.example.GestoreAgenti.service.email.EmailDeliveryException per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.email.EmailDeliveryService; // Importa com.example.GestoreAgenti.service.email.EmailDeliveryService per abilitare le funzionalità utilizzate nel file.
import jakarta.validation.Valid; // Importa jakarta.validation.Valid per abilitare le funzionalità utilizzate nel file.
import org.springframework.http.HttpStatus; // Importa org.springframework.http.HttpStatus per abilitare le funzionalità utilizzate nel file.
import org.springframework.http.ResponseEntity; // Importa org.springframework.http.ResponseEntity per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.MethodArgumentNotValidException; // Importa org.springframework.web.bind.MethodArgumentNotValidException per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.ExceptionHandler; // Importa org.springframework.web.bind.annotation.ExceptionHandler per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.PostMapping; // Importa org.springframework.web.bind.annotation.PostMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestBody; // Importa org.springframework.web.bind.annotation.RequestBody per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestMapping; // Importa org.springframework.web.bind.annotation.RequestMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RestController; // Importa org.springframework.web.bind.annotation.RestController per abilitare le funzionalità utilizzate nel file.

import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.stream.Collectors; // Importa java.util.stream.Collectors per abilitare le funzionalità utilizzate nel file.

/**
 * Espone l'endpoint REST che permette al client JavaFX di inviare email reali tramite SMTP.
 */
@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/email") // Applica l'annotazione @RequestMapping per configurare il componente.
public class EmailController { // Definisce la classe EmailController che incapsula la logica applicativa.

    private final EmailDeliveryService emailDeliveryService; // Dichiara il campo emailDeliveryService dell'oggetto.

    public EmailController(EmailDeliveryService emailDeliveryService) { // Costruttore della classe EmailController che inizializza le dipendenze necessarie.
        this.emailDeliveryService = emailDeliveryService; // Aggiorna il campo emailDeliveryService dell'istanza.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailSendRequest request) { // Definisce il metodo sendEmail che supporta la logica di dominio.
        emailDeliveryService.send(request.from(), request.to(), request.subject(), request.body()); // Esegue l'istruzione terminata dal punto e virgola.
        return ResponseEntity.accepted().build(); // Restituisce il risultato dell'espressione ResponseEntity.accepted().build().
    } // Chiude il blocco di codice precedente.

    @ExceptionHandler(EmailDeliveryDisabledException.class) // Applica l'annotazione @ExceptionHandler per configurare il componente.
    public ResponseEntity<Map<String, String>> handleDisabled(EmailDeliveryDisabledException ex) { // Definisce il metodo handleDisabled che supporta la logica di dominio.
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE) // Restituisce il risultato dell'espressione ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).
                .body(Map.of("message", ex.getMessage())); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @ExceptionHandler(EmailDeliveryException.class) // Applica l'annotazione @ExceptionHandler per configurare il componente.
    public ResponseEntity<Map<String, String>> handleDeliveryError(EmailDeliveryException ex) { // Definisce il metodo handleDeliveryError che supporta la logica di dominio.
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY) // Restituisce il risultato dell'espressione ResponseEntity.status(HttpStatus.BAD_GATEWAY).
                .body(Map.of("message", ex.getMessage())); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @ExceptionHandler(MethodArgumentNotValidException.class) // Applica l'annotazione @ExceptionHandler per configurare il componente.
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) { // Definisce il metodo handleValidationError che supporta la logica di dominio.
        String message = ex.getBindingResult().getFieldErrors().stream() // Esegue l'istruzione necessaria alla logica applicativa.
                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // Esegue l'istruzione necessaria alla logica applicativa.
                .collect(Collectors.joining(", ")); // Esegue l'istruzione terminata dal punto e virgola.
        if (message.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
            message = "Dati non validi"; // Assegna il valore calcolato alla variabile message.
        } // Chiude il blocco di codice precedente.
        return ResponseEntity.badRequest().body(Map.of("message", message)); // Restituisce il risultato dell'espressione ResponseEntity.badRequest().body(Map.of("message", message)).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
