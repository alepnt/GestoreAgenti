package com.example.GestoreAgenti.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "provvigione")
public class Provvigione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProvvigione;

    @ManyToOne
    @JoinColumn(name = "id_dipendente", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "id_contratto", nullable = false)
    private Contratto contratto;

    private BigDecimal percentuale;
    private BigDecimal importo;

    private String stato;

    private LocalDate dataCalcolo;

    // Getters e Setters
    public Long getIdProvvigione() { return idProvvigione; }
    public void setIdProvvigione(Long idProvvigione) { this.idProvvigione = idProvvigione; }

    public Dipendente getDipendente() { return dipendente; }
    public void setDipendente(Dipendente dipendente) { this.dipendente = dipendente; }

    public Contratto getContratto() { return contratto; }
    public void setContratto(Contratto contratto) { this.contratto = contratto; }

    public BigDecimal getPercentuale() { return percentuale; }
    public void setPercentuale(BigDecimal percentuale) { this.percentuale = percentuale; }

    public BigDecimal getImporto() { return importo; }
    public void setImporto(BigDecimal importo) { this.importo = importo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public LocalDate getDataCalcolo() { return dataCalcolo; }
    public void setDataCalcolo(LocalDate dataCalcolo) { this.dataCalcolo = dataCalcolo; }
}

