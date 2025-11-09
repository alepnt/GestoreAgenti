package com.example.GestoreAgenti.model.state.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.model.state.pagamento che contiene questa classe.

import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

/**
 * Stato che rappresenta un pagamento in corso di elaborazione.
 */
public class InElaborazioneState implements PagamentoState { // Definisce la classe InElaborazioneState che incapsula la logica applicativa.

    /** Nome descrittivo dello stato. */
    public static final String NOME = "IN_ELABORAZIONE"; // Dichiara il campo "IN_ELABORAZIONE" dell'oggetto.

    /** Colore associato allo stato. */
    private static final String COLORE = "#0D6EFD"; // Blu informativo.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void completa(Pagamento pagamento) { // Definisce il metodo completa che supporta la logica di dominio.
        pagamento.setState(new CompletatoState()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void fallisci(Pagamento pagamento) { // Definisce il metodo fallisci che supporta la logica di dominio.
        pagamento.setState(new FallitoState()); // Esegue l'istruzione terminata dal punto e virgola.
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
