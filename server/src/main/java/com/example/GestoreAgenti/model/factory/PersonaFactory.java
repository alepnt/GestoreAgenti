package com.example.GestoreAgenti.model.factory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Persona;

/**
 * Factory centralizzata per creare {@link Cliente} e {@link Dipendente} con configurazioni standard.
 */
public final class PersonaFactory {

    private PersonaFactory() {
        // Utility class
    }

    public static Cliente creaCliente(ClienteConfig config) {
        Objects.requireNonNull(config, "config");
        Cliente cliente = new Cliente();
        applicaDatiPersona(cliente, config.personaData);
        cliente.setType(Optional.ofNullable(config.type).orElse("STANDARD"));
        if (config.ragioneSociale != null) {
            cliente.setRagioneSociale(config.ragioneSociale);
        } else {
            cliente.setRagioneSociale(generaRagioneSociale(cliente));
        }
        cliente.setPartitaIva(config.partitaIva);
        if (config.customizer != null) {
            config.customizer.accept(cliente);
        }
        return cliente;
    }

    public static Dipendente creaDipendente(DipendenteConfig config) {
        Objects.requireNonNull(config, "config");
        Dipendente dipendente = new Dipendente();
        applicaDatiPersona(dipendente, config.personaData);
        dipendente.setUsername(config.username);
        dipendente.setPassword(config.password);
        dipendente.setRanking(Optional.ofNullable(config.ranking).orElse("JUNIOR"));
        dipendente.setTeam(config.team);
        dipendente.setContrattoNo(config.contrattoNo);
        dipendente.setTotProvvigioneMensile(config.totProvvigioneMensile);
        dipendente.setTotProvvigioneAnnuale(config.totProvvigioneAnnuale);
        if (config.customizer != null) {
            config.customizer.accept(dipendente);
        }
        return dipendente;
    }

    private static void applicaDatiPersona(Persona persona, PersonaData data) {
        if (persona == null || data == null) {
            return;
        }
        persona.setNome(data.getNome());
        persona.setCognome(data.getCognome());
        persona.setSesso(data.getSesso());
        persona.setDataNascita(data.getDataNascita());
        persona.setIndirizzo(data.getIndirizzo());
        persona.setCitta(data.getCitta());
        persona.setCap(data.getCap());
        persona.setProvincia(data.getProvincia());
        persona.setPaese(data.getPaese());
        persona.setEmail(data.getEmail());
        persona.setTelefono(data.getTelefono());
    }

    private static String generaRagioneSociale(Cliente cliente) {
        String nome = Optional.ofNullable(cliente.getNome()).orElse("");
        String cognome = Optional.ofNullable(cliente.getCognome()).orElse("");
        String base = (nome + " " + cognome).trim();
        return base.isEmpty() ? "Cliente" : base;
    }

    public static PersonaData.Builder persona() {
        return new PersonaData.Builder();
    }

    public static ClienteConfig.Builder cliente(PersonaData personaData) {
        return new ClienteConfig.Builder(personaData);
    }

    public static DipendenteConfig.Builder dipendente(PersonaData personaData) {
        return new DipendenteConfig.Builder(personaData);
    }

    public static final class PersonaData {
        private final String nome;
        private final String cognome;
        private final String sesso;
        private final LocalDate dataNascita;
        private final String indirizzo;
        private final String citta;
        private final String cap;
        private final String provincia;
        private final String paese;
        private final String email;
        private final String telefono;

        private PersonaData(Builder builder) {
            this.nome = builder.nome;
            this.cognome = builder.cognome;
            this.sesso = builder.sesso;
            this.dataNascita = builder.dataNascita;
            this.indirizzo = builder.indirizzo;
            this.citta = builder.citta;
            this.cap = builder.cap;
            this.provincia = builder.provincia;
            this.paese = builder.paese;
            this.email = builder.email;
            this.telefono = builder.telefono;
        }

        public String getNome() { return nome; }
        public String getCognome() { return cognome; }
        public String getSesso() { return sesso; }
        public LocalDate getDataNascita() { return dataNascita; }
        public String getIndirizzo() { return indirizzo; }
        public String getCitta() { return citta; }
        public String getCap() { return cap; }
        public String getProvincia() { return provincia; }
        public String getPaese() { return paese; }
        public String getEmail() { return email; }
        public String getTelefono() { return telefono; }

