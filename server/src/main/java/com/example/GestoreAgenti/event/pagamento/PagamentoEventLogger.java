package com.example.GestoreAgenti.event.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.event.pagamento che contiene questa classe.

import org.slf4j.Logger; // Importa org.slf4j.Logger per abilitare le funzionalità utilizzate nel file.
import org.slf4j.LoggerFactory; // Importa org.slf4j.LoggerFactory per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.

@Component // Applica l'annotazione @Component per configurare il componente.
public class PagamentoEventLogger { // Definisce la classe PagamentoEventLogger che incapsula la logica applicativa.

    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoEventLogger.class); // Definisce il metodo LoggerFactory.getLogger che supporta la logica di dominio.

    public PagamentoEventLogger(DomainEventPublisher publisher) { // Costruttore della classe PagamentoEventLogger che inizializza le dipendenze necessarie.
        publisher.subscribe(PagamentoStatusChangedEvent.class, this::logStatusChange); // Esegue l'istruzione terminata dal punto e virgola.
        publisher.subscribe(PagamentoCreatedEvent.class, event -> // Esegue l'istruzione necessaria alla logica applicativa.
                LOGGER.info("Creato pagamento {} in stato {}", event.pagamento().getIdPagamento(), event.pagamento().getStato())); // Esegue l'istruzione terminata dal punto e virgola.
        publisher.subscribe(PagamentoDeletedEvent.class, event -> // Esegue l'istruzione necessaria alla logica applicativa.
                LOGGER.info("Eliminato pagamento {}", event.pagamento().getIdPagamento())); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    private void logStatusChange(PagamentoStatusChangedEvent event) { // Definisce il metodo logStatusChange che supporta la logica di dominio.
        LOGGER.info("Pagamento {} passato da {} a {}", event.pagamento().getIdPagamento(), // Esegue l'istruzione necessaria alla logica applicativa.
                event.previousStatus(), event.newStatus()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
