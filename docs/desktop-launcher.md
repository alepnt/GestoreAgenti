# Avvio dal desktop di Gestore Agenti

Questa guida descrive come installare un collegamento nel menu applicazioni (o sul desktop) per aprire direttamente la demo **Gestore Agenti**. Il lancio automatico si occupa di avviare sia il server Spring Boot sia l'interfaccia JavaFX.

## Prerequisiti

- Sistema operativo Linux con ambiente desktop compatibile con gli standard freedesktop.org.
- Java 21 o superiore e Maven Wrapper (`./mvnw`) funzionante, come già richiesto dal progetto.

## Passaggi rapidi

1. Apri un terminale nella cartella del repository.
2. Esegui lo script di installazione:
   ```bash
   scripts/install-gestore-agenti-desktop-entry.sh
   ```
3. Cerca "Gestore Agenti" nel menu Applicazioni e trascina la voce sul desktop se desideri un collegamento visibile.

Lo script crea il file `gestore-agenti.desktop` in `~/.local/share/applications/` puntando allo script di avvio del progetto.

## Cosa fa lo script di avvio

Lo script [`scripts/launch-gestore-agenti.sh`](../scripts/launch-gestore-agenti.sh) esegue le seguenti operazioni:

- Verifica se esiste già un server avviato tramite il PID salvato nella cartella `.desktop-launcher/` del progetto.
- Se necessario avvia il server Spring Boot (`./mvnw -pl server spring-boot:run -DskipTests`) in background, salvando log e PID.
- Attende che la porta `8081` risulti raggiungibile.
- Avvia il client JavaFX (`./mvnw -pl client -am javafx:run -DskipTests`).
- Arresta il server al termine **solo** se era stato avviato dallo script (per non interrompere un'istanza lanciata manualmente).

I log del server vengono salvati in `.desktop-launcher/server.log` per facilitare la diagnostica in caso di problemi d'avvio.

## Personalizzazioni facoltative

- Per mostrare un'icona personalizzata è sufficiente aggiungere un file PNG nel percorso del progetto e modificare la chiave `Icon=` dentro `~/.local/share/applications/gestore-agenti.desktop`.
- Se preferisci non visualizzare un terminale durante l'avvio, puoi cambiare la riga `Terminal=true` in `Terminal=false` nel file `.desktop` generato.

## Rimozione

Per rimuovere il collegamento elimina il file `~/.local/share/applications/gestore-agenti.desktop` e, se presente, la cartella `.desktop-launcher/` creata nel repository.

## Creare un'immagine desktop auto-contenuta

Per generare un'immagine runtime del client JavaFX con tutte le risorse FXML/CSS presenti in `client/src/main/resources/fx/` usa il profilo Maven `desktop` dedicato:

```bash
mvn -pl client -Pdesktop clean package
```

Il comando produce un'immagine jlink pronta all'uso in `client/target/agent-manager-app/` e un archivio compresso in `client/target/agent-manager-app.zip`, utili per distribuire il launcher desktop senza richiedere una JRE preinstallata.
