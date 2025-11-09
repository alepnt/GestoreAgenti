package com.example.GestoreAgenti.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.model.Notification;
import com.example.GestoreAgenti.model.NotificationType;
import com.example.GestoreAgenti.service.NotificationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Permette di consultare e generare notifiche rivolte ai dipendenti.
 */
@RestController
@RequestMapping("/api/notifications")
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /** Restituisce le notifiche indirizzate al destinatario indicato. */
    @GetMapping
    public List<NotificationResponse> listByRecipient(@RequestParam("recipientId") Long recipientId) {
        return notificationService.findForRecipient(recipientId).stream()
                .map(NotificationController::toResponse)
                .collect(Collectors.toList());
    }

    /** Crea una nuova notifica e la associa al destinatario richiesto. */
    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationCreateRequest request) {
        Notification created = notificationService.notifyRecipient(request.recipientId(),
                request.type(),
                request.title(),
                request.message());
        if (created == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    /** Segna una notifica come letta. */
    @PostMapping("/{id}/read")
    public NotificationResponse markAsRead(@PathVariable Long id) {
        Notification updated = notificationService.markAsRead(id);
        return toResponse(updated);
    }

    private static NotificationResponse toResponse(Notification notification) {
        Long recipientId = notification.getRecipient() != null ? notification.getRecipient().getId() : null;
        return new NotificationResponse(notification.getId(),
                recipientId,
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.isRead());
    }

    public record NotificationCreateRequest(
            @NotNull Long recipientId,
            @NotNull NotificationType type,
            @NotBlank @Size(max = 200) String title,
            @NotBlank @Size(max = 2000) String message) {
    }

    public record NotificationResponse(Long id,
            Long recipientId,
            NotificationType type,
            String title,
            String message,
            LocalDateTime createdAt,
            boolean read) {
    }
}
