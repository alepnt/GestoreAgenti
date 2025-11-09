package com.example.GestoreAgenti.model.state; // Definisce il pacchetto com.example.GestoreAgenti.model.state che contiene questa classe.

/**
 * Factory per ottenere l'istanza di {@link FatturaState} a partire dal nome persistito.
 */
public final class FatturaStateFactory { // Definisce la classe FatturaStateFactory che incapsula la logica applicativa.

    private FatturaStateFactory() { // Costruttore della classe FatturaStateFactory che inizializza le dipendenze necessarie.
        // Utility class
    } // Chiude il blocco di codice precedente.

    /**
     * Restituisce lo stato corrispondente al nome fornito.
     *
     * @param nome nome dello stato
     * @return istanza di {@link FatturaState}
     */
    public static FatturaState fromName(String nome) { // Definisce il metodo fromName che supporta la logica di dominio.
        if (nome == null) { // Valuta la condizione per controllare il flusso applicativo.
            return new BozzaState(); // Restituisce il risultato dell'espressione new BozzaState().
        } // Chiude il blocco di codice precedente.

        String statoNormalizzato = nome.toUpperCase(); // Assegna il valore calcolato alla variabile String statoNormalizzato.
        if (EmessaState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new EmessaState(); // Restituisce il risultato dell'espressione new EmessaState().
        } // Chiude il blocco di codice precedente.
        if (PagataState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new PagataState(); // Restituisce il risultato dell'espressione new PagataState().
        } // Chiude il blocco di codice precedente.
        if (AnnullataState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new AnnullataState(); // Restituisce il risultato dell'espressione new AnnullataState().
        } // Chiude il blocco di codice precedente.
        if (BozzaState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new BozzaState(); // Restituisce il risultato dell'espressione new BozzaState().
        } // Chiude il blocco di codice precedente.
        throw new IllegalArgumentException("Stato fattura sconosciuto: " + nome); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
