package com.example.GestoreAgenti.model.commission;

/**
 * Identifica l'importo di riferimento su cui calcolare le provvigioni.
 */
public enum CommissionBase {
    /** Calcolo effettuato sull'imponibile della fattura. */
    IMPONIBILE,
    /** Calcolo effettuato sull'importo lordo (totale) della fattura. */
    TOTALE
}
