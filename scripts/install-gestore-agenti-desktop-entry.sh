#!/usr/bin/env bash

set -euo pipefail

if [[ "${OSTYPE:-}" != linux* ]]; then
    echo "Questo script supporta solo ambienti desktop Linux compatibili con gli standard freedesktop.org." >&2
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
LAUNCHER_SCRIPT="${ROOT_DIR}/scripts/launch-gestore-agenti.sh"

if [[ ! -x "${LAUNCHER_SCRIPT}" ]]; then
    echo "Rendo eseguibile lo script di avvio..."
    chmod +x "${LAUNCHER_SCRIPT}"
fi

APPLICATIONS_DIR="${XDG_DATA_HOME:-${HOME}/.local/share}/applications"
DESKTOP_FILE="${APPLICATIONS_DIR}/gestore-agenti.desktop"

mkdir -p "${APPLICATIONS_DIR}"

escaped_exec_path="${LAUNCHER_SCRIPT// /\\ }"

cat > "${DESKTOP_FILE}" <<EOF_DESKTOP
[Desktop Entry]
Type=Application
Version=1.0
Name=Gestore Agenti
Comment=Avvia il server demo e l'applicazione desktop Gestore Agenti
Exec=${escaped_exec_path}
Terminal=true
Categories=Office;Utility;
EOF_DESKTOP

chmod +x "${DESKTOP_FILE}"

echo "File desktop installato in: ${DESKTOP_FILE}"
echo "Ora puoi cercare \"Gestore Agenti\" nel menu Applicazioni o creare un collegamento sul desktop."
