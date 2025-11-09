package com.example.GestoreAgenti.service.crud; // Definisce il pacchetto com.example.GestoreAgenti.service.crud che contiene questa classe.

/**
 * Strategy interface that allows domain services to plug custom validation and
 * mapping logic inside the standard CRUD pipeline.
 *
 * @param <T> type of the managed aggregate
 */
public interface CrudEntityHandler<T> { // Definisce la interfaccia CrudEntityHandler che incapsula la logica applicativa.

    /**
     * Validates the entity before the creation pipeline runs.
     */
    default void validateForCreate(T entity) { // Definisce il metodo validateForCreate che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Gives implementors the chance to normalise or enrich the entity before it
     * is persisted for the first time.
     */
    default T prepareForCreate(T entity) { // Definisce il metodo prepareForCreate che supporta la logica di dominio.
        return entity; // Restituisce il risultato dell'espressione entity.
    } // Chiude il blocco di codice precedente.

    /**
     * Validates the incoming changes before the update pipeline runs.
     */
    default void validateForUpdate(T existing, T changes) { // Definisce il metodo validateForUpdate che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Applies the incoming changes to the managed entity and returns the
     * instance that should be persisted.
     */
    default T merge(T existing, T changes) { // Definisce il metodo merge che supporta la logica di dominio.
        return changes; // Restituisce il risultato dell'espressione changes.
    } // Chiude il blocco di codice precedente.

    /**
     * Called after an entity has been persisted for the first time.
     */
    default void afterCreate(T entity) { // Definisce il metodo afterCreate che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Called after an entity has been updated.
     */
    default void afterUpdate(T entity) { // Definisce il metodo afterUpdate che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Called before an entity is removed from the persistence context.
     */
    default void beforeDelete(T entity) { // Definisce il metodo beforeDelete che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Called after an entity has been deleted.
     */
    default void afterDelete(T entity) { // Definisce il metodo afterDelete che supporta la logica di dominio.
        // default no-op
    } // Chiude il blocco di codice precedente.

    /**
     * Utility method returning a shared no-op handler instance.
     */
    static <T> CrudEntityHandler<T> noop() { // Definisce il metodo noop che supporta la logica di dominio.
        return new CrudEntityHandler<>() { }; // Restituisce il risultato dell'espressione new CrudEntityHandler<>() { }.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
