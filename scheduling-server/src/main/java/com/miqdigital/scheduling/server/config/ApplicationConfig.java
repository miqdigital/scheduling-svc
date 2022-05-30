package com.miqdigital.scheduling.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



/**
 * The type Application config.
 */
@Configuration
@ConfigurationProperties("application")
public class ApplicationConfig {

  private int httpJobCallBackTimeout;
  private int minSchedulingInterval;
  private String schedulingServiceEnv;

  /**
   * Instantiates a new Application config.
   */
  public ApplicationConfig() {
    //Empty constructor
  }

  /**
   * Gets http job call back timeout.
   *
   * @return the http job call back timeout
   */
  public int getHttpJobCallBackTimeout() {
    return httpJobCallBackTimeout;
  }

  /**
   * Sets http job call back timeout.
   *
   * @param httpJobCallBackTimeout the http job call back timeout
   */
  public void setHttpJobCallBackTimeout(int httpJobCallBackTimeout) {
    this.httpJobCallBackTimeout = httpJobCallBackTimeout;
  }

  /**
   * Gets min scheduling interval.
   *
   * @return the min scheduling interval
   */
  public int getMinSchedulingInterval() {
    return minSchedulingInterval;
  }

  /**
   * Sets min scheduling interval.
   *
   * @param minSchedulingInterval the min scheduling interval
   */
  public void setMinSchedulingInterval(int minSchedulingInterval) {
    this.minSchedulingInterval = minSchedulingInterval;
  }

  /**
   * Gets scheduling service env.
   *
   * @return the scheduling service env
   */
  public String getSchedulingServiceEnv() {
    return schedulingServiceEnv;
  }

  /**
   * Sets scheduling service env.
   *
   * @param schedulingServiceEnv the scheduling service env
   */
  public void setSchedulingServiceEnv(String schedulingServiceEnv) {
    this.schedulingServiceEnv = schedulingServiceEnv;
  }


}
