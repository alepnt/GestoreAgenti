package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.
import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.
import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.JoinColumn; // Importa JoinColumn per descrivere la colonna di relazione nella tabella.
import jakarta.persistence.ManyToOne; // Importa ManyToOne per modellare una relazione molti-a-uno.
import jakarta.persistence.Table; // Importa Table per impostare il nome della tabella su cui mappare l'entità.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "provvigione") // Applica l'annotazione @Table per configurare il componente.
public class Provvigione { // Dichiara la classe Provvigione che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idProvvigione; // Memorizza l'ID della provvigione dell'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_dipendente", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Dipendente dipendente; // Memorizza il dipendente associato all'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_contratto", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Contratto contratto; // Memorizza il contratto associato all'entità.

    private BigDecimal percentuale; // Memorizza la percentuale dell'entità.
    private BigDecimal importo; // Memorizza l'importo dell'entità.

    private String stato; // Memorizza lo stato dell'entità.

    private LocalDate dataCalcolo; // Memorizza la data di calcolo dell'entità.

    // Getters e Setters
    public Long getIdProvvigione() { return idProvvigione; } // Restituisce l'ID della provvigione dell'entità.
    public void setIdProvvigione(Long idProvvigione) { this.idProvvigione = idProvvigione; } // Imposta l'ID della provvigione per l'entità.

    public Dipendente getDipendente() { return dipendente; } // Restituisce il dipendente dell'entità.
    public void setDipendente(Dipendente dipendente) { this.dipendente = dipendente; } // Imposta il dipendente per l'entità.

    public Contratto getContratto() { return contratto; } // Restituisce il contratto dell'entità.
    public void setContratto(Contratto contratto) { this.contratto = contratto; } // Imposta il contratto per l'entità.

    public BigDecimal getPercentuale() { return percentuale; } // Restituisce la percentuale dell'entità.
    public void setPercentuale(BigDecimal percentuale) { this.percentuale = percentuale; } // Imposta la percentuale per l'entità.

    public BigDecimal getImporto() { return importo; } // Restituisce l'importo dell'entità.
    public void setImporto(BigDecimal importo) { this.importo = importo; } // Imposta l'importo per l'entità.

    public String getStato() { return stato; } // Restituisce lo stato dell'entità.
    public void setStato(String stato) { this.stato = stato; } // Imposta lo stato per l'entità.

    public LocalDate getDataCalcolo() { return dataCalcolo; } // Restituisce la data di calcolo dell'entità.
    public void setDataCalcolo(LocalDate dataCalcolo) { this.dataCalcolo = dataCalcolo; } // Imposta la data di calcolo per l'entità.
} // Chiude il blocco di codice precedente.

