package com.mediaiq.caps.platform.scheduling.client.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Executor
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Executor implements Serializable {
  @JsonProperty("type")
  @Builder.Default
  private ExecutorType type = null;

  @JsonProperty("curlConfig")
  @Builder.Default
  private CurlConfig curlConfig = null;

  @JsonProperty("httpConfig")
  @Builder.Default
  private HttpConfig httpConfig = null;

  /**
   * Type of Executor
   *
   * @return type
   **/

  public Executor.ExecutorType getType() {
    return type;
  }

  public void setType(ExecutorType type) {
    this.type = type;
  }

  /**
   * Get curlConfig
   *
   * @return curlConfig
   **/


  public CurlConfig getCurlConfig() {
    return curlConfig;
  }

  public void setCurlConfig(CurlConfig curlConfig) {
    this.curlConfig = curlConfig;
  }

  /**
   * Get httpConfig
   *
   * @return httpConfig
   **/


  public HttpConfig getHttpConfig() {
    return httpConfig;
  }

  public void setHttpConfig(HttpConfig httpConfig) {
    this.httpConfig = httpConfig;
  }

  @Override
  public String toString() {
    return "Executor{" + "type=" + type + ", curlConfig=" + curlConfig + ", httpConfig="
        + httpConfig + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Executor executor = (Executor) o;
    return getType() == executor.getType() && Objects.equals(getCurlConfig(),
        executor.getCurlConfig()) && Objects.equals(getHttpConfig(), executor.getHttpConfig());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getType(), getCurlConfig(), getHttpConfig());
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Type of Executor
   */
  public enum ExecutorType {
    CURL("curl"),

    HTTP("http");

    private String value;

    ExecutorType(String value) {
      this.value = value;
    }

    @JsonCreator
    public static ExecutorType fromValue(String text) {
      for (ExecutorType b : ExecutorType.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("No enum constant for " + text + " found");
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }
}
