package com.mediaiq.caps.platform.scheduling.client.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * CurlConfig
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CurlConfig implements Serializable {
  @JsonProperty("curlCommand")
  @Builder.Default
  private String curlCommand = null;

  /**
   * Get curlCommand
   *
   * @return curlCommand
   **/
  public String getCurlCommand() {
    return curlCommand;
  }

  public void setCurlCommand(String curlCommand) {
    this.curlCommand = curlCommand;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurlConfig curlConfig = (CurlConfig) o;
    return Objects.equals(this.curlCommand, curlConfig.curlCommand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(curlCommand);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurlConfig {\n");

    sb.append("    curlCommand: ").append(toIndentedString(curlCommand)).append("\n");
    sb.append("}");
    return sb.toString();
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
}
