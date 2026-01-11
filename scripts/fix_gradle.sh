#!/bin/bash
# scripts/fix_gradle.sh
# Robust build script for Piggy Mods
set -e

echo "=== Piggy Mods Robust Builder ==="

# 0. Clean Stale Mods
# Remove any piggy-* jars from run/mods to prevent them from overriding the dev classpath.
echo "Step 0: Cleaning Stale JARs from run/mods..."
rm -f run/mods/piggy-*.jar

# 1. Clean and Bootstrap Build
# We use -Pbootstrap to tell the root project NOT to try adding dependencies yet.
# This allows 'assemble' to run on subprojects without the root project crashing due to missing JARs.
echo "Step 1: Cleaning and Bootstrapping Subprojects..."
./gradlew clean assemble -Pbootstrap

# 2. Final Client Launch
# Now that subprojects are built (JARs exist in build/libs), we run the normal build.
# The conditional logic in build.gradle will see the JARs and correctly add them as dependencies.
echo "Step 2: Launching Client with Fresh Dependencies..."
./gradlew :runClient
