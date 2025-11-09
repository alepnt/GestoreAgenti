package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

/**
 * Definisce il contratto per gli stati di un {@link Pagamento}.
 */
public interface PagamentoState { // Definisce la interfaccia PagamentoState che incapsula la logica applicativa.

    /**
     * Richiede di avviare l'elaborazione del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void elabora(Pagamento pagamento) { // Definisce il metodo elabora che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: elaborare pagamento in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Richiede la conferma del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void completa(Pagamento pagamento) { // Definisce il metodo completa che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: completare pagamento in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Richiede di marcare il pagamento come fallito.
     *
     * @param pagamento pagamento di riferimento
     */
    default void fallisci(Pagamento pagamento) { // Definisce il metodo fallisci che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: fallire pagamento in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.

    /**
     * Richiede di ripetere l'elaborazione del pagamento.
     *
     * @param pagamento pagamento di riferimento
     */
    default void ripeti(Pagamento pagamento) { // Definisce il metodo ripeti che supporta la logica di dominio.
        throw new IllegalStateException("Transizione di stato non consentita: ripetere pagamento in stato " + getNome()); // Propaga un'eccezione verso il chiamante.
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
