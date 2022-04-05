# Scheduling Service

Welcome to Scheduling service.The follow document provides an overview of the service. Scheduling service provides REST based interface on top of quartz scheduler 
along with additional functionality of persisting each scheduled run in db.

## Tech Requirements

- Java 17
- PostgreSQL 11
  
## Technology Overview

- [Architecture](wiki/architecture.md)

### Setting up db and running jar locally
Login to psql shell and create db tables 
<code>

	make psql
	dev=# \i deployment/db_migrations/quartz_table_postgres.sql

</code>

To reset db, 
<code>
	
	make reset

</code>

Set following env variables for db, username and password.
<code>
Run 
   . ./set-env.sh

It will set following env variables , referred in spring application.yml   

	export db=dev
	export dbuser=dev
	export dbpassword=dev

</code>

Create jar file and run scheduling service

<code>
	
	mvn clean install -DskipTests
	java -jar ./scheduling-server/target/scheduling-server-1.12.0-jar-with-dependencies.jar


</code>


### Running integration test
Build and install Scheduling mockservice docker image locally

<code>

    cd scheduling-mockservice 
    docker build -t  scheduling-mockservice .

</code>

Run scheduling-integration-test/src/test/java/com/mediaiq/caps/platform/scheduling/RunnerTest.java
