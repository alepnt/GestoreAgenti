package com.example.GestoreAgenti.fx.event; // Esegue: package com.example.GestoreAgenti.fx.event;

import com.example.GestoreAgenti.fx.model.EmailMessage; // Esegue: import com.example.GestoreAgenti.fx.model.EmailMessage;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;

public record EmailSentEvent(Employee employee, EmailMessage message) implements FxEvent { // Esegue: public record EmailSentEvent(Employee employee, EmailMessage message) implements FxEvent {
} // Esegue: }
