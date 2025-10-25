package com.example.GestoreAgenti.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String provincia;

    private Long responsabileId;

    private BigDecimal totProfittoMensile;
    private BigDecimal totProvvigioneMensile;
    private BigDecimal totProvvigioneAnnuo;

    // Costruttori
    public Team() {}

    public Team(String provincia, Long responsabileId, BigDecimal totProfittoMensile,
                BigDecimal totProvvigioneMensile, BigDecimal totProvvigioneAnnuo) {
        this.provincia = provincia;
        this.responsabileId = responsabileId;
        this.totProfittoMensile = totProfittoMensile;
        this.totProvvigioneMensile = totProvvigioneMensile;
        this.totProvvigioneAnnuo = totProvvigioneAnnuo;
    }

    // Getter e Setter
    public Long getNo() { return no; }
    public void setNo(Long no) { this.no = no; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public Long getResponsabileId() { return responsabileId; }
    public void setResponsabileId(Long responsabileId) { this.responsabileId = responsabileId; }

    public BigDecimal getTotProfittoMensile() { return totProfittoMensile; }
    public void setTotProfittoMensile(BigDecimal totProfittoMensile) { this.totProfittoMensile = totProfittoMensile; }

    public BigDecimal getTotProvvigioneMensile() { return totProvvigioneMensile; }
    public void setTotProvvigioneMensile(BigDecimal totProvvigioneMensile) { this.totProvvigioneMensile = totProvvigioneMensile; }

    public BigDecimal getTotProvvigioneAnnuo() { return totProvvigioneAnnuo; }
    public void setTotProvvigioneAnnuo(BigDecimal totProvvigioneAnnuo) { this.totProvvigioneAnnuo = totProvvigioneAnnuo; }
}

