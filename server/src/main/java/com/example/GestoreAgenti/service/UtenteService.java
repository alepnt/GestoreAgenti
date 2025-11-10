package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Utente; // Importa com.example.GestoreAgenti.model.Utente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.security.UserRole; // Importa com.example.GestoreAgenti.security.UserRole per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.security.AccountAssociation; // Importa com.example.GestoreAgenti.security.AccountAssociation per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.UtenteRepository; // Importa com.example.GestoreAgenti.repository.UtenteRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.CrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.CrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class UtenteService extends AbstractCrudService<Utente, Long> { // Definisce la classe UtenteService che incapsula la logica applicativa.

    private static final class UtenteCrudHandler implements CrudEntityHandler<Utente> { // Apre il blocco di codice associato alla dichiarazione.
        private final BCryptPasswordEncoder encoder; // Dichiara il campo encoder dell'oggetto.
        private final UtenteRepository repository; // Dichiara il campo repository dell'oggetto.

        private UtenteCrudHandler(BCryptPasswordEncoder encoder, UtenteRepository repository) { // Definisce il metodo UtenteCrudHandler che supporta la logica di dominio.
            this.encoder = encoder; // Aggiorna il campo encoder dell'istanza.
            this.repository = repository; // Aggiorna il campo repository dell'istanza.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public Utente prepareForCreate(Utente entity) { // Definisce il metodo prepareForCreate che supporta la logica di dominio.
            requireRole(entity); // Esegue l'istruzione terminata dal punto e virgola.
            String rawPassword = entity.getPasswordHash(); // Assegna il valore calcolato alla variabile String rawPassword.
            if (rawPassword == null || rawPassword.isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
                throw new IllegalArgumentException("passwordHash"); // Propaga un'eccezione verso il chiamante.
            } // Chiude il blocco di codice precedente.
            entity.setPasswordHash(encoder.encode(rawPassword)); // Esegue l'istruzione terminata dal punto e virgola.
            return entity; // Restituisce il risultato dell'espressione entity.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public void validateForCreate(Utente entity) { // Definisce il metodo validateForCreate che supporta la logica di dominio.
            UserRole role = requireRole(entity); // Assegna il valore calcolato alla variabile UserRole role.
            validateAssociations(role, entity.getDipendente(), entity.getCliente(), entity.getDipendente(), // Esegue l'istruzione necessaria alla logica applicativa.
                    entity.getCliente(), null); // Esegue l'istruzione terminata dal punto e virgola.
            clearUnusedAssociations(entity, role); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public void validateForUpdate(Utente existing, Utente changes) { // Definisce il metodo validateForUpdate che supporta la logica di dominio.
            UserRole role = changes.getRuolo() != null ? changes.getRuolo() : requireRole(existing); // Assegna il valore calcolato alla variabile UserRole role.
            AccountAssociation associationType = associationFor(role); // Assegna il valore calcolato alla variabile AccountAssociation associationType.
            Dipendente finalDipendente = associationType == AccountAssociation.DIPENDENTE // Esegue l'istruzione necessaria alla logica applicativa.
                    ? firstNonNull(changes.getDipendente(), existing.getDipendente()) // Esegue l'istruzione necessaria alla logica applicativa.
                    : null; // Esegue l'istruzione terminata dal punto e virgola.
            Cliente finalCliente = associationType == AccountAssociation.CLIENTE // Esegue l'istruzione necessaria alla logica applicativa.
                    ? firstNonNull(changes.getCliente(), existing.getCliente()) // Esegue l'istruzione necessaria alla logica applicativa.
                    : null; // Esegue l'istruzione terminata dal punto e virgola.

            validateAssociations(role, finalDipendente, finalCliente, changes.getDipendente(), changes.getCliente(), // Esegue l'istruzione necessaria alla logica applicativa.
                    existing.getIdUtente()); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public Utente merge(Utente existing, Utente changes) { // Definisce il metodo merge che supporta la logica di dominio.
            if (changes.getUsername() != null) { // Valuta la condizione per controllare il flusso applicativo.
                existing.setUsername(changes.getUsername()); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            if (changes.getRuolo() != null) { // Valuta la condizione per controllare il flusso applicativo.
                existing.setRuolo(changes.getRuolo()); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            UserRole role = requireRole(existing); // Assegna il valore calcolato alla variabile UserRole role.
            AccountAssociation associationType = associationFor(role); // Assegna il valore calcolato alla variabile AccountAssociation associationType.
            if (associationType == AccountAssociation.DIPENDENTE) { // Valuta la condizione per controllare il flusso applicativo.
                Dipendente dipendente = firstNonNull(changes.getDipendente(), existing.getDipendente()); // Assegna il valore calcolato alla variabile Dipendente dipendente.
                existing.setDipendente(dipendente); // Esegue l'istruzione terminata dal punto e virgola.
                existing.setCliente(null); // Esegue l'istruzione terminata dal punto e virgola.
            } else if (associationType == AccountAssociation.CLIENTE) { // Apre il blocco di codice associato alla dichiarazione.
                Cliente cliente = firstNonNull(changes.getCliente(), existing.getCliente()); // Assegna il valore calcolato alla variabile Cliente cliente.
                existing.setCliente(cliente); // Esegue l'istruzione terminata dal punto e virgola.
                existing.setDipendente(null); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            if (changes.getPasswordHash() != null && !changes.getPasswordHash().isBlank()) { // Valuta la condizione per controllare il flusso applicativo.
                existing.setPasswordHash(encoder.encode(changes.getPasswordHash())); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
            return existing; // Restituisce il risultato dell'espressione existing.
        } // Chiude il blocco di codice precedente.

        private void validateAssociations(UserRole role, Dipendente finalDipendente, Cliente finalCliente, // Definisce il metodo validateAssociations che supporta la logica di dominio.
                Dipendente rawDipendente, Cliente rawCliente, Long currentUserId) { // Apre il blocco di codice associato alla dichiarazione.
            AccountAssociation associationType = associationFor(role); // Assegna il valore calcolato alla variabile AccountAssociation associationType.
            switch (associationType) { // Seleziona il ramo logico in base al valore indicato.
                case DIPENDENTE -> { // Gestisce uno dei possibili casi nello switch.
                    Dipendente dipendente = requireDipendente(finalDipendente); // Assegna il valore calcolato alla variabile Dipendente dipendente.
                    if (finalCliente != null || rawCliente != null) { // Valuta la condizione per controllare il flusso applicativo.
                        throw new IllegalArgumentException("cliente"); // Propaga un'eccezione verso il chiamante.
                    } // Chiude il blocco di codice precedente.
                    ensureUniqueDipendente(dipendente.getId(), currentUserId); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
                case CLIENTE -> { // Gestisce uno dei possibili casi nello switch.
                    Cliente cliente = requireCliente(finalCliente); // Assegna il valore calcolato alla variabile Cliente cliente.
                    if (finalDipendente != null || rawDipendente != null) { // Valuta la condizione per controllare il flusso applicativo.
                        throw new IllegalArgumentException("dipendente"); // Propaga un'eccezione verso il chiamante.
                    } // Chiude il blocco di codice precedente.
                    ensureUniqueCliente(cliente.getId(), currentUserId); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
                default -> throw new IllegalStateException("Associazione non gestita per il ruolo " + role); // Definisce il metodo IllegalStateException che supporta la logica di dominio.
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.

        private void ensureUniqueDipendente(Long dipendenteId, Long currentUserId) { // Definisce il metodo ensureUniqueDipendente che supporta la logica di dominio.
            repository.findByDipendente_Id(dipendenteId) // Esegue l'istruzione necessaria alla logica applicativa.
                    .filter(existing -> !Objects.equals(existing.getIdUtente(), currentUserId)) // Esegue l'istruzione necessaria alla logica applicativa.
                    .ifPresent(existing -> { // Apre il blocco di codice associato alla dichiarazione.
                        throw new IllegalStateException("Esiste già un utente associato al dipendente con id " // Propaga un'eccezione verso il chiamante.
                                + dipendenteId); // Esegue l'istruzione terminata dal punto e virgola.
                    }); // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.

        private void ensureUniqueCliente(Long clienteId, Long currentUserId) { // Definisce il metodo ensureUniqueCliente che supporta la logica di dominio.
            repository.findByCliente_Id(clienteId) // Esegue l'istruzione necessaria alla logica applicativa.
                    .filter(existing -> !Objects.equals(existing.getIdUtente(), currentUserId)) // Esegue l'istruzione necessaria alla logica applicativa.
                    .ifPresent(existing -> { // Apre il blocco di codice associato alla dichiarazione.
                        throw new IllegalStateException("Esiste già un utente associato al cliente con id " + clienteId); // Propaga un'eccezione verso il chiamante.
                    }); // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.

        private Dipendente requireDipendente(Dipendente dipendente) { // Definisce il metodo requireDipendente che supporta la logica di dominio.
            if (dipendente == null || dipendente.getId() == null) { // Valuta la condizione per controllare il flusso applicativo.
                throw new IllegalArgumentException("dipendente"); // Propaga un'eccezione verso il chiamante.
            } // Chiude il blocco di codice precedente.
            return dipendente; // Restituisce il risultato dell'espressione dipendente.
        } // Chiude il blocco di codice precedente.

        private Cliente requireCliente(Cliente cliente) { // Definisce il metodo requireCliente che supporta la logica di dominio.
            if (cliente == null || cliente.getId() == null) { // Valuta la condizione per controllare il flusso applicativo.
                throw new IllegalArgumentException("cliente"); // Propaga un'eccezione verso il chiamante.
            } // Chiude il blocco di codice precedente.
            return cliente; // Restituisce il risultato dell'espressione cliente.
        } // Chiude il blocco di codice precedente.

        private void clearUnusedAssociations(Utente entity, UserRole role) { // Definisce il metodo clearUnusedAssociations che supporta la logica di dominio.
            AccountAssociation associationType = associationFor(role); // Assegna il valore calcolato alla variabile AccountAssociation associationType.
            if (associationType == AccountAssociation.DIPENDENTE) { // Valuta la condizione per controllare il flusso applicativo.
                entity.setCliente(null); // Esegue l'istruzione terminata dal punto e virgola.
            } else if (associationType == AccountAssociation.CLIENTE) { // Apre il blocco di codice associato alla dichiarazione.
                entity.setDipendente(null); // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.

        private AccountAssociation associationFor(UserRole role) { // Definisce il metodo associationFor che supporta la logica di dominio.
            AccountAssociation associationType = role.getAccountAssociation(); // Assegna il valore calcolato alla variabile AccountAssociation associationType.
            if (associationType == null) { // Valuta la condizione per controllare il flusso applicativo.
                throw new IllegalStateException("Ruolo non supportato: " + role); // Propaga un'eccezione verso il chiamante.
            } // Chiude il blocco di codice precedente.
            return associationType; // Restituisce il risultato dell'espressione associationType.
        } // Chiude il blocco di codice precedente.

        private UserRole requireRole(Utente entity) { // Definisce il metodo requireRole che supporta la logica di dominio.
            UserRole role = entity.getRuolo(); // Assegna il valore calcolato alla variabile UserRole role.
            if (role == null) { // Valuta la condizione per controllare il flusso applicativo.
                throw new IllegalArgumentException("ruolo"); // Propaga un'eccezione verso il chiamante.
            } // Chiude il blocco di codice precedente.
            return role; // Restituisce il risultato dell'espressione role.
        } // Chiude il blocco di codice precedente.

        private static <T> T firstNonNull(T primary, T fallback) { // Definisce il metodo firstNonNull che supporta la logica di dominio.
            return primary != null ? primary : fallback; // Restituisce il risultato dell'espressione primary != null ? primary : fallback.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public UtenteService(UtenteRepository repository) { // Costruttore della classe UtenteService che inizializza le dipendenze necessarie.
        super(repository, new UtenteCrudHandler(new BCryptPasswordEncoder(), repository), "Utente"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public List<Utente> getAllUtenti() { // Definisce il metodo getAllUtenti che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Utente> getUtenteById(Long id) { // Definisce il metodo getUtenteById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Utente createUtente(Utente utente) { // Definisce il metodo createUtente che supporta la logica di dominio.
        return create(utente); // Restituisce il risultato dell'espressione create(utente).
    } // Chiude il blocco di codice precedente.

    public Utente updateUtente(Long id, Utente utenteDetails) { // Definisce il metodo updateUtente che supporta la logica di dominio.
        return update(id, utenteDetails); // Restituisce il risultato dell'espressione update(id, utenteDetails).
    } // Chiude il blocco di codice precedente.

    public void deleteUtente(Long id) { // Definisce il metodo deleteUtente che supporta la logica di dominio.
        delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

