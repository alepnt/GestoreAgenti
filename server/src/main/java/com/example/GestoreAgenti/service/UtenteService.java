package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.repository.UtenteRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.CrudEntityHandler;

@Service
public class UtenteService extends AbstractCrudService<Utente, Long> {

    private static final class UtenteCrudHandler implements CrudEntityHandler<Utente> {
        private final BCryptPasswordEncoder encoder;

        private UtenteCrudHandler(BCryptPasswordEncoder encoder) {
            this.encoder = encoder;
        }

        @Override
        public Utente prepareForCreate(Utente entity) {
            Objects.requireNonNull(entity.getRuolo(), "ruolo");
            entity.setPasswordHash(encoder.encode(entity.getPasswordHash()));
            return entity;
        }

        @Override
        public Utente merge(Utente existing, Utente changes) {
            existing.setUsername(changes.getUsername());
            if (changes.getRuolo() != null) {
                existing.setRuolo(changes.getRuolo());
            }
            existing.setDipendente(changes.getDipendente());
            if (changes.getPasswordHash() != null && !changes.getPasswordHash().isBlank()) {
                existing.setPasswordHash(encoder.encode(changes.getPasswordHash()));
            }
            return existing;
        }
    }

    public UtenteService(UtenteRepository repository) {
        super(repository, new UtenteCrudHandler(new BCryptPasswordEncoder()), "Utente");
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

