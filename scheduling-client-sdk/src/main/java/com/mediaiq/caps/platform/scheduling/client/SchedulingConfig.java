package com.mediaiq.caps.platform.scheduling.client;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.mediaiq.caps.platform.trackingtags.Environment;

/**
 * The type Scheduling config.
 */
public class SchedulingConfig {
  private final Environment environment;
  private final String authorizationKey;
  private final String url;

  /**
   * Instantiates a new Scheduling config.
   *
   * @param environment the environment
   * @param authorizationKey the authorization key
   */
  private SchedulingConfig(Environment environment, String authorizationKey , String url) {
    this.environment = environment;
    this.authorizationKey = authorizationKey;
    this.url = url;
  }

  /**
   * Gets environment.
   *
   * @return the environment
   */
  public Environment getEnvironment() {
    return environment;
  }

  /**
   * Gets authorization key.
   *
   * @return the authorization key
   */
  public String getAuthorizationKey() {
    return authorizationKey;
  }

  
  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SchedulingConfig that = (SchedulingConfig) o;
    return getEnvironment() == that.getEnvironment() && Objects
        .equals(getAuthorizationKey(), that.getAuthorizationKey());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEnvironment(), getAuthorizationKey());
  }

  @Override
  public String toString() {
    return "SchedulingConfig{" + "environment=" + environment + ", authorizationKey='"
        + authorizationKey + '\'' + '}';
  }

  public static class SchedulingConfigBuilder{
    private Environment environment;
    private String authorizationKey;
    private String url;

    public SchedulingConfigBuilder setEnvironment(Environment environment) {
      this.environment = environment;
      return this;
    }

    public SchedulingConfigBuilder setAuthorizationKey(String authorizationKey) {
      this.authorizationKey = authorizationKey;
      return this;
    }

    public SchedulingConfigBuilder setUrl(String url) {
      this.url = url;
      return this;
    }
    
    public SchedulingConfig build(){
      switch (this.environment) {
        case INTEGRATION:
          this.url = "https://api-gateway.dev.miqdigital.com/integration-scheduling-service/v1/";
          break;
        case PRODUCTION:
          this.url = "https://api-gateway.prod.miqdigital.com/production-scheduling-service/v1/";
          break;
        case LOCAL:
          if (StringUtils.isEmpty(this.url)){
            this.url = "https://api-gateway.dev.miqdigital.com/integration-scheduling-service/v1/";
          }
          break;
        default:
          url = "http://localhost:8080/";
      }
      
      return new SchedulingConfig(this.environment,this.authorizationKey,this.url);
    }
  }
}
