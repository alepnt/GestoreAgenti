package com.example.GestoreAgenti.fx.event; // Esegue: package com.example.GestoreAgenti.fx.event;

import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.Notification; // Esegue: import com.example.GestoreAgenti.fx.model.Notification;

public record NotificationUpdatedEvent(Employee employee, Notification notification) implements FxEvent { // Esegue: public record NotificationUpdatedEvent(Employee employee, Notification notification) implements FxEvent {
} // Esegue: }
