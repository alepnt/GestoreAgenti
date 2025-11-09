package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service che contiene questa classe.

import java.time.LocalDateTime; // Importa java.time.LocalDateTime per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Notification; // Importa com.example.GestoreAgenti.model.Notification per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.NotificationType; // Importa com.example.GestoreAgenti.model.NotificationType per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.DipendenteRepository; // Importa com.example.GestoreAgenti.repository.DipendenteRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.NotificationRepository; // Importa com.example.GestoreAgenti.repository.NotificationRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class NotificationService extends AbstractCrudService<Notification, Long> { // Definisce la classe NotificationService che incapsula la logica applicativa.

    private final NotificationRepository notificationRepository; // Dichiara il campo notificationRepository dell'oggetto.
    private final DipendenteRepository dipendenteRepository; // Dichiara il campo dipendenteRepository dell'oggetto.

    public NotificationService(NotificationRepository notificationRepository, // Costruttore della classe NotificationService che inizializza le dipendenze necessarie.
                               DipendenteRepository dipendenteRepository) { // Apre il blocco di codice associato alla dichiarazione.
        super(notificationRepository, new BeanCopyCrudEntityHandler<>("id"), "Notifica"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
        this.notificationRepository = notificationRepository; // Aggiorna il campo notificationRepository dell'istanza.
        this.dipendenteRepository = dipendenteRepository; // Aggiorna il campo dipendenteRepository dell'istanza.
    } // Chiude il blocco di codice precedente.

    public List<Notification> findForRecipient(Long recipientId) { // Definisce il metodo findForRecipient che supporta la logica di dominio.
        return notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(recipientId); // Restituisce il risultato dell'espressione notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(recipientId).
    } // Chiude il blocco di codice precedente.

    public Notification notifyRecipient(Dipendente recipient, // Definisce il metodo notifyRecipient che supporta la logica di dominio.
                                        NotificationType type, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String title, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String message) { // Apre il blocco di codice associato alla dichiarazione.
        if (recipient == null || type == null || !hasText(title) || !hasText(message)) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        Notification notification = new Notification(); // Assegna il valore calcolato alla variabile Notification notification.
        notification.setRecipient(recipient); // Esegue l'istruzione terminata dal punto e virgola.
        notification.setType(type); // Esegue l'istruzione terminata dal punto e virgola.
        notification.setTitle(title.trim()); // Esegue l'istruzione terminata dal punto e virgola.
        notification.setMessage(message.trim()); // Esegue l'istruzione terminata dal punto e virgola.
        notification.setRead(false); // Esegue l'istruzione terminata dal punto e virgola.
        notification.setCreatedAt(LocalDateTime.now()); // Esegue l'istruzione terminata dal punto e virgola.
        return create(notification); // Restituisce il risultato dell'espressione create(notification).
    } // Chiude il blocco di codice precedente.

    public Notification notifyRecipient(Long recipientId, // Definisce il metodo notifyRecipient che supporta la logica di dominio.
                                        NotificationType type, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String title, // Esegue l'istruzione necessaria alla logica applicativa.
                                        String message) { // Apre il blocco di codice associato alla dichiarazione.
        if (recipientId == null) { // Valuta la condizione per controllare il flusso applicativo.
            return null; // Restituisce il risultato dell'espressione null.
        } // Chiude il blocco di codice precedente.
        return dipendenteRepository.findById(recipientId) // Restituisce il risultato dell'espressione dipendenteRepository.findById(recipientId).
                .map(employee -> notifyRecipient(employee, type, title, message)) // Esegue l'istruzione necessaria alla logica applicativa.
                .orElse(null); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public Notification markAsRead(Long notificationId) { // Definisce il metodo markAsRead che supporta la logica di dominio.
        Notification existing = findRequiredById(notificationId); // Assegna il valore calcolato alla variabile Notification existing.
        if (!existing.isRead()) { // Valuta la condizione per controllare il flusso applicativo.
            existing.setRead(true); // Esegue l'istruzione terminata dal punto e virgola.
            existing = notificationRepository.save(existing); // Assegna il valore calcolato alla variabile existing.
        } // Chiude il blocco di codice precedente.
        return existing; // Restituisce il risultato dell'espressione existing.
    } // Chiude il blocco di codice precedente.

    private boolean hasText(String value) { // Definisce il metodo hasText che supporta la logica di dominio.
        return value != null && !value.trim().isEmpty(); // Restituisce il risultato dell'espressione value != null && !value.trim().isEmpty().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
