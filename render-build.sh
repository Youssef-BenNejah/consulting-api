#!/usr/bin/env bash
# exit on error
set -o errexit

# For Maven
./mvnw clean install -DskipTests

# OR for Gradle (uncomment if using Gradle)
# ./gradlew clean build -x test