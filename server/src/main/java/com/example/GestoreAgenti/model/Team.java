package com.example.GestoreAgenti.model;

import java.math.BigDecimal;

import com.example.GestoreAgenti.model.commission.CommissionBase;
import com.example.GestoreAgenti.model.commission.CommissionDistributionMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(precision = 5, scale = 4)
    private BigDecimal percentualeProvvigione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'PERCENTUALE'")
    private CommissionDistributionMode distribuzioneProvvigioni = CommissionDistributionMode.PERCENTUALE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'IMPONIBILE'")
    private CommissionBase baseCalcolo = CommissionBase.IMPONIBILE;

    public Team() {}

    public Team(String provincia, Long responsabileId, BigDecimal totProfittoMensile,
                BigDecimal totProvvigioneMensile, BigDecimal totProvvigioneAnnuo,
                BigDecimal percentualeProvvigione, CommissionDistributionMode distribuzioneProvvigioni,
                CommissionBase baseCalcolo) {
        this.provincia = provincia;
        this.responsabileId = responsabileId;
        this.totProfittoMensile = totProfittoMensile;
        this.totProvvigioneMensile = totProvvigioneMensile;
        this.totProvvigioneAnnuo = totProvvigioneAnnuo;
        this.percentualeProvvigione = percentualeProvvigione;
        this.distribuzioneProvvigioni = distribuzioneProvvigioni;
        this.baseCalcolo = baseCalcolo;
    }

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

    public BigDecimal getPercentualeProvvigione() { return percentualeProvvigione; }
    public void setPercentualeProvvigione(BigDecimal percentualeProvvigione) { this.percentualeProvvigione = percentualeProvvigione; }

    public CommissionDistributionMode getDistribuzioneProvvigioni() { return distribuzioneProvvigioni; }
    public void setDistribuzioneProvvigioni(CommissionDistributionMode distribuzioneProvvigioni) { this.distribuzioneProvvigioni = distribuzioneProvvigioni; }

    public CommissionBase getBaseCalcolo() { return baseCalcolo; }
    public void setBaseCalcolo(CommissionBase baseCalcolo) { this.baseCalcolo = baseCalcolo; }
}
