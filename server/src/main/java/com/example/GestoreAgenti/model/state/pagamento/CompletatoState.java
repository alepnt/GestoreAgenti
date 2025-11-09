package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

/**
 * Stato che rappresenta un pagamento completato con successo.
 */
public class CompletatoState implements PagamentoState { // Definisce la classe CompletatoState che incapsula la logica applicativa.

    /** Nome descrittivo dello stato. */
    public static final String NOME = "COMPLETATO"; // Dichiara il campo "COMPLETATO" dell'oggetto.

    /** Colore associato allo stato. */
    private static final String COLORE = "#198754"; // Verde di conferma.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getNome() { // Definisce il metodo getNome che supporta la logica di dominio.
        return NOME; // Restituisce il risultato dell'espressione NOME.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getColore() { // Definisce il metodo getColore che supporta la logica di dominio.
        return COLORE; // Restituisce il risultato dell'espressione COLORE.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
