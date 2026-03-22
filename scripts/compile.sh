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

# 1. Clean and Bootstrap Build
# We use -Pbootstrap to tell the root project NOT to try adding dependencies yet.
# This allows 'assemble' to run on subprojects without the root project crashing due to missing JARs.
echo "Step 1: Cleaning and Bootstrapping Subprojects..."
./gradlew clean assemble -Pbootstrap
