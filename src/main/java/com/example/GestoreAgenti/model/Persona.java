package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.MappedSuperclass; // Importa MappedSuperclass per definire una superclasse condivisa fra entità.

@MappedSuperclass // Applica l'annotazione @MappedSuperclass per configurare il componente.
public abstract class Persona { // Dichiara la classe Persona che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long id; // Memorizza l'ID dell'entità.

    private String nome; // Memorizza il nome dell'entità.
    private String cognome; // Memorizza il cognome dell'entità.
    private String sesso; // Memorizza il sesso dell'entità.
    private LocalDate dataNascita; // Memorizza la data di nascita dell'entità.

    private String indirizzo; // Memorizza l'indirizzo dell'entità.
    private String citta; // Memorizza la città dell'entità.
    private String cap; // Memorizza il CAP dell'entità.
    private String provincia; // Memorizza la provincia dell'entità.
    private String paese; // Memorizza il paese dell'entità.
    private String email; // Memorizza l'indirizzo email dell'entità.
    private String telefono; // Memorizza il numero di telefono dell'entità.

    // Getters e Setters
    public Long getId() { return id; } // Restituisce l'ID dell'entità.
    public void setId(Long id) { this.id = id; } // Imposta l'ID per l'entità.

    public String getNome() { return nome; } // Restituisce il nome dell'entità.
    public void setNome(String nome) { this.nome = nome; } // Imposta il nome per l'entità.

    public String getCognome() { return cognome; } // Restituisce il cognome dell'entità.
    public void setCognome(String cognome) { this.cognome = cognome; } // Imposta il cognome per l'entità.

    public String getSesso() { return sesso; } // Restituisce il sesso dell'entità.
    public void setSesso(String sesso) { this.sesso = sesso; } // Imposta il sesso per l'entità.

    public LocalDate getDataNascita() { return dataNascita; } // Restituisce la data di nascita dell'entità.
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; } // Imposta la data di nascita per l'entità.

    public String getIndirizzo() { return indirizzo; } // Restituisce l'indirizzo dell'entità.
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; } // Imposta l'indirizzo per l'entità.

    public String getCitta() { return citta; } // Restituisce la città dell'entità.
    public void setCitta(String citta) { this.citta = citta; } // Imposta la città per l'entità.

    public String getCap() { return cap; } // Restituisce il CAP dell'entità.
    public void setCap(String cap) { this.cap = cap; } // Imposta il CAP per l'entità.

    public String getProvincia() { return provincia; } // Restituisce la provincia dell'entità.
    public void setProvincia(String provincia) { this.provincia = provincia; } // Imposta la provincia per l'entità.

    public String getPaese() { return paese; } // Restituisce il paese dell'entità.
    public void setPaese(String paese) { this.paese = paese; } // Imposta il paese per l'entità.

    public String getEmail() { return email; } // Restituisce l'indirizzo email dell'entità.
    public void setEmail(String email) { this.email = email; } // Imposta l'indirizzo email per l'entità.

    public String getTelefono() { return telefono; } // Restituisce il numero di telefono dell'entità.
    public void setTelefono(String telefono) { this.telefono = telefono; } // Imposta il numero di telefono per l'entità.
} // Chiude il blocco di codice precedente.

