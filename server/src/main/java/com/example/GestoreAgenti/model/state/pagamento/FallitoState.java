package com.example.GestoreAgenti.model.state.pagamento;

import com.example.GestoreAgenti.model.Pagamento;

/**
 * Stato che rappresenta un pagamento fallito.
 */
public class FallitoState implements PagamentoState {

    /** Nome descrittivo dello stato. */
    public static final String NOME = "FALLITO";

    /** Colore associato allo stato. */
    private static final String COLORE = "#DC3545"; // Rosso di errore.

    @Override
    public void ripeti(Pagamento pagamento) {
        pagamento.setState(new InAttesaState());
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
