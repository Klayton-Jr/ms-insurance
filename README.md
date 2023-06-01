# Audsat Gradle Java Test

* Architectural diagram: architectural-diagram.png
* API Documentation: api-docs.yaml

# Prerequisites
* [Java 17](https://learn.microsoft.com/pt-br/java/openjdk/download#openjdk-17)
* [Gradle 7+](https://docs.gradle.org/7.6.1/userguide/userguide.html)
* [Docker](https://www.docker.com)

## Starting project

To initialize the project you should use these commands:
#### Docker
````
docker-compose up
````

#### Gradle
````
./gradlew build
java {APP_ROOT_PATH}/buil/libs/ms-insurance-0.0.1-SNAPSHOT.jar
````

### Swagger
Link: http://localhost:8080/swagger-ui/index.html

### Database H2
Console: http://localhost:8080/h2-console

Credentials:
* user: sa
* pass: password
