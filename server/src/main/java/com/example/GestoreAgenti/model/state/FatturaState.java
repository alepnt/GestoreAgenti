package com.example.GestoreAgenti.model.state;

import com.example.GestoreAgenti.model.Fattura;

/**
 * Definisce il contratto per gli stati di una {@link Fattura}.
 */
public interface FatturaState {

    /**
     * Richiede l'emissione della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void emetti(Fattura fattura) {
        throw new IllegalStateException("Transizione di stato non consentita: emettere fattura in stato " + getNome());
    }

    /**
     * Richiede il pagamento della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void paga(Fattura fattura) {
        throw new IllegalStateException("Transizione di stato non consentita: pagare fattura in stato " + getNome());
    }

    /**
     * Richiede l'annullamento della fattura.
     *
     * @param fattura fattura di riferimento
     */
    default void annulla(Fattura fattura) {
        throw new IllegalStateException("Transizione di stato non consentita: annullare fattura in stato " + getNome());
    }

    /**
     * Nome descrittivo dello stato.
     *
     * @return nome dello stato
     */
    String getNome();

    /**
     * Codice colore esadecimale associato allo stato per la rappresentazione grafica.
     *
     * @return colore esadecimale nel formato #RRGGBB
     */
    String getColore();
}
