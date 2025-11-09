package com.example.GestoreAgenti.model.state; // Definisce il pacchetto com.example.GestoreAgenti.model.state che contiene questa classe.

import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

/**
 * Definisce il contratto per gli stati di una {@link Fattura}.
 */
public interface FatturaState { // Definisce la interfaccia FatturaState che incapsula la logica applicativa.

    /**
     * Richiede l'emissione della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void emetti(Fattura fattura) { // Definisce il metodo emetti che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: emettere fattura in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Richiede il pagamento della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void paga(Fattura fattura) { // Definisce il metodo paga che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: pagare fattura in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Richiede l'annullamento della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void annulla(Fattura fattura) { // Definisce il metodo annulla che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: annullare fattura in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Nome descrittivo dello stato.
     *
     * @return nome dello stato
     */
    String getNome(); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Codice colore esadecimale associato allo stato per la rappresentazione grafica.
     *
     * @return colore esadecimale nel formato #RRGGBB
     */
    String getColore(); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
