package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller che contiene questa classe.

import java.util.Arrays; // Importa java.util.Arrays per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import org.springframework.web.bind.annotation.GetMapping; // Importa org.springframework.web.bind.annotation.GetMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RequestMapping; // Importa org.springframework.web.bind.annotation.RequestMapping per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.bind.annotation.RestController; // Importa org.springframework.web.bind.annotation.RestController per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.security.UserRole; // Importa com.example.GestoreAgenti.security.UserRole per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.security.AccountAssociation; // Importa com.example.GestoreAgenti.security.AccountAssociation per abilitare le funzionalità utilizzate nel file.

/**
 * Espone le informazioni sui ruoli disponibili nel sistema.
 */
@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/ruoli") // Applica l'annotazione @RequestMapping per configurare il componente.
public class RoleController { // Definisce la classe RoleController che incapsula la logica applicativa.

    /**
     * DTO minimale che restituisce sia il nome tecnico del ruolo (per la sicurezza)
     * sia la descrizione user-friendly da mostrare nelle interfacce.
     */
    public record RoleResponse(String name, String displayName, AccountAssociation association) { // Definisce la record RoleResponse che incapsula la logica applicativa.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<RoleResponse> listRoles() { // Definisce il metodo listRoles che supporta la logica di dominio.
        return Arrays.stream(UserRole.values()) // Restituisce il risultato dell'espressione Arrays.stream(UserRole.values()).
                .map(role -> new RoleResponse(role.name(), role.getDisplayName(), role.getAccountAssociation())) // Esegue l'istruzione necessaria alla logica applicativa.
                .toList(); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
