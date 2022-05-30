package com.miqdigital.scheduling.server;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;

@SpringBootTest
public class RunnerTest {
  private static final Logger logger = LoggerFactory.getLogger(RunnerTest.class);

  @Test
  public void testParallel() throws InterruptedException {
    logger.info("Starting test container");
    PostgresRunner pgrunner = new PostgresRunner();
    pgrunner.startPostgres();

    MockRunner mockRunner = new MockRunner();
    mockRunner.startMockService();

    SchedulingServerStartUp.main(new String[] {""});
    TimeUnit.SECONDS.sleep(10);
    final Results stats =
        Runner.path("classpath:features").tags("@sanity,@regression").parallel(2);
    Assert.assertEquals("There are scenario failures", 0, stats.getFailCount());
  }
}
