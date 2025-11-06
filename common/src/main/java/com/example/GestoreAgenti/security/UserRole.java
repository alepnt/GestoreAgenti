package com.example.GestoreAgenti.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumerazione centralizzata dei ruoli supportati dal sistema.
 * <p>
 * Viene definita nel modulo {@code common} per essere condivisa fra backend e frontend,
 * evitando di duplicare le stringhe dei ruoli nelle varie componenti.
 */
public enum UserRole {
    ADMIN("Amministratore di sistema", AccountAssociation.DIPENDENTE),
    SALES_MANAGER("Responsabile vendite", AccountAssociation.DIPENDENTE),
    TEAM_LEAD("Coordinatore di team", AccountAssociation.DIPENDENTE),
    AGENTE("Agente commerciale", AccountAssociation.DIPENDENTE),
    OPERATIONS("Supporto operativo", AccountAssociation.DIPENDENTE),
    CLIENTE("Cliente", AccountAssociation.CLIENTE);

    public enum AccountAssociation {
        DIPENDENTE,
        CLIENTE
    }

    private static final Map<String, UserRole> LOOKUP_BY_NORMALIZED_DISPLAY =
            Collections.unmodifiableMap(Arrays.stream(values())
                    .collect(Collectors.toMap(role -> normalize(role.displayName), Function.identity())));

    private static final List<String> DISPLAY_NAMES = Collections.unmodifiableList(
            Arrays.stream(values())
                    .map(UserRole::getDisplayName)
                    .toList());

    private final String displayName;
    private final AccountAssociation accountAssociation;

    UserRole(String displayName, AccountAssociation accountAssociation) {
        this.displayName = displayName;
        this.accountAssociation = accountAssociation;
    }

    /**
     * Restituisce il nome descrittivo da esporre nelle interfacce utente.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Fornisce una vista immutabile dei nomi descrittivi di tutti i ruoli.
     */
    public static List<String> getDisplayNames() {
        return DISPLAY_NAMES;
    }

    /**
     * Risolve un ruolo a partire dal nome descrittivo o dall'identificativo.
     *
     * @param value testo inserito (es. "Amministratore di sistema" oppure "ADMIN")
     * @return il ruolo corrispondente
     * @throws IllegalArgumentException se il valore non corrisponde a nessun ruolo
     */
    public static UserRole resolve(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Il valore del ruolo non può essere vuoto");
        }
        String normalized = normalize(value);
        UserRole match = LOOKUP_BY_NORMALIZED_DISPLAY.get(normalized);
        if (match != null) {
            return match;
        }
        try {
            return UserRole.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Ruolo non riconosciuto: " + value, ex);
        }
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Indica quale tipo di entità (dipendente o cliente) deve essere associato al ruolo.
     */
    public AccountAssociation getAccountAssociation() {
        return accountAssociation;
    }
}
