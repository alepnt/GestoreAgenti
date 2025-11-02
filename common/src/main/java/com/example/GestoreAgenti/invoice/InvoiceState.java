package com.example.GestoreAgenti.invoice;

import java.util.Locale;

/**
 * Stati gestiti per la fatturazione e riutilizzati dal client e dal server.
 */
public enum InvoiceState {
    EMESSA("Emessa"),
    IN_SOLLECITO("In sollecito"),
    SALDATA("Saldato");

    private final String label;

    InvoiceState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Risolve lo stato dell'applicazione a partire dal valore persistito.
     *
     * @param raw valore proveniente dal database o dalla business logic
     * @return stato coerente con {@link InvoiceState}
     */
    public static InvoiceState fromPersistence(String raw) {
        if (raw == null || raw.isBlank()) {
            return EMESSA;
        }
        String normalized = raw.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "IN_SOLLECITO" -> IN_SOLLECITO;
            case "SALDATA", "SALDATO", "PAGATA", "PAGATO" -> SALDATA;
            default -> EMESSA;
        };
    }
}
