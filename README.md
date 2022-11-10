# Todo-API based on Spring Boot 2.7 and Java
This project uses Spring Boot and Java to provide a simple todo-app API and is used for workshops.
It implements an [OpenAPI spec](src/main/resources/todo-spec.yaml) and can be tested with a [frontend based on Vue.js](https://github.com/devshred/todo-web).

## set up database
```shell
docker compose -f infrastructure/docker-compose.yaml up
```

## Run with Maven
```shell
mvn spring-boot:run
```

## test API
Use [HTTP-client](todo-endpoints.http) to test the API.
