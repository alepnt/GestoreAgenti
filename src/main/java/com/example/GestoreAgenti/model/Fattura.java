package com.example.GestoreAgenti.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fattura")
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFattura;

    private String numeroFattura;

    private LocalDate dataEmissione;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_contratto", nullable = false)
    private Contratto contratto;

    private BigDecimal imponibile;
    private BigDecimal iva;
    private BigDecimal totale;

    private String stato;

    // Getters e Setters
    public Long getIdFattura() { return idFattura; }
    public void setIdFattura(Long idFattura) { this.idFattura = idFattura; }

    public String getNumeroFattura() { return numeroFattura; }
    public void setNumeroFattura(String numeroFattura) { this.numeroFattura = numeroFattura; }

    public LocalDate getDataEmissione() { return dataEmissione; }
    public void setDataEmissione(LocalDate dataEmissione) { this.dataEmissione = dataEmissione; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Contratto getContratto() { return contratto; }
    public void setContratto(Contratto contratto) { this.contratto = contratto; }

    public BigDecimal getImponibile() { return imponibile; }
    public void setImponibile(BigDecimal imponibile) { this.imponibile = imponibile; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotale() { return totale; }
    public void setTotale(BigDecimal totale) { this.totale = totale; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}

