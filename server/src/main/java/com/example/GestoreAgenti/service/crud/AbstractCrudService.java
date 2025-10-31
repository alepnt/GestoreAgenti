package com.example.GestoreAgenti.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Template method that encapsulates the lifecycle of typical CRUD operations.
 * Concrete services only need to provide a repository and, optionally, a
 * {@link CrudEntityHandler} to customise validation and mapping rules.
 */
public abstract class AbstractCrudService<T, ID> {

    private final JpaRepository<T, ID> repository;
    private final CrudEntityHandler<T> handler;
    private final String entityDescription;

    protected AbstractCrudService(JpaRepository<T, ID> repository,
                                  CrudEntityHandler<T> handler,
                                  String entityDescription) {
        this.repository = repository;
        this.handler = handler == null ? CrudEntityHandler.noop() : handler;
        this.entityDescription = entityDescription == null ? "Entity" : entityDescription;
    }

    protected JpaRepository<T, ID> repository() {
        return repository;
    }

    protected CrudEntityHandler<T> handler() {
        return handler;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findOptionalById(ID id) {
        return repository.findById(id);
    }

    public T findRequiredById(ID id) {
        return findOptionalById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityDescription + " con id " + id + " non trovato"));
    }

    public T create(T entity) {
        handler.validateForCreate(entity);
        T prepared = handler.prepareForCreate(entity);
        T saved = repository.save(prepared);
        handler.afterCreate(saved);
        return saved;
    }

    public T update(ID id, T changes) {
        T existing = findRequiredById(id);
        handler.validateForUpdate(existing, changes);
        T merged = handler.merge(existing, changes);
        T saved = repository.save(merged);
        handler.afterUpdate(saved);
        return saved;
    }

    public T delete(ID id) {
        T existing = findRequiredById(id);
        handler.beforeDelete(existing);
        repository.delete(existing);
        handler.afterDelete(existing);
        return existing;
    }
}
