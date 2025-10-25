package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.

@Entity // Applica l'annotazione @Entity per configurare il componente.
public class Dipendente extends Persona { // Dichiara la classe Dipendente che incapsula la logica del dominio.

    private String username; // Memorizza il nome utente dell'entità.
    private String password; // Memorizza la password dell'entità.
    private String ranking; // Memorizza il ranking dell'entità.
    private String team; // Memorizza il team dell'entità.
    private String contrattoNo; // Memorizza il numero di contratto dell'entità.
    private Double totProvvigioneMensile; // Memorizza la totale provvigione mensile dell'entità.
    private Double totProvvigioneAnnuale; // Memorizza la totale provvigione annuale dell'entità.

    // Getters e Setters
    public String getUsername() { return username; } // Restituisce il nome utente dell'entità.
    public void setUsername(String username) { this.username = username; } // Imposta il nome utente per l'entità.

    public String getPassword() { return password; } // Restituisce la password dell'entità.
    public void setPassword(String password) { this.password = password; } // Imposta la password per l'entità.

    public String getRanking() { return ranking; } // Restituisce il ranking dell'entità.
    public void setRanking(String ranking) { this.ranking = ranking; } // Imposta il ranking per l'entità.

    public String getTeam() { return team; } // Restituisce il team dell'entità.
    public void setTeam(String team) { this.team = team; } // Imposta il team per l'entità.

    public String getContrattoNo() { return contrattoNo; } // Restituisce il numero di contratto dell'entità.
    public void setContrattoNo(String contrattoNo) { this.contrattoNo = contrattoNo; } // Imposta il numero di contratto per l'entità.

    public Double getTotProvvigioneMensile() { return totProvvigioneMensile; } // Restituisce la totale provvigione mensile dell'entità.
    public void setTotProvvigioneMensile(Double totProvvigioneMensile) { this.totProvvigioneMensile = totProvvigioneMensile; } // Imposta la totale provvigione mensile per l'entità.

    public Double getTotProvvigioneAnnuale() { return totProvvigioneAnnuale; } // Restituisce la totale provvigione annuale dell'entità.
    public void setTotProvvigioneAnnuale(Double totProvvigioneAnnuale) { this.totProvvigioneAnnuale = totProvvigioneAnnuale; } // Imposta la totale provvigione annuale per l'entità.
} // Chiude il blocco di codice precedente.
