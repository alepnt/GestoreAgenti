package com.example.GestoreAgenti.model.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.GestoreAgenti.model.Dipendente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Evento pianificato nell'agenda di un agente.
 */
@Entity
@Table(name = "agenda_evento")
public class AgendaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    private LocalDate data;

    private LocalTime oraInizio;

    private LocalTime oraFine;

    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgendaItemType tipo = AgendaItemType.MEETING;

    private boolean completato;

    private LocalDateTime creatoIl;

    private LocalDateTime aggiornatoIl;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.creatoIl = now;
        this.aggiornatoIl = now;
    }

    @PreUpdate
    void onUpdate() {
        this.aggiornatoIl = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Dipendente getDipendente() {
        return dipendente;
    }

    public void setDipendente(Dipendente dipendente) {
        this.dipendente = dipendente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public AgendaItemType getTipo() {
        return tipo;
    }

    public void setTipo(AgendaItemType tipo) {
        this.tipo = tipo;
    }

    public boolean isCompletato() {
        return completato;
    }

    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    public LocalDateTime getCreatoIl() {
        return creatoIl;
    }

    public LocalDateTime getAggiornatoIl() {
        return aggiornatoIl;
    }
}
