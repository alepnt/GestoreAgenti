package com.example.GestoreAgenti.fx.data.adapter; // Definisce il package che contiene gli adapter del livello dati della GUI.

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Importa il DTO proveniente dal backend REST.
import com.example.GestoreAgenti.fx.model.Employee; // Importa il modello specifico usato dall'applicazione JavaFX.

/**
 * Adapter che converte tra il DTO REST e il modello utilizzato dalla GUI.
 */
public class EmployeeAdapter { // Dichiara la classe che esegue le conversioni fra DTO e modello.

    public Employee toModel(EmployeeDto dto) { // Trasforma il DTO ricevuto dal backend in un oggetto utilizzabile dalla GUI.
        if (dto == null) { // Se il DTO è assente non è possibile costruire un modello.
            return null; // Restituisce null per indicare che non c'è nulla da convertire.
        } // Esegue: }
        String id = trimToNull(dto.id()); // Normalizza l'identificativo rimuovendo spazi superflui.
        if (id == null) { // Se dopo la normalizzazione non resta un id valido...
            throw new IllegalArgumentException("L'identificativo del dipendente non può essere vuoto"); // ...viene generata un'eccezione descrittiva.
        } // Esegue: }
        String fullName = buildFullName(trimToNull(dto.firstName()), trimToNull(dto.lastName())); // Combina nome e cognome curandone la formattazione.
        return new Employee(id, // Costruisce il modello del dipendente passando l'identificativo pulito.
                fullName, // Imposta il nome completo calcolato in precedenza.
                trimToNull(dto.role()), // Normalizza il ruolo eventualmente ricevuto.
                trimToNull(dto.team()), // Normalizza il nome del team.
                trimToNull(dto.email())); // Normalizza l'indirizzo email associato.
    } // Esegue: }

    public EmployeeDto toDto(Employee employee) { // Esegue l'operazione inversa convertendo il modello locale in DTO.
        if (employee == null) { // Se non viene fornito un modello non si può procedere.
            return null; // Restituisce null per indicare l'assenza di dati.
        } // Esegue: }
        String id = safeTrim(employee.id()); // Rimuove gli spazi superflui dall'identificativo mantenendo null se mancante.
        NameParts parts = splitFullName(employee.fullName()); // Divide il nome completo in nome e cognome.
        return new EmployeeDto(id, // Costruisce il DTO con l'identificativo ripulito.
                parts.firstName(), // Inserisce il nome ottenuto dalla scomposizione.
                parts.lastName(), // Inserisce il cognome ottenuto dalla scomposizione.
                safeTrim(employee.role()), // Ripulisce il ruolo mantenendo eventuale null.
                safeTrim(employee.teamName()), // Ripulisce il nome del team mantenendo eventuale null.
                safeTrim(employee.email())); // Ripulisce l'indirizzo email mantenendo eventuale null.
    } // Esegue: }

    private String buildFullName(String firstName, String lastName) { // Combina nome e cognome gestendo i casi mancanti.
        if (firstName == null && lastName == null) { // Se entrambi i campi sono vuoti...
            return ""; // ...restituisce una stringa vuota.
        } // Esegue: }
        if (firstName == null) { // Se manca solo il nome...
            return lastName; // ...restituisce il cognome come nome completo.
        } // Esegue: }
        if (lastName == null) { // Se manca solo il cognome...
            return firstName; // ...restituisce il nome come nome completo.
        } // Esegue: }
        return firstName + " " + lastName; // In caso completo concatena nome e cognome separati da uno spazio.
    } // Esegue: }

    private NameParts splitFullName(String fullName) { // Suddivide il nome completo in due parti.
        if (fullName == null) { // Se il valore è nullo...
            return new NameParts("", ""); // ...restituisce una coppia vuota per evitare NPE.
        } // Esegue: }
        String normalized = fullName.trim(); // Elimina gli spazi superflui agli estremi del testo.
        if (normalized.isEmpty()) { // Se dopo il trim il testo è vuoto...
            return new NameParts("", ""); // ...restituisce una coppia vuota.
        } // Esegue: }
        int separatorIndex = normalized.indexOf(' '); // Cerca il primo spazio per distinguere nome e cognome.
        if (separatorIndex < 0) { // Se non esiste uno spazio...
            return new NameParts(normalized, ""); // ...tratta l'intero testo come nome lasciando vuoto il cognome.
        } // Esegue: }
        String first = normalized.substring(0, separatorIndex); // Estrae il nome dalla parte iniziale.
        String last = normalized.substring(separatorIndex + 1).stripLeading(); // Estrae il cognome e rimuove eventuali spazi iniziali residui.
        return new NameParts(first, last); // Restituisce le due parti in un record di supporto.
    } // Esegue: }

    private String trimToNull(String value) { // Restituisce null se la stringa è vuota dopo il trim.
        if (value == null) { // Se l'input è null...
            return null; // ...mantiene null.
        } // Esegue: }
        String trimmed = value.trim(); // Rimuove gli spazi superflui.
        return trimmed.isEmpty() ? null : trimmed; // Se resta vuota restituisce null, altrimenti la stringa ripulita.
    } // Esegue: }

    private String safeTrim(String value) { // Applica il trim mantenendo null in caso di input mancante.
        return value == null ? null : value.trim(); // Restituisce direttamente null oppure la stringa ripulita.
    } // Esegue: }

    private record NameParts(String firstName, String lastName) { // Record di supporto per restituire nome e cognome.
    } // Esegue: }
} // Esegue: }
