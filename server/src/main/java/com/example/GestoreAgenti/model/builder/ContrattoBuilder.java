package com.example.GestoreAgenti.model.builder; // Definisce il pacchetto com.example.GestoreAgenti.model.builder che contiene questa classe.

import java.math.BigDecimal; // Importa java.math.BigDecimal per abilitare le funzionalità utilizzate nel file.
import java.time.LocalDate; // Importa java.time.LocalDate per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Servizio; // Importa com.example.GestoreAgenti.model.Servizio per abilitare le funzionalità utilizzate nel file.

/**
 * Builder per creare contratti garantendo la coerenza del periodo e degli importi.
 */
public final class ContrattoBuilder { // Definisce la classe ContrattoBuilder che incapsula la logica applicativa.

    private final Contratto contratto; // Dichiara il campo contratto dell'oggetto.

    private ContrattoBuilder(Cliente cliente, Dipendente dipendente, Servizio servizio) { // Costruttore della classe ContrattoBuilder che inizializza le dipendenze necessarie.
        this.contratto = new Contratto(); // Aggiorna il campo contratto dell'istanza.
        this.contratto.setCliente(Objects.requireNonNull(cliente, "cliente")); // Aggiorna il campo contratto.setCliente(Objects.requireNonNull(cliente, "cliente")); dell'istanza.
        this.contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente")); // Aggiorna il campo contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente")); dell'istanza.
        this.contratto.setServizio(Objects.requireNonNull(servizio, "servizio")); // Aggiorna il campo contratto.setServizio(Objects.requireNonNull(servizio, "servizio")); dell'istanza.
    } // Chiude il blocco di codice precedente.

    private ContrattoBuilder(Contratto contratto) { // Costruttore della classe ContrattoBuilder che inizializza le dipendenze necessarie.
        this.contratto = contratto; // Aggiorna il campo contratto dell'istanza.
    } // Chiude il blocco di codice precedente.

    public static ContrattoBuilder nuovoContratto(Cliente cliente, Dipendente dipendente, Servizio servizio) { // Definisce il metodo nuovoContratto che supporta la logica di dominio.
        return new ContrattoBuilder(cliente, dipendente, servizio); // Restituisce il risultato dell'espressione new ContrattoBuilder(cliente, dipendente, servizio).
    } // Chiude il blocco di codice precedente.

    public static ContrattoBuilder daPrototype(Contratto contratto) { // Definisce il metodo daPrototype che supporta la logica di dominio.
        Objects.requireNonNull(contratto, "contratto"); // Esegue l'istruzione terminata dal punto e virgola.
        Contratto copia = new Contratto(); // Assegna il valore calcolato alla variabile Contratto copia.
        copia.setCliente(contratto.getCliente()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setDipendente(contratto.getDipendente()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setServizio(contratto.getServizio()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setDataInizio(contratto.getDataInizio()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setDataFine(contratto.getDataFine()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setImporto(contratto.getImporto()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setStato(contratto.getStato()); // Esegue l'istruzione terminata dal punto e virgola.
        copia.setNote(contratto.getNote()); // Esegue l'istruzione terminata dal punto e virgola.
        return new ContrattoBuilder(copia); // Restituisce il risultato dell'espressione new ContrattoBuilder(copia).
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conCliente(Cliente cliente) { // Definisce il metodo conCliente che supporta la logica di dominio.
        this.contratto.setCliente(Objects.requireNonNull(cliente, "cliente")); // Aggiorna il campo contratto.setCliente(Objects.requireNonNull(cliente, "cliente")); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conDipendente(Dipendente dipendente) { // Definisce il metodo conDipendente che supporta la logica di dominio.
        this.contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente")); // Aggiorna il campo contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente")); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conServizio(Servizio servizio) { // Definisce il metodo conServizio che supporta la logica di dominio.
        this.contratto.setServizio(Objects.requireNonNull(servizio, "servizio")); // Aggiorna il campo contratto.setServizio(Objects.requireNonNull(servizio, "servizio")); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conDataInizio(LocalDate dataInizio) { // Definisce il metodo conDataInizio che supporta la logica di dominio.
        this.contratto.setDataInizio(Objects.requireNonNull(dataInizio, "dataInizio")); // Aggiorna il campo contratto.setDataInizio(Objects.requireNonNull(dataInizio, "dataInizio")); dell'istanza.
        if (contratto.getDataFine() != null && contratto.getDataFine().isBefore(dataInizio)) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conDataFine(LocalDate dataFine) { // Definisce il metodo conDataFine che supporta la logica di dominio.
        if (dataFine != null && contratto.getDataInizio() != null && dataFine.isBefore(contratto.getDataInizio())) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.contratto.setDataFine(dataFine); // Aggiorna il campo contratto.setDataFine(dataFine); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conPeriodo(LocalDate dataInizio, LocalDate dataFine) { // Definisce il metodo conPeriodo che supporta la logica di dominio.
        conDataInizio(Objects.requireNonNull(dataInizio, "dataInizio")); // Esegue l'istruzione terminata dal punto e virgola.
        conDataFine(dataFine); // Esegue l'istruzione terminata dal punto e virgola.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conImporto(BigDecimal importo) { // Definisce il metodo conImporto che supporta la logica di dominio.
        Objects.requireNonNull(importo, "importo"); // Esegue l'istruzione terminata dal punto e virgola.
        if (importo.signum() <= 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("L'importo deve essere positivo"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        this.contratto.setImporto(importo); // Aggiorna il campo contratto.setImporto(importo); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conStato(String stato) { // Definisce il metodo conStato che supporta la logica di dominio.
        this.contratto.setStato(stato); // Aggiorna il campo contratto.setStato(stato); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public ContrattoBuilder conNote(String note) { // Definisce il metodo conNote che supporta la logica di dominio.
        this.contratto.setNote(note); // Aggiorna il campo contratto.setNote(note); dell'istanza.
        return this; // Restituisce il risultato dell'espressione this.
    } // Chiude il blocco di codice precedente.

    public Contratto build() { // Definisce il metodo build che supporta la logica di dominio.
        Objects.requireNonNull(contratto.getCliente(), "Cliente obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(contratto.getDipendente(), "Dipendente obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(contratto.getServizio(), "Servizio obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(contratto.getDataInizio(), "Data inizio obbligatoria"); // Esegue l'istruzione terminata dal punto e virgola.
        Objects.requireNonNull(contratto.getImporto(), "Importo obbligatorio"); // Esegue l'istruzione terminata dal punto e virgola.
        if (contratto.getDataFine() != null && contratto.getDataFine().isBefore(contratto.getDataInizio())) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalStateException("La data di fine deve essere successiva o uguale alla data di inizio"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        if (contratto.getImporto().signum() <= 0) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalStateException("L'importo deve essere positivo"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        return contratto; // Restituisce il risultato dell'espressione contratto.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
