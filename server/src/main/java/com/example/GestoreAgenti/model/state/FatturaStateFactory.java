package com.example.GestoreAgenti.model.state;

/**
 * Factory per ottenere l'istanza di {@link FatturaState} a partire dal nome persistito.
 */
public final class FatturaStateFactory {

    private FatturaStateFactory() {
        // Utility class
    }

    /**
     * Restituisce lo stato corrispondente al nome fornito.
     *
     * @param nome nome dello stato
     * @return istanza di {@link FatturaState}
     */
    public static FatturaState fromName(String nome) {
        if (nome == null) {
            return new BozzaState();
        }

        String statoNormalizzato = nome.toUpperCase();
        if (EmessaState.NOME.equals(statoNormalizzato)) {
            return new EmessaState();
        }
        if (PagataState.NOME.equals(statoNormalizzato)) {
            return new PagataState();
        }
        if (AnnullataState.NOME.equals(statoNormalizzato)) {
            return new AnnullataState();
        }
        if (BozzaState.NOME.equals(statoNormalizzato)) {
            return new BozzaState();
        }
        throw new IllegalArgumentException("Stato fattura sconosciuto: " + nome);
    }
}
