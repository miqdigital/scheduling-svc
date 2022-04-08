package com.mediaiq.caps.platform.scheduling.client;

import java.util.Objects;



/**
 * The type Scheduling config.
 */
public class SchedulingConfig {
  private final String authorizationKey;
  private final String url;

  /**
   * Instantiates a new Scheduling config.
   *
   * @param authorizationKey the authorization key
   */
  private SchedulingConfig(String authorizationKey, String url) {
    this.authorizationKey = authorizationKey;
    this.url = url;
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
    return Objects.equals(getAuthorizationKey(), that.getAuthorizationKey());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAuthorizationKey());
  }

  @Override
  public String toString() {
    return "SchedulingConfig{authorizationKey='" + authorizationKey + '\'' + '}';
  }

  public static class SchedulingConfigBuilder {
    private String authorizationKey;
    private String url;

    public SchedulingConfigBuilder setAuthorizationKey(String authorizationKey) {
      this.authorizationKey = authorizationKey;
      return this;
    }

    public SchedulingConfigBuilder setUrl(String url) {
      this.url = url;
      return this;
    }

    public SchedulingConfig build() {
      url = "http://localhost:8080/";
      return new SchedulingConfig(this.authorizationKey, this.url);
    }
  }
}
