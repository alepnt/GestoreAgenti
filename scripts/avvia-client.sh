#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
IMAGE_DIR="${ROOT_DIR}/client/target/agent-manager-app"
LAUNCHER_BIN="${IMAGE_DIR}/bin/AgentManagerApp"

if [[ ! -f "${LAUNCHER_BIN}" ]]; then
    echo "L'eseguibile del client non è stato trovato." >&2
    echo "Assicurati di aver eseguito il packaging con:" >&2
    echo "  ./mvnw -pl client -Pdesktop clean package -DskipTests" >&2
    exit 1
fi

if [[ ! -x "${LAUNCHER_BIN}" ]]; then
    chmod +x "${LAUNCHER_BIN}" 2>/dev/null || true
fi

exec "${LAUNCHER_BIN}" "$@"
