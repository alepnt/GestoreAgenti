package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

/**
 * Stato che rappresenta un pagamento fallito.
 */
public class FallitoState implements PagamentoState { // Definisce la classe FallitoState che incapsula la logica applicativa.

    /** Nome descrittivo dello stato. */
    public static final String NOME = "FALLITO"; // Dichiara il campo "FALLITO" dell'oggetto.

    /** Colore associato allo stato. */
    private static final String COLORE = "#DC3545"; // Rosso di errore.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void ripeti(Pagamento pagamento) { // Definisce il metodo ripeti che supporta la logica di dominio.
        pagamento.setState(new InAttesaState()); // Esegue l'istruzione terminata dal punto e virgola.
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
