package com.mediaiq.caps.platform.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

import com.mediaiq.caps.platform.scheduling.util.Constants;

public class MockRunner {
  private static final Logger logger = LoggerFactory.getLogger(PostgresRunner.class);

  public void startMockService() {
    GenericContainer mock = new GenericContainer("scheduling-mockservice")
        .withExposedPorts(Constants.MOCKPORT);
    mock.start();
    int mappedPort = mock.getMappedPort(Constants.MOCKPORT);
    logger.info("mock started on {}", mappedPort);
    String mockUrl = "http://localhost:" + mappedPort + "/";
    System.setProperty("MOCK_URL", mockUrl);
  }
}
