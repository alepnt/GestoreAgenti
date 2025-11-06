package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.security.UserRole;
import com.example.GestoreAgenti.repository.UtenteRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.CrudEntityHandler;

@Service
public class UtenteService extends AbstractCrudService<Utente, Long> {

    private static final class UtenteCrudHandler implements CrudEntityHandler<Utente> {
        private final BCryptPasswordEncoder encoder;
        private final UtenteRepository repository;

        private UtenteCrudHandler(BCryptPasswordEncoder encoder, UtenteRepository repository) {
            this.encoder = encoder;
            this.repository = repository;
        }

        @Override
        public Utente prepareForCreate(Utente entity) {
            requireRole(entity);
            String rawPassword = entity.getPasswordHash();
            if (rawPassword == null || rawPassword.isBlank()) {
                throw new IllegalArgumentException("passwordHash");
            }
            entity.setPasswordHash(encoder.encode(rawPassword));
            return entity;
        }

        @Override
        public void validateForCreate(Utente entity) {
            UserRole role = requireRole(entity);
            validateAssociations(role, entity.getDipendente(), entity.getCliente(), entity.getDipendente(),
                    entity.getCliente(), null);
            clearUnusedAssociations(entity, role);
        }

        @Override
        public void validateForUpdate(Utente existing, Utente changes) {
            UserRole role = changes.getRuolo() != null ? changes.getRuolo() : requireRole(existing);
            UserRole.AccountAssociation associationType = associationFor(role);
            Dipendente finalDipendente = associationType == UserRole.AccountAssociation.DIPENDENTE
                    ? firstNonNull(changes.getDipendente(), existing.getDipendente())
                    : null;
            Cliente finalCliente = associationType == UserRole.AccountAssociation.CLIENTE
                    ? firstNonNull(changes.getCliente(), existing.getCliente())
                    : null;

            validateAssociations(role, finalDipendente, finalCliente, changes.getDipendente(), changes.getCliente(),
                    existing.getIdUtente());
        }

        @Override
        public Utente merge(Utente existing, Utente changes) {
            if (changes.getUsername() != null) {
                existing.setUsername(changes.getUsername());
            }
            if (changes.getRuolo() != null) {
                existing.setRuolo(changes.getRuolo());
            }
            UserRole role = requireRole(existing);
            UserRole.AccountAssociation associationType = associationFor(role);
            if (associationType == UserRole.AccountAssociation.DIPENDENTE) {
                Dipendente dipendente = firstNonNull(changes.getDipendente(), existing.getDipendente());
                existing.setDipendente(dipendente);
                existing.setCliente(null);
            } else if (associationType == UserRole.AccountAssociation.CLIENTE) {
                Cliente cliente = firstNonNull(changes.getCliente(), existing.getCliente());
                existing.setCliente(cliente);
                existing.setDipendente(null);
            }
            if (changes.getPasswordHash() != null && !changes.getPasswordHash().isBlank()) {
                existing.setPasswordHash(encoder.encode(changes.getPasswordHash()));
            }
            return existing;
        }

        private void validateAssociations(UserRole role, Dipendente finalDipendente, Cliente finalCliente,
                Dipendente rawDipendente, Cliente rawCliente, Long currentUserId) {
            UserRole.AccountAssociation associationType = associationFor(role);
            switch (associationType) {
                case DIPENDENTE -> {
                    Dipendente dipendente = requireDipendente(finalDipendente);
                    if (finalCliente != null || rawCliente != null) {
                        throw new IllegalArgumentException("cliente");
                    }
                    ensureUniqueDipendente(dipendente.getId(), currentUserId);
                }
                case CLIENTE -> {
                    Cliente cliente = requireCliente(finalCliente);
                    if (finalDipendente != null || rawDipendente != null) {
                        throw new IllegalArgumentException("dipendente");
                    }
                    ensureUniqueCliente(cliente.getId(), currentUserId);
                }
                default -> throw new IllegalStateException("Associazione non gestita per il ruolo " + role);
            }
        }

        private void ensureUniqueDipendente(Long dipendenteId, Long currentUserId) {
            repository.findByDipendente_Id(dipendenteId)
                    .filter(existing -> !Objects.equals(existing.getIdUtente(), currentUserId))
                    .ifPresent(existing -> {
                        throw new IllegalStateException("Esiste già un utente associato al dipendente con id "
                                + dipendenteId);
                    });
        }

        private void ensureUniqueCliente(Long clienteId, Long currentUserId) {
            repository.findByCliente_Id(clienteId)
                    .filter(existing -> !Objects.equals(existing.getIdUtente(), currentUserId))
                    .ifPresent(existing -> {
                        throw new IllegalStateException("Esiste già un utente associato al cliente con id " + clienteId);
                    });
        }

        private Dipendente requireDipendente(Dipendente dipendente) {
            if (dipendente == null || dipendente.getId() == null) {
                throw new IllegalArgumentException("dipendente");
            }
            return dipendente;
        }

        private Cliente requireCliente(Cliente cliente) {
            if (cliente == null || cliente.getId() == null) {
                throw new IllegalArgumentException("cliente");
            }
            return cliente;
        }

        private void clearUnusedAssociations(Utente entity, UserRole role) {
            UserRole.AccountAssociation associationType = associationFor(role);
            if (associationType == UserRole.AccountAssociation.DIPENDENTE) {
                entity.setCliente(null);
            } else if (associationType == UserRole.AccountAssociation.CLIENTE) {
                entity.setDipendente(null);
            }
        }

        private UserRole.AccountAssociation associationFor(UserRole role) {
            UserRole.AccountAssociation associationType = role.getAccountAssociation();
            if (associationType == null) {
                throw new IllegalStateException("Ruolo non supportato: " + role);
            }
            return associationType;
        }

        private UserRole requireRole(Utente entity) {
            UserRole role = entity.getRuolo();
            if (role == null) {
                throw new IllegalArgumentException("ruolo");
            }
            return role;
        }

        private static <T> T firstNonNull(T primary, T fallback) {
            return primary != null ? primary : fallback;
        }
    }

    public UtenteService(UtenteRepository repository) {
        super(repository, new UtenteCrudHandler(new BCryptPasswordEncoder(), repository), "Utente");
    }

    public List<Utente> getAllUtenti() {
        return findAll();
    }

    public Optional<Utente> getUtenteById(Long id) {
        return findOptionalById(id);
    }

    public Utente createUtente(Utente utente) {
        return create(utente);
    }

    public Utente updateUtente(Long id, Utente utenteDetails) {
        return update(id, utenteDetails);
    }

    public void deleteUtente(Long id) {
        delete(id);
    }
}

