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
 * Schedule
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {
  @JsonProperty("type")
  @Builder.Default
  private ScheduleType type = null;

  @JsonProperty("value")
  @Builder.Default
  private String value = null;

  /**
   * scheduling expression type
   *
   * @return type
   **/
  public Schedule.ScheduleType getType() {
    return type;
  }

  public void setType(ScheduleType type) {
    this.type = type;
  }

  /**
   * cron expression or predefinedExpression strings (every15m, hourly, every3h, daily, weekly,
   * fortnightly, monthly)
   *
   * @return value
   **/

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Schedule schedule = (Schedule) o;
    return Objects.equals(this.type, schedule.type) && Objects.equals(this.value, schedule.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Schedule {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

  /**
   * scheduling expression type
   */
  public enum ScheduleType {
    CRONEXPRESSION("cronExpression"),

    PREDEFINEDEXPRESSION("predefinedExpression"),

    EVERY_N_MINUTES("everyNMinutes"),

    EVERY_N_MONTHS("everyNMonths");

    private String value;

    ScheduleType(String value) {
      this.value = value;
    }

    @JsonCreator
    public static ScheduleType fromValue(String text) {
      for (ScheduleType b : ScheduleType.values()) {
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


  /**
   * scheduling expression type
   */
  public enum PreDefinedExpression {
    EVERY15MIN("every15m"),

    HOURLY("hourly"),

    EVERY3HOUR("every3h"),

    DAILY("daily"),

    WEEKLY("weekly"),

    FORTNIGHTLY("fortnightly"),

    MONTHLY("monthly");

    private String value;

    PreDefinedExpression(String value) {
      this.value = value;
    }

    @JsonCreator
    public static PreDefinedExpression fromValue(String text) {
      for (PreDefinedExpression b : PreDefinedExpression.values()) {
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
