package com.example.GestoreAgenti.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.security.UserRole;

/**
 * Espone le informazioni sui ruoli disponibili nel sistema.
 */
@RestController
@RequestMapping("/api/ruoli")
public class RoleController {

    /**
     * DTO minimale che restituisce sia il nome tecnico del ruolo (per la sicurezza)
     * sia la descrizione user-friendly da mostrare nelle interfacce.
     */
    public record RoleResponse(String name, String displayName) {
    }

    @GetMapping
    public List<RoleResponse> listRoles() {
        return Arrays.stream(UserRole.values())
                .map(role -> new RoleResponse(role.name(), role.getDisplayName()))
                .toList();
    }
}
