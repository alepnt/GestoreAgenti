package com.example.GestoreAgenti.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Notification;
import com.example.GestoreAgenti.model.NotificationType;
import com.example.GestoreAgenti.repository.DipendenteRepository;
import com.example.GestoreAgenti.repository.NotificationRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class NotificationService extends AbstractCrudService<Notification, Long> {

    private final NotificationRepository notificationRepository;
    private final DipendenteRepository dipendenteRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               DipendenteRepository dipendenteRepository) {
        super(notificationRepository, new BeanCopyCrudEntityHandler<>("id"), "Notifica");
        this.notificationRepository = notificationRepository;
        this.dipendenteRepository = dipendenteRepository;
    }

    public List<Notification> findForRecipient(Long recipientId) {
        return notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(recipientId);
    }

    public Notification notifyRecipient(Dipendente recipient,
                                        NotificationType type,
                                        String title,
                                        String message) {
        if (recipient == null || type == null || !hasText(title) || !hasText(message)) {
            return null;
        }
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setTitle(title.trim());
        notification.setMessage(message.trim());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return create(notification);
    }

    public Notification notifyRecipient(Long recipientId,
                                        NotificationType type,
                                        String title,
                                        String message) {
        if (recipientId == null) {
            return null;
        }
        return dipendenteRepository.findById(recipientId)
                .map(employee -> notifyRecipient(employee, type, title, message))
                .orElse(null);
    }

    public Notification markAsRead(Long notificationId) {
        Notification existing = findRequiredById(notificationId);
        if (!existing.isRead()) {
            existing.setRead(true);
            existing = notificationRepository.save(existing);
        }
        return existing;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
