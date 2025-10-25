package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import jakarta.persistence.Column; // Importa Column per configurare le colonne specifiche dell'entità.
import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.
import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.JoinColumn; // Importa JoinColumn per descrivere la colonna di relazione nella tabella.
import jakarta.persistence.ManyToOne; // Importa ManyToOne per modellare una relazione molti-a-uno.
import jakarta.persistence.Table; // Importa Table per impostare il nome della tabella su cui mappare l'entità.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "utente") // Applica l'annotazione @Table per configurare il componente.
public class Utente { // Dichiara la classe Utente che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idUtente; // Memorizza l'ID dell'utente dell'entità.

    @Column(unique = true, nullable = false) // Applica l'annotazione @Column per configurare il componente.
    private String username; // Memorizza il nome utente dell'entità.

    @Column(nullable = false) // Applica l'annotazione @Column per configurare il componente.
    private String passwordHash; // Memorizza l'hash della password dell'entità.

    private String ruolo; // Memorizza il ruolo dell'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_dipendente") // Applica l'annotazione @JoinColumn per configurare il componente.
    private Dipendente dipendente; // opzionale // Memorizza il dipendente associato all'entità.

    // Getters e Setters
    public Long getIdUtente() { return idUtente; } // Restituisce l'ID dell'utente dell'entità.
    public void setIdUtente(Long idUtente) { this.idUtente = idUtente; } // Imposta l'ID dell'utente per l'entità.

    public String getUsername() { return username; } // Restituisce il nome utente dell'entità.
    public void setUsername(String username) { this.username = username; } // Imposta il nome utente per l'entità.

    public String getPasswordHash() { return passwordHash; } // Restituisce l'hash della password dell'entità.
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; } // Imposta l'hash della password per l'entità.

    public String getRuolo() { return ruolo; } // Restituisce il ruolo dell'entità.
    public void setRuolo(String ruolo) { this.ruolo = ruolo; } // Imposta il ruolo per l'entità.

    public Dipendente getDipendente() { return dipendente; } // Restituisce il dipendente dell'entità.
    public void setDipendente(Dipendente dipendente) { this.dipendente = dipendente; } // Imposta il dipendente per l'entità.
} // Chiude il blocco di codice precedente.
