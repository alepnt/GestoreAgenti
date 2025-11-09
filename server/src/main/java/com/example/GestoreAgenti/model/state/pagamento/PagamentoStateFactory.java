package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

/**
 * Factory per ottenere l'istanza di {@link PagamentoState} a partire dal nome persistito.
 */
public final class PagamentoStateFactory { // Definisce la classe PagamentoStateFactory che incapsula la logica applicativa.

    private PagamentoStateFactory() { // Costruttore della classe PagamentoStateFactory che inizializza le dipendenze necessarie.
        // Utility class
    } // Chiude il blocco di codice precedente.

    /**
     * Restituisce lo stato corrispondente al nome fornito.
     *
     * @param nome nome dello stato
     * @return istanza di {@link PagamentoState}
     */
    public static PagamentoState fromName(String nome) { // Definisce il metodo fromName che supporta la logica di dominio.
        if (nome == null) { // Valuta la condizione per controllare il flusso applicativo.
            return new InAttesaState(); // Restituisce il risultato dell'espressione new InAttesaState().
        } // Chiude il blocco di codice precedente.

        String statoNormalizzato = nome.toUpperCase(); // Assegna il valore calcolato alla variabile String statoNormalizzato.
        if (InElaborazioneState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new InElaborazioneState(); // Restituisce il risultato dell'espressione new InElaborazioneState().
        } // Chiude il blocco di codice precedente.
        if (CompletatoState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new CompletatoState(); // Restituisce il risultato dell'espressione new CompletatoState().
        } // Chiude il blocco di codice precedente.
        if (FallitoState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new FallitoState(); // Restituisce il risultato dell'espressione new FallitoState().
        } // Chiude il blocco di codice precedente.
        if (InAttesaState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
            return new InAttesaState(); // Restituisce il risultato dell'espressione new InAttesaState().
        } // Chiude il blocco di codice precedente.
        throw new IllegalArgumentException("Stato pagamento sconosciuto: " + nome); // Propaga un'eccezione verso il chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
