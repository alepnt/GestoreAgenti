package com.example.GestoreAgenti.model;

/**
 * Interfaccia di supporto per il pattern Prototype.
 * @param <T> tipo di oggetto restituito dalla copia.
 */
public interface Prototype<T> {

    /**
     * Crea una copia dell'istanza corrente.
     *
     * @return nuova istanza con lo stesso stato dell'oggetto prototipo.
     */
    T copia();
}
