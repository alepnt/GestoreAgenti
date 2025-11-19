# Avvio dal desktop di Gestore Agenti

Questa guida descrive come installare un collegamento nel menu applicazioni (o sul desktop) per aprire direttamente la demo **Gestore Agenti** nelle build desktop del modulo `client`.

## Prerequisiti comuni

- Java 21 o superiore e Maven Wrapper (`./mvnw`) funzionante, come già richiesto dal progetto.
- Il pacchetto desktop generato dal modulo client tramite:
  ```bash
  ./mvnw -pl client -Pdesktop clean package -DskipTests
  ```
  Il comando produce l'immagine jlink in `client/target/agent-manager-app/` con il launcher `bin/AgentManagerApp`.

## Linux (desktop entry freedesktop)

1. Verifica di avere completato il packaging desktop del client (vedi prerequisiti). Lo script userà `client/target/agent-manager-app/bin/AgentManagerApp`.
2. Installa il collegamento eseguendo:
   ```bash
   scripts/install-gestore-agenti-desktop-entry.sh
   ```
3. Cerca "Gestore Agenti Client" nel menu Applicazioni o trascina l'icona sul desktop. Il file `.desktop` viene installato in `~/.local/share/applications/` e punta allo script [`scripts/avvia-client.sh`](../scripts/avvia-client.sh). L'icona viene letta da `offline/gestore-agenti.svg`.

Lo script [`scripts/avvia-client.sh`](../scripts/avvia-client.sh) lancia direttamente l'eseguibile creato dal profilo `desktop`, senza avviare il server backend.

## Windows (script e collegamento sul desktop)

1. Genera l'immagine desktop del client con Maven sullo stesso percorso indicato nei prerequisiti.
2. Copia lo script [`scripts/avvia-client.bat`](../scripts/avvia-client.bat) nella macchina di destinazione insieme alla cartella `client/target/agent-manager-app/`.
3. Crea il collegamento sul desktop con PowerShell (eseguito nel repository):
   ```powershell
   $shell = New-Object -ComObject WScript.Shell
   $shortcut = $shell.CreateShortcut("$env:USERPROFILE\Desktop\Gestore Agenti Client.lnk")
   $shortcut.TargetPath = "$PWD\scripts\avvia-client.bat"
   $shortcut.WorkingDirectory = "$PWD"
   # Opzionale: per un'icona personalizzata crea un file `.ico` (ad es. convertendo `offline/gestore-agenti.svg`)
   # e decommenta la riga seguente impostando il percorso dell'icona.
   # $shortcut.IconLocation = "$PWD\offline\gestore-agenti.ico"
   $shortcut.Save()
   ```
   In alternativa, crea manualmente un collegamento che punti a `scripts\avvia-client.bat`.

## macOS (immagine .app tramite jpackage)

1. Su macOS con Xcode command line tools e `jpackage` disponibili, esegui:
   ```bash
   ./mvnw -pl client -Pdesktop,macos-app clean package -DskipTests
   ```
   Il profilo `macos-app` crea `target/agent-manager-app` (jlink) e un bundle `.app` nella stessa cartella usando `jpackage`.
2. Trascina il file `.app` generato in `/Applications` per installarlo. Lo start avvia direttamente il client JavaFX offline.

## Troubleshooting comune

- **Permesso negato sugli script**: esegui `chmod +x scripts/avvia-client.sh` e, se necessario, `chmod +x client/target/agent-manager-app/bin/AgentManagerApp`.
- **Dipendenze JavaFX mancanti**: assicurati che la build desktop sia stata creata con `./mvnw -pl client -Pdesktop clean package -DskipTests` e che l'immagine `agent-manager-app` sia presente. Su Linux alcune distribuzioni richiedono pacchetti `libgtk`/`libglib` aggiornati per JavaFX.
- **Percorsi con spazi**: il template `.desktop` sostituisce automaticamente gli spazi con escape (`\ `); nei collegamenti Windows assicurati che i percorsi siano racchiusi fra virgolette.
