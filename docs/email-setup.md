# Configurare l'invio email reale

L'applicazione ora delega al modulo Spring Boot l'invio delle email tramite SMTP. Per usare la tua casella Outlook/Exchange (o un altro server compatibile) devi fornire le credenziali al backend e abilitare esplicitamente la funzionalità.

## 1. Imposta le proprietà del server

Apri `server/src/main/resources/application.properties` e sostituisci i valori placeholder con i dati del tuo provider:

```properties
mail.enabled=true
mail.override-sender=la-tua-mail@azienda.it
spring.mail.host=smtp.office365.com
spring.mail.port=587
spring.mail.username=la-tua-mail@azienda.it
spring.mail.password=la-tua-password-o-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

* `mail.enabled` deve essere `true` per abilitare l'invio reale.
* `mail.override-sender` è facoltativo ma consigliato se il server accetta un solo mittente (es. l'indirizzo aziendale principale). Se lo imposti, il backend userà sempre quell'indirizzo come mittente e imposterà il campo **Reply-To** con l'indirizzo originale inviato dal client.
* `spring.mail.*` deve puntare all'SMTP della tua organizzazione (per Outlook 365 i parametri in esempio sono validi nella maggior parte dei tenant; valuta l'uso di una password applicativa se l'account richiede MFA).

> Suggerimento: non commitare le credenziali. Crea un file `application-local.properties` ignorato da Git e avvia il server con `--spring.config.name=application,application-local`.

## 2. Riavvia il server

Una volta aggiornate le proprietà, ricostruisci e riavvia il modulo `server`:

```bash
mvn -pl server -am spring-boot:run
```

Assicurati che la JVM abbia accesso a Internet o alla rete aziendale per contattare l'SMTP.

## 3. Invia email dal client

Apri il client JavaFX, accedi con un account demo (o registrane uno nuovo) e invia un messaggio dalla sezione **Email**. Il client contatta l'endpoint `POST /api/email` del backend, che inoltra il messaggio tramite SMTP.

Se il backend risponde con un errore (es. credenziali errate, server irraggiungibile), il messaggio di errore viene mostrato direttamente nell'etichetta di stato della GUI.

## Troubleshooting

| Problema | Possibile causa | Soluzione |
| --- | --- | --- |
| `L'invio email è disabilitato...` | Hai dimenticato `mail.enabled=true` | Aggiorna `application.properties` o un profilo attivo |
| `Invio email fallito: AuthenticationFailedException` | Credenziali SMTP sbagliate o account senza password app | Verifica username/password o genera una password applicativa |
| `Invio email fallito (HTTP 502)` | Il server SMTP non è raggiungibile | Controlla rete, firewall e parametri `spring.mail.host/port` |

Con questa configurazione la dashboard invia davvero le email usando il tuo account aziendale, senza più simulazioni lato client.
