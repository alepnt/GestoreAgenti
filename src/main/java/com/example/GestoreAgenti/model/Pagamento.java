package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import java.math.BigDecimal; // Importa BigDecimal per rappresentare importi monetari con precisione.
import java.time.LocalDate; // Importa LocalDate per gestire le date senza informazioni temporali.

import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.
import jakarta.persistence.GeneratedValue; // Importa GeneratedValue per definire la generazione automatica della chiave primaria.
import jakarta.persistence.GenerationType; // Importa GenerationType per specificare la strategia di generazione delle chiavi.
import jakarta.persistence.Id; // Importa Id per identificare il campo chiave primaria dell'entità.
import jakarta.persistence.JoinColumn; // Importa JoinColumn per descrivere la colonna di relazione nella tabella.
import jakarta.persistence.ManyToOne; // Importa ManyToOne per modellare una relazione molti-a-uno.
import jakarta.persistence.Table; // Importa Table per impostare il nome della tabella su cui mappare l'entità.

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
} // Chiude il blocco di codice precedente.
