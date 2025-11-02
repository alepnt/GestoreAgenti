package com.example.GestoreAgenti.fx.command;

import java.util.Objects;
import java.util.function.Supplier;

import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SendEmailCommand implements Command {

    private final Supplier<String> recipientSupplier;
    private final Supplier<String> subjectSupplier;
    private final Supplier<String> bodySupplier;
    private final FxDataService dataService;
    private final Employee employee;
    private final Label statusLabel;
    private final TextField recipientField;
    private final TextField subjectField;
    private final TextArea bodyArea;
    public SendEmailCommand(Supplier<String> recipientSupplier,
                            Supplier<String> subjectSupplier,
                            Supplier<String> bodySupplier,
                            FxDataService dataService,
                            Employee employee,
                            Label statusLabel,
                            TextField recipientField,
                            TextField subjectField,
                            TextArea bodyArea) {
        this.recipientSupplier = Objects.requireNonNull(recipientSupplier, "recipientSupplier");
        this.subjectSupplier = Objects.requireNonNull(subjectSupplier, "subjectSupplier");
        this.bodySupplier = Objects.requireNonNull(bodySupplier, "bodySupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.employee = Objects.requireNonNull(employee, "employee");
        this.statusLabel = Objects.requireNonNull(statusLabel, "statusLabel");
        this.recipientField = Objects.requireNonNull(recipientField, "recipientField");
        this.subjectField = Objects.requireNonNull(subjectField, "subjectField");
        this.bodyArea = Objects.requireNonNull(bodyArea, "bodyArea");
    }

    @Override
    public void execute() {
        String recipient = recipientSupplier.get();
        String subject = subjectSupplier.get();
        String body = bodySupplier.get();

        if (recipient == null || recipient.isBlank()
                || subject == null || subject.isBlank()
                || body == null || body.isBlank()) {
            statusLabel.setText("Compila destinatario, oggetto e testo");
            return;
        }

        statusLabel.setText("Invio in corso...");
        dataService.sendEmail(employee, recipient, subject, body)
                .whenComplete((ignored, error) -> {
                    if (error != null) {
                        Throwable cause = error instanceof java.util.concurrent.CompletionException && error.getCause() != null
                                ? error.getCause() : error;
                        javafx.application.Platform.runLater(() -> statusLabel.setText(
                                "Errore invio: " + (cause.getMessage() == null ? cause.toString() : cause.getMessage())));
                    } else {
                        javafx.application.Platform.runLater(() -> {
                            recipientField.clear();
                            subjectField.clear();
                            bodyArea.clear();
                        });
                    }
                });
    }
}
