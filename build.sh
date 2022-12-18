#!/bin/bash

mode=$1

help() {
  echo "available modes: native, jvm"
}

if [ "$mode" == "native" ]; then
   ./mvnw -Pnative -DskipTests clean native:compile
  docker build -f src/main/docker/Dockerfile.native -t todo-api-spring-java:native .
  docker tag todo-api-spring-java:native registry.local:5000/todo-api-spring-java:native
  docker push registry.local:5000/todo-api-spring-java:native
elif [ "$mode" == "jvm" ]; then
  ./mvnw clean package -DskipTests
  docker build -f src/main/docker/Dockerfile.jvm -t todo-api-spring-java:jvm .
  docker tag todo-api-spring-java:jvm registry.local:5000/todo-api-spring-java:jvm
  docker push registry.local:5000/todo-api-spring-java:jvm
elif [ -z "$mode" ]; then
  echo "mode not set"
  help
else
  echo "mode $mode unknown"
  help
fi