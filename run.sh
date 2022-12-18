#!/bin/bash

mode=$1

CONTAINER_NAME=todo-api-spring-java

help() {
  echo "available modes: native, jvm"
}

wait_for_log() {
  _container_name=$1
  _needle=$2
  _seconds=0
  _timeout=60

  while true
  do
    logs=$(docker logs $_container_name 2>&1 | grep "$_needle")
    if [ -n "$logs" ]
    then
      echo $logs
      break
    fi

    if [ $_seconds -ge $_timeout ]
    then
      echo "timeout ${_timeout}s exceeded"
      break
    fi
    sleep 1
  done
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

docker run --rm -d \
    -e DB_HOST=db \
    -e DB_USER=todo \
    -e DB_PASS=todo \
    -e DB_NAME=todo \
    -e DB_PORT=5432 \
    --network=todo \
    -p "8080:8080" \
    --name ${CONTAINER_NAME}-${mode} \
    -m 512M \
    todo-api-spring-java:${mode}

wait_for_log ${CONTAINER_NAME}-${mode} "Started TodoApiApplication in"

mem_usage=$(docker stats ${CONTAINER_NAME}-${mode} --no-stream --format "{{ json .MemUsage }}" | jq . -r | awk '{print $1}')
echo "Memory usage: $mem_usage"
