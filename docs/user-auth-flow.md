# Flusso di autenticazione e registrazione

Questa nota descrive il percorso che un utente segue dall'apertura del client
fino all'accesso alla propria dashboard, con particolare attenzione alla
validazione dei dati di login e registrazione e alla persistenza sul server.

## Apertura del client e schermata iniziale

All'avvio dell'applicazione desktop JavaFX viene mostrata la schermata di
login, gestita da `LoginController`. Da qui è possibile sia inserire le
credenziali, sia aprire la finestra di registrazione di un nuovo agente.【F:client/src/main/java/com/example/GestoreAgenti/fx/controller/LoginController.java†L40-L124】

## Login tramite email e password

Il form di login richiede l'inserimento di email, cognome, identificativo
agente e password. Prima di contattare i servizi applicativi vengono eseguiti i
controlli lato interfaccia:

- L'email deve essere valorizzata e contenere il carattere `@`.
- La password deve includere almeno una lettera maiuscola, una minuscola, un
  numero e un carattere speciale.

In caso di esito positivo viene invocato il metodo `authenticate` del
`FxDataService`, che restituisce l'agente associato alle credenziali valide e
apre la dashboard.【F:client/src/main/java/com/example/GestoreAgenti/fx/controller/LoginController.java†L66-L118】

## Registrazione di un nuovo agente

La finestra di registrazione consente di inserire nome e cognome, ruolo,
team, email e password. Il pulsante "Registrati" rimane disabilitato finché i
campi obbligatori non sono completi e le regole di validazione (email con `@`
e password robusta) non sono soddisfatte. Se i dati non rispettano i vincoli
viene mostrato un messaggio di errore dedicato.【F:client/src/main/java/com/example/GestoreAgenti/fx/controller/LoginController.java†L126-L211】

Quando i dati sono validi, il `FxDataService` normalizza le informazioni,
genera un nuovo identificativo agente se necessario e richiede al backend la
creazione dell'account. Anche lato servizio viene verificato che l'email
contenga `@` e che la password rispetti i criteri di complessità prima di
procedere con la registrazione.【F:client/src/main/java/com/example/GestoreAgenti/fx/data/FxDataService.java†L576-L625】【F:client/src/main/java/com/example/GestoreAgenti/fx/data/FxDataService.java†L757-L771】

## Persistenza sul database SQL Server

Sul backend Spring Boot l'API `UtenteService` gestisce la creazione degli
account persistendo l'hash della password e garantendo che ogni utente abbia un
ruolo e, se previsto, l'associazione con il dipendente. Il servizio codifica la
password con BCrypt e salva i dati nel database SQL Server tramite il
`UtenteRepository`. L'ID primario è generato automaticamente dal database, in
linea con il requisito di assegnazione automatica dell'identificativo
utente.【F:server/src/main/java/com/example/GestoreAgenti/service/UtenteService.java†L38-L141】【F:server/src/main/java/com/example/GestoreAgenti/model/Utente.java†L19-L54】

## Accesso alla dashboard

Una volta completata l'autenticazione, il `LoginController` carica la scena
della dashboard e passa al controller dedicato le informazioni dell'agente. Da
qui l'utente può consultare agenda, notifiche e le altre funzionalità del
sistema.【F:client/src/main/java/com/example/GestoreAgenti/fx/controller/LoginController.java†L94-L124】
