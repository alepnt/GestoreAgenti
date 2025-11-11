# Configurare SQL Server per GestoreAgenti

Il modulo server utilizza Microsoft SQL Server come database principale. Senza
alcuna configurazione aggiuntiva viene sfruttata l'autenticazione integrata di
Windows, ma sono disponibili anche un profilo dedicato alle credenziali SQL e un
database H2 in memoria per scenari di test rapido.

## Autenticazione integrata di Windows (impostazione predefinita)

Il file [`application.properties`](../server/src/main/resources/application.properties)
configura la connessione verso un'istanza locale di SQL Server sulla porta 1433
con `integratedSecurity=true` e `authenticationScheme=NativeAuthentication`.

1. Installa il driver Microsoft JDBC e assicurati che la libreria nativa
   `sqljdbc_auth.dll` sia nel `PATH` di sistema oppure aggiungila con
   `-Djava.library.path` quando avvii l'applicazione.
2. Avvia il server normalmente:

   ```powershell
   mvn spring-boot:run -pl server -am
   ```

Per collegarti a un'istanza remota o cambiare database sovrascrivi
`DB_URL`. Se hai bisogno di usare un driver diverso imposta `DB_DRIVER`.
Le proprietà `DB_USERNAME` e `DB_PASSWORD` possono rimanere vuote perché non
sono utilizzate dall'autenticazione integrata.

## Autenticazione SQL con credenziali dedicate

Se il tuo ambiente richiede username e password SQL, attiva il profilo Spring
Boot `sql`, che carica
[`application-sql.properties`](../server/src/main/resources/application-sql.properties)
con un login predefinito (`gestore_app` / `CambiaSubito!`).

```powershell
mvn spring-boot:run -pl server -am -Dspring-boot.run.profiles=sql
```

Puoi personalizzare le credenziali tramite `DB_USERNAME` e `DB_PASSWORD`, oppure
ridefinire completamente l'URL con `DB_URL`.

## Database H2 in memoria (per test veloci)

Se desideri avviare il server senza una dipendenza da SQL Server puoi usare H2
definendo esplicitamente l'URL e il driver via variabili d'ambiente, ad esempio:

```bash
export DB_URL="jdbc:h2:mem:gestoreagenti;MODE=MSSQLServer;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
export DB_DRIVER="org.h2.Driver"
export DB_USERNAME="sa"
mvn spring-boot:run -pl server -am
```

Ricorda che il contenuto del database H2 viene perso al riavvio dell'applicazione.

## Risoluzione dei problemi

- **Errore `Login failed for user 'gestore_app'`**: hai attivato il profilo `sql`
  ma il server non riconosce l'utente. Verifica `DB_USERNAME`/`DB_PASSWORD` o
  torna all'autenticazione integrata rimuovendo il profilo.
- **Errore `This driver is not configured for integrated authentication`**:
  il file `sqljdbc_auth.dll` non è stato trovato. Copialo in una cartella
  presente nel `PATH` o aggiungi `-Djava.library.path="C:\\path\\alla\\dll"`
  al comando di avvio.
