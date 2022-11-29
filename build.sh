#!/bin/bash

mode=$1

help() {
  echo "available modes: native, jit"
}

if [ "$mode" == "native" ]; then
   ./mvnw -Pnative -DskipTests clean native:compile
  docker build -f src/main/docker/Dockerfile.native -t todo-api-spring-java:native .
  docker tag todo-api-spring-java:native registry.local:5000/todo-api-spring-java:native
  docker push registry.local:5000/todo-api-spring-java:native
elif [ "$mode" == "jit" ]; then
  ./mvnw clean package -DskipTests
  docker build -f src/main/docker/Dockerfile.jit -t todo-api-spring-java:jit .
  docker tag todo-api-spring-java:jit registry.local:5000/todo-api-spring-java:jit
  docker push registry.local:5000/todo-api-spring-java:jit
elif [ -z "$mode" ]; then
  echo "mode not set"
  help
else
  echo "mode $mode unknown"
  help
fi