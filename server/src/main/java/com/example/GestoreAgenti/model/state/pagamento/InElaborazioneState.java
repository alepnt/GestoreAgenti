package com.example.GestoreAgenti.model.state.pagamento;

import com.example.GestoreAgenti.model.Pagamento;

/**
 * Stato che rappresenta un pagamento in corso di elaborazione.
 */
public class InElaborazioneState implements PagamentoState {

    /** Nome descrittivo dello stato. */
    public static final String NOME = "IN_ELABORAZIONE";

    /** Colore associato allo stato. */
    private static final String COLORE = "#0D6EFD"; // Blu informativo.

    @Override
    public void completa(Pagamento pagamento) {
        pagamento.setState(new CompletatoState());
    }

    @Override
    public void fallisci(Pagamento pagamento) {
        pagamento.setState(new FallitoState());
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getColore() {
        return COLORE;
    }
}
