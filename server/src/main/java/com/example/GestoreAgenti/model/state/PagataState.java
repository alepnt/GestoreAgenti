package com.example.GestoreAgenti.model.state; // Definisce il pacchetto com.example.GestoreAgenti.model.state che contiene questa classe.

/**
 * Stato di una fattura già saldata.
 */
public class PagataState implements FatturaState { // Definisce la classe PagataState che incapsula la logica applicativa.
    public static final String NOME = "PAGATA"; // Dichiara il campo "PAGATA" dell'oggetto.
    public static final String COLORE = "#48BB78"; // Dichiara il campo "#48BB78" dell'oggetto.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getNome() { // Definisce il metodo getNome che supporta la logica di dominio.
        return NOME; // Restituisce il risultato dell'espressione NOME.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getColore() { // Definisce il metodo getColore che supporta la logica di dominio.
        return COLORE; // Restituisce il risultato dell'espressione COLORE.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
