package com.example.GestoreAgenti.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.repository.ClienteRepository;
import com.example.GestoreAgenti.repository.DipendenteRepository;
import com.example.GestoreAgenti.security.UserRole;

@DataJpaTest
@Import(UtenteService.class)
class UtenteServiceTest {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void createUtenteRequiresDipendenteAssociationForEmployeeRole() {
        Utente utente = new Utente();
        utente.setUsername("nouser");
        utente.setPasswordHash("password");
        utente.setRuolo(UserRole.AGENTE);

        assertThatThrownBy(() -> utenteService.createUtente(utente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("dipendente");
    }

    @Test
    void createUtenteRejectsClienteAssociationForEmployeeRole() {
        Cliente cliente = persistCliente("1");

        Utente utente = new Utente();
        utente.setUsername("mismatch");
        utente.setPasswordHash("password");
        utente.setRuolo(UserRole.AGENTE);
        utente.setCliente(cliente);

        assertThatThrownBy(() -> utenteService.createUtente(utente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cliente");
    }

    @Test
    void createUtenteRejectsDuplicateDipendenteAssociation() {
        Dipendente dipendente = persistDipendente("1");
        utenteService.createUtente(buildDipendenteUtente("user1", dipendente));

        Utente duplicate = buildDipendenteUtente("user2", dipendente);

        assertThatThrownBy(() -> utenteService.createUtente(duplicate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("dipendente");
    }

    @Test
    void createUtenteAllowsClienteAssociation() {
        Cliente cliente = persistCliente("2");

        Utente created = utenteService.createUtente(buildClienteUtente("clientUser", cliente));

        assertThat(created.getCliente()).isNotNull();
        assertThat(created.getCliente().getId()).isEqualTo(cliente.getId());
        assertThat(created.getDipendente()).isNull();
    }

    @Test
    void createUtenteRejectsDuplicateClienteAssociation() {
        Cliente cliente = persistCliente("3");
        utenteService.createUtente(buildClienteUtente("clientA", cliente));

        Utente duplicate = buildClienteUtente("clientB", cliente);

        assertThatThrownBy(() -> utenteService.createUtente(duplicate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cliente");
    }

    @Test
    void updateUtenteCannotStealAssociationFromAnotherUser() {
        Dipendente first = persistDipendente("4");
        Dipendente second = persistDipendente("5");

        utenteService.createUtente(buildDipendenteUtente("firstUser", first));
        Utente secondAccount = utenteService.createUtente(buildDipendenteUtente("secondUser", second));

        Utente changes = new Utente();
        changes.setUsername("secondUser");
        changes.setRuolo(UserRole.AGENTE);
        changes.setDipendente(first);

        assertThatThrownBy(() -> utenteService.updateUtente(secondAccount.getIdUtente(), changes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("dipendente");
    }

    @Test
    void updateUtenteMaintainsExistingDipendenteWhenNotProvided() {
        Dipendente dipendente = persistDipendente("6");
        Utente account = utenteService.createUtente(buildDipendenteUtente("stable", dipendente));

        Utente changes = new Utente();
        changes.setRuolo(UserRole.AGENTE);

        Utente updated = utenteService.updateUtente(account.getIdUtente(), changes);

        assertThat(updated.getDipendente()).isNotNull();
        assertThat(updated.getDipendente().getId()).isEqualTo(dipendente.getId());
    }

    @Test
    void updateUtenteSwitchingToClienteRoleClearsDipendente() {
        Dipendente dipendente = persistDipendente("7");
        Utente account = utenteService.createUtente(buildDipendenteUtente("convert", dipendente));
        Cliente cliente = persistCliente("7");

        Utente changes = new Utente();
        changes.setRuolo(UserRole.CLIENTE);
        changes.setCliente(cliente);

        Utente updated = utenteService.updateUtente(account.getIdUtente(), changes);

        assertThat(updated.getCliente()).isNotNull();
        assertThat(updated.getCliente().getId()).isEqualTo(cliente.getId());
        assertThat(updated.getDipendente()).isNull();
    }

    @Test
    void updateUtenteCannotReuseClienteAssociation() {
        Cliente first = persistCliente("8");
        Cliente second = persistCliente("9");

        utenteService.createUtente(buildClienteUtente("clientFirst", first));
        Utente secondAccount = utenteService.createUtente(buildClienteUtente("clientSecond", second));

        Utente changes = new Utente();
        changes.setUsername("clientSecond");
        changes.setRuolo(UserRole.CLIENTE);
        changes.setCliente(first);

        assertThatThrownBy(() -> utenteService.updateUtente(secondAccount.getIdUtente(), changes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cliente");
    }

    private Dipendente persistDipendente(String suffix) {
        Dipendente dipendente = new Dipendente();
        dipendente.setNome("Nome" + suffix);
        dipendente.setCognome("Cognome" + suffix);
        dipendente.setSesso("M");
        dipendente.setDataNascita(LocalDate.of(1985, 1, 1));
        dipendente.setIndirizzo("Via Roma " + suffix);
        dipendente.setCitta("Milano");
        dipendente.setCap("20100");
        dipendente.setProvincia("MI");
        dipendente.setPaese("Italia");
        dipendente.setEmail("utente" + suffix + "@example.com");
        dipendente.setTelefono("+39-02-000000" + suffix);
        dipendente.setUsername("user" + suffix);
        dipendente.setPassword("password");
        dipendente.setRanking("Junior");
        dipendente.setTeam("Team " + suffix);
        dipendente.setContrattoNo("CNT-" + suffix);
        dipendente.setTotProvvigioneMensile(0.0);
        dipendente.setTotProvvigioneAnnuale(0.0);
        return dipendenteRepository.save(dipendente);
    }

    private Cliente persistCliente(String suffix) {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente" + suffix);
        cliente.setCognome("Referente" + suffix);
        cliente.setSesso("F");
        cliente.setDataNascita(LocalDate.of(1990, 2, 2));
        cliente.setIndirizzo("Via Verdi " + suffix);
        cliente.setCitta("Torino");
        cliente.setCap("10100");
        cliente.setProvincia("TO");
        cliente.setPaese("Italia");
        cliente.setEmail("cliente" + suffix + "@example.com");
        cliente.setTelefono("+39-011-000000" + suffix);
        cliente.setType("Privato");
        cliente.setRagioneSociale(null);
        cliente.setPartitaIva(null);
        return clienteRepository.save(cliente);
    }

    private Utente buildDipendenteUtente(String username, Dipendente dipendente) {
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash("password");
        utente.setRuolo(UserRole.AGENTE);
        utente.setDipendente(dipendente);
        return utente;
    }

    private Utente buildClienteUtente(String username, Cliente cliente) {
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPasswordHash("password");
        utente.setRuolo(UserRole.CLIENTE);
        utente.setCliente(cliente);
        return utente;
    }
}
