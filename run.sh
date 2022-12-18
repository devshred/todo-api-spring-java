#!/bin/bash

mode=$1

help() {
  echo "available modes: native, jvm"
}

if [ -z "$mode" ]; then
  echo "mode not set"
  help
  exit 1
elif [ "$mode" != "native" ] && [ "$mode" != "jvm" ]; then
  echo "mode $mode unknown"
  help
  exit 1
fi

docker run --rm \
  --name "todo-api-spring-${mode}" \
  --network todo \
  -p 8080:8080 \
  todo-api-spring-java:$mode
