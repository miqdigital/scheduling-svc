package com.miqdigital.scheduling.commons.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
 * HttpConfig
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpConfig implements Serializable {

  private static final long serialVersionUID = -8149498852839264874L;

  @NotNull
  @JsonProperty("url")
  @Builder.Default
  private String url = null;

  @NotNull
  @JsonProperty("method")
  @Builder.Default
  private MethodEnum method = null;

  @JsonProperty("headers")
  @Valid
  @Builder.Default
  private Map<String, String> headers = new HashMap<>();

  @JsonProperty("body")
  @Builder.Default
  private HttpConfigBody body = null;

  /**
   * Get url
   *
   * @return url
   **/

  @NotNull
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * http method
   *
   * @return method
   **/

  @NotNull
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  /**
   * Get headers
   *
   * @return headers
   **/

  @NotNull
  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /**
   * Get body
   *
   * @return body
   **/


  @Valid
  public HttpConfigBody getBody() {
    return body;
  }

  public void setBody(HttpConfigBody body) {
    this.body = body;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HttpConfig httpConfig = (HttpConfig) o;
    return Objects.equals(this.url, httpConfig.url) && Objects.equals(this.method,
        httpConfig.method) && Objects.equals(this.headers, httpConfig.headers) && Objects.equals(
        this.body, httpConfig.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, method, headers, body);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HttpConfig {\n");

    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
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
   * http method
   */
  public enum MethodEnum {
    GET("GET"),

    PUT("PUT"),

    POST("POST"),

    DELETE("DELETE"),

    PATCH("PATCH");

    private String value;

    MethodEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static MethodEnum fromValue(String text) {
      for (MethodEnum b : MethodEnum.values()) {
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
