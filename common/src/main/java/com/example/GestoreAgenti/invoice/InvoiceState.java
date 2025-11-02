package com.example.GestoreAgenti.invoice; // Definisce il package condiviso per gli oggetti legati alla fatturazione.

import java.util.Locale; // Importa Locale per gestire la normalizzazione indipendente dalla lingua.

/**
 * Stati gestiti per la fatturazione e riutilizzati da client e server.
 */
public enum InvoiceState { // Dichiara l'enum che rappresenta i possibili stati delle fatture.
    EMESSA("Emessa"), // Stato che indica una fattura emessa ma non ancora saldata.
    IN_SOLLECITO("In sollecito"), // Stato che segnala l'invio di un sollecito al cliente.
    SALDATA("Saldato"); // Stato che indica una fattura completamente pagata.

    private final String label; // Mantiene l'etichetta leggibile associata a ciascuno stato.

    InvoiceState(String label) { // Costruttore privato invocato per ogni valore dell'enum.
        this.label = label; // Assegna al campo label la descrizione passata.
    }

    public String getLabel() { // Espone l'etichetta leggibile all'esterno.
        return label; // Restituisce l'etichetta memorizzata.
    }

    /**
     * Risolve lo stato dell'applicazione a partire dal valore persistito.
     *
     * @param raw valore proveniente dal database o dalla business logic
     * @return stato coerente con {@link InvoiceState}
     */
    public static InvoiceState fromPersistence(String raw) { // Converte la stringa memorizzata nello stato enum corrispondente.
        if (raw == null || raw.isBlank()) { // Gestisce input nulli o vuoti riportando lo stato predefinito.
            return EMESSA; // Se manca un valore valido si assume che la fattura sia solo emessa.
        }
        String normalized = raw.trim().toUpperCase(Locale.ROOT); // Normalizza la stringa togliendo spazi e uniformando il case.
        return switch (normalized) { // Usa uno switch expression per mappare rapidamente i valori riconosciuti.
            case "IN_SOLLECITO" -> IN_SOLLECITO; // Quando la stringa combacia con IN_SOLLECITO restituisce il relativo stato.
            case "SALDATA", "SALDATO", "PAGATA", "PAGATO" -> SALDATA; // Gestisce sinonimi e varianti riportandoli allo stato saldato.
            default -> EMESSA; // Qualsiasi altro valore ricade nel caso predefinito "emessa".
        };
    }
}
