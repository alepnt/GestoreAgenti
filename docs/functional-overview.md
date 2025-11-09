# GestoreAgenti – Panoramica Funzionale

Questa panoramica descrive le funzionalità principali del gestionale GestoreAgenti e mette in relazione i requisiti funzionali con i moduli software già presenti nel repository.

## Architettura generale

Il progetto segue un'architettura client–server:

- **Client JavaFX** (`client/`) che fornisce l'interfaccia desktop e comunica con il backend tramite REST API e WebSocket.
- **Server Spring Boot** (`server/`) che espone i servizi REST, gestisce la persistenza su SQL Server e distribuisce notifiche in tempo reale.

## Anagrafiche principali

| Dominio | Controller REST | Servizio applicativo | Modello JPA |
| --- | --- | --- | --- |
| Clienti | `ClienteController` | `ClienteService` | `Cliente` |
| Dipendenti | `DipendenteController` | `DipendenteService` | `Dipendente` |
| Team | `TeamController` | `TeamService` | `Team` |

Ogni controller implementa le classiche operazioni CRUD (lista, dettaglio, creazione, aggiornamento ed eliminazione) che alimentano le rispettive schermate anagrafiche nel client.【F:server/src/main/java/com/example/GestoreAgenti/controller/ClienteController.java†L23-L46】【F:server/src/main/java/com/example/GestoreAgenti/controller/DipendenteController.java†L23-L47】【F:server/src/main/java/com/example/GestoreAgenti/controller/TeamController.java†L26-L51】

## Contratti e assegnazioni

Il dominio dei contratti copre sia la relazione con i clienti sia l'organizzazione dei team e dei singoli dipendenti. Il controller dedicato espone le API per creare e gestire lo storico dei contratti, mentre il modello `Contratto` associa clienti, team e dipendenti tramite relazioni JPA.【F:server/src/main/java/com/example/GestoreAgenti/controller/ContrattoController.java†L24-L50】【F:server/src/main/java/com/example/GestoreAgenti/model/Contratto.java†L29-L99】

## Fatture di vendita

Le fatture sono gestite dal `FatturaController`, che permette sia la registrazione delle fatture di vendita sia la consultazione delle fatture già registrate. Il modello `Fattura` traccia lo stato della fattura e collega il documento al relativo cliente e contratto.【F:server/src/main/java/com/example/GestoreAgenti/controller/FatturaController.java†L24-L56】【F:server/src/main/java/com/example/GestoreAgenti/model/Fattura.java†L28-L102】

## Home e notifiche

Il backend fornisce un sistema di notifiche per popolare la dashboard iniziale con avvisi e scadenze. Le API del `NotificationController` restituiscono notifiche filtrate per utente, mentre il modello `Notification` conserva il tipo di evento e le date di scadenza.【F:server/src/main/java/com/example/GestoreAgenti/controller/NotificationController.java†L26-L58】【F:server/src/main/java/com/example/GestoreAgenti/model/Notification.java†L28-L86】

## Agenda e pianificazione

L'agenda giornaliera è gestita attraverso il servizio `ServizioService` esposto dal `ServizioController`, che consente di creare record per impegni, call e appunti collegati ai contratti o ai clienti. Il modello `Servizio` include data, descrizione e riferimenti alla struttura organizzativa.【F:server/src/main/java/com/example/GestoreAgenti/controller/ServizioController.java†L24-L55】【F:server/src/main/java/com/example/GestoreAgenti/model/Servizio.java†L27-L96】

## Messaggistica interna

Il modulo di comunicazione interna utilizza WebSocket per lo scambio in tempo reale di messaggi tra i team. Il `ChatController` e il `ChatWebSocketHandler` orchestrano la consegna dei messaggi, mentre `ChatMessage` rappresenta il payload inviato dai dipendenti.【F:server/src/main/java/com/example/GestoreAgenti/controller/ChatController.java†L23-L62】【F:server/src/main/java/com/example/GestoreAgenti/websocket/ChatWebSocketHandler.java†L31-L97】【F:server/src/main/java/com/example/GestoreAgenti/model/ChatMessage.java†L27-L95】

## Reportistica e grafici

Il `FatturaController` e il `PagamentoController` espongono endpoint per estrarre report sui fatturati e sui pagamenti, consentendo al client di generare grafici e scaricare dati storicizzati. I modelli `Pagamento` e `Provvigione` tracciano lo stato economico dei contratti per fornire viste aggregate lato client.【F:server/src/main/java/com/example/GestoreAgenti/controller/PagamentoController.java†L24-L63】【F:server/src/main/java/com/example/GestoreAgenti/model/Pagamento.java†L26-L84】【F:server/src/main/java/com/example/GestoreAgenti/model/Provvigione.java†L26-L98】

## Sicurezza e autenticazione

L'accesso alle API è protetto da JWT tramite le componenti definite nel pacchetto `security/`, mentre le anagrafiche utenti e ruoli sono gestite dai rispettivi controller. Questa struttura permette di limitare le funzioni disponibili a seconda del ruolo dell'agente.【F:server/src/main/java/com/example/GestoreAgenti/security/JwtAuthenticationFilter.java†L30-L115】【F:server/src/main/java/com/example/GestoreAgenti/controller/AuthController.java†L30-L85】【F:server/src/main/java/com/example/GestoreAgenti/controller/RoleController.java†L23-L53】

## Navigazione client


Sul lato client, il modulo `controller` contiene i controller JavaFX delle singole viste, mentre il pacchetto `command` definisce i comandi per il menu agile e la navigazione contestuale. Il servizio `FxDataService` sincronizza i dati locali con le API REST e alimenta l'agenda, le notifiche e le liste delle anagrafiche.【F:client/src/main/java/com/example/GestoreAgenti/fx/controller/dashboard/DashboardController.java†L1-L120】【F:client/src/main/java/com/example/GestoreAgenti/fx/command/ShowPaneCommand.java†L1-L34】【F:client/src/main/java/com/example/GestoreAgenti/fx/data/FxDataService.java†L1-L180】
Questa documentazione funge da riferimento rapido per comprendere come i requisiti funzionali si mappano sulle componenti software già presenti, facilitando l'estensione del gestionale con nuove funzionalità.
