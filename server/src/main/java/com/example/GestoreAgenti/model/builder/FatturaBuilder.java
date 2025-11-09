package com.example.GestoreAgenti.model.builder; // Definisce il pacchetto com.example.GestoreAgenti.model.builder che contiene questa classe.

import java.math.BigDecimal; // Importa java.math.BigDecimal per abilitare le funzionalità utilizzate nel file.
import java.math.RoundingMode; // Importa java.math.RoundingMode per abilitare le funzionalità utilizzate nel file.
import java.time.LocalDate; // Importa java.time.LocalDate per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

/**
 * Builder per creare istanze di {@link Fattura} applicando le regole di dominio
 * su importi e stato iniziale.
 */
public final class FatturaBuilder { // Definisce la classe FatturaBuilder che incapsula la logica applicativa.

    private final Fattura fattura; // Dichiara il campo fattura dell'oggetto.
    private BigDecimal aliquotaIva; // Dichiara il campo aliquotaIva dell'oggetto.

    private FatturaBuilder(Cliente cliente, Contratto contratto) { // Costruttore della classe FatturaBuilder che inizializza le dipendenze necessarie.
        this.fattura = new Fattura(); // Aggiorna il campo fattura dell'istanza.
        this.fattura.setCliente(Objects.requireNonNull(cliente, "cliente")); // Aggiorna il campo fattura.setCliente(Objects.requireNonNull(cliente, "cliente")); dell'istanza.
        this.fattura.setContratto(Objects.requireNonNull(contratto, "contratto")); // Aggiorna il campo fattura.setContratto(Objects.requireNonNull(contratto, "contratto")); dell'istanza.
    } // Chiude il blocco di codice precedente.

    private FatturaBuilder(Fattura fattura) { // Costruttore della classe FatturaBuilder che inizializza le dipendenze necessarie.
        this.fattura = fattura; // Aggiorna il campo fattura dell'istanza.
    } // Chiude il blocco di codice precedente.

    /**
     * Crea un builder per una nuova fattura partendo da cliente e contratto.
     */
    public static FatturaBuilder nuovaFattura(Cliente cliente, Contratto contratto) { // Definisce il metodo nuovaFattura che supporta la logica di dominio.
        return new FatturaBuilder(cliente, contratto); // Restituisce il risultato dell'espressione new FatturaBuilder(cliente, contratto).
    } // Chiude il blocco di codice precedente.

