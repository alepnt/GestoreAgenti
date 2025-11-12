# Verifica dell'autenticazione

Questa guida spiega come controllare rapidamente che la correzione applicata al
JWT consenta il login sia lato backend sia lato client.

## 1. Avviare solo il test unitario

Dopo aver popolato la cache Maven locale (vedi [offline-build](./offline-build.md)),
puoi lanciare il test che riproduce l'errore segnalato e verifica che la nuova
logica di `JwtUtil` generi un token valido anche quando il segreto configurato è
più corto di 256 bit:

```bash
./mvnw -pl server -Dtest=JwtUtilTest test -o -Dmaven.repo.local=/workspace/.m2/repository
```

L'esecuzione termina con **BUILD SUCCESS** quando la rigenerazione della chiave
funziona correttamente. Un `BUILD FAILURE` accompagnato da `Network is
unreachable` indica che la cache non è stata importata.

## 2. Smoke test manuale del login

1. Avvia il backend dal progetto `server`:
   ```bash
   ./mvnw -pl server spring-boot:run -DskipTests
   ```
2. In un secondo terminale esegui il client JavaFX:
   ```bash
   ./mvnw -pl client -am javafx:run -DskipTests
   ```
3. Inserisci le credenziali di un utente presente nel database (di default
   `admin` / `password`). Se l'autenticazione va a buon fine la dashboard si apre
   e nel log del server appare una riga `INFO` emessa dal controller di login con
   lo status `200`. In caso di errore comparirà uno `status=401`.

## 3. Conferma del token lato REST

Per un riscontro rapido senza il client grafico puoi usare `curl` contro
l'endpoint REST di login:

```bash
curl -X POST http://localhost:8081/api/auth/login \
     -H 'Content-Type: application/json' \
     -d '{"username":"admin","password":"password"}'
```

La risposta corretta contiene il campo `token`. Se il campo è presente e non
vuoto, il client riceverà lo stesso token e potrà accedere alla dashboard.
