# Configurare SQL Server per GestoreAgenti

L'applicazione server utilizza Microsoft SQL Server come database principale. Di
seguito trovi le opzioni di connessione supportate e come selezionarle a seconda
che tu voglia autenticarti con credenziali SQL dedicate oppure sfruttare
l'account di Windows dell'utente corrente.

## Autenticazione SQL (impostazione predefinita)

Il file [`application.properties`](../server/src/main/resources/application.properties)
usa un login SQL chiamato `gestore_app` con password `CambiaSubito!` e si connette
per default all'istanza locale sulla porta 1433 tramite il driver Microsoft
ufficiale. Puoi personalizzare questi valori impostando le variabili d'ambiente:

- `DB_URL` per cambiare completamente l'URL JDBC (host, porta, database, ecc.).
- `DB_USERNAME` e `DB_PASSWORD` per indicare altre credenziali SQL.
- `DB_DRIVER` se devi usare un driver JDBC alternativo.
- `JPA_DIALECT` nel caso tu stia lavorando con un database diverso da SQL Server.

Esempio su Windows PowerShell:

```powershell
$env:DB_USERNAME = "mio_utente"
$env:DB_PASSWORD = "mia_password_sicura"
mvn spring-boot:run -pl server -am
```

## Autenticazione integrata di Windows

Se il tuo server SQL accetta soltanto l'autenticazione di Windows, attiva il
profilo Spring Boot `windows` che imposta l'URL JDBC con `integratedSecurity=true`
(e mantiene driver e dialetto SQL Server di default).

1. Assicurati che la libreria nativa `sqljdbc_auth.dll` fornita dal driver
   Microsoft JDBC sia disponibile nel `PATH` di sistema oppure aggiungila alla
   proprietà di JVM `-Djava.library.path`.
2. Avvia l'applicazione specificando il profilo:

```powershell
mvn spring-boot:run -pl server -am -Dspring-boot.run.profiles=windows
```

Puoi comunque personalizzare l'URL con `DB_URL` nel caso tu debba collegarti a
un'istanza remota o cambiare il nome del database. Quando il profilo `windows`
è attivo le proprietà `DB_USERNAME` e `DB_PASSWORD` vengono ignorate.

## Risoluzione dei problemi

- **Errore `Login failed for user 'gestore_app'`**: il server SQL non riconosce
  l'utente configurato. Verifica le credenziali oppure attiva il profilo
  `windows` se devi usare l'autenticazione integrata.
- **Errore `This driver is not configured for integrated authentication`**:
  il file `sqljdbc_auth.dll` non è stato trovato. Copialo in una cartella
  presente nel `PATH` o aggiungi `-Djava.library.path="C:\\path\\alla\\dll"`
  al comando di avvio.
