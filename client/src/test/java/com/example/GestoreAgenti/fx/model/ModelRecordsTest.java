package com.example.GestoreAgenti.fx.model;

import com.example.GestoreAgenti.invoice.InvoiceState;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ModelRecordsTest {

    @Test
    void employeeRecordStoresValues() {
        Employee employee = new Employee("1", "Mario Rossi", "Vendite", "Team A", "mario@example.com");
        assertEquals("1", employee.id());
        assertEquals("Mario Rossi", employee.fullName());
        assertEquals("Vendite", employee.role());
        assertEquals("Team A", employee.teamName());
        assertEquals("mario@example.com", employee.email());
    }

    @Test
    void paymentRecordStoresValues() {
        LocalDate today = LocalDate.now();
        PaymentRecord payment = new PaymentRecord("INV-1", BigDecimal.TEN, today, "Carta");
        assertEquals("INV-1", payment.invoiceNumber());
        assertEquals(BigDecimal.TEN, payment.amount());
        assertEquals(today, payment.paymentDate());
        assertEquals("Carta", payment.method());
    }

    @Test
    void notificationRecordStoresValues() {
        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification("Titolo", "Messaggio", now, true);
        assertEquals("Titolo", notification.title());
        assertEquals("Messaggio", notification.message());
        assertEquals(now, notification.createdAt());
        assertTrue(notification.read());
    }

    @Test
    void chatMessageRecordStoresValues() {
        LocalDateTime timestamp = LocalDateTime.now();
        ChatMessage message = new ChatMessage("Team", "Anna", timestamp, "Ciao");
        assertEquals("Team", message.teamName());
        assertEquals("Anna", message.sender());
        assertEquals(timestamp, message.timestamp());
        assertEquals("Ciao", message.content());
    }

    @Test
    void invoiceRecordStoresValues() {
        LocalDate issueDate = LocalDate.now();
        InvoiceRecord invoice = new InvoiceRecord("INV-2", issueDate, "Cliente", InvoiceState.SENT, BigDecimal.ONE);
        assertEquals("INV-2", invoice.number());
        assertEquals(issueDate, invoice.issueDate());
        assertEquals("Cliente", invoice.customer());
        assertEquals(InvoiceState.SENT, invoice.state());
        assertEquals(BigDecimal.ONE, invoice.total());
    }

    @Test
    void emailMessageRecordStoresValues() {
        LocalDateTime ts = LocalDateTime.now();
        EmailMessage email = new EmailMessage("mittente@example.com", "destinatario@example.com", "Oggetto", "Corpo", ts, true);
        assertEquals("mittente@example.com", email.sender());
        assertEquals("destinatario@example.com", email.recipient());
        assertEquals("Oggetto", email.subject());
        assertEquals("Corpo", email.body());
        assertEquals(ts, email.timestamp());
        assertTrue(email.incoming());
    }

    @Test
    void agendaItemRecordStoresValues() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        AgendaItem item = new AgendaItem(start, end, "Riunione", "Sala 1");
        assertEquals(start, item.start());
        assertEquals(end, item.end());
        assertEquals("Riunione", item.description());
        assertEquals("Sala 1", item.location());
    }
}
