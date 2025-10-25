package com.example.GestoreAgenti.fx.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Rappresenta una fattura gestita nelle schermate JavaFX.
 */
public record InvoiceRecord(String number, LocalDate issueDate, String customer,
                            InvoiceState state, BigDecimal total) {
}
