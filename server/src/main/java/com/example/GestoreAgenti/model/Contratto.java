package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import jakarta.persistence.*; // Importa tutte le annotazioni JPA necessarie per mappare l'entità nel database.
import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.
import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "contratto") // Applica l'annotazione @Table per configurare il componente.
public class Contratto { // Dichiara la classe Contratto che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idContratto; // Memorizza l'ID del contratto dell'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_cliente", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Cliente cliente; // Memorizza il cliente associato all'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_dipendente", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Dipendente dipendente; // Memorizza il dipendente associato all'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_servizio", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Servizio servizio; // Memorizza il servizio associato all'entità.

    private LocalDate dataInizio; // Memorizza la data di inizio dell'entità.
    private LocalDate dataFine; // Memorizza la data di fine dell'entità.

    private BigDecimal importo; // Memorizza l'importo dell'entità.

    private String stato; // Memorizza lo stato dell'entità.

    @Column(columnDefinition = "TEXT") // Applica l'annotazione @Column per configurare il componente.
    private String note; // Memorizza le note dell'entità.

    // Getters e Setters
    public Long getIdContratto() { return idContratto; } // Restituisce l'ID del contratto dell'entità.
    public void setIdContratto(Long idContratto) { this.idContratto = idContratto; } // Imposta l'ID del contratto per l'entità.

    public Cliente getCliente() { return cliente; } // Restituisce il cliente dell'entità.
    public void setCliente(Cliente cliente) { this.cliente = cliente; } // Imposta il cliente per l'entità.

    public Dipendente getDipendente() { return dipendente; } // Restituisce il dipendente dell'entità.
    public void setDipendente(Dipendente dipendente) { this.dipendente = dipendente; } // Imposta il dipendente per l'entità.

    public Servizio getServizio() { return servizio; } // Restituisce il servizio dell'entità.
    public void setServizio(Servizio servizio) { this.servizio = servizio; } // Imposta il servizio per l'entità.

    public LocalDate getDataInizio() { return dataInizio; } // Restituisce la data di inizio dell'entità.
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; } // Imposta la data di inizio per l'entità.

    public LocalDate getDataFine() { return dataFine; } // Restituisce la data di fine dell'entità.
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; } // Imposta la data di fine per l'entità.

    public BigDecimal getImporto() { return importo; } // Restituisce l'importo dell'entità.
    public void setImporto(BigDecimal importo) { this.importo = importo; } // Imposta l'importo per l'entità.

    public String getStato() { return stato; } // Restituisce lo stato dell'entità.
    public void setStato(String stato) { this.stato = stato; } // Imposta lo stato per l'entità.

    public String getNote() { return note; } // Restituisce le note dell'entità.
    public void setNote(String note) { this.note = note; } // Imposta le note per l'entità.
} // Chiude il blocco di codice precedente.


