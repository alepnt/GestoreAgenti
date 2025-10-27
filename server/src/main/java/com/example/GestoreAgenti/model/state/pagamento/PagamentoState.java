package com.example.GestoreAgenti.model.state.pagamento;

import com.example.GestoreAgenti.model.Pagamento;

/**
 * Definisce il contratto per gli stati di un {@link Pagamento}.
 */
public interface PagamentoState {

    /**
     * Richiede di avviare l'elaborazione del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void elabora(Pagamento pagamento) {
        throw new IllegalStateException("Transizione di stato non consentita: elaborare pagamento in stato " + getNome());
    }

    /**
     * Richiede la conferma del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void completa(Pagamento pagamento) {
        throw new IllegalStateException("Transizione di stato non consentita: completare pagamento in stato " + getNome());
    }

    /**
     * Richiede di marcare il pagamento come fallito.
     *
     * @param pagamento pagamento di riferimento
     */
    default void fallisci(Pagamento pagamento) {
        throw new IllegalStateException("Transizione di stato non consentita: fallire pagamento in stato " + getNome());
    }

    /**
     * Richiede di ripetere l'elaborazione del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void ripeti(Pagamento pagamento) {
        throw new IllegalStateException("Transizione di stato non consentita: ripetere pagamento in stato " + getNome());
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
