#!/bin/bash
set -euo pipefail

echo "=== AuraFrameFX File Organization Cleanup ==="
echo
echo "Creating backups before migration..."
BACKUP_DIR="cleanup_backup_$(date +%s)"
mkdir -p "$BACKUP_DIR"

# Backup entire app/src/main/java structure
if [ -d "app/src/main/java" ]; then
    cp -r app/src/main/java "$BACKUP_DIR/app_java_backup"
    echo "✓ Backed up app/src/main/java"
fi

echo
echo "=== STEP 1: Remove wrong namespace files ==="
echo "-- Deleting com.aurakai.genesis/GenesisApplication.kt (correct version exists) --"
rm -f app/src/main/java/com/aurakai/genesis/GenesisApplication.kt
rmdir app/src/main/java/com/aurakai/genesis 2>/dev/null || true
rmdir app/src/main/java/com/aurakai 2>/dev/null || true
rmdir app/src/main/java/com 2>/dev/null || true
echo "✓ Removed wrong namespace GenesisApplication"

echo
echo "=== STEP 2: Move Kotlin files from java/ to kotlin/ ==="
echo "-- Creating kotlin directory structure if needed --"
mkdir -p app/src/main/kotlin/dev/aurakai/auraframefx

echo "-- Moving all .kt files to kotlin/ directory --"
# Find all .kt files in app/src/main/java and move them
fd -e kt . app/src/main/java -t f -E build -E generated -x bash -c '
    file="$1"
    # Extract path after app/src/main/java/
    relpath="${file#app/src/main/java/}"
    # Target in kotlin directory
    target="app/src/main/kotlin/$relpath"
    targetdir="$(dirname "$target")"
    mkdir -p "$targetdir"
    mv "$file" "$target"
    echo "  Moved: $relpath"
' _ {}

echo "✓ Moved all .kt files to kotlin/ directory"

echo
echo "=== STEP 3: Remove third-party source files (should be dependencies) ==="
rm -rf app/src/main/java/com/highcapable 2>/dev/null || true
echo "✓ Removed YukiHookAPI source files (should use Gradle dependency)"

echo
echo "=== STEP 4: Clean up duplicate ai.yml files ==="
echo "Keeping: data/api/ai.yml (canonical)"
echo "Removing: app/api/ai.yml (duplicate)"
rm -f app/api/ai.yml
echo "✓ Removed duplicate app/api/ai.yml"

echo "-- app/api/_fragments/ai.yml appears to be an actual fragment, keeping for now --"

echo
echo "=== STEP 5: Investigate root dev/ directory ==="
if [ -d "dev" ]; then
    echo "Contents of root dev/ directory:"
    fd . dev -t f
    echo
    echo "⚠️  This directory should probably be deleted or contents moved."
    echo "   Moving to backup for manual review..."
    mv dev "$BACKUP_DIR/root_dev_directory"
    echo "✓ Moved root dev/ to backup"
fi

echo
echo "=== STEP 6: Fix collab-canvas namespace ==="
echo "-- Updating HookEntry.kt namespace --"
if [ -f "collab-canvas/src/main/java/com/auraos/collab_canvas/HookEntry.kt" ]; then
    sed -i 's/^package com\.auraos\.collab_canvas/package dev.aurakai.auraframefx.collabcanvas/' \
        collab-canvas/src/main/java/com/auraos/collab_canvas/HookEntry.kt

    # Move to correct directory
    mkdir -p collab-canvas/src/main/kotlin/dev/aurakai/auraframefx/collabcanvas
    mv collab-canvas/src/main/java/com/auraos/collab_canvas/HookEntry.kt \
       collab-canvas/src/main/kotlin/dev/aurakai/auraframefx/collabcanvas/

    # Clean up old directories
    rmdir collab-canvas/src/main/java/com/auraos/collab_canvas 2>/dev/null || true
    rmdir collab-canvas/src/main/java/com/auraos 2>/dev/null || true
    rmdir collab-canvas/src/main/java/com 2>/dev/null || true

    echo "✓ Fixed collab-canvas namespace and moved file"
fi

echo
echo "=== STEP 7: Verify no .kt files remain in java/ directories ==="
kt_in_java=$(fd -e kt . app/src/main/java -t f -E build -E generated 2>/dev/null | wc -l)
echo "Kotlin files remaining in app/src/main/java: $kt_in_java"
if [ "$kt_in_java" -eq 0 ]; then
    echo "✓ All Kotlin files successfully moved"
else
    echo "⚠️  Some .kt files still in java/ directory"
    fd -e kt . app/src/main/java -t f -E build -E generated
fi

echo
echo "=== CLEANUP COMPLETE ==="
echo "Backup location: $BACKUP_DIR"
echo
echo "Next steps:"
echo "1. Run './gradlew clean' to clear build caches"
echo "2. Sync project in Android Studio"
echo "3. Fix any import statements if needed"
echo "4. Test build: './gradlew assembleDebug'"
echo
echo "If everything works, you can delete the backup:"
echo "  rm -rf $BACKUP_DIR"