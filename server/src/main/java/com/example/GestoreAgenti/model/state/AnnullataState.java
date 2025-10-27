package com.example.GestoreAgenti.model.state;

/**
 * Stato di una fattura annullata.
 */
public class AnnullataState implements FatturaState {
    public static final String NOME = "ANNULLATA";
    public static final String COLORE = "#F56565";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getColore() {
        return COLORE;
    }
}
