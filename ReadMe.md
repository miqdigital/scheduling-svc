[![CircleCI](https://circleci.com/gh/miqdigital/scheduling-svc/tree/main.svg?style=svg)](https://circleci.com/gh/miqdigital/scheduling-svc/tree/main)


# Scheduling Service

Welcome to Scheduling service, the follow document provides an overview of the service. Scheduling service provides REST based interface on top of 
quartz scheduler along with additional functionality of persisting each scheduled run in db.

## Tech Requirements

- Java 17
- Python 3
- PostgreSQL 11

### Web Framework
- Spring Boot 2

### Testing Framework
- Junit ( Unit Testing )
- Karate ( Component/Integration Tests )
- Test Containers ( Component/Integration Tests )

### When to use scheduling service ?

If you have below requirements, then scheduling service is the right fit for you
1. Want to run a scheduler outside of application code base
2. Need rest base easy interface to schedule new tasks
3. Tasks are Http/Curl based and execution of the task happens outside
  
## Technology Overview

- [Architecture](wiki/architecture.md)

### Setting up db and running jar locally
- Login to psql shell and create db tables 

```
   make psql
   dev=# \i deployment/db_migrations/quartz_table_postgres.sql
```

- To reset db 
	
   `make reset`


- Set following env variables for db, username and password using
`. ./set-env.sh`.
It will set following env variables , referred in spring application.yml   

```
    export db=dev
    export dbuser=dev
    export dbpassword=dev
```

- Create jar file and run scheduling service

```
	mvn clean install
	java -jar ./scheduling-server/target/scheduling-server-<version>-jar-with-dependencies.jar
```


### Running Integration Test

#### What is scheduling mockservice ?

You might have noticed that there is a module in the project `scheduling-mockservice` written in python, it is nothing but a test double(stub), this mock service stores data like
1. Schedule execution
2. Complete request with time
3. Supports delayed response

this is then used in integration tests to verify the schedule execution, without this we can only test if the schedule was fired but not if it every reached the target or not.

#### Starting scheduling mockservice

Build and install Scheduling mockservice docker image locally

```
    cd scheduling-mockservice 
    docker build -t  scheduling-mockservice .
```

#### Running integration tests 

```
scheduling-integration-test/src/test/java/com/mediaiq/caps/platform/scheduling/RunnerTest.java
```

### CI/CD

For automated build as of now we have integrated `CircleCI` which when a PR is merged to main branch will do below tasks
1. Run the build
2. Run unit tests
3. Perform secret scan

//TODO: Add complete pipeline with running component tests and auto publish of jar to maven
