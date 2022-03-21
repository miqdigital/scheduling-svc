package com.mediaiq.caps.platform.scheduling.commons.model;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * HttpConfigBody
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpConfigBody implements Serializable {

  private static final long serialVersionUID = 4250367721384028576L;

  @JsonProperty("contentType")
  @Builder.Default
  private String contentType = null;

  @JsonProperty("content")
  @Builder.Default
  private String content = null;

  /**
   * Get contentType
   *
   * @return contentType
   **/


  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Get content
   *
   * @return content
   **/


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HttpConfigBody httpConfigBody = (HttpConfigBody) o;
    return Objects.equals(this.contentType, httpConfigBody.contentType) && Objects.equals(
        this.content, httpConfigBody.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentType, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HttpConfigBody {\n");

    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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
