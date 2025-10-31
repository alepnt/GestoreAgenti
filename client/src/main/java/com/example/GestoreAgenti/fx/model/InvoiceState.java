package com.example.GestoreAgenti.fx.model;

/**
 * Stati gestiti per la fatturazione mostrata nella GUI.
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
}
