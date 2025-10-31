package com.example.GestoreAgenti.fx.event;

import com.example.GestoreAgenti.fx.model.Employee;
import com.example.GestoreAgenti.fx.model.Notification;

public record NotificationUpdatedEvent(Employee employee, Notification notification) implements FxEvent {
}
