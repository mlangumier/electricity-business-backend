#!/usr/bin/env bash

# Stop the script when an error occurs
set -e

# Set variables here
REPO_URL="https://github.com/mlangumier/electricity-business-backend.git"
APP_DIR="${APP_DIR:-$(basename "${REPO_URL%.git}")}"
BRANCH="${BRANCH:-main}"
PROFILE="${PROFILE:-prod}"

# Deployment steps
echo "Starting deployment procedure..."

# Checking if project exists or cloning repository
echo "[STEP-1] Checking project directory..."
if [[ ! -d "$APP_DIR/.git" ]]; then
  echo "Project doesn't exist. Cloning '$REPO_URL' into '$APP_DIR' ..."
  git clone "$REPO_URL" "$APP_DIR"
else
  echo "Repository already exists in $APP_DIR"
fi

cd "$APP_DIR"

# Pull latest changes
echo "[STEP-2] Switching to branch '$BRANCH' and pulling latest changes..."
git switch "$BRANCH"
git pull --ff-only

# Install dependencies & automatically run tests
echo "[STEP-3] Installing dependencies & running tests..."
./mvnw -B test
echo "Tests passed."

# Package the application
echo "[STEP-4] Packaging artifact (skip tests)..."
./mvnw -B -DskipTests package

# Store new JAR file in /releases folder and names it after timestamp
echo "[STEP-5] "
mkdir -p releases
NEW_JAR="$(ls -1 target/*.jar | head -n1)"
STAMP="$(date+%Y%m%d-%H%M%S)"
cp "$NEW_JAR" "releases/app-$STAMP}.jar"

# Shift symlink
if [[ -L releases/current.jar ]]; then
  OLD_TARGET="$(readlink -f releases/current.jar)"
  ln -sfn "$OLD_TARGET" releases/previous.jar
fi
ln -sfn "releases/app-$STAMP.jar" releases/current.jar

# Run the application
echo "[STEP-6] Starting: java -jar \"releases/current.jar\" --spring.profiles.active=$PROFILE"
echo "Press Ctrl+C to stop the application."
exec java -jar releases/current.jar --spring-profiles.active="$PROFILE"
