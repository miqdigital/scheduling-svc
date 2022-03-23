package com.mediaiq.caps.platform.scheduling.commons.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * MessagingConfig
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MessagingConfig implements Serializable {

  private static final long serialVersionUID = -1417171031110248549L;

  @JsonProperty("topicName")
  @Builder.Default
  private String topicName = null;

  @JsonProperty("type")
  @Builder.Default
  private String type = null;

  @NotNull
  @JsonProperty("payload")
  @Builder.Default
  private String payload = null;

  /**
   * Gets topic name.
   *
   * @return the topic name
   */
  @Valid
  public String getTopicName() {
    return topicName;
  }

  /**
   * Sets topic name.
   *
   * @param topicName the topic name
   */
  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  @Valid
  public String getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets payload.
   *
   * @return the payload
   */
  @Valid
  public String getPayload() {
    return payload;
  }

  /**
   * Sets payload.
   *
   * @param payload the payload
   */
  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MessagingConfig that = (MessagingConfig) o;
    return Objects.equals(getTopicName(), that.getTopicName()) && Objects
        .equals(getType(), that.getType()) && Objects.equals(getPayload(), that.getPayload());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTopicName(), getType(), getPayload());
  }

  @Override
  public String toString() {
    return "MessagingConfig{" + "topicName='" + topicName + '\'' + ", type='" + type + '\''
        + ", payload='" + payload + '\'' + '}';
  }

}
