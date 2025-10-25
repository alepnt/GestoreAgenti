package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

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

    private String stato; // Memorizza lo stato dell'entità.

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

    public String getStato() { return stato; } // Restituisce lo stato dell'entità.
    public void setStato(String stato) { this.stato = stato; } // Imposta lo stato per l'entità.
} // Chiude il blocco di codice precedente.

