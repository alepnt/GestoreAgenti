package com.example.GestoreAgenti.model.state; // Definisce il pacchetto com.example.GestoreAgenti.model.state che contiene questa classe.

import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

/**
 * Stato di una fattura emessa ma non ancora saldata.
 */
public class EmessaState implements FatturaState { // Definisce la classe EmessaState che incapsula la logica applicativa.
    public static final String NOME = "EMESSA"; // Dichiara il campo "EMESSA" dell'oggetto.
    public static final String COLORE = "#4299E1"; // Dichiara il campo "#4299E1" dell'oggetto.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void paga(Fattura fattura) { // Definisce il metodo paga che supporta la logica di dominio.
        fattura.setState(new PagataState()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void annulla(Fattura fattura) { // Definisce il metodo annulla che supporta la logica di dominio.
        fattura.setState(new AnnullataState()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getNome() { // Definisce il metodo getNome che supporta la logica di dominio.
        return NOME; // Restituisce il risultato dell'espressione NOME.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getColore() { // Definisce il metodo getColore che supporta la logica di dominio.
        return COLORE; // Restituisce il risultato dell'espressione COLORE.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
