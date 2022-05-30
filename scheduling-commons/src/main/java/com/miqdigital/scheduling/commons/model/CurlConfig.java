package com.miqdigital.scheduling.commons.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * CurlConfig
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CurlConfig implements Serializable {

  private static final long serialVersionUID = 7902564911717413712L;

  @NotNull
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
  public boolean equals(java.lang.Object o) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
