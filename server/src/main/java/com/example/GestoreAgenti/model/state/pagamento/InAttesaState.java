package com.example.GestoreAgenti.model.state.pagamento;

import com.example.GestoreAgenti.model.Pagamento;

/**
 * Stato iniziale del pagamento, in attesa di essere elaborato.
 */
public class InAttesaState implements PagamentoState {

    /** Nome descrittivo dello stato. */
    public static final String NOME = "IN_ATTESA";

    /** Colore associato allo stato. */
    private static final String COLORE = "#6C757D"; // Grigio neutro.

    @Override
    public void elabora(Pagamento pagamento) {
        pagamento.setState(new InElaborazioneState());
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
