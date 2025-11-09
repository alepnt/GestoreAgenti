package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.
import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

import com.example.GestoreAgenti.model.state.pagamento.InAttesaState; // Importa lo stato iniziale InAttesa per i pagamenti.
import com.example.GestoreAgenti.model.state.pagamento.PagamentoState; // Importa l'interfaccia dello state pattern per i pagamenti.
import com.example.GestoreAgenti.model.state.pagamento.PagamentoStateFactory; // Importa la factory per risolvere gli stati del pagamento.
import com.fasterxml.jackson.annotation.JsonIgnore; // Importa JsonIgnore per evitare la serializzazione del campo di stato interno.
import jakarta.persistence.*; // Importa tutte le annotazioni JPA necessarie per mappare l'entità nel database.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "pagamento") // Applica l'annotazione @Table per configurare il componente.
public class Pagamento { // Dichiara la classe Pagamento che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idPagamento; // Memorizza l'ID del pagamento dell'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_fattura", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Fattura fattura; // Memorizza la fattura associato all'entità.

    private LocalDate dataPagamento; // Memorizza la data di pagamento dell'entità.

    private BigDecimal importo; // Memorizza l'importo dell'entità.

    private String metodo; // Memorizza il metodo di pagamento dell'entità.

    private String stato = InAttesaState.NOME; // Memorizza lo stato del pagamento.

    @Transient // Impedisce a JPA di persistere direttamente l'oggetto stato.
    private PagamentoState state = new InAttesaState(); // Riferimento all'implementazione concreta dello stato del pagamento.

    @PrePersist // Assicura la coerenza tra valore testuale e implementazione prima del salvataggio.
    @PreUpdate // Assicura la coerenza tra valore testuale e implementazione prima dell'aggiornamento.
    private void sincronizzaStatoPersistito() { // Definisce il metodo sincronizzaStatoPersistito che supporta la logica di dominio.
        this.stato = risolviStato().getNome(); // Aggiorna il campo stato dell'istanza.
    } // Chiude il blocco di codice precedente.

    @PostLoad // Ricostruisce l'oggetto stato dopo il caricamento dal database.
    private void ricostruisciStato() { // Definisce il metodo ricostruisciStato che supporta la logica di dominio.
        this.state = PagamentoStateFactory.fromName(this.stato); // Aggiorna il campo state dell'istanza.
    } // Chiude il blocco di codice precedente.

    // Getters e Setters
    public Long getIdPagamento() { return idPagamento; } // Restituisce l'ID del pagamento dell'entità.
    public void setIdPagamento(Long idPagamento) { this.idPagamento = idPagamento; } // Imposta l'ID del pagamento per l'entità.

    public Fattura getFattura() { return fattura; } // Restituisce la fattura dell'entità.
    public void setFattura(Fattura fattura) { this.fattura = fattura; } // Imposta la fattura per l'entità.

    public LocalDate getDataPagamento() { return dataPagamento; } // Restituisce la data di pagamento dell'entità.
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; } // Imposta la data di pagamento per l'entità.

    public BigDecimal getImporto() { return importo; } // Restituisce l'importo dell'entità.
    public void setImporto(BigDecimal importo) { this.importo = importo; } // Imposta l'importo per l'entità.

    public String getMetodo() { return metodo; } // Restituisce il metodo di pagamento dell'entità.
    public void setMetodo(String metodo) { this.metodo = metodo; } // Imposta il metodo di pagamento per l'entità.

    public String getStato() { return risolviStato().getNome(); } // Restituisce lo stato del pagamento.
    public String getColoreStato() { return risolviStato().getColore(); } // Restituisce il colore associato allo stato del pagamento.
    public void setStato(String stato) { setState(PagamentoStateFactory.fromName(stato)); } // Imposta lo stato partendo dal nome persistito.

    @JsonIgnore // Evita di serializzare lo stato interno durante le risposte REST.
    public PagamentoState getState() { return risolviStato(); } // Restituisce l'implementazione concreta dello stato.
    public void setState(PagamentoState state) { // Definisce il metodo setState che supporta la logica di dominio.
        this.state = state; // Aggiorna il campo state dell'istanza.
        this.stato = state.getNome(); // Aggiorna il campo stato dell'istanza.
    } // Aggiorna implementazione concreta e valore persistito.

    public void elabora() { risolviStato().elabora(this); } // Richiede l'avvio dell'elaborazione del pagamento.
    public void completa() { risolviStato().completa(this); } // Richiede la conferma del pagamento.
    public void fallisci() { risolviStato().fallisci(this); } // Richiede di marcare il pagamento come fallito.
    public void ripeti() { risolviStato().ripeti(this); } // Richiede di ripetere l'elaborazione del pagamento.

    private PagamentoState risolviStato() { // Definisce il metodo risolviStato che supporta la logica di dominio.
        if (state == null) { // Valuta la condizione per controllare il flusso applicativo.
            state = PagamentoStateFactory.fromName(stato); // Assegna il valore calcolato alla variabile state.
        } // Chiude il blocco di codice precedente.
        return state; // Restituisce il risultato dell'espressione state.
    } // Risolve l'implementazione dello stato partendo dal valore persistito.
} // Chiude il blocco di codice precedente.
