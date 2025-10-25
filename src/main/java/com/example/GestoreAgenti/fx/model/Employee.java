package com.example.GestoreAgenti.fx.model;

/**
 * Rappresenta un dipendente abilitato all'accesso nell'interfaccia JavaFX.
 */
public record Employee(String id, String fullName, String role, String teamName, String email) {
}
