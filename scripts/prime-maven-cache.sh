#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

usage() {
    cat <<'EOF'
Usage: scripts/prime-maven-cache.sh [<output-directory> [<archive-path>]]

Prime a standalone Maven repository containing all of the dependencies that the
GestoreAgenti project needs, then package it into a tarball. Run this script on
an online machine and copy the resulting archive to the offline environment.

Arguments:
  <output-directory>  Directory where the populated repository will be written.
                      Defaults to "offline" at the project root.
  <archive-path>      Path of the tar.gz archive to create. Defaults to
                      "offline/maven-repo.tar.gz".
EOF
}

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
    usage
    exit 0
fi

OUTPUT_DIR="${1:-${PROJECT_ROOT}/offline}"
ARCHIVE_PATH="${2:-${OUTPUT_DIR%/}/maven-repo.tar.gz}"
REPO_DIR="${OUTPUT_DIR%/}/repository"

echo "==> Preparing clean repository under ${REPO_DIR}"
rm -rf "${REPO_DIR}"
mkdir -p "${REPO_DIR}"

function go_offline() {
    local module="$1"
    echo "==> Prefetching dependencies for module '${module}'"
    (cd "${PROJECT_ROOT}" && ./mvnw -pl "${module}" -am dependency:go-offline -DskipTests -Dmaven.repo.local="${REPO_DIR}")
}

go_offline server
go_offline client

echo "==> Creating archive ${ARCHIVE_PATH}"
mkdir -p "$(dirname "${ARCHIVE_PATH}")"
tar -C "${OUTPUT_DIR}" -czf "${ARCHIVE_PATH}" repository

echo "==> Maven repository primed at ${REPO_DIR}"
echo "==> Archive ready at ${ARCHIVE_PATH}"
