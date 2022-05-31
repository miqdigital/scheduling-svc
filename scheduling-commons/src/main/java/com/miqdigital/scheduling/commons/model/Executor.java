package com.miqdigital.scheduling.commons.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Executor
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Executor implements Serializable {

  private static final long serialVersionUID = 6627636857699070368L;

  @NotNull
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
   * @return type type
   */
  @NotNull
  public Executor.ExecutorType getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(ExecutorType type) {
    this.type = type;
  }


  /**
   * Get curlConfig
   *
   * @return curlConfig curl config
   */
  @Valid
  public CurlConfig getCurlConfig() {
    return curlConfig;
  }

  /**
   * Sets curl config.
   *
   * @param curlConfig the curl config
   */
  public void setCurlConfig(CurlConfig curlConfig) {
    this.curlConfig = curlConfig;
  }


  /**
   * Get httpConfig
   *
   * @return httpConfig http config
   */
  @Valid
  public HttpConfig getHttpConfig() {
    return httpConfig;
  }

  /**
   * Sets http config.
   *
   * @param httpConfig the http config
   */
  public void setHttpConfig(HttpConfig httpConfig) {
    this.httpConfig = httpConfig;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Executor executor = (Executor) o;
    return Objects.equals(this.type, executor.type) && Objects.equals(this.curlConfig,
        executor.curlConfig) && Objects.equals(this.httpConfig, executor.httpConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, curlConfig, httpConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Executor {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    curlConfig: ").append(toIndentedString(curlConfig)).append("\n");
    sb.append("    httpConfig: ").append(toIndentedString(httpConfig)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Type of Executor
   */
  public enum ExecutorType {
    /**
     * Curl executor type.
     */
    CURL("curl"),

    /**
     * Http executor type.
     */
    HTTP("http");

    private String value;

    ExecutorType(String value) {
      this.value = value;
    }

    /**
     * From value executor type.
     *
     * @param text the text
     * @return the executor type
     */
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
