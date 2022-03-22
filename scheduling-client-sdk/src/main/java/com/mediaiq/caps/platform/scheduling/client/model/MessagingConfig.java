package com.mediaiq.caps.platform.scheduling.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MessagingConfig
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MessagingConfig implements Serializable {

  @JsonProperty("topicName")
  @Builder.Default
  private String topicName = null;

  @JsonProperty("type")
  @Builder.Default
  private String type = null;

  @JsonProperty("payload")
  @Builder.Default
  private String payload = null;

  /**
   * Gets topic name.
   *
   * @return the topic name
   */
  public String getTopicName() {
    return topicName;
  }

  /**
   * Sets topic name.
   * This field is Optional and if not passed default will be used
   * Default: [env]-scheduling-pub
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
  public String getType() {
    return type;
  }

  /**
   * Sets type.
   * This field is Optional and if not passes default will be used
   * Default: [JOB_GROUP_NAME]
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
    return Objects
        .equals(getTopicName(), that.getTopicName()) && Objects.equals(getType(), that.getType())
        && Objects.equals(getPayload(), that.getPayload());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTopicName(), getType(), getPayload());
  }

  @Override
  public String toString() {
    return "MessagingConfig{" + "topicName='" + topicName + '\''
        + ", type='" + type + '\'' + ", payload='" + payload + '\'' + '}';
  }
}
