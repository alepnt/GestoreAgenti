package com.example.GestoreAgenti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GestoreAgenti.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient_IdOrderByCreatedAtDesc(Long recipientId);
}
