package com.example.GestoreAgenti.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratto")
public class Contratto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContratto;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_dipendente", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "id_servizio", nullable = false)
    private Servizio servizio;

    private LocalDate dataInizio;
    private LocalDate dataFine;

    private BigDecimal importo;

    private String stato;

    @Column(columnDefinition = "TEXT")
    private String note;

    // Getters e Setters
    public Long getIdContratto() { return idContratto; }
    public void setIdContratto(Long idContratto) { this.idContratto = idContratto; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Dipendente getDipendente() { return dipendente; }
    public void setDipendente(Dipendente dipendente) { this.dipendente = dipendente; }

    public Servizio getServizio() { return servizio; }
    public void setServizio(Servizio servizio) { this.servizio = servizio; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public BigDecimal getImporto() { return importo; }
    public void setImporto(BigDecimal importo) { this.importo = importo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}


