package com.example.GestoreAgenti.model.state.pagamento;

/**
 * Stato che rappresenta un pagamento completato con successo.
 */
public class CompletatoState implements PagamentoState {

    /** Nome descrittivo dello stato. */
    public static final String NOME = "COMPLETATO";

    /** Colore associato allo stato. */
    private static final String COLORE = "#198754"; // Verde di conferma.

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getColore() {
        return COLORE;
    }
}
