package com.example.GestoreAgenti.model.state.pagamento;

/**
 * Factory per ottenere l'istanza di {@link PagamentoState} a partire dal nome persistito.
 */
public final class PagamentoStateFactory {

    private PagamentoStateFactory() {
        // Utility class
    }

    /**
     * Restituisce lo stato corrispondente al nome fornito.
     *
     * @param nome nome dello stato
     * @return istanza di {@link PagamentoState}
     */
    public static PagamentoState fromName(String nome) {
        if (nome == null) {
            return new InAttesaState();
        }

        String statoNormalizzato = nome.toUpperCase();
        if (InElaborazioneState.NOME.equals(statoNormalizzato)) {
            return new InElaborazioneState();
        }
        if (CompletatoState.NOME.equals(statoNormalizzato)) {
            return new CompletatoState();
        }
        if (FallitoState.NOME.equals(statoNormalizzato)) {
            return new FallitoState();
        }
        if (InAttesaState.NOME.equals(statoNormalizzato)) {
            return new InAttesaState();
        }
        throw new IllegalArgumentException("Stato pagamento sconosciuto: " + nome);
    }
}
