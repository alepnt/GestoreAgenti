package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model che contiene questa classe.

/**
 * Interfaccia di supporto per il pattern Prototype.
 * @param <T> tipo di oggetto restituito dalla copia.
 */
public interface Prototype<T> { // Definisce la interfaccia Prototype che incapsula la logica applicativa.

    /**
     * Crea una copia dell'istanza corrente.
     *
     * @return nuova istanza con lo stesso stato dell'oggetto prototipo.
     */
    T copia(); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
