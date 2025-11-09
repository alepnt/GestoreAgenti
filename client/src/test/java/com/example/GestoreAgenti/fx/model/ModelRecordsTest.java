package com.example.GestoreAgenti.fx.model; // Esegue: package com.example.GestoreAgenti.fx.model;

import com.example.GestoreAgenti.invoice.InvoiceState; // Esegue: import com.example.GestoreAgenti.invoice.InvoiceState;
import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.math.BigDecimal; // Esegue: import java.math.BigDecimal;
import java.time.LocalDate; // Esegue: import java.time.LocalDate;
import java.time.LocalDateTime; // Esegue: import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class ModelRecordsTest { // Esegue: class ModelRecordsTest {

    @Test // Esegue: @Test
    void employeeRecordStoresValues() { // Esegue: void employeeRecordStoresValues() {
        Employee employee = new Employee("1", "Mario Rossi", "Vendite", "Team A", "mario@example.com"); // Esegue: Employee employee = new Employee("1", "Mario Rossi", "Vendite", "Team A", "mario@example.com");
        assertEquals("1", employee.id()); // Esegue: assertEquals("1", employee.id());
        assertEquals("Mario Rossi", employee.fullName()); // Esegue: assertEquals("Mario Rossi", employee.fullName());
        assertEquals("Vendite", employee.role()); // Esegue: assertEquals("Vendite", employee.role());
        assertEquals("Team A", employee.teamName()); // Esegue: assertEquals("Team A", employee.teamName());
        assertEquals("mario@example.com", employee.email()); // Esegue: assertEquals("mario@example.com", employee.email());
    } // Esegue: }

    @Test // Esegue: @Test
    void paymentRecordStoresValues() { // Esegue: void paymentRecordStoresValues() {
        LocalDate today = LocalDate.now(); // Esegue: LocalDate today = LocalDate.now();
        PaymentRecord payment = new PaymentRecord("INV-1", BigDecimal.TEN, today, "Carta"); // Esegue: PaymentRecord payment = new PaymentRecord("INV-1", BigDecimal.TEN, today, "Carta");
        assertEquals("INV-1", payment.invoiceNumber()); // Esegue: assertEquals("INV-1", payment.invoiceNumber());
        assertEquals(BigDecimal.TEN, payment.amount()); // Esegue: assertEquals(BigDecimal.TEN, payment.amount());
        assertEquals(today, payment.paymentDate()); // Esegue: assertEquals(today, payment.paymentDate());
        assertEquals("Carta", payment.method()); // Esegue: assertEquals("Carta", payment.method());
    } // Esegue: }

    @Test // Esegue: @Test
    void notificationRecordStoresValues() { // Esegue: void notificationRecordStoresValues() {
        LocalDateTime now = LocalDateTime.now(); // Esegue: LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification("Titolo", "Messaggio", now, true); // Esegue: Notification notification = new Notification("Titolo", "Messaggio", now, true);
        assertEquals("Titolo", notification.title()); // Esegue: assertEquals("Titolo", notification.title());
        assertEquals("Messaggio", notification.message()); // Esegue: assertEquals("Messaggio", notification.message());
        assertEquals(now, notification.createdAt()); // Esegue: assertEquals(now, notification.createdAt());
        assertTrue(notification.read()); // Esegue: assertTrue(notification.read());
    } // Esegue: }

    @Test // Esegue: @Test
    void chatMessageRecordStoresValues() { // Esegue: void chatMessageRecordStoresValues() {
        LocalDateTime timestamp = LocalDateTime.now(); // Esegue: LocalDateTime timestamp = LocalDateTime.now();
        ChatMessage message = new ChatMessage("Team", "Anna", timestamp, "Ciao"); // Esegue: ChatMessage message = new ChatMessage("Team", "Anna", timestamp, "Ciao");
        assertEquals("Team", message.teamName()); // Esegue: assertEquals("Team", message.teamName());
        assertEquals("Anna", message.sender()); // Esegue: assertEquals("Anna", message.sender());
        assertEquals(timestamp, message.timestamp()); // Esegue: assertEquals(timestamp, message.timestamp());
        assertEquals("Ciao", message.content()); // Esegue: assertEquals("Ciao", message.content());
    } // Esegue: }

    @Test // Esegue: @Test
    void invoiceRecordStoresValues() { // Esegue: void invoiceRecordStoresValues() {
        LocalDate issueDate = LocalDate.now(); // Esegue: LocalDate issueDate = LocalDate.now();
        InvoiceRecord invoice = new InvoiceRecord("INV-2", issueDate, "Cliente", InvoiceState.EMESSA, BigDecimal.ONE, false); // Esegue: InvoiceRecord invoice = new InvoiceRecord("INV-2", issueDate, "Cliente", InvoiceState.EMESSA, BigDecimal.ONE, false);
        assertEquals("INV-2", invoice.number()); // Esegue: assertEquals("INV-2", invoice.number());
        assertEquals(issueDate, invoice.issueDate()); // Esegue: assertEquals(issueDate, invoice.issueDate());
        assertEquals("Cliente", invoice.customer()); // Esegue: assertEquals("Cliente", invoice.customer());
        assertEquals(InvoiceState.EMESSA, invoice.state()); // Esegue: assertEquals(InvoiceState.EMESSA, invoice.state());
        assertEquals(BigDecimal.ONE, invoice.total()); // Esegue: assertEquals(BigDecimal.ONE, invoice.total());
    } // Esegue: }

    @Test // Esegue: @Test
    void emailMessageRecordStoresValues() { // Esegue: void emailMessageRecordStoresValues() {
        LocalDateTime ts = LocalDateTime.now(); // Esegue: LocalDateTime ts = LocalDateTime.now();
        EmailMessage email = new EmailMessage("mittente@example.com", "destinatario@example.com", "Oggetto", "Corpo", ts, true); // Esegue: EmailMessage email = new EmailMessage("mittente@example.com", "destinatario@example.com", "Oggetto", "Corpo", ts, true);
        assertEquals("mittente@example.com", email.sender()); // Esegue: assertEquals("mittente@example.com", email.sender());
        assertEquals("destinatario@example.com", email.recipient()); // Esegue: assertEquals("destinatario@example.com", email.recipient());
        assertEquals("Oggetto", email.subject()); // Esegue: assertEquals("Oggetto", email.subject());
        assertEquals("Corpo", email.body()); // Esegue: assertEquals("Corpo", email.body());
        assertEquals(ts, email.timestamp()); // Esegue: assertEquals(ts, email.timestamp());
        assertTrue(email.incoming()); // Esegue: assertTrue(email.incoming());
    } // Esegue: }

    @Test // Esegue: @Test
    void agendaItemRecordStoresValues() { // Esegue: void agendaItemRecordStoresValues() {
        LocalDateTime start = LocalDateTime.now(); // Esegue: LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1); // Esegue: LocalDateTime end = start.plusHours(1);
        AgendaItem item = new AgendaItem(start, end, "Riunione", "Sala 1"); // Esegue: AgendaItem item = new AgendaItem(start, end, "Riunione", "Sala 1");
        assertEquals(start, item.start()); // Esegue: assertEquals(start, item.start());
        assertEquals(end, item.end()); // Esegue: assertEquals(end, item.end());
        assertEquals("Riunione", item.description()); // Esegue: assertEquals("Riunione", item.description());
        assertEquals("Sala 1", item.location()); // Esegue: assertEquals("Sala 1", item.location());
    } // Esegue: }
} // Esegue: }
