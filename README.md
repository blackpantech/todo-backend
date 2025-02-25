# todo-backend

Todo Backend for Java 21, Spring Boot 3.4.2, JPA and MapStruct.

This is an implementation for Todo Backend: https://www.todobackend.com/ made with hexagonal architecture and TDD.

There are 2 profiles to launch the project with.

## 'dev' profile
Uses H2 database. Creates and drops database for each runtime.

## 'prod' profile
Connects to a PostgresSQL database.
