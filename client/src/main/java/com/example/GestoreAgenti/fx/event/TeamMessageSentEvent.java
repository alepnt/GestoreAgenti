package com.example.GestoreAgenti.fx.event; // Esegue: package com.example.GestoreAgenti.fx.event;

import com.example.GestoreAgenti.fx.model.ChatMessage; // Esegue: import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;

public record TeamMessageSentEvent(Employee employee, ChatMessage message) implements FxEvent { // Esegue: public record TeamMessageSentEvent(Employee employee, ChatMessage message) implements FxEvent {
} // Esegue: }
