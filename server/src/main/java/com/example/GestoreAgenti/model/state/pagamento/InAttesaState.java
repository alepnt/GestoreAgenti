package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

/**
 * Stato iniziale del pagamento, in attesa di essere elaborato.
 */
public class InAttesaState implements PagamentoState { // Definisce la classe InAttesaState che incapsula la logica applicativa.

    /** Nome descrittivo dello stato. */
    public static final String NOME = "IN_ATTESA"; // Dichiara il campo "IN_ATTESA" dell'oggetto.

    /** Colore associato allo stato. */
    private static final String COLORE = "#6C757D"; // Grigio neutro.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void elabora(Pagamento pagamento) { // Definisce il metodo elabora che supporta la logica di dominio.
        pagamento.setState(new InElaborazioneState()); // Esegue l'istruzione terminata dal punto e virgola.
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
