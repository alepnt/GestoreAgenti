package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service che contiene questa classe.

import java.math.BigDecimal; // Importa java.math.BigDecimal per abilitare le funzionalità utilizzate nel file.
import java.time.LocalDate; // Importa java.time.LocalDate per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

/**
 * Singleton che incapsula la logica di generazione delle fatture a partire da contratti.
 */
public final class GeneratoreFattura { // Definisce la classe GeneratoreFattura che incapsula la logica applicativa.

    private static final GeneratoreFattura INSTANCE = new GeneratoreFattura(); // Costruttore della classe GeneratoreFattura che inizializza le dipendenze necessarie.

    private GeneratoreFattura() { // Costruttore della classe GeneratoreFattura che inizializza le dipendenze necessarie.
    } // Chiude il blocco di codice precedente.

    public static GeneratoreFattura getInstance() { // Definisce il metodo getInstance che supporta la logica di dominio.
        return INSTANCE; // Restituisce il risultato dell'espressione INSTANCE.
    } // Chiude il blocco di codice precedente.

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, BigDecimal aliquotaIva) { // Definisce il metodo creaDaContratto che supporta la logica di dominio.
        return creaDaContratto(contratto, numeroFattura, LocalDate.now(), aliquotaIva); // Restituisce il risultato dell'espressione creaDaContratto(contratto, numeroFattura, LocalDate.now(), aliquotaIva).
    } // Chiude il blocco di codice precedente.

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, LocalDate dataEmissione, BigDecimal aliquotaIva) { // Definisce il metodo creaDaContratto che supporta la logica di dominio.
        Objects.requireNonNull(contratto, "contratto"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(numeroFattura, "numeroFattura"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(dataEmissione, "dataEmissione"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(aliquotaIva, "aliquotaIva"); // Esegue l'istruzione terminata dal punto e virgola.
        if (contratto.getCliente() == null) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il contratto deve avere un cliente associato"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        if (contratto.getImporto() == null) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Il contratto deve avere un importo per generare una fattura"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.

        return Fattura.builder(contratto) // Restituisce il risultato dell'espressione Fattura.builder(contratto).
                .conNumero(numeroFattura) // Esegue l'istruzione necessaria alla logica applicativa.
                .emessaIl(dataEmissione) // Esegue l'istruzione necessaria alla logica applicativa.
                .conImponibile(contratto.getImporto()) // Esegue l'istruzione necessaria alla logica applicativa.
                .conAliquotaIva(aliquotaIva) // Esegue l'istruzione necessaria alla logica applicativa.
                .build(); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
