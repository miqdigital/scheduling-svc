# Scheduling Service

Welcome to Scheduling service.The follow document provides an over view of the services, with it's architecture and also how to use the service for deployment.

## Tech Requirements

- Java 8
- PostgreSQL 11
  
## Technology Overview

- [Architecture](wiki/architecture.md)
- [API Documentation](wiki/api.md)


### Setting up db

<code>
	pgsql/bin/pgsql -U postgres
postgres=# create user quartz with password 'quartz123';
postgres=# create database quartz with owner quartz;
postgres=# \c quartz quartz
quartz=> \i quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore/tables_postgres.sql
</code>>