#!/usr/bin/env bash

set -euo pipefail

if [[ "${OSTYPE:-}" != linux* ]]; then
    echo "Questo script supporta solo ambienti desktop Linux compatibili con gli standard freedesktop.org." >&2
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
LAUNCHER_SCRIPT="${ROOT_DIR}/scripts/avvia-client.sh"
DESKTOP_TEMPLATE="${ROOT_DIR}/offline/gestore-agenti.desktop"
ICON_PATH="${ROOT_DIR}/offline/gestore-agenti.svg"

if [[ ! -f "${LAUNCHER_SCRIPT}" ]]; then
    echo "Lo script di avvio non esiste: ${LAUNCHER_SCRIPT}" >&2
    echo "Verifica di aver clonato correttamente il repository." >&2
    exit 1
fi

if [[ ! -f "${DESKTOP_TEMPLATE}" ]]; then
    echo "Il template .desktop non esiste: ${DESKTOP_TEMPLATE}" >&2
    exit 1
fi

if [[ ! -x "${LAUNCHER_SCRIPT}" ]]; then
    echo "Rendo eseguibile lo script di avvio..."
    chmod +x "${LAUNCHER_SCRIPT}"
fi

APPLICATIONS_DIR="${XDG_DATA_HOME:-${HOME}/.local/share}/applications"
DESKTOP_FILE="${APPLICATIONS_DIR}/gestore-agenti.desktop"

mkdir -p "${APPLICATIONS_DIR}"

escaped_root=${ROOT_DIR// /\\ }
sed "s#__PROJECT_ROOT__#${escaped_root}#g" "${DESKTOP_TEMPLATE}" > "${DESKTOP_FILE}"

if [[ -f "${ICON_PATH}" ]]; then
    chmod +r "${ICON_PATH}" || true
fi
chmod +x "${DESKTOP_FILE}" || true

echo "File desktop installato in: ${DESKTOP_FILE}"  
echo "L'icona utilizza: ${ICON_PATH}"  
echo "Ora puoi cercare 'Gestore Agenti Client' nel menu Applicazioni o trascinare la voce sul desktop."
