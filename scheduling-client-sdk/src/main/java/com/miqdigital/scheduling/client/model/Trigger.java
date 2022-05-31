package com.miqdigital.scheduling.client.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Trigger
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Trigger implements Serializable {
  @JsonProperty("startDateTime")
  @Builder.Default
  private ZonedDateTime startDateTime = null;

  @JsonProperty("endDateTime")
  @Builder.Default
  private ZonedDateTime endDateTime = null;

  @JsonProperty("schedule")
  @Builder.Default
  private Schedule schedule = null;

  /**
   * Start date-time (\"in format yyyy-mm-ddThh:mm:ss e.g. 2011-12-03T10:15:30\"). Note the \"T\" in
   * middle
   *
   * @return startDateTime
   **/
  public ZonedDateTime getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(ZonedDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }

  /**
   * End date-time (\"in format yyyy-mm-ddThh:mm:ss e.g. 2011-12-04T11:15:30\").  Note the \"T\" in
   * middle
   *
   * @return endDateTime
   **/
  public ZonedDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(ZonedDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  /**
   * Get schedule
   *
   * @return schedule
   **/

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Trigger trigger = (Trigger) o;
    return Objects.equals(this.startDateTime, trigger.startDateTime) && Objects.equals(
        this.endDateTime, trigger.endDateTime) && Objects.equals(this.schedule, trigger.schedule);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDateTime, endDateTime, schedule);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Trigger {\n");

    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
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