    /**
     * Crea un builder inizializzando cliente e importo a partire dal contratto.
     */
    public static FatturaBuilder perContratto(Contratto contratto) { // Definisce il metodo perContratto che supporta la logica di dominio.
        Objects.requireNonNull(contratto, "contratto"); // Esegue l'istruzione terminata dal punto e virgola.
        Cliente cliente = Objects.requireNonNull(contratto.getCliente(), "Il contratto deve avere un cliente associato"); // Assegna il valore calcolato alla variabile Cliente cliente.
        FatturaBuilder builder = new FatturaBuilder(cliente, contratto); // Assegna il valore calcolato alla variabile FatturaBuilder builder.
        if (contratto.getImporto() != null) { // Valuta la condizione per controllare il flusso applicativo.
            builder.conImponibile(contratto.getImporto()); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        builder.fattura.setNumeroFattura(contratto.getIdContratto() != null // Esegue l'istruzione necessaria alla logica applicativa.
                ? "CTR-" + contratto.getIdContratto() // Esegue l'istruzione necessaria alla logica applicativa.
                : null); // Esegue l'istruzione terminata dal punto e virgola.
        builder.fattura.setDataEmissione(contratto.getDataFine() != null ? contratto.getDataFine() : LocalDate.now()); // Esegue l'istruzione terminata dal punto e virgola.
        return builder; // Restituisce il risultato dell'espressione builder.
    } // Chiude il blocco di codice precedente.

    /**
     * Popola il builder a partire da una fattura esistente.
     */
    public static FatturaBuilder daPrototype(Fattura fattura) { // Definisce il metodo daPrototype che supporta la logica di dominio.
        Objects.requireNonNull(fattura, "fattura"); // Esegue l'istruzione terminata dal punto e virgola.
        Fattura copia = new Fattura(); // Assegna il valore calcolato alla variabile Fattura copia.
        copia.setCliente(fattura.getCliente()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setContratto(fattura.getContratto()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setNumeroFattura(fattura.getNumeroFattura()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setDataEmissione(fattura.getDataEmissione()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setImponibile(fattura.getImponibile()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setIva(fattura.getIva()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setTotale(fattura.getTotale()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setState(fattura.getState()); // Esegue l'istruzione terminata dal punto e virgola.
        return new FatturaBuilder(copia); // Restituisce il risultato dell'espressione new FatturaBuilder(copia).
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder conNumero(String numeroFattura) { // Definisce il metodo conNumero che supporta la logica di dominio.
        this.fattura.setNumeroFattura(Objects.requireNonNull(numeroFattura, "numeroFattura")); // Aggiorna il campo fattura.setNumeroFattura(Objects.requireNonNull(numeroFattura, "numeroFattura")); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder emessaIl(LocalDate dataEmissione) { // Definisce il metodo emessaIl che supporta la logica di dominio.
        this.fattura.setDataEmissione(Objects.requireNonNull(dataEmissione, "dataEmissione")); // Aggiorna il campo fattura.setDataEmissione(Objects.requireNonNull(dataEmissione, "dataEmissione")); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder conImponibile(BigDecimal imponibile) { // Definisce il metodo conImponibile che supporta la logica di dominio.
        Objects.requireNonNull(imponibile, "imponibile"); // Esegue l'istruzione terminata dal punto e virgola.
        if (imponibile.signum() < 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("L'imponibile deve essere positivo"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.fattura.setImponibile(imponibile); // Aggiorna il campo fattura.setImponibile(imponibile); dell'istanza.
        ricalcolaTotali(); // Esegue l'istruzione terminata dal punto e virgola.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder conIva(BigDecimal iva) { // Definisce il metodo conIva che supporta la logica di dominio.
        Objects.requireNonNull(iva, "iva"); // Esegue l'istruzione terminata dal punto e virgola.
        if (iva.signum() < 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("L'IVA deve essere positiva"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.fattura.setIva(iva); // Aggiorna il campo fattura.setIva(iva); dell'istanza.
        ricalcolaTotali(); // Esegue l'istruzione terminata dal punto e virgola.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder conAliquotaIva(BigDecimal aliquota) { // Definisce il metodo conAliquotaIva che supporta la logica di dominio.
        Objects.requireNonNull(aliquota, "aliquota"); // Esegue l'istruzione terminata dal punto e virgola.
        if (aliquota.compareTo(BigDecimal.ZERO) < 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("L'aliquota IVA non può essere negativa"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.aliquotaIva = aliquota; // Aggiorna il campo aliquotaIva dell'istanza.
        ricalcolaTotali(); // Esegue l'istruzione terminata dal punto e virgola.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public FatturaBuilder conTotale(BigDecimal totale) { // Definisce il metodo conTotale che supporta la logica di dominio.
        Objects.requireNonNull(totale, "totale"); // Esegue l'istruzione terminata dal punto e virgola.
        if (totale.signum() < 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il totale deve essere positivo"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.fattura.setTotale(totale); // Aggiorna il campo fattura.setTotale(totale); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    private void ricalcolaTotali() { // Definisce il metodo ricalcolaTotali che supporta la logica di dominio.
        if (fattura.getImponibile() == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        if (aliquotaIva != null) { // Valuta la condizione per controllare il flusso applicativo.
            BigDecimal ivaCalcolata = fattura.getImponibile().multiply(aliquotaIva).setScale(2, RoundingMode.HALF_UP); // Assegna il valore calcolato alla variabile BigDecimal ivaCalcolata.
            fattura.setIva(ivaCalcolata); // Esegue l'istruzione terminata dal punto e virgola.
            fattura.setTotale(fattura.getImponibile().add(ivaCalcolata)); // Esegue l'istruzione terminata dal punto e virgola.
        } else if (fattura.getIva() != null) { // Apre il blocco di codice associato alla dichiarazione.
            fattura.setTotale(fattura.getImponibile().add(fattura.getIva())); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public Fattura build() { // Definisce il metodo build che supporta la logica di dominio.
        Objects.requireNonNull(fattura.getNumeroFattura(), "Numero fattura obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(fattura.getDataEmissione(), "Data emissione obbligatoria"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(fattura.getImponibile(), "Imponibile obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        if (fattura.getIva() == null && aliquotaIva != null) { // Valuta la condizione per controllare il flusso applicativo.
            ricalcolaTotali(); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        Objects.requireNonNull(fattura.getIva(), "IVA obbligatoria"); // Esegue l'istruzione terminata dal punto e virgola.
        if (fattura.getTotale() == null) { // Valuta la condizione per controllare il flusso applicativo.
            fattura.setTotale(fattura.getImponibile().add(fattura.getIva())); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        BigDecimal atteso = fattura.getImponibile().add(fattura.getIva()); // Assegna il valore calcolato alla variabile BigDecimal atteso.
        if (fattura.getTotale().compareTo(atteso) != 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalStateException("Totale incoerente con imponibile e IVA"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        return fattura; // Restituisce il risultato dell'espressione fattura.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
