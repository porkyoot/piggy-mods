#!/bin/bash
# scripts/fix_gradle.sh
# Robust build script for Piggy Mods
set -e

echo "=== Piggy Mods Robust Builder ==="

# 0. Clean Stale Mods
# Remove any piggy-* jars from run/mods to prevent them from overriding the dev classpath.
echo "Step 0: Cleaning Stale JARs and Gradle Cache..."
rm -f run/mods/piggy-*.jar
rm -rf .gradle/loom-cache

echo "Step 1: Cleaning and Bootstrapping piggy-lib..."
./gradlew :piggy-lib:clean :piggy-lib:assemble -Pbootstrap

echo "Step 2: Building everything else with dependencies..."
./gradlew assemble
