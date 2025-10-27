package com.example.GestoreAgenti.model.state;

import com.example.GestoreAgenti.model.Fattura;

/**
 * Stato di una fattura emessa ma non ancora saldata.
 */
public class EmessaState implements FatturaState {
    public static final String NOME = "EMESSA";
    public static final String COLORE = "#4299E1";

    @Override
    public void paga(Fattura fattura) {
        fattura.setState(new PagataState());
    }

    @Override
    public void annulla(Fattura fattura) {
        fattura.setState(new AnnullataState());
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
