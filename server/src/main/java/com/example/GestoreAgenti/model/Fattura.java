package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import com.example.GestoreAgenti.model.builder.FatturaBuilder; // Importa il builder per creare istanze coerenti di Fattura.
import com.example.GestoreAgenti.model.state.BozzaState; // Importa lo stato Bozza per definire lo stato iniziale della fattura.
import com.example.GestoreAgenti.model.state.FatturaState; // Importa l'interfaccia degli stati della fattura.
import com.example.GestoreAgenti.model.state.FatturaStateFactory; // Importa la factory che restituisce le implementazioni degli stati.
import com.fasterxml.jackson.annotation.JsonIgnore; // Importa JsonIgnore per evitare la serializzazione del campo di stato interno.
import jakarta.persistence.*; // Importa tutte le annotazioni JPA necessarie per mappare l'entità nel database.
import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.
import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

@Entity // Applica l'annotazione @Entity per configurare il componente.
@Table(name = "fattura") // Applica l'annotazione @Table per configurare il componente.
public class Fattura { // Dichiara la classe Fattura che incapsula la logica del dominio.

    @Id // Applica l'annotazione @Id per configurare il componente.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Applica l'annotazione @GeneratedValue per configurare il componente.
    private Long idFattura; // Memorizza l'ID della fattura dell'entità.

    private String numeroFattura; // Memorizza il numero della fattura dell'entità.

    private LocalDate dataEmissione; // Memorizza la data di emissione dell'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_cliente", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Cliente cliente; // Memorizza il cliente associato all'entità.

    @ManyToOne // Applica l'annotazione @ManyToOne per configurare il componente.
    @JoinColumn(name = "id_contratto", nullable = false) // Applica l'annotazione @JoinColumn per configurare il componente.
    private Contratto contratto; // Memorizza il contratto associato all'entità.

    private BigDecimal imponibile; // Memorizza l'imponibile dell'entità.
    private BigDecimal iva; // Memorizza l'IVA dell'entità.
    private BigDecimal totale; // Memorizza il totale dell'entità.

    private String stato = BozzaState.NOME; // Memorizza lo stato dell'entità.

    private boolean registrata;

    @Transient // Impedisce a JPA di persistere direttamente l'oggetto stato.
    private FatturaState state = new BozzaState(); // Riferimento all'implementazione concreta dello stato della fattura.

    @PrePersist // Assicura che lo stato testuale sia coerente prima del salvataggio.
    @PreUpdate // Assicura che lo stato testuale sia coerente prima dell'aggiornamento.
    private void sincronizzaStatoPersistito() { // Definisce il metodo sincronizzaStatoPersistito che supporta la logica di dominio.
        this.stato = risolviStato().getNome(); // Aggiorna il campo stato dell'istanza.
    } // Chiude il blocco di codice precedente.

    @PostLoad // Ricostruisce l'oggetto stato dopo il caricamento dal database.
    private void ricostruisciStato() { // Definisce il metodo ricostruisciStato che supporta la logica di dominio.
        this.state = FatturaStateFactory.fromName(this.stato); // Aggiorna il campo state dell'istanza.
    } // Chiude il blocco di codice precedente.

    // Getters e Setters
    public Long getIdFattura() { return idFattura; } // Restituisce l'ID della fattura dell'entità.
    public void setIdFattura(Long idFattura) { this.idFattura = idFattura; } // Imposta l'ID della fattura per l'entità.

    public String getNumeroFattura() { return numeroFattura; } // Restituisce il numero della fattura dell'entità.
    public void setNumeroFattura(String numeroFattura) { this.numeroFattura = numeroFattura; } // Imposta il numero della fattura per l'entità.

    public LocalDate getDataEmissione() { return dataEmissione; } // Restituisce la data di emissione dell'entità.
    public void setDataEmissione(LocalDate dataEmissione) { this.dataEmissione = dataEmissione; } // Imposta la data di emissione per l'entità.

    public Cliente getCliente() { return cliente; } // Restituisce il cliente dell'entità.
    public void setCliente(Cliente cliente) { this.cliente = cliente; } // Imposta il cliente per l'entità.

    public Contratto getContratto() { return contratto; } // Restituisce il contratto dell'entità.
    public void setContratto(Contratto contratto) { this.contratto = contratto; } // Imposta il contratto per l'entità.

    public BigDecimal getImponibile() { return imponibile; } // Restituisce l'imponibile dell'entità.
    public void setImponibile(BigDecimal imponibile) { this.imponibile = imponibile; } // Imposta l'imponibile per l'entità.

    public BigDecimal getIva() { return iva; } // Restituisce l'IVA dell'entità.
    public void setIva(BigDecimal iva) { this.iva = iva; } // Imposta l'IVA per l'entità.

    public BigDecimal getTotale() { return totale; } // Restituisce il totale dell'entità.
    public void setTotale(BigDecimal totale) { this.totale = totale; } // Imposta il totale per l'entità.

    public String getStato() { return risolviStato().getNome(); } // Restituisce lo stato dell'entità.
    public String getColoreStato() { return risolviStato().getColore(); } // Restituisce il colore associato allo stato.
    public void setStato(String stato) { setState(FatturaStateFactory.fromName(stato)); } // Imposta lo stato per l'entità.

    public boolean isRegistrata() { return registrata; }

    public void setRegistrata(boolean registrata) { this.registrata = registrata; }

    @JsonIgnore // Evita di esporre lo stato interno durante la serializzazione JSON.
    public FatturaState getState() { return risolviStato(); } // Restituisce l'implementazione dello stato.
    public void setState(FatturaState state) { // Definisce il metodo setState che supporta la logica di dominio.
        this.state = state; // Aggiorna il campo state dell'istanza.
        this.stato = state.getNome(); // Aggiorna il campo stato dell'istanza.
    } // Aggiorna l'implementazione e il valore persistito dello stato.

    public void emetti() { risolviStato().emetti(this); } // Richiede l'emissione della fattura seguendo le regole dello stato.
    public void paga() { risolviStato().paga(this); } // Richiede il pagamento della fattura seguendo le regole dello stato.
    public void annulla() { risolviStato().annulla(this); } // Richiede l'annullamento della fattura seguendo le regole dello stato.

    private FatturaState risolviStato() { // Definisce il metodo risolviStato che supporta la logica di dominio.
        if (state == null) { // Valuta la condizione per controllare il flusso applicativo.
            state = FatturaStateFactory.fromName(stato); // Assegna il valore calcolato alla variabile state.
        } // Chiude il blocco di codice precedente.
        return state; // Restituisce il risultato dell'espressione state.
    } // Risolve l'implementazione dello stato a partire dal valore persistito.

    public static FatturaBuilder builder(Cliente cliente, Contratto contratto) { // Definisce il metodo builder che supporta la logica di dominio.
        return FatturaBuilder.nuovaFattura(cliente, contratto); // Restituisce il risultato dell'espressione FatturaBuilder.nuovaFattura(cliente, contratto).
    } // Espone un factory method per la creazione fluente.

    public static FatturaBuilder builder(Contratto contratto) { // Definisce il metodo builder che supporta la logica di dominio.
        return FatturaBuilder.perContratto(contratto); // Restituisce il risultato dell'espressione FatturaBuilder.perContratto(contratto).
    } // Shortcut per costruire la fattura a partire dal contratto.
} // Chiude il blocco di codice precedente.

