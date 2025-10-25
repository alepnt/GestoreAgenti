package com.example.GestoreAgenti.model;

import jakarta.persistence.Entity;

@Entity
public class Dipendente extends Persona {

    private String username;
    private String password;
    private String ranking;
    private String team;
    private String contrattoNo;
    private Double totProvvigioneMensile;
    private Double totProvvigioneAnnuale;

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRanking() { return ranking; }
    public void setRanking(String ranking) { this.ranking = ranking; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public String getContrattoNo() { return contrattoNo; }
    public void setContrattoNo(String contrattoNo) { this.contrattoNo = contrattoNo; }

    public Double getTotProvvigioneMensile() { return totProvvigioneMensile; }
    public void setTotProvvigioneMensile(Double totProvvigioneMensile) { this.totProvvigioneMensile = totProvvigioneMensile; }

    public Double getTotProvvigioneAnnuale() { return totProvvigioneAnnuale; }
    public void setTotProvvigioneAnnuale(Double totProvvigioneAnnuale) { this.totProvvigioneAnnuale = totProvvigioneAnnuale; }
}
