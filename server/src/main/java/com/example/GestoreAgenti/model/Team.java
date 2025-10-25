package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.

import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.
import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.Table; // Importa Table per impostare il nome della tabella su cui mappare l'entità.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "Team") // Applica l'annotazione @Table per configurare il componente.
public class Team { // Dichiara la classe Team che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long no; // Memorizza il numero identificativo dell'entità.

    private String provincia; // Memorizza la provincia dell'entità.

    private Long responsabileId; // Memorizza l'ID del responsabile dell'entità.

    private BigDecimal totProfittoMensile; // Memorizza il totale profitto mensile dell'entità.
    private BigDecimal totProvvigioneMensile; // Memorizza la totale provvigione mensile dell'entità.
    private BigDecimal totProvvigioneAnnuo; // Memorizza la totale provvigione annua dell'entità.

    // Costruttori
    public Team() {} // Costruttore della classe Team che inizializza le dipendenze richieste.

    public Team(String provincia, Long responsabileId, BigDecimal totProfittoMensile, // Avvia la dichiarazione del costruttore Team con i parametri principali.
                BigDecimal totProvvigioneMensile, BigDecimal totProvvigioneAnnuo) { // Completa la firma del costruttore includendo gli ultimi parametri.
        this.provincia = provincia; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.responsabileId = responsabileId; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.totProfittoMensile = totProfittoMensile; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.totProvvigioneMensile = totProvvigioneMensile; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.totProvvigioneAnnuo = totProvvigioneAnnuo; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    // Getter e Setter
    public Long getNo() { return no; } // Restituisce il numero identificativo dell'entità.
    public void setNo(Long no) { this.no = no; } // Imposta il numero identificativo per l'entità.

    public String getProvincia() { return provincia; } // Restituisce la provincia dell'entità.
    public void setProvincia(String provincia) { this.provincia = provincia; } // Imposta la provincia per l'entità.

    public Long getResponsabileId() { return responsabileId; } // Restituisce l'ID del responsabile dell'entità.
    public void setResponsabileId(Long responsabileId) { this.responsabileId = responsabileId; } // Imposta l'ID del responsabile per l'entità.

    public BigDecimal getTotProfittoMensile() { return totProfittoMensile; } // Restituisce il totale profitto mensile dell'entità.
    public void setTotProfittoMensile(BigDecimal totProfittoMensile) { this.totProfittoMensile = totProfittoMensile; } // Imposta il totale profitto mensile per l'entità.

    public BigDecimal getTotProvvigioneMensile() { return totProvvigioneMensile; } // Restituisce la totale provvigione mensile dell'entità.
    public void setTotProvvigioneMensile(BigDecimal totProvvigioneMensile) { this.totProvvigioneMensile = totProvvigioneMensile; } // Imposta la totale provvigione mensile per l'entità.

    public BigDecimal getTotProvvigioneAnnuo() { return totProvvigioneAnnuo; } // Restituisce la totale provvigione annua dell'entità.
    public void setTotProvvigioneAnnuo(BigDecimal totProvvigioneAnnuo) { this.totProvvigioneAnnuo = totProvvigioneAnnuo; } // Imposta la totale provvigione annua per l'entità.
} // Chiude il blocco di codice precedente.

