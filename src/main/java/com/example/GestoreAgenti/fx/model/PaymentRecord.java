package com.example.GestoreAgenti.fx.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pagamento associato a una fattura.
 */
public record PaymentRecord(String invoiceNumber, BigDecimal amount, LocalDate paymentDate, String method) {
}
