#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

DEFAULT_ARCHIVE="${PROJECT_ROOT}/offline/maven-repo.tar.gz"
DEFAULT_TARGET="/workspace/.m2/repository"
if [[ ! -d /workspace ]]; then
    DEFAULT_TARGET="${HOME}/.m2/repository"
fi

usage() {
    cat <<EOF
Usage: scripts/install-maven-cache.sh [<archive-path> [<target-repository>]]

Extract a Maven repository archive (created by prime-maven-cache.sh) into the
current machine so Maven can run fully offline.

Arguments:
  <archive-path>      Path of the tar.gz archive to extract.
                      Defaults to ${DEFAULT_ARCHIVE}.
  <target-repository> Directory that will contain the extracted repository.
                      Defaults to ${DEFAULT_TARGET}.
EOF
}

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
    usage
    exit 0
fi

ARCHIVE_PATH="${1:-${DEFAULT_ARCHIVE}}"
TARGET_DIR="${2:-${DEFAULT_TARGET}}"

if [[ ! -f "${ARCHIVE_PATH}" ]]; then
    echo "Archive not found: ${ARCHIVE_PATH}" >&2
    exit 1
fi

echo "==> Extracting ${ARCHIVE_PATH}"
mkdir -p "$(dirname "${TARGET_DIR}")"

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TMP_DIR}"' EXIT

tar -C "${TMP_DIR}" -xzf "${ARCHIVE_PATH}"

if [[ ! -d "${TMP_DIR}/repository" ]]; then
    echo "Archive ${ARCHIVE_PATH} does not contain a 'repository' directory" >&2
    exit 1
fi

echo "==> Populating ${TARGET_DIR}"
rm -rf "${TARGET_DIR}"
mv "${TMP_DIR}/repository" "${TARGET_DIR}"

echo "==> Maven cache installed at ${TARGET_DIR}"
