package com.example.GestoreAgenti.fx.command; // Esegue: package com.example.GestoreAgenti.fx.command;

import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.function.Supplier; // Esegue: import java.util.function.Supplier;

import com.example.GestoreAgenti.fx.data.FxDataService; // Esegue: import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;

import javafx.scene.control.Label; // Esegue: import javafx.scene.control.Label;
import javafx.scene.control.TextArea; // Esegue: import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; // Esegue: import javafx.scene.control.TextField;

public class SendEmailCommand implements Command { // Esegue: public class SendEmailCommand implements Command {

    private final Supplier<String> recipientSupplier; // Esegue: private final Supplier<String> recipientSupplier;
    private final Supplier<String> subjectSupplier; // Esegue: private final Supplier<String> subjectSupplier;
    private final Supplier<String> bodySupplier; // Esegue: private final Supplier<String> bodySupplier;
    private final FxDataService dataService; // Esegue: private final FxDataService dataService;
    private final Employee employee; // Esegue: private final Employee employee;
    private final Label statusLabel; // Esegue: private final Label statusLabel;
    private final TextField recipientField; // Esegue: private final TextField recipientField;
    private final TextField subjectField; // Esegue: private final TextField subjectField;
    private final TextArea bodyArea; // Esegue: private final TextArea bodyArea;
    public SendEmailCommand(Supplier<String> recipientSupplier, // Esegue: public SendEmailCommand(Supplier<String> recipientSupplier,
                            Supplier<String> subjectSupplier, // Esegue: Supplier<String> subjectSupplier,
                            Supplier<String> bodySupplier, // Esegue: Supplier<String> bodySupplier,
                            FxDataService dataService, // Esegue: FxDataService dataService,
                            Employee employee, // Esegue: Employee employee,
                            Label statusLabel, // Esegue: Label statusLabel,
                            TextField recipientField, // Esegue: TextField recipientField,
                            TextField subjectField, // Esegue: TextField subjectField,
                            TextArea bodyArea) { // Esegue: TextArea bodyArea) {
        this.recipientSupplier = Objects.requireNonNull(recipientSupplier, "recipientSupplier"); // Esegue: this.recipientSupplier = Objects.requireNonNull(recipientSupplier, "recipientSupplier");
        this.subjectSupplier = Objects.requireNonNull(subjectSupplier, "subjectSupplier"); // Esegue: this.subjectSupplier = Objects.requireNonNull(subjectSupplier, "subjectSupplier");
        this.bodySupplier = Objects.requireNonNull(bodySupplier, "bodySupplier"); // Esegue: this.bodySupplier = Objects.requireNonNull(bodySupplier, "bodySupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService"); // Esegue: this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.employee = Objects.requireNonNull(employee, "employee"); // Esegue: this.employee = Objects.requireNonNull(employee, "employee");
        this.statusLabel = Objects.requireNonNull(statusLabel, "statusLabel"); // Esegue: this.statusLabel = Objects.requireNonNull(statusLabel, "statusLabel");
        this.recipientField = Objects.requireNonNull(recipientField, "recipientField"); // Esegue: this.recipientField = Objects.requireNonNull(recipientField, "recipientField");
        this.subjectField = Objects.requireNonNull(subjectField, "subjectField"); // Esegue: this.subjectField = Objects.requireNonNull(subjectField, "subjectField");
        this.bodyArea = Objects.requireNonNull(bodyArea, "bodyArea"); // Esegue: this.bodyArea = Objects.requireNonNull(bodyArea, "bodyArea");
    } // Esegue: }

    @Override // Esegue: @Override
    public void execute() { // Esegue: public void execute() {
        String recipient = recipientSupplier.get(); // Esegue: String recipient = recipientSupplier.get();
        String subject = subjectSupplier.get(); // Esegue: String subject = subjectSupplier.get();
        String body = bodySupplier.get(); // Esegue: String body = bodySupplier.get();

        if (recipient == null || recipient.isBlank() // Esegue: if (recipient == null || recipient.isBlank()
                || subject == null || subject.isBlank() // Esegue: || subject == null || subject.isBlank()
                || body == null || body.isBlank()) { // Esegue: || body == null || body.isBlank()) {
            statusLabel.setText("Compila destinatario, oggetto e testo"); // Esegue: statusLabel.setText("Compila destinatario, oggetto e testo");
            return; // Esegue: return;
        } // Esegue: }

        statusLabel.setText("Invio in corso..."); // Esegue: statusLabel.setText("Invio in corso...");
        dataService.sendEmail(employee, recipient, subject, body) // Esegue: dataService.sendEmail(employee, recipient, subject, body)
                .whenComplete((ignored, error) -> { // Esegue: .whenComplete((ignored, error) -> {
                    if (error != null) { // Esegue: if (error != null) {
                        Throwable cause = error instanceof java.util.concurrent.CompletionException && error.getCause() != null // Esegue: Throwable cause = error instanceof java.util.concurrent.CompletionException && error.getCause() != null
                                ? error.getCause() : error; // Esegue: ? error.getCause() : error;
                        javafx.application.Platform.runLater(() -> statusLabel.setText( // Esegue: javafx.application.Platform.runLater(() -> statusLabel.setText(
                                "Errore invio: " + (cause.getMessage() == null ? cause.toString() : cause.getMessage()))); // Esegue: "Errore invio: " + (cause.getMessage() == null ? cause.toString() : cause.getMessage())));
                    } else { // Esegue: } else {
                        javafx.application.Platform.runLater(() -> { // Esegue: javafx.application.Platform.runLater(() -> {
                            recipientField.clear(); // Esegue: recipientField.clear();
                            subjectField.clear(); // Esegue: subjectField.clear();
                            bodyArea.clear(); // Esegue: bodyArea.clear();
                        }); // Esegue: });
                    } // Esegue: }
                }); // Esegue: });
    } // Esegue: }
} // Esegue: }
