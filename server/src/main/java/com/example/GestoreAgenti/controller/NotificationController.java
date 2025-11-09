package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller che contiene questa classe.

import java.time.LocalDateTime; // Importa java.time.LocalDateTime per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.stream.Collectors; // Importa java.util.stream.Collectors per abilitare le funzionalità utilizzate nel file.

import org.springframework.http.HttpStatus; // Importa org.springframework.http.HttpStatus per abilitare le funzionalità utilizzate nel file.
import org.springframework.http.ResponseEntity; // Importa org.springframework.http.ResponseEntity per abilitare le funzionalità utilizzate nel file.
import org.springframework.validation.annotation.Validated; // Importa org.springframework.validation.annotation.Validated per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.GetMapping; // Importa org.springframework.web.bind.annotation.GetMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.PathVariable; // Importa org.springframework.web.bind.annotation.PathVariable per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.PostMapping; // Importa org.springframework.web.bind.annotation.PostMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestBody; // Importa org.springframework.web.bind.annotation.RequestBody per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestMapping; // Importa org.springframework.web.bind.annotation.RequestMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestParam; // Importa org.springframework.web.bind.annotation.RequestParam per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RestController; // Importa org.springframework.web.bind.annotation.RestController per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Notification; // Importa com.example.GestoreAgenti.model.Notification per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.NotificationType; // Importa com.example.GestoreAgenti.model.NotificationType per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.NotificationService; // Importa com.example.GestoreAgenti.service.NotificationService per abilitare le funzionalità utilizzate nel file.

import jakarta.validation.Valid; // Importa jakarta.validation.Valid per abilitare le funzionalità utilizzate nel file.
import jakarta.validation.constraints.NotBlank; // Importa jakarta.validation.constraints.NotBlank per abilitare le funzionalità utilizzate nel file.
import jakarta.validation.constraints.NotNull; // Importa jakarta.validation.constraints.NotNull per abilitare le funzionalità utilizzate nel file.
import jakarta.validation.constraints.Size; // Importa jakarta.validation.constraints.Size per abilitare le funzionalità utilizzate nel file.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/notifications") // Applica l'annotazione @RequestMapping per configurare il componente.
@Validated // Applica l'annotazione @Validated per configurare il componente.
public class NotificationController { // Definisce la classe NotificationController che incapsula la logica applicativa.

    private final NotificationService notificationService; // Dichiara il campo notificationService dell'oggetto.

    public NotificationController(NotificationService notificationService) { // Costruttore della classe NotificationController che inizializza le dipendenze necessarie.
        this.notificationService = notificationService; // Aggiorna il campo notificationService dell'istanza.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<NotificationResponse> listByRecipient(@RequestParam("recipientId") Long recipientId) { // Definisce il metodo listByRecipient che supporta la logica di dominio.
        return notificationService.findForRecipient(recipientId).stream() // Restituisce il risultato dell'espressione notificationService.findForRecipient(recipientId).stream().
                .map(NotificationController::toResponse) // Esegue l'istruzione necessaria alla logica applicativa.
                .collect(Collectors.toList()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationCreateRequest request) { // Definisce il metodo create che supporta la logica di dominio.
        Notification created = notificationService.notifyRecipient(request.recipientId(), // Esegue l'istruzione necessaria alla logica applicativa.
                request.type(), // Esegue l'istruzione necessaria alla logica applicativa.
                request.title(), // Esegue l'istruzione necessaria alla logica applicativa.
                request.message()); // Esegue l'istruzione terminata dal punto e virgola.
        if (created == null) { // Valuta la condizione per controllare il flusso applicativo.
            return ResponseEntity.notFound().build(); // Restituisce il risultato dell'espressione ResponseEntity.notFound().build().
        } // Chiude il blocco di codice precedente.
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created)); // Restituisce il risultato dell'espressione ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created)).
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{id}/read") // Applica l'annotazione @PostMapping per configurare il componente.
    public NotificationResponse markAsRead(@PathVariable Long id) { // Definisce il metodo markAsRead che supporta la logica di dominio.
        Notification updated = notificationService.markAsRead(id); // Assegna il valore calcolato alla variabile Notification updated.
        return toResponse(updated); // Restituisce il risultato dell'espressione toResponse(updated).
    } // Chiude il blocco di codice precedente.

    private static NotificationResponse toResponse(Notification notification) { // Definisce il metodo toResponse che supporta la logica di dominio.
        Long recipientId = notification.getRecipient() != null ? notification.getRecipient().getId() : null; // Assegna il valore calcolato alla variabile Long recipientId.
        return new NotificationResponse(notification.getId(), // Restituisce il risultato dell'espressione new NotificationResponse(notification.getId(),.
                recipientId, // Esegue l'istruzione necessaria alla logica applicativa.
                notification.getType(), // Esegue l'istruzione necessaria alla logica applicativa.
                notification.getTitle(), // Esegue l'istruzione necessaria alla logica applicativa.
                notification.getMessage(), // Esegue l'istruzione necessaria alla logica applicativa.
                notification.getCreatedAt(), // Esegue l'istruzione necessaria alla logica applicativa.
                notification.isRead()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public record NotificationCreateRequest( // Definisce la record NotificationCreateRequest che incapsula la logica applicativa.
            @NotNull Long recipientId, // Applica l'annotazione @NotNull Long recipientId, per configurare il componente.
            @NotNull NotificationType type, // Applica l'annotazione @NotNull NotificationType type, per configurare il componente.
            @NotBlank @Size(max = 200) String title, // Applica l'annotazione @NotBlank @Size per configurare il componente.
            @NotBlank @Size(max = 2000) String message) { // Applica l'annotazione @NotBlank @Size per configurare il componente.
    } // Chiude il blocco di codice precedente.

    public record NotificationResponse(Long id, // Definisce la record NotificationResponse che incapsula la logica applicativa.
                                        Long recipientId, // Esegue l'istruzione necessaria alla logica applicativa.
                                        NotificationType type, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String title, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String message, // Esegue l'istruzione necessaria alla logica applicativa.
                                        LocalDateTime createdAt, // Esegue l'istruzione necessaria alla logica applicativa.
                                        boolean read) { // Apre il blocco di codice associato alla dichiarazione.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
