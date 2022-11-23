# Todo-API based on Spring Boot 3.0 and Java
This project uses Spring Boot and Java to provide a simple todo-app API and is used for workshops.
It implements an [OpenAPI spec](src/main/resources/todo-spec.yaml) and can be tested with a [frontend based on Vue.js](https://github.com/devshred/todo-web).

## set up database

```shell
docker compose -f infrastructure/docker-compose.yaml up
```

## run with Maven

```shell
mvn spring-boot:run
```

## run with AOT generated code

```shell
./mvnw clean package -DskipTests -Pnative
java -Dspring.aot.enabled=true -jar target/todo-api-spring-java-0.0.1-SNAPSHOT.jar
```

## build and run native app

```shell
./mvnw -Pnative -DskipTests clean native:compile
./target/todo-api-spring-java
```

## build docker image with native app (using Dockerfile)

```shell
./mvnw -Pnative -DskipTests clean native:compile
docker build -f src/main/docker/Dockerfile.native -t todo-api-spring-java:native .
```

## build docker image with native app (using buildpacks)

```shell
./mvnw -Pnative -DskipTests clean spring-boot:build-image
```

## test API

Use [HTTP-client](todo-endpoints.http) to test the API.
