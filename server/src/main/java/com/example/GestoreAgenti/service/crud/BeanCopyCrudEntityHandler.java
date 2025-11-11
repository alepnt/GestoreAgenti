package com.example.GestoreAgenti.service.crud; // Definisce il pacchetto com.example.GestoreAgenti.service.crud che contiene questa classe.

import java.util.Arrays; // Importa java.util.Arrays per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per garantire il rispetto dei contratti @NonNull delle API Spring.

import org.springframework.beans.BeanUtils; // Importa org.springframework.beans.BeanUtils per abilitare le funzionalità utilizzate nel file.

/**
 * Default handler that merges entities by copying every property from the
 * incoming instance into the managed one, optionally skipping some attributes
 * (for example identifiers).
 */
public class BeanCopyCrudEntityHandler<T> implements CrudEntityHandler<T> { // Definisce la classe BeanCopyCrudEntityHandler che incapsula la logica applicativa.

    private final String[] ignoredProperties; // Dichiara il campo ignoredProperties dell'oggetto.

    public BeanCopyCrudEntityHandler(String... ignoredProperties) { // Costruttore della classe BeanCopyCrudEntityHandler che inizializza le dipendenze necessarie.
        this.ignoredProperties = ignoredProperties == null ? new String[0] : ignoredProperties.clone(); // Aggiorna il campo ignoredProperties dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public T merge(T existing, T changes) { // Definisce il metodo merge che supporta la logica di dominio.
        BeanUtils.copyProperties(Objects.requireNonNull(changes, "I dati aggiornati sono obbligatori"), // Esegue l'istruzione terminata dal punto e virgola.
                Objects.requireNonNull(existing, "L'entità esistente è obbligatoria"),
                Objects.requireNonNull(ignoredProperties, "La lista delle proprietà da ignorare non può essere nulla"));
        return existing; // Restituisce il risultato dell'espressione existing.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String toString() { // Definisce il metodo toString che supporta la logica di dominio.
        return "BeanCopyCrudEntityHandler" + Arrays.toString(ignoredProperties); // Restituisce il risultato dell'espressione "BeanCopyCrudEntityHandler" + Arrays.toString(ignoredProperties).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
