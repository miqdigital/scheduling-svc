package com.mediaiq.caps.platform.scheduling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mediaiq.caps.platform.trackingtags.Department;
import com.mediaiq.caps.platform.trackingtags.Environment;
import com.mediaiq.caps.platform.trackingtags.Product;
import com.mediaiq.caps.platform.trackingtags.Service;
import com.mediaiq.caps.platform.trackingtags.Team;
import com.mediaiq.caps.platform.trackingtags.TrackingTags;

/**
 * The type Application config.
 */
@Configuration
@ConfigurationProperties("application")
public class ApplicationConfig {

  private int httpJobCallBackTimeout;
  private int minSchedulingInterval;
  private String messagingPublishEndpoint;
  private String messagingDefaultPublishTopic;
  private String apiGatewayToken;
  private String schedulingServiceEnv;
  private TrackingTags schedulingTrackingTags;

  /**
   * Instantiates a new Application config.
   */
  public ApplicationConfig() {
    //Empty constructor
  }

  /**
   * Gets messaging default publish topic.
   *
   * @return the messaging default publish topic
   */
  public String getMessagingDefaultPublishTopic() {
    return messagingDefaultPublishTopic;
  }

  /**
   * Sets messaging default publish topic.
   *
   * @param messagingDefaultPublishTopic the messaging default publish topic
   */
  public void setMessagingDefaultPublishTopic(String messagingDefaultPublishTopic) {
    this.messagingDefaultPublishTopic = messagingDefaultPublishTopic;
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
   * Gets messaging publish endpoint.
   *
   * @return the messaging publish endpoint
   */
  public String getMessagingPublishEndpoint() {
    return messagingPublishEndpoint;
  }

  /**
   * Sets messaging publish endpoint.
   *
   * @param messagingPublishEndpoint the messaging publish endpoint
   */
  public void setMessagingPublishEndpoint(String messagingPublishEndpoint) {
    this.messagingPublishEndpoint = messagingPublishEndpoint;
  }

  /**
   * Gets api gateway token.
   *
   * @return the api gateway token
   */
  public String getApiGatewayToken() {
    return apiGatewayToken;
  }

  /**
   * Sets api gateway token.
   *
   * @param apiGatewayToken the api gateway token
   */
  public void setApiGatewayToken(String apiGatewayToken) {
    this.apiGatewayToken = apiGatewayToken;
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

  /**
   * Gets scheduling tracking tags.
   *
   * @return the scheduling tracking tags
   */
  public TrackingTags getSchedulingTrackingTags() {
    return schedulingTrackingTags;
  }

  /**
   * Sets scheduling tracking tags.
   *
   * @param environment the environment
   */
  public void setSchedulingTrackingTags(String environment) {
    Environment environmentFromString = getTrackingTagsEnvironmentFromString(environment);
    this.schedulingTrackingTags = getTrackingTags(environmentFromString);
  }

  private TrackingTags getTrackingTags(Environment environment) {
    return new TrackingTags.TrackingTagsBuilder().setTeam(Team.PLATFORMSERVICES)
        .setProduct(Product.SCHEDULING_SERVICE).setService(Service.SCHEDULING_SERVER)
        .setDepartment(Department.TECH).setEnvironment(environment).setFunction("schedule callback")
        .setOwner("platform@miqdigital.com").build();
  }

  private Environment getTrackingTagsEnvironmentFromString(String environment) {
    Environment trackingEnv;
    switch (environment) {
      case "production":
        trackingEnv = Environment.PRODUCTION;
        break;
      case "qa":
        trackingEnv = Environment.QA;
        break;
      case "pr":
        trackingEnv = Environment.PR;
        break;
      case "pre-prod":
        trackingEnv = Environment.PRE_PROD;
        break;
      default:
        trackingEnv = Environment.INTEGRATION;
    }
    return trackingEnv;
  }


}
