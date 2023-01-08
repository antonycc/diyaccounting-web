#!/usr/bin/env bash
# Purpose: Build and a static site
# Usage: build.sh [--clean|--purge]
# Examples:
# $ source ./github-antonycc-keys.sh
# $ ./build.sh
# $ ./build.sh --clean
# $ ./build.sh --purge
clean="${1:-false}" ;
purge="${1:-false}" ;

# Clean-up of java components (not needed if running on a fresh container)
if [[ "$clean" = "--clean" || "$purge" = "--purge" ]]; then
  rm -rf './target' ;
  mvn ./mvnw --settings settings.xml clean install spring-boot:build-image ;
fi

# Extract app image name using the Maven version
mvn help:evaluate -Dexpression=project.version ;
POM_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v "^\[" | tail -1 ) ; echo "${POM_VERSION?}" ;
export APP_IMAGE="diyaccounting-web:${POM_VERSION?}" ; echo "${APP_IMAGE?}" ;

# Clean-up of docker elements (not needed if running on a fresh container)
docker compose down --remove-orphans ;
if [ "$purge" = "--purge" ]; then
  docker system prune --all --volumes --force ;
fi

# Build java components and move resources to target/classes
mvn install ;

# Build docker images to create a local cluster
mkdir -p './target/mirror'
docker compose build --no-cache --pull ;
docker compose up --force-recreate --detach --wait ;
# until docker compose logs --tail="all" | grep -e 'web application archive .* has finished' ; do echo "Waiting..." ; sleep 1 ; done ;
docker compose logs --tail="all" --follow ;
