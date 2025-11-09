package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model che contiene questa classe.

import java.time.LocalDateTime; // Importa java.time.LocalDateTime per abilitare le funzionalità utilizzate nel file.

import jakarta.persistence.Column; // Importa jakarta.persistence.Column per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.Entity; // Importa jakarta.persistence.Entity per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.EnumType; // Importa jakarta.persistence.EnumType per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.Enumerated; // Importa jakarta.persistence.Enumerated per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.FetchType; // Importa jakarta.persistence.FetchType per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.GeneratedValue; // Importa jakarta.persistence.GeneratedValue per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.GenerationType; // Importa jakarta.persistence.GenerationType per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.Id; // Importa jakarta.persistence.Id per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.JoinColumn; // Importa jakarta.persistence.JoinColumn per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.ManyToOne; // Importa jakarta.persistence.ManyToOne per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.PrePersist; // Importa jakarta.persistence.PrePersist per abilitare le funzionalità utilizzate nel file.
import jakarta.persistence.Table; // Importa jakarta.persistence.Table per abilitare le funzionalità utilizzate nel file.

/**
 * Entità persistente che rappresenta una notifica per un dipendente.
 */
@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "notification") // Applica l'annotazione @Table per configurare il componente.
public class Notification { // Definisce la classe Notification che incapsula la logica applicativa.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long id; // Dichiara il campo id dell'oggetto.

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "recipient_id", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Dipendente recipient; // Dichiara il campo recipient dell'oggetto.

    @Enumerated(EnumType.STRING) // Applica l'annotazione @Enumerated per configurare il componente.
    @Column(nullable = false, length = 40) // Applica l'annotazione @Column per configurare il componente.
    private NotificationType type; // Dichiara il campo type dell'oggetto.

    @Column(nullable = false, length = 200) // Applica l'annotazione @Column per configurare il componente.
    private String title; // Dichiara il campo title dell'oggetto.

    @Column(nullable = false, length = 2000) // Applica l'annotazione @Column per configurare il componente.
    private String message; // Dichiara il campo message dell'oggetto.

    @Column(name = "created_at", nullable = false) // Applica l'annotazione @Column per configurare il componente.
    private LocalDateTime createdAt = LocalDateTime.now(); // Definisce il metodo LocalDateTime.now che supporta la logica di dominio.

    @Column(name = "is_read", nullable = false) // Applica l'annotazione @Column per configurare il componente.
    private boolean read; // Dichiara il campo read dell'oggetto.

    @PrePersist // Applica l'annotazione @PrePersist per configurare il componente.
    private void ensureCreatedAt() { // Definisce il metodo ensureCreatedAt che supporta la logica di dominio.
        if (createdAt == null) { // Valuta la condizione per controllare il flusso applicativo.
            createdAt = LocalDateTime.now(); // Assegna il valore calcolato alla variabile createdAt.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public Long getId() { // Definisce il metodo getId che supporta la logica di dominio.
        return id; // Restituisce il risultato dell'espressione id.
    } // Chiude il blocco di codice precedente.

    public void setId(Long id) { // Definisce il metodo setId che supporta la logica di dominio.
        this.id = id; // Aggiorna il campo id dell'istanza.
    } // Chiude il blocco di codice precedente.

    public Dipendente getRecipient() { // Definisce il metodo getRecipient che supporta la logica di dominio.
        return recipient; // Restituisce il risultato dell'espressione recipient.
    } // Chiude il blocco di codice precedente.

    public void setRecipient(Dipendente recipient) { // Definisce il metodo setRecipient che supporta la logica di dominio.
        this.recipient = recipient; // Aggiorna il campo recipient dell'istanza.
    } // Chiude il blocco di codice precedente.

    public NotificationType getType() { // Definisce il metodo getType che supporta la logica di dominio.
        return type; // Restituisce il risultato dell'espressione type.
    } // Chiude il blocco di codice precedente.

    public void setType(NotificationType type) { // Definisce il metodo setType che supporta la logica di dominio.
        this.type = type; // Aggiorna il campo type dell'istanza.
    } // Chiude il blocco di codice precedente.

    public String getTitle() { // Definisce il metodo getTitle che supporta la logica di dominio.
        return title; // Restituisce il risultato dell'espressione title.
    } // Chiude il blocco di codice precedente.

    public void setTitle(String title) { // Definisce il metodo setTitle che supporta la logica di dominio.
        this.title = title; // Aggiorna il campo title dell'istanza.
    } // Chiude il blocco di codice precedente.

    public String getMessage() { // Definisce il metodo getMessage che supporta la logica di dominio.
        return message; // Restituisce il risultato dell'espressione message.
    } // Chiude il blocco di codice precedente.

    public void setMessage(String message) { // Definisce il metodo setMessage che supporta la logica di dominio.
        this.message = message; // Aggiorna il campo message dell'istanza.
    } // Chiude il blocco di codice precedente.

    public LocalDateTime getCreatedAt() { // Definisce il metodo getCreatedAt che supporta la logica di dominio.
        return createdAt; // Restituisce il risultato dell'espressione createdAt.
    } // Chiude il blocco di codice precedente.

    public void setCreatedAt(LocalDateTime createdAt) { // Definisce il metodo setCreatedAt che supporta la logica di dominio.
        this.createdAt = createdAt; // Aggiorna il campo createdAt dell'istanza.
    } // Chiude il blocco di codice precedente.

    public boolean isRead() { // Definisce il metodo isRead che supporta la logica di dominio.
        return read; // Restituisce il risultato dell'espressione read.
    } // Chiude il blocco di codice precedente.

    public void setRead(boolean read) { // Definisce il metodo setRead che supporta la logica di dominio.
        this.read = read; // Aggiorna il campo read dell'istanza.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
