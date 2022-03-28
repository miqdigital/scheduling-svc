# Scheduling Service

Welcome to Scheduling service.The follow document provides an overview of the service. Scheduling service provides REST based interface on top of quartz scheduler 
along with additional functionality of persisting each scheduled run in db.

## Tech Requirements

- Java 8
- PostgreSQL 11
  
## Technology Overview

- [Architecture](wiki/architecture.md)

### Setting up db
Set following env variables for db, username and password.
<code>

	export db=dev
	export dbuser=dev
	export dbpassword=dev

</code>
Refer these instructions to log in to psql and create db tables.
<code>

	pgsql/bin/pgsql -U dev
	postgres=# create user dev with password 'dev';
	postgres=# create database dev with owner dev;
	postgres=# \c dev dev
	Run deployment/db_migrations/quartz_table_postgres.sql to create db tables.

</code>