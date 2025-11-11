package com.example.GestoreAgenti.service.crud; // Definisce il pacchetto com.example.GestoreAgenti.service.crud che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare i controlli di nullità richiesti dalle API annotate @NonNull.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.data.jpa.repository.JpaRepository; // Importa org.springframework.data.jpa.repository.JpaRepository per abilitare le funzionalità utilizzate nel file.

import jakarta.persistence.EntityNotFoundException; // Importa jakarta.persistence.EntityNotFoundException per abilitare le funzionalità utilizzate nel file.

/**
 * Template method that encapsulates the lifecycle of typical CRUD operations.
 * Concrete services only need to provide a repository and, optionally, a
 * {@link CrudEntityHandler} to customise validation and mapping rules.
 */
public abstract class AbstractCrudService<T, ID> { // Apre il blocco di codice associato alla dichiarazione.

    private final JpaRepository<T, ID> repository; // Dichiara il campo repository dell'oggetto.
    private final CrudEntityHandler<T> handler; // Dichiara il campo handler dell'oggetto.
    private final String entityDescription; // Dichiara il campo entityDescription dell'oggetto.

    protected AbstractCrudService(JpaRepository<T, ID> repository, // Definisce il metodo AbstractCrudService che supporta la logica di dominio.
                                  CrudEntityHandler<T> handler, // Esegue l'istruzione necessaria alla logica applicativa.
                                  String entityDescription) { // Apre il blocco di codice associato alla dichiarazione.
        this.repository = Objects.requireNonNull(repository); // Aggiorna il campo repository dell'istanza.
        this.handler = handler == null ? CrudEntityHandler.noop() : handler; // Aggiorna il campo handler dell'istanza.
        this.entityDescription = entityDescription == null ? "Entity" : entityDescription; // Aggiorna il campo entityDescription dell'istanza.
    } // Chiude il blocco di codice precedente.

    protected JpaRepository<T, ID> repository() { // Definisce il metodo repository che supporta la logica di dominio.
        return repository; // Restituisce il risultato dell'espressione repository.
    } // Chiude il blocco di codice precedente.

    protected CrudEntityHandler<T> handler() { // Definisce il metodo handler che supporta la logica di dominio.
        return handler; // Restituisce il risultato dell'espressione handler.
    } // Chiude il blocco di codice precedente.

    public List<T> findAll() { // Definisce il metodo findAll che supporta la logica di dominio.
        return repository.findAll(); // Restituisce il risultato dell'espressione repository.findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<T> findOptionalById(ID id) { // Definisce il metodo findOptionalById che supporta la logica di dominio.
        return repository.findById(Objects.requireNonNull(id, "L'identificativo è obbligatorio")); // Restituisce il risultato dell'espressione repository.findById(id).
    } // Chiude il blocco di codice precedente.

    public T findRequiredById(ID id) { // Definisce il metodo findRequiredById che supporta la logica di dominio.
        return findOptionalById(id) // Restituisce il risultato dell'espressione findOptionalById(id).
                .orElseThrow(() -> new EntityNotFoundException(entityDescription + " con id " + id + " non trovato")); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public T create(T entity) { // Definisce il metodo create che supporta la logica di dominio.
        T safeEntity = Objects.requireNonNull(entity, "L'entità da creare è obbligatoria"); // Assegna il valore calcolato alla variabile T safeEntity.
        handler.validateForCreate(safeEntity); // Esegue l'istruzione terminata dal punto e virgola.
        T prepared = handler.prepareForCreate(safeEntity); // Assegna il valore calcolato alla variabile T prepared.
        T saved = repository.save(prepared); // Assegna il valore calcolato alla variabile T saved.
        handler.afterCreate(saved); // Esegue l'istruzione terminata dal punto e virgola.
        return Objects.requireNonNull(saved); // Restituisce il risultato dell'espressione saved.
    } // Chiude il blocco di codice precedente.

    public T update(ID id, T changes) { // Definisce il metodo update che supporta la logica di dominio.
        T existing = findRequiredById(Objects.requireNonNull(id, "L'identificativo è obbligatorio")); // Assegna il valore calcolato alla variabile T existing.
        T safeChanges = Objects.requireNonNull(changes, "I dati aggiornati sono obbligatori"); // Assegna il valore calcolato alla variabile T safeChanges.
        handler.validateForUpdate(existing, safeChanges); // Esegue l'istruzione terminata dal punto e virgola.
        T merged = handler.merge(existing, safeChanges); // Assegna il valore calcolato alla variabile T merged.
        T saved = repository.save(merged); // Assegna il valore calcolato alla variabile T saved.
        handler.afterUpdate(saved); // Esegue l'istruzione terminata dal punto e virgola.
        return Objects.requireNonNull(saved); // Restituisce il risultato dell'espressione saved.
    } // Chiude il blocco di codice precedente.

    public T delete(ID id) { // Definisce il metodo delete che supporta la logica di dominio.
        T existing = findRequiredById(Objects.requireNonNull(id, "L'identificativo è obbligatorio")); // Assegna il valore calcolato alla variabile T existing.
        handler.beforeDelete(existing); // Esegue l'istruzione terminata dal punto e virgola.
        repository.delete(existing); // Esegue l'istruzione terminata dal punto e virgola.
        handler.afterDelete(existing); // Esegue l'istruzione terminata dal punto e virgola.
        return Objects.requireNonNull(existing); // Restituisce il risultato dell'espressione existing.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
