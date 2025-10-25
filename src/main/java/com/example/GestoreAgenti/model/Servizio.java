package com.example.GestoreAgenti.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servizio")
public class Servizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServizio;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    private BigDecimal prezzoBase;

    private BigDecimal commissionePercentuale;

    // Getters e Setters
    public Long getIdServizio() { return idServizio; }
    public void setIdServizio(Long idServizio) { this.idServizio = idServizio; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public BigDecimal getPrezzoBase() { return prezzoBase; }
    public void setPrezzoBase(BigDecimal prezzoBase) { this.prezzoBase = prezzoBase; }

    public BigDecimal getCommissionePercentuale() { return commissionePercentuale; }
    public void setCommissionePercentuale(BigDecimal commissionePercentuale) { this.commissionePercentuale = commissionePercentuale; }
}
