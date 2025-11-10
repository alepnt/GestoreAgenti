# Configurare SQL Server per GestoreAgenti

Di default il backend utilizza un database H2 locale, così puoi avviare l'applicazione
senza predisporre un'istanza di Microsoft SQL Server. I dati vengono salvati nel
percorso `server/target/h2/gestoreagenti` e rimangono disponibili tra un avvio e
l'altro.

Se vuoi collegarti a SQL Server sono disponibili due profili Spring Boot dedicati,
uno basato su credenziali SQL e l'altro sull'autenticazione integrata di Windows.

## Autenticazione SQL (`mssql`)

Attiva il profilo `mssql` per riutilizzare la configurazione classica con un login
SQL. Il file [`application-mssql.properties`](../server/src/main/resources/application-mssql.properties)
imposta un utente predefinito `gestore_app` con password `CambiaSubito!`, che puoi
personalizzare tramite variabili d'ambiente:

- `DB_URL` per cambiare completamente l'URL JDBC (host, porta, database, ecc.).
- `DB_USERNAME` e `DB_PASSWORD` per indicare altre credenziali SQL.

Esempio su Windows PowerShell:

```powershell
$env:DB_USERNAME = "mio_utente"
$env:DB_PASSWORD = "mia_password_sicura"
mvn spring-boot:run -pl server -am -Dspring-boot.run.profiles=mssql
```

## Autenticazione integrata di Windows (`windows`)

Se il tuo server SQL accetta soltanto l'autenticazione di Windows, attiva il
profilo Spring Boot `windows`, che include automaticamente la configurazione
`mssql` ma forza l'uso di `integratedSecurity=true` (vedi
[`application-windows.properties`](../server/src/main/resources/application-windows.properties)).

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
