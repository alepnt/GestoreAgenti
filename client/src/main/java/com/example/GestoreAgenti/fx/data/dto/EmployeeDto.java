package com.example.GestoreAgenti.fx.data.dto;

/**
 * Rappresenta il contratto dati esposto dal servizio REST per i dipendenti.
 */
public record EmployeeDto(String id,
                          String firstName,
                          String lastName,
                          String role,
                          String team,
                          String email) {
}
