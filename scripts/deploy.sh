#!/usr/bin/env bash

# Stop the script when an error occurs
set -e

# Set variables here
REPO_URL="https://github.com/mlangumier/electricity-business-backend.git"
APP_DIR="${APP_DIR:-$(basename "${REPO_URL%.git}")}"
BRANCH="${BRANCH:-main}"
PROFILE="${PROFILE:-prod}"

# Deployment steps
echo "[0/6] Starting deployment procedure..."

echo "[1/5] Checking project directory..."
if [[ ! -d "$APP_DIR/.git" ]]; then
  echo "Project doesn't exist. Cloning '$REPO_URL' into '$APP_DIR' ..."
  git clone "$REPO_URL" "$APP_DIR"
else
  echo "Repository already exists in $APP_DIR"
fi

cd "$APP_DIR"

echo "[2/5] Switching to branch '$BRANCH' and pulling latest changes..."
git switch "$BRANCH"
git pull --ff-only

echo "[3/5] Installing dependencies & running tests..."
./mvnw -B test
echo "[3/5] Tests passed."

echo "[4/5] Packaging artifact (skip tests)..."
./mvnw -B -DskipTests package

echo "[5/5] Run the application JAR with profile=$PROFILE..."
#mvn -q spring-boot-run -Dspring-boot.run.profiles="${PROFILE}"
JAR_PATH="$(ls -1 target/*.jar | head -n1)"
if [[ -z "${JAR_PATH}" ]]; then
  echo "No JAR found in 'target/.'. Check packaging step." >&2
  exit 1
fi

echo "Starting: java -jar \"$JAR_PATH\" --spring.profiles.active=$PROFILE"
echo "Press Ctrl+C to stop the application."
exec java -jar "$JAR_PATH" --spring-profiles.active="$PROFILE"