        public static final class Builder {
            private String nome;
            private String cognome;
            private String sesso;
            private LocalDate dataNascita;
            private String indirizzo;
            private String citta;
            private String cap;
            private String provincia;
            private String paese;
            private String email;
            private String telefono;

            private Builder() {
            }

            public Builder nome(String nome) {
                this.nome = nome;
                return this;
            }

            public Builder cognome(String cognome) {
                this.cognome = cognome;
                return this;
            }

            public Builder sesso(String sesso) {
                this.sesso = sesso;
                return this;
            }

            public Builder dataNascita(LocalDate dataNascita) {
                this.dataNascita = dataNascita;
                return this;
            }

            public Builder indirizzo(String indirizzo) {
                this.indirizzo = indirizzo;
                return this;
            }

            public Builder citta(String citta) {
                this.citta = citta;
                return this;
            }

            public Builder cap(String cap) {
                this.cap = cap;
                return this;
            }

            public Builder provincia(String provincia) {
                this.provincia = provincia;
                return this;
            }

            public Builder paese(String paese) {
                this.paese = paese;
                return this;
            }

            public Builder email(String email) {
                this.email = email;
                return this;
            }

            public Builder telefono(String telefono) {
                this.telefono = telefono;
                return this;
            }

            public PersonaData build() {
                return new PersonaData(this);
            }
        }
    }

    public static final class ClienteConfig {
        private final PersonaData personaData;
        private final String type;
        private final String ragioneSociale;
        private final String partitaIva;
        private final Consumer<Cliente> customizer;

        private ClienteConfig(Builder builder) {
            this.personaData = builder.personaData;
            this.type = builder.type;
            this.ragioneSociale = builder.ragioneSociale;
            this.partitaIva = builder.partitaIva;
            this.customizer = builder.customizer;
        }

        public static final class Builder {
            private final PersonaData personaData;
            private String type;
            private String ragioneSociale;
            private String partitaIva;
            private Consumer<Cliente> customizer;

            private Builder(PersonaData personaData) {
                this.personaData = Objects.requireNonNull(personaData, "personaData");
            }

            public Builder type(String type) {
                this.type = type;
                return this;
            }

            public Builder ragioneSociale(String ragioneSociale) {
                this.ragioneSociale = ragioneSociale;
                return this;
            }

            public Builder partitaIva(String partitaIva) {
                this.partitaIva = partitaIva;
                return this;
            }

            public Builder customizer(Consumer<Cliente> customizer) {
                this.customizer = customizer;
                return this;
            }

            public ClienteConfig build() {
                return new ClienteConfig(this);
            }
        }
    }

    public static final class DipendenteConfig {
        private final PersonaData personaData;
        private final String username;
        private final String password;
        private final String ranking;
        private final String team;
        private final String contrattoNo;
        private final Double totProvvigioneMensile;
        private final Double totProvvigioneAnnuale;
        private final Consumer<Dipendente> customizer;

        private DipendenteConfig(Builder builder) {
            this.personaData = builder.personaData;
            this.username = builder.username;
            this.password = builder.password;
            this.ranking = builder.ranking;
            this.team = builder.team;
            this.contrattoNo = builder.contrattoNo;
            this.totProvvigioneMensile = builder.totProvvigioneMensile;
            this.totProvvigioneAnnuale = builder.totProvvigioneAnnuale;
            this.customizer = builder.customizer;
        }

        public static final class Builder {
            private final PersonaData personaData;
            private String username;
            private String password;
            private String ranking;
            private String team;
            private String contrattoNo;
            private Double totProvvigioneMensile;
            private Double totProvvigioneAnnuale;
            private Consumer<Dipendente> customizer;

            private Builder(PersonaData personaData) {
                this.personaData = Objects.requireNonNull(personaData, "personaData");
            }

            public Builder credenziali(String username, String password) {
                this.username = username;
                this.password = password;
                return this;
            }

            public Builder ranking(String ranking) {
                this.ranking = ranking;
                return this;
            }

            public Builder team(String team) {
                this.team = team;
                return this;
            }

            public Builder contrattoNo(String contrattoNo) {
                this.contrattoNo = contrattoNo;
                return this;
            }

            public Builder provvigioni(Double mensile, Double annuale) {
                this.totProvvigioneMensile = mensile;
                this.totProvvigioneAnnuale = annuale;
                return this;
            }

            public Builder customizer(Consumer<Dipendente> customizer) {
                this.customizer = customizer;
                return this;
            }

            public DipendenteConfig build() {
                return new DipendenteConfig(this);
            }
        }
    }
}
