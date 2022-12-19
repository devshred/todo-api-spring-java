#!/bin/bash

mode=$1

help() {
  echo "available modes: native, jvm"
}

if [ "$mode" == "native" ]; then
  ./mvnw -Pnative -DskipTests clean spring-boot:build-image
elif [ "$mode" == "jvm" ]; then
  ./mvnw clean package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t todo-api-spring-java:jvm .
elif [ -z "$mode" ]; then
  echo "mode not set"
  help
else
  echo "mode $mode unknown"
  help
fi
