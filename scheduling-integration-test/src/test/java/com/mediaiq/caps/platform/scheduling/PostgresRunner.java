package com.mediaiq.caps.platform.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresRunner {
  private static final Logger logger = LoggerFactory.getLogger(PostgresRunner.class);

  public void startPostgres() {
    PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:11.1").withDatabaseName("scheduling_local");
    postgres.withInitScript("quartz_table_postgres.sql");
    postgres.start();
    logger.info("Postgres started on {}", postgres.getJdbcUrl());
    System.setProperty("DB_URL", postgres.getJdbcUrl());
    System.setProperty("DB_USERNAME", postgres.getUsername());
    System.setProperty("DB_PASSWORD", postgres.getPassword());
  }
}
