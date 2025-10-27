package com.example.GestoreAgenti.model.state;

/**
 * Stato di una fattura già saldata.
 */
public class PagataState implements FatturaState {
    public static final String NOME = "PAGATA";
    public static final String COLORE = "#48BB78";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getColore() {
        return COLORE;
    }
}
