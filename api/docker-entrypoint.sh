#!/bin/sh
set -e

if [ -z "$DB_PASSWORD" ]; then
  echo "ERROR: DB_PASSWORD is required"
  exit 1
fi

if [ -z "$PUBLIC_URL" ]; then
  echo "ERROR: PUBLIC_URL is required (the address you open Nightshift at)"
  exit 1
fi

case "$PUBLIC_URL" in
  https://*) SECURE_COOKIES=true ;;
  *) SECURE_COOKIES=false ;;
esac
export SECURE_COOKIES
export DB_HOST="${DB_HOST:-postgres}"

exec java -jar api.jar