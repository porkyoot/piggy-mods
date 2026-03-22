#!/bin/bash
./scripts/compile.sh
echo "Step 2: Launching Client with Fresh Dependencies..."
./gradlew :runClient
