# Soccer Tournament System
Developed using [Spring Boot](http://projects.spring.io/spring-boot/)

# The Goal
To build a service that helps vendors run a soccer game

# This Application
### Base Functionality:
1. Register Teams
2. Record matches
3. Determine winners
   1. Winners can be determined even when facing draws
   2. 2 Fallback Mechanisms included

# Table of Contents
- [Requirements](#Requirements)
- [Pre-requisites](#Pre-requisites)
- [Running the application](#running-the-application)
   - [Using Java](#using-java)
   - [Using Docker & docker-compose](#using-docker)
- [Verifying Results](#verifying-results)
- [TODO](#TODO)

## Requirements
For building and running the application you need:

- [JDK 18](https://jdk.java.net/18/)
- [Maven 3](https://maven.apache.org)
- [PostgreSQL](https://www.postgresql.org/download/)

## Pre-requisites
- A PostgreSQL instance with
   - Valid credentials (eg: <b>username</b>=postgres, <b>password</b>=postgres)
   - Valid database name (eg: tournament)
## Running the application
### Using Java

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.demo.SoccerTournamentApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
- <b>Note</b>: This method will require the maven plugin to point to the correct JDK (JDK 18, which may not be configured on your computer)
```shell
mvn spring-boot:run
```

### Using Docker

The easiest way to run the Spring Application using Docker is to use [Docker CLI](https://docs.docker.com/engine/reference/commandline/cli/):

```shell
docker run -p 8080:8080 -t shafiq98/spring_tournament_service
```
- <b>Optional</b>: Add a <b>-d</b> flag to allow the service to run in detached mode (the terminal will not keep printing logs)

This will:

* Pull the docker image from my public DockerHub Repository
* Expose port 8080
* Start the required service

#### Stopping the service
Run the following commands(<i>in a separate CLI instance if you did not add a -d flag</i>)
```shell
C:\\User\\User1>docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
containerID    imageName
c:\\User\User1>docker stop containerID
```

### Deploying the application using Docker


The easiest way to run the entire application (SpringBoot, Postgres) using Docker is to use [docker-compose CLI](https://docs.docker.com/compose/reference/):

Steps
1. Open a CLI instance and navigate to the folder docker-compose.yaml is saved in
2. Run
```
docker-compose pull
docker-compose up
```
3. <b>Optional</b> : Add a <b>-d</b> flag to allow the service to run in detached mode (the terminal will not keep printing logs)
4. Navigate to localhost:8080 to access the back-end application
   1. There is a Postman Collection & Environment to use for testing
5. From the same directory, run ```docker-compose down``` to stop the service

## Verifying Results
Since there is no front-end application, a Postman Collection is included to help with testing
### Components
1. Postman Collection
   1. Contains set of requests to be run for testing
2. Postman Environment
   1. Contains environment variables, for long inputs and storing results
### Using Postman Collection Runner
1. [Import the collection & environment](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/)
2. Navigate to SoccerTournament/OfficialTestCases in the collection
   1. ![Alt text](./READMEResources/CollectionPostman.png?raw=true "Image")
3. Ensure the environment is set to SoccerTournamentSpringBoot
   1. ![Alt text](./READMEResources/EnvironmentPostman.png?raw=true "Image")
4. Click on the 3 dots beside "Official Testcase"
   1. ![Alt text](./READMEResources/TestcasePostman.png?raw=true "Image")
5. Click on "Run Folder"
6. Run all tests
   1. ![Alt text](./READMEResources/RunTestcasesPostman.png?raw=true "Image")
7. Inspect results
   1. ![Alt text](./READMEResources/TestcaseResults.png?raw=true "Image")
   2. ![Alt text](./READMEResources/TestcaseResults2.png?raw=true "Image")
## TODO
1. Increase code coverage in testing
2. Deploy on scalable more scalable platforms like OpenShift/Kubernetes to allow more servers to be run