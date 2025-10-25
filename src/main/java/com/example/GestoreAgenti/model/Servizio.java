package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.

import jakarta.persistence.Column; // Importa Column per configurare le colonne specifiche dell'entità.
import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.
import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.Table; // Importa Table per impostare il nome della tabella su cui mappare l'entità.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "servizio") // Applica l'annotazione @Table per configurare il componente.
public class Servizio { // Dichiara la classe Servizio che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idServizio; // Memorizza l'ID del servizio dell'entità.

    private String nome; // Memorizza il nome dell'entità.

    @Column(columnDefinition = "TEXT") // Applica l'annotazione @Column per configurare il componente.
    private String descrizione; // Memorizza la descrizione dell'entità.

    private BigDecimal prezzoBase; // Memorizza il prezzo base dell'entità.

    private BigDecimal commissionePercentuale; // Memorizza la percentuale di commissione dell'entità.

    // Getters e Setters
    public Long getIdServizio() { return idServizio; } // Restituisce l'ID del servizio dell'entità.
    public void setIdServizio(Long idServizio) { this.idServizio = idServizio; } // Imposta l'ID del servizio per l'entità.

    public String getNome() { return nome; } // Restituisce il nome dell'entità.
    public void setNome(String nome) { this.nome = nome; } // Imposta il nome per l'entità.

    public String getDescrizione() { return descrizione; } // Restituisce la descrizione dell'entità.
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; } // Imposta la descrizione per l'entità.

    public BigDecimal getPrezzoBase() { return prezzoBase; } // Restituisce il prezzo base dell'entità.
    public void setPrezzoBase(BigDecimal prezzoBase) { this.prezzoBase = prezzoBase; } // Imposta il prezzo base per l'entità.

    public BigDecimal getCommissionePercentuale() { return commissionePercentuale; } // Restituisce la percentuale di commissione dell'entità.
    public void setCommissionePercentuale(BigDecimal commissionePercentuale) { this.commissionePercentuale = commissionePercentuale; } // Imposta la percentuale di commissione per l'entità.
} // Chiude il blocco di codice precedente.
