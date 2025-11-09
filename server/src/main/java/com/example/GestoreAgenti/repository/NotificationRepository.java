package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import org.springframework.data.jpa.repository.JpaRepository; // Importa org.springframework.data.jpa.repository.JpaRepository per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Notification; // Importa com.example.GestoreAgenti.model.Notification per abilitare le funzionalità utilizzate nel file.

public interface NotificationRepository extends JpaRepository<Notification, Long> { // Definisce la interfaccia NotificationRepository che incapsula la logica applicativa.

    List<Notification> findByRecipient_IdOrderByCreatedAtDesc(Long recipientId); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
