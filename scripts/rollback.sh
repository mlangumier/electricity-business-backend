#!/usr/bin/env bash

# Stops the script when an error occurs
set -e

PROFILE="${PROFILE:-prod}"

cd "$(dirname "$0")"

if [[ ! -L releases/previous.jar ]]; then
  echo "No \"previous.jar\" to roll back to." >&2
  exit 1
fi

CURRENT="$(readlink -f releases/current.jar || true)"
PREVIOUS="$(readlink -f releases/previous.jar)"

# Swap symlinks
ln -sfn "$PREVIOUS" releases/current.jar
ln -sfn "$CURRENT" releases/previous.jar || true

# Run the application
echo "Rollback successful"
echo "Starting: java -jar \"releases/current.jar\" --spring.profiles.active=$PROFILE"
echo "Press Ctrl+C to stop the application."
exec java -jar releases/current.jar --spring-profiles.active="$PROFILE"