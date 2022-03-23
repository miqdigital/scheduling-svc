package com.mediaiq.caps.platform.scheduling.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mediaiq.caps.platform.trackingtags.Environment;

public class SchedulingConfigTest {

  @Test
  public void shouldCheckIntegrationEnvironment() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.INTEGRATION)
            .setAuthorizationKey("auth_key").build();

    assertEquals("https://api-gateway.dev.miqdigital.com/integration-scheduling-service/v1/",
        schedulingConfig.getUrl());
    assertEquals(Environment.INTEGRATION, schedulingConfig.getEnvironment());
  }

  @Test
  public void shouldCheckProductionEnvironment() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.PRODUCTION)
            .setAuthorizationKey("auth_key").build();

    assertEquals("https://api-gateway.prod.miqdigital.com/production-scheduling-service/v1/",
        schedulingConfig.getUrl());
    assertEquals(Environment.PRODUCTION, schedulingConfig.getEnvironment());
  }

  @Test
  public void shouldCheckLocalEnvironment() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.LOCAL)
            .setAuthorizationKey("auth_key").build();

    assertEquals("https://api-gateway.dev.miqdigital.com/integration-scheduling-service/v1/",
        schedulingConfig.getUrl());
    assertEquals(Environment.LOCAL, schedulingConfig.getEnvironment());


  }

  @Test
  public void shouldCheckOtherEnvironment() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.PR)
            .setAuthorizationKey("auth_key").build();
    assertEquals("http://localhost:8080/", schedulingConfig.getUrl());

  }

  @Test
  public void shouldCheckHashCode() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.LOCAL)
            .setUrl("http://localhost:8080/").build();
    SchedulingConfig expected =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.LOCAL)
            .setUrl("http://localhost:8080/").build();

    assertEquals(expected.hashCode(), schedulingConfig.hashCode());
  }

  @Test
  public void shouldCheckEquals() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.INTEGRATION)
            .build();
    SchedulingConfig expected =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.INTEGRATION)
            .build();
    assertTrue(schedulingConfig.equals(expected));

  }

  @Test
  public void shouldCheckToString() {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setEnvironment(Environment.INTEGRATION)
            .setAuthorizationKey("auth_key").build();
    String expected =
        "SchedulingConfig{" + "environment=" + Environment.INTEGRATION + ", authorizationKey='"
            + "auth_key" + '\'' + '}';
    assertEquals(expected, schedulingConfig.toString());

  }
}