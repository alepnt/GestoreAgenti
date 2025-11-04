#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# Script di utilità che avvia l'intero stack "Gestore Agenti" in locale.
# 1. Avvia (se necessario) il backend Spring Boot in background.
# 2. Attende che la porta HTTP del server sia raggiungibile.
# 3. Avvia il client JavaFX richiedendo a Maven di risolvere le dipendenze
#    mancanti e di eseguire l'applicazione desktop.
# -----------------------------------------------------------------------------

# Termina lo script in caso di errori, variabili non inizializzate o pipe fallite.
set -euo pipefail

# -----------------------------------------------------------------------------
# Percorsi utilizzati durante l'esecuzione
# -----------------------------------------------------------------------------
# Directory che contiene questo script. Serve per calcolare i path relativi.
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Directory radice del progetto Maven (una cartella sopra a scripts/).
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
# Cartella che ospita i file di log e il PID del server lato backend.
LOG_DIR="${ROOT_DIR}/.desktop-launcher"
# File che contiene l'identificativo di processo (PID) del server backend.
SERVER_PID_FILE="${LOG_DIR}/server.pid"
# File di log in cui viene rediretto l'output del server.
SERVER_LOG_FILE="${LOG_DIR}/server.log"
# Porta su cui il server Spring Boot espone le API HTTP.
SERVER_PORT=8081

# Garantisce l'esistenza della directory per PID e log.
mkdir -p "${LOG_DIR}"

# Flag che ci permette di capire se il server è stato avviato da questo script.
started_server=false

# -----------------------------------------------------------------------------
# cleanup
# -----------------------------------------------------------------------------
# Gestisce la chiusura del server backend quando lo script termina.
cleanup() {
    if [[ "${started_server}" == true ]] && [[ -f "${SERVER_PID_FILE}" ]]; then
        local pid
        pid="$(cat "${SERVER_PID_FILE}")"
        if kill -0 "${pid}" 2>/dev/null; then
            echo "Arresto del server Gestore Agenti (PID ${pid})..."
            kill "${pid}" 2>/dev/null || true
            wait "${pid}" 2>/dev/null || true
        fi
        rm -f "${SERVER_PID_FILE}"
        echo "Server arrestato."
    fi
}

# Assicura che cleanup venga sempre eseguito (uscita normale o per errore).
trap cleanup EXIT

# -----------------------------------------------------------------------------
# ensure_server_running
# -----------------------------------------------------------------------------
# Avvia il server backend se non è già in esecuzione e attende la disponibilità
# della porta HTTP, così da evitare che il client tenti la connessione troppo
# presto.
ensure_server_running() {
    if [[ -f "${SERVER_PID_FILE}" ]]; then
        local pid
        pid="$(cat "${SERVER_PID_FILE}")"
        if kill -0 "${pid}" 2>/dev/null; then
            echo "Server già in esecuzione (PID ${pid})."
            return 0
        else
            echo "File PID obsoleto rilevato, lo elimino."
            rm -f "${SERVER_PID_FILE}"
        fi
    fi

    echo "Avvio del server Gestore Agenti in background..."
    (cd "${ROOT_DIR}" && ./mvnw -pl server spring-boot:run -DskipTests > "${SERVER_LOG_FILE}" 2>&1 &)
    local pid=$!
    echo "${pid}" > "${SERVER_PID_FILE}"
    started_server=true
    echo "Server avviato (PID ${pid}). Log: ${SERVER_LOG_FILE}"

    echo "Attendo la disponibilità della porta ${SERVER_PORT}..."
    for attempt in {1..40}; do
        if ! kill -0 "${pid}" 2>/dev/null; then
            echo "Il processo del server si è arrestato inaspettatamente. Controlla il log a ${SERVER_LOG_FILE}." >&2
            exit 1
        fi
        if python3 - "${SERVER_PORT}" <<'PY'
import socket
import sys
port = int(sys.argv[1])
with socket.socket() as sock:
    sock.settimeout(1)
    try:
        sock.connect(("127.0.0.1", port))
    except OSError:
        sys.exit(1)
    else:
        sys.exit(0)
PY
        then
            echo "Server pronto."
            return 0
        fi
        sleep 1
    done

    echo "La porta ${SERVER_PORT} non è risultata disponibile entro il tempo limite. Proseguo comunque."
    return 0
}

# -----------------------------------------------------------------------------
# main
# -----------------------------------------------------------------------------
# Punto di ingresso principale dello script.
main() {
    ensure_server_running

    echo "Avvio dell'interfaccia desktop Gestore Agenti..."
    (cd "${ROOT_DIR}" && ./mvnw -pl client -am javafx:run -DskipTests)
}

main "$@"
