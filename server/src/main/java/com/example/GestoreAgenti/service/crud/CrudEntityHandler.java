package com.example.GestoreAgenti.service.crud;

/**
 * Strategy interface that allows domain services to plug custom validation and
 * mapping logic inside the standard CRUD pipeline.
 *
 * @param <T> type of the managed aggregate
 */
public interface CrudEntityHandler<T> {

    /**
     * Validates the entity before the creation pipeline runs.
     */
    default void validateForCreate(T entity) {
        // default no-op
    }

    /**
     * Gives implementors the chance to normalise or enrich the entity before it
     * is persisted for the first time.
     */
    default T prepareForCreate(T entity) {
        return entity;
    }

    /**
     * Validates the incoming changes before the update pipeline runs.
     */
    default void validateForUpdate(T existing, T changes) {
        // default no-op
    }

    /**
     * Applies the incoming changes to the managed entity and returns the
     * instance that should be persisted.
     */
    default T merge(T existing, T changes) {
        return changes;
    }

    /**
     * Called after an entity has been persisted for the first time.
     */
    default void afterCreate(T entity) {
        // default no-op
    }

    /**
     * Called after an entity has been updated.
     */
    default void afterUpdate(T entity) {
        // default no-op
    }

    /**
     * Called before an entity is removed from the persistence context.
     */
    default void beforeDelete(T entity) {
        // default no-op
    }

    /**
     * Called after an entity has been deleted.
     */
    default void afterDelete(T entity) {
        // default no-op
    }

    /**
     * Utility method returning a shared no-op handler instance.
     */
    static <T> CrudEntityHandler<T> noop() {
        return new CrudEntityHandler<>() { };
    }
}
