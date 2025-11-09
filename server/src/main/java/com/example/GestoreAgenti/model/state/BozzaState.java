package com.example.GestoreAgenti.model.state; // Definisce il pacchetto com.example.GestoreAgenti.model.state che contiene questa classe.

import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

/**
 * Stato iniziale di una fattura appena creata.
 */
public class BozzaState implements FatturaState { // Definisce la classe BozzaState che incapsula la logica applicativa.
    public static final String NOME = "BOZZA"; // Dichiara il campo "BOZZA" dell'oggetto.
    public static final String COLORE = "#A0AEC0"; // Dichiara il campo "#A0AEC0" dell'oggetto.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void emetti(Fattura fattura) { // Definisce il metodo emetti che supporta la logica di dominio.
        fattura.setState(new EmessaState()); // Esegue l'istruzione terminata dal punto e virgola.
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
