package com.mediaiq.caps.platform.scheduling;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.miq.heimdall.Heimdall;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RunnerTest {
    private static final Logger logger = LoggerFactory.getLogger(RunnerTest.class);

    @Test
    public void testParallel() throws InterruptedException {
        final String karateOutputPath = "target/cucumber-html-reports";
        String environment = System.getProperty("environment") != null ? System.getProperty("environment") : "none";
        if (environment.equals("pr")) {
            logger.info("Starting test container");
            PostgresRunner pgrunner = new PostgresRunner();
            pgrunner.startPostgres();

            MockRunner mockRunner = new MockRunner();
            mockRunner.startMockService();

            SchedulingServerStartUp.main(new String[]{""});
            TimeUnit.SECONDS.sleep(10);
        }
        final Results stats =
        Runner.path("classpath:features").tags("@sanity, @regression").parallel(2);


        try {
            if (Objects.nonNull(System.getProperty("buildNumber"))) {
                String propertiesFile = "src/test/resources/properties/runner.properties";
                final Heimdall heimdall = new Heimdall();
                heimdall.updateStatusInS3AndNotifySlack(propertiesFile, karateOutputPath);
            }
        } catch (final IllegalAccessException | NoSuchFieldException | IOException | InterruptedException e) {
            logger.error("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("There are scenario failures", 0, stats.getFailCount());
    }
}
