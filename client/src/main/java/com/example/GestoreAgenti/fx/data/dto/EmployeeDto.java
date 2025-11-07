package com.example.GestoreAgenti.fx.data.dto; // Esegue: package com.example.GestoreAgenti.fx.data.dto;

/**
 * Rappresenta il contratto dati esposto dal servizio REST per i dipendenti.
 */
public record EmployeeDto(String id, // Esegue: public record EmployeeDto(String id,
                          String firstName, // Esegue: String firstName,
                          String lastName, // Esegue: String lastName,
                          String role, // Esegue: String role,
                          String team, // Esegue: String team,
                          String email) { // Esegue: String email) {
} // Esegue: }
