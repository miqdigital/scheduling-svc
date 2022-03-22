package com.mediaiq.caps.platform.scheduling.commons.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * ScheduleTaskExecutionHistory
 */
@Entity
@Table(name = "SCHEDULE_TASK_EXECUTION_HISTORY")
@Validated
@NoArgsConstructor
public class ScheduleTaskExecutionHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "execution_id")
  private Integer executionId = null;

  @NotNull
  @Column(name = "schedule_task_id")
  private String scheduleTaskId = null;

  @NotNull
  @Column(name = "schedule_time")
  private ZonedDateTime scheduleDateTime = null;

  @NotNull
  @Column(name = "start_time")
  private ZonedDateTime startDateTime = null;

  @NotNull
  @Column(name = "end_time")
  private ZonedDateTime endDateTime = null;

  @NotNull
  @Column(name = "execution_status")
  @Enumerated(EnumType.STRING)
  private ExecutionStatusEnum executionStatus = null;

  @Column(name = "error_message")
  private String errorMessage = null;

  @Builder(toBuilder = true)
  public ScheduleTaskExecutionHistory(@NotNull Integer executionId, @NotNull String scheduleTaskId,
      @NotNull ZonedDateTime scheduleDateTime, @NotNull ZonedDateTime startDateTime,
      @NotNull ZonedDateTime endDateTime, @NotNull ExecutionStatusEnum executionStatus,
      String errorMessage) {
    this.executionId = executionId;
    this.scheduleTaskId = scheduleTaskId;
    this.scheduleDateTime = scheduleDateTime;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.executionStatus = executionStatus;
    this.errorMessage = errorMessage;
  }

  public Integer getExecutionId() {
    return executionId;
  }

  public void setExecutionId(Integer executionId) {
    this.executionId = executionId;
  }

  public String getScheduleTaskId() {
    return scheduleTaskId;
  }

  public void setScheduleTaskId(String scheduleTaskId) {
    this.scheduleTaskId = scheduleTaskId;
  }

  public ZonedDateTime getScheduleDateTime() {
    return scheduleDateTime;
  }

  public void setScheduleDateTime(ZonedDateTime scheduleDateTime) {
    this.scheduleDateTime = scheduleDateTime;
  }

  public ZonedDateTime getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(ZonedDateTime startDateTime) {
    this.startDateTime = startDateTime;
  }

  public ZonedDateTime getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(ZonedDateTime endDateTime) {
    this.endDateTime = endDateTime;
  }

  public ExecutionStatusEnum getExecutionStatus() {
    return executionStatus;
  }

  public void setExecutionStatus(ExecutionStatusEnum executionStatus) {
    this.executionStatus = executionStatus;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleTaskExecutionHistory scheduleTaskRunResponse = (ScheduleTaskExecutionHistory) o;
    return Objects.equals(this.executionId, scheduleTaskRunResponse.executionId) && Objects
        .equals(this.scheduleTaskId, scheduleTaskRunResponse.scheduleTaskId) && Objects
        .equals(this.scheduleDateTime, scheduleTaskRunResponse.scheduleDateTime) && Objects
        .equals(this.startDateTime, scheduleTaskRunResponse.startDateTime) && Objects
        .equals(this.endDateTime, scheduleTaskRunResponse.endDateTime) && Objects
        .equals(this.executionStatus, scheduleTaskRunResponse.executionStatus) && Objects
        .equals(this.errorMessage, scheduleTaskRunResponse.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionId, scheduleTaskId, scheduleDateTime, startDateTime, endDateTime,
        executionStatus, errorMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleTaskExecutionHistory {\n");

    sb.append("    executionId: ").append(toIndentedString(executionId)).append("\n");
    sb.append("    scheduleTaskId: ").append(toIndentedString(scheduleTaskId)).append("\n");
    sb.append("    scheduleDateTime: ").append(toIndentedString(scheduleDateTime)).append("\n");
    sb.append("    startDateTime: ").append(toIndentedString(startDateTime)).append("\n");
    sb.append("    endDateTime: ").append(toIndentedString(endDateTime)).append("\n");
    sb.append("    executionStatus: ").append(toIndentedString(executionStatus)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}