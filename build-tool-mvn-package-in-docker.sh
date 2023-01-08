#!/usr/bin/env bash
# Purpose: Run the mvn clean install inside a container to mimic the BitBucketPipeline build
# Performance metrics:
#   Native:
#      mvn clean package -DskipTests -DskipITs
#      190.33s user 29.02s system 111% cpu 3:16.62 total
#   In this container:
#       ./build-tool-mvn-package-in-docker.sh
#       1.31s user 2.06s system 0% cpu 12:53.97 total
# Usage:
#    ./build-tool-mvn-package-in-docker.sh

docker run --interactive --tty --env ANTONYCC_REPOSITORY_GITHUB_PAT="${ANTONYCC_REPOSITORY_GITHUB_PAT}" --mount type=bind,source="$(pwd)",target=/workspace maven:3.6-openjdk-11-slim bash -c 'cd /workspace && mvn --settings settings.xml package -DskipTests -DskipITs --also-make --projects=gb-web'
