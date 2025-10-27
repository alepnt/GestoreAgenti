package com.example.GestoreAgenti.model.state;

import com.example.GestoreAgenti.model.Fattura;

/**
 * Stato iniziale di una fattura appena creata.
 */
public class BozzaState implements FatturaState {
    public static final String NOME = "BOZZA";
    public static final String COLORE = "#A0AEC0";

    @Override
    public void emetti(Fattura fattura) {
        fattura.setState(new EmessaState());
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
