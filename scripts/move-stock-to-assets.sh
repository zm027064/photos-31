#!/usr/bin/env bash
# Move stock images from res/stock to assets/stock and normalize filenames
# Usage: run from repository root (macOS / Linux)

set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
SRC="$ROOT_DIR/app/src/main/res/stock"
DST="$ROOT_DIR/app/src/main/assets/stock"

if [ ! -d "$SRC" ]; then
  echo "Source folder not found: $SRC"
  exit 1
fi

mkdir -p "$DST"

for f in "$SRC"/*; do
  [ -f "$f" ] || continue
  base=$(basename "$f")
  new=$(echo "$base" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9._-]/_/g' | sed 's/_\+/_/g')
  cp -f "$f" "$DST/$new"
  echo "Copied $base -> $new"
done

read -p "Remove original files under res/stock? (y/N) " remove
if [ "$remove" = "y" ] || [ "$remove" = "Y" ]; then
  rm -rf "$SRC"
  echo "Removed $SRC"
else
  echo "Left original files in place. You can remove them manually when ready."
fi

echo "Done. You can now run: ./gradlew clean assembleDebug"
