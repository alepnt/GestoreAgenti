package com.example.GestoreAgenti.model.factory; // Definisce il pacchetto com.example.GestoreAgenti.model.factory che contiene questa classe.

import java.time.LocalDate; // Importa java.time.LocalDate per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.
import java.util.function.Consumer; // Importa java.util.function.Consumer per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Persona; // Importa com.example.GestoreAgenti.model.Persona per abilitare le funzionalità utilizzate nel file.

/**
 * Factory centralizzata per creare {@link Cliente} e {@link Dipendente} con configurazioni standard.
 */
public final class PersonaFactory { // Definisce la classe PersonaFactory che incapsula la logica applicativa.

    private PersonaFactory() { // Costruttore della classe PersonaFactory che inizializza le dipendenze necessarie.
        // Utility class
    } // Chiude il blocco di codice precedente.

    public static Cliente creaCliente(ClienteConfig config) { // Definisce il metodo creaCliente che supporta la logica di dominio.
        Objects.requireNonNull(config, "config"); // Esegue l'istruzione terminata dal punto e virgola.
        Cliente cliente = new Cliente(); // Assegna il valore calcolato alla variabile Cliente cliente.
        applicaDatiPersona(cliente, config.personaData); // Esegue l'istruzione terminata dal punto e virgola.
        cliente.setType(Optional.ofNullable(config.type).orElse("STANDARD")); // Esegue l'istruzione terminata dal punto e virgola.
        if (config.ragioneSociale != null) { // Valuta la condizione per controllare il flusso applicativo.
            cliente.setRagioneSociale(config.ragioneSociale); // Esegue l'istruzione terminata dal punto e virgola.
        } else { // Apre il blocco di codice associato alla dichiarazione.
            cliente.setRagioneSociale(generaRagioneSociale(cliente)); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        cliente.setPartitaIva(config.partitaIva); // Esegue l'istruzione terminata dal punto e virgola.
        if (config.customizer != null) { // Valuta la condizione per controllare il flusso applicativo.
            config.customizer.accept(cliente); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        return cliente; // Restituisce il risultato dell'espressione cliente.
    } // Chiude il blocco di codice precedente.

    public static Dipendente creaDipendente(DipendenteConfig config) { // Definisce il metodo creaDipendente che supporta la logica di dominio.
        Objects.requireNonNull(config, "config"); // Esegue l'istruzione terminata dal punto e virgola.
        Dipendente dipendente = new Dipendente(); // Assegna il valore calcolato alla variabile Dipendente dipendente.
        applicaDatiPersona(dipendente, config.personaData); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setUsername(config.username); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setPassword(config.password); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setRanking(Optional.ofNullable(config.ranking).orElse("JUNIOR")); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setTeam(config.team); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setContrattoNo(config.contrattoNo); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setTotProvvigioneMensile(config.totProvvigioneMensile); // Esegue l'istruzione terminata dal punto e virgola.
        dipendente.setTotProvvigioneAnnuale(config.totProvvigioneAnnuale); // Esegue l'istruzione terminata dal punto e virgola.
        if (config.customizer != null) { // Valuta la condizione per controllare il flusso applicativo.
            config.customizer.accept(dipendente); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        return dipendente; // Restituisce il risultato dell'espressione dipendente.
    } // Chiude il blocco di codice precedente.

    private static void applicaDatiPersona(Persona persona, PersonaData data) { // Definisce il metodo applicaDatiPersona che supporta la logica di dominio.
        if (persona == null || data == null) { // Valuta la condizione per controllare il flusso applicativo.
            return; // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
        persona.setNome(data.getNome()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setCognome(data.getCognome()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setSesso(data.getSesso()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setDataNascita(data.getDataNascita()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setIndirizzo(data.getIndirizzo()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setCitta(data.getCitta()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setCap(data.getCap()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setProvincia(data.getProvincia()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setPaese(data.getPaese()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setEmail(data.getEmail()); // Esegue l'istruzione terminata dal punto e virgola.
        persona.setTelefono(data.getTelefono()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private static String generaRagioneSociale(Cliente cliente) { // Definisce il metodo generaRagioneSociale che supporta la logica di dominio.
        String nome = Optional.ofNullable(cliente.getNome()).orElse(""); // Assegna il valore calcolato alla variabile String nome.
        String cognome = Optional.ofNullable(cliente.getCognome()).orElse(""); // Assegna il valore calcolato alla variabile String cognome.
        String base = (nome + " " + cognome).trim(); // Assegna il valore calcolato alla variabile String base.
        return base.isEmpty() ? "Cliente" : base; // Restituisce il risultato dell'espressione base.isEmpty() ? "Cliente" : base.
    } // Chiude il blocco di codice precedente.

    public static PersonaData.Builder persona() { // Definisce il metodo persona che supporta la logica di dominio.
        return new PersonaData.Builder(); // Restituisce il risultato dell'espressione new PersonaData.Builder().
    } // Chiude il blocco di codice precedente.

    public static ClienteConfig.Builder cliente(PersonaData personaData) { // Definisce il metodo cliente che supporta la logica di dominio.
        return new ClienteConfig.Builder(personaData); // Restituisce il risultato dell'espressione new ClienteConfig.Builder(personaData).
    } // Chiude il blocco di codice precedente.

    public static DipendenteConfig.Builder dipendente(PersonaData personaData) { // Definisce il metodo dipendente che supporta la logica di dominio.
        return new DipendenteConfig.Builder(personaData); // Restituisce il risultato dell'espressione new DipendenteConfig.Builder(personaData).
    } // Chiude il blocco di codice precedente.

    public static final class PersonaData { // Apre il blocco di codice associato alla dichiarazione.
        private final String nome; // Dichiara il campo nome dell'oggetto.
        private final String cognome; // Dichiara il campo cognome dell'oggetto.
        private final String sesso; // Dichiara il campo sesso dell'oggetto.
        private final LocalDate dataNascita; // Dichiara il campo dataNascita dell'oggetto.
        private final String indirizzo; // Dichiara il campo indirizzo dell'oggetto.
        private final String citta; // Dichiara il campo citta dell'oggetto.
        private final String cap; // Dichiara il campo cap dell'oggetto.
        private final String provincia; // Dichiara il campo provincia dell'oggetto.
        private final String paese; // Dichiara il campo paese dell'oggetto.
        private final String email; // Dichiara il campo email dell'oggetto.
        private final String telefono; // Dichiara il campo telefono dell'oggetto.

        private PersonaData(Builder builder) { // Definisce il metodo PersonaData che supporta la logica di dominio.
            this.nome = builder.nome; // Aggiorna il campo nome dell'istanza.
            this.cognome = builder.cognome; // Aggiorna il campo cognome dell'istanza.
            this.sesso = builder.sesso; // Aggiorna il campo sesso dell'istanza.
            this.dataNascita = builder.dataNascita; // Aggiorna il campo dataNascita dell'istanza.
            this.indirizzo = builder.indirizzo; // Aggiorna il campo indirizzo dell'istanza.
            this.citta = builder.citta; // Aggiorna il campo citta dell'istanza.
            this.cap = builder.cap; // Aggiorna il campo cap dell'istanza.
            this.provincia = builder.provincia; // Aggiorna il campo provincia dell'istanza.
            this.paese = builder.paese; // Aggiorna il campo paese dell'istanza.
            this.email = builder.email; // Aggiorna il campo email dell'istanza.
            this.telefono = builder.telefono; // Aggiorna il campo telefono dell'istanza.
        } // Chiude il blocco di codice precedente.

        public String getNome() { return nome; } // Definisce il metodo getNome che supporta la logica di dominio.
        public String getCognome() { return cognome; } // Definisce il metodo getCognome che supporta la logica di dominio.
        public String getSesso() { return sesso; } // Definisce il metodo getSesso che supporta la logica di dominio.
        public LocalDate getDataNascita() { return dataNascita; } // Definisce il metodo getDataNascita che supporta la logica di dominio.
        public String getIndirizzo() { return indirizzo; } // Definisce il metodo getIndirizzo che supporta la logica di dominio.
        public String getCitta() { return citta; } // Definisce il metodo getCitta che supporta la logica di dominio.
        public String getCap() { return cap; } // Definisce il metodo getCap che supporta la logica di dominio.
        public String getProvincia() { return provincia; } // Definisce il metodo getProvincia che supporta la logica di dominio.
        public String getPaese() { return paese; } // Definisce il metodo getPaese che supporta la logica di dominio.
        public String getEmail() { return email; } // Definisce il metodo getEmail che supporta la logica di dominio.
        public String getTelefono() { return telefono; } // Definisce il metodo getTelefono che supporta la logica di dominio.

        public static final class Builder { // Apre il blocco di codice associato alla dichiarazione.
            private String nome; // Dichiara il campo nome dell'oggetto.
            private String cognome; // Dichiara il campo cognome dell'oggetto.
            private String sesso; // Dichiara il campo sesso dell'oggetto.
            private LocalDate dataNascita; // Dichiara il campo dataNascita dell'oggetto.
            private String indirizzo; // Dichiara il campo indirizzo dell'oggetto.
            private String citta; // Dichiara il campo citta dell'oggetto.
            private String cap; // Dichiara il campo cap dell'oggetto.
            private String provincia; // Dichiara il campo provincia dell'oggetto.
            private String paese; // Dichiara il campo paese dell'oggetto.
            private String email; // Dichiara il campo email dell'oggetto.
            private String telefono; // Dichiara il campo telefono dell'oggetto.

            private Builder() { // Definisce il metodo Builder che supporta la logica di dominio.
            } // Chiude il blocco di codice precedente.

            public Builder nome(String nome) { // Definisce il metodo nome che supporta la logica di dominio.
                this.nome = nome; // Aggiorna il campo nome dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder cognome(String cognome) { // Definisce il metodo cognome che supporta la logica di dominio.
                this.cognome = cognome; // Aggiorna il campo cognome dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder sesso(String sesso) { // Definisce il metodo sesso che supporta la logica di dominio.
                this.sesso = sesso; // Aggiorna il campo sesso dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder dataNascita(LocalDate dataNascita) { // Definisce il metodo dataNascita che supporta la logica di dominio.
                this.dataNascita = dataNascita; // Aggiorna il campo dataNascita dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder indirizzo(String indirizzo) { // Definisce il metodo indirizzo che supporta la logica di dominio.
                this.indirizzo = indirizzo; // Aggiorna il campo indirizzo dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder citta(String citta) { // Definisce il metodo citta che supporta la logica di dominio.
                this.citta = citta; // Aggiorna il campo citta dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder cap(String cap) { // Definisce il metodo cap che supporta la logica di dominio.
                this.cap = cap; // Aggiorna il campo cap dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder provincia(String provincia) { // Definisce il metodo provincia che supporta la logica di dominio.
                this.provincia = provincia; // Aggiorna il campo provincia dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder paese(String paese) { // Definisce il metodo paese che supporta la logica di dominio.
                this.paese = paese; // Aggiorna il campo paese dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder email(String email) { // Definisce il metodo email che supporta la logica di dominio.
                this.email = email; // Aggiorna il campo email dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder telefono(String telefono) { // Definisce il metodo telefono che supporta la logica di dominio.
                this.telefono = telefono; // Aggiorna il campo telefono dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public PersonaData build() { // Definisce il metodo build che supporta la logica di dominio.
                return new PersonaData(this); // Restituisce il risultato dell'espressione new PersonaData(this).
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public static final class ClienteConfig { // Apre il blocco di codice associato alla dichiarazione.
        private final PersonaData personaData; // Dichiara il campo personaData dell'oggetto.
        private final String type; // Dichiara il campo type dell'oggetto.
        private final String ragioneSociale; // Dichiara il campo ragioneSociale dell'oggetto.
        private final String partitaIva; // Dichiara il campo partitaIva dell'oggetto.
        private final Consumer<Cliente> customizer; // Dichiara il campo customizer dell'oggetto.

        private ClienteConfig(Builder builder) { // Definisce il metodo ClienteConfig che supporta la logica di dominio.
            this.personaData = builder.personaData; // Aggiorna il campo personaData dell'istanza.
            this.type = builder.type; // Aggiorna il campo type dell'istanza.
            this.ragioneSociale = builder.ragioneSociale; // Aggiorna il campo ragioneSociale dell'istanza.
            this.partitaIva = builder.partitaIva; // Aggiorna il campo partitaIva dell'istanza.
            this.customizer = builder.customizer; // Aggiorna il campo customizer dell'istanza.
        } // Chiude il blocco di codice precedente.

        public static final class Builder { // Apre il blocco di codice associato alla dichiarazione.
            private final PersonaData personaData; // Dichiara il campo personaData dell'oggetto.
            private String type; // Dichiara il campo type dell'oggetto.
            private String ragioneSociale; // Dichiara il campo ragioneSociale dell'oggetto.
            private String partitaIva; // Dichiara il campo partitaIva dell'oggetto.
            private Consumer<Cliente> customizer; // Dichiara il campo customizer dell'oggetto.

            private Builder(PersonaData personaData) { // Definisce il metodo Builder che supporta la logica di dominio.
                this.personaData = Objects.requireNonNull(personaData, "personaData"); // Aggiorna il campo personaData dell'istanza.
            } // Chiude il blocco di codice precedente.

            public Builder type(String type) { // Definisce il metodo type che supporta la logica di dominio.
                this.type = type; // Aggiorna il campo type dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder ragioneSociale(String ragioneSociale) { // Definisce il metodo ragioneSociale che supporta la logica di dominio.
                this.ragioneSociale = ragioneSociale; // Aggiorna il campo ragioneSociale dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder partitaIva(String partitaIva) { // Definisce il metodo partitaIva che supporta la logica di dominio.
                this.partitaIva = partitaIva; // Aggiorna il campo partitaIva dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder customizer(Consumer<Cliente> customizer) { // Definisce il metodo customizer che supporta la logica di dominio.
                this.customizer = customizer; // Aggiorna il campo customizer dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public ClienteConfig build() { // Definisce il metodo build che supporta la logica di dominio.
                return new ClienteConfig(this); // Restituisce il risultato dell'espressione new ClienteConfig(this).
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    public static final class DipendenteConfig { // Apre il blocco di codice associato alla dichiarazione.
        private final PersonaData personaData; // Dichiara il campo personaData dell'oggetto.
        private final String username; // Dichiara il campo username dell'oggetto.
        private final String password; // Dichiara il campo password dell'oggetto.
        private final String ranking; // Dichiara il campo ranking dell'oggetto.
        private final String team; // Dichiara il campo team dell'oggetto.
        private final String contrattoNo; // Dichiara il campo contrattoNo dell'oggetto.
        private final Double totProvvigioneMensile; // Dichiara il campo totProvvigioneMensile dell'oggetto.
        private final Double totProvvigioneAnnuale; // Dichiara il campo totProvvigioneAnnuale dell'oggetto.
        private final Consumer<Dipendente> customizer; // Dichiara il campo customizer dell'oggetto.

        private DipendenteConfig(Builder builder) { // Definisce il metodo DipendenteConfig che supporta la logica di dominio.
            this.personaData = builder.personaData; // Aggiorna il campo personaData dell'istanza.
            this.username = builder.username; // Aggiorna il campo username dell'istanza.
            this.password = builder.password; // Aggiorna il campo password dell'istanza.
            this.ranking = builder.ranking; // Aggiorna il campo ranking dell'istanza.
            this.team = builder.team; // Aggiorna il campo team dell'istanza.
            this.contrattoNo = builder.contrattoNo; // Aggiorna il campo contrattoNo dell'istanza.
            this.totProvvigioneMensile = builder.totProvvigioneMensile; // Aggiorna il campo totProvvigioneMensile dell'istanza.
            this.totProvvigioneAnnuale = builder.totProvvigioneAnnuale; // Aggiorna il campo totProvvigioneAnnuale dell'istanza.
            this.customizer = builder.customizer; // Aggiorna il campo customizer dell'istanza.
        } // Chiude il blocco di codice precedente.

        public static final class Builder { // Apre il blocco di codice associato alla dichiarazione.
            private final PersonaData personaData; // Dichiara il campo personaData dell'oggetto.
            private String username; // Dichiara il campo username dell'oggetto.
            private String password; // Dichiara il campo password dell'oggetto.
            private String ranking; // Dichiara il campo ranking dell'oggetto.
            private String team; // Dichiara il campo team dell'oggetto.
            private String contrattoNo; // Dichiara il campo contrattoNo dell'oggetto.
            private Double totProvvigioneMensile; // Dichiara il campo totProvvigioneMensile dell'oggetto.
            private Double totProvvigioneAnnuale; // Dichiara il campo totProvvigioneAnnuale dell'oggetto.
            private Consumer<Dipendente> customizer; // Dichiara il campo customizer dell'oggetto.

            private Builder(PersonaData personaData) { // Definisce il metodo Builder che supporta la logica di dominio.
                this.personaData = Objects.requireNonNull(personaData, "personaData"); // Aggiorna il campo personaData dell'istanza.
            } // Chiude il blocco di codice precedente.

            public Builder credenziali(String username, String password) { // Definisce il metodo credenziali che supporta la logica di dominio.
                this.username = username; // Aggiorna il campo username dell'istanza.
                this.password = password; // Aggiorna il campo password dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder ranking(String ranking) { // Definisce il metodo ranking che supporta la logica di dominio.
                this.ranking = ranking; // Aggiorna il campo ranking dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder team(String team) { // Definisce il metodo team che supporta la logica di dominio.
                this.team = team; // Aggiorna il campo team dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder contrattoNo(String contrattoNo) { // Definisce il metodo contrattoNo che supporta la logica di dominio.
                this.contrattoNo = contrattoNo; // Aggiorna il campo contrattoNo dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder provvigioni(Double mensile, Double annuale) { // Definisce il metodo provvigioni che supporta la logica di dominio.
                this.totProvvigioneMensile = mensile; // Aggiorna il campo totProvvigioneMensile dell'istanza.
                this.totProvvigioneAnnuale = annuale; // Aggiorna il campo totProvvigioneAnnuale dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public Builder customizer(Consumer<Dipendente> customizer) { // Definisce il metodo customizer che supporta la logica di dominio.
                this.customizer = customizer; // Aggiorna il campo customizer dell'istanza.
                return this; // Restituisce il risultato dell'espressione this.
            } // Chiude il blocco di codice precedente.

            public DipendenteConfig build() { // Definisce il metodo build che supporta la logica di dominio.
                return new DipendenteConfig(this); // Restituisce il risultato dell'espressione new DipendenteConfig(this).
            } // Chiude il blocco di codice precedente.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
