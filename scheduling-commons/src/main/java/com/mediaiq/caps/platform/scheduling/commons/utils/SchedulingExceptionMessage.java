package com.mediaiq.caps.platform.scheduling.commons.utils;

public enum SchedulingExceptionMessage {

  //400, "Bad Request"
  SCHEDULE_TASK_ID_DOES_NOT_MATCH(40001,
      "Path param 'id' does not match the id of Schedule Task in request body"),

  MISSING_CONFIG(40002, "Missing config in request body"),

  INVALID_EXECUTOR_TYPE(40003, "executor.type is invalid"),

  INVALID_SCHEDULE_TYPE(40004, "schedule.type is invalid"),

  INVALID_PREDEFINED_EXPRESSION(40005, "schedule.value predefined expression is invalid"),

  INVALID_SCHEDULE_EXPRESSION(40006, "schedule.value is invalid"),

  SCHEDULE_TASK_IS_NOT_SCHEDULED(40007, "The Schedule Task isn't scheduled."),

  SCHEDULE_TASK_IS_IN_ACTIVE_STATUS(40008, "The Schedule Task is already in ACTIVE status."),

  SCHEDULE_TASK_IS_NOT_IN_ACTIVE_STATUS(40009, "The Schedule Task isn't in ACTIVE status."),

  MIN_INTERVAL_CONSTRAINT_VIOLATION(40011,
      "Given scheduling interval is less than min allowed interval"),

  START_TIME_IN_BACK_DATE(40012, "Start time should not be less than current time"),

  END_TIME_START_TIME_DIFF_VIOLATION(40013, "End time should be within 3 years of start time"),

  START_TIME_AFTER_END_TIME(40014, "Start time should not be after endtime"),

  INVALID_EXECUTION_STATUS(40015, "Invalid Execution Status"),

  NEVER_FIRE_SCHEDULE(40016, "Invalid schedule defined, this will never execute"),

  SCHEDULE_GROUP_UPDATE_ERROR(40017, "Schedule task group cannot be updated"),

  //404, "Not Found"
  SCHEDULE_TASK_ID_DOES_NOT_EXIST(40401, "Schedule Task id does not exist"),

  //409, "Conflict"
  SCHEDULE_TASK_ALREADY_EXIST(40901, "Schedule Task with same name already exist for this group");

  private final int code;
  private final String message;

  SchedulingExceptionMessage(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public static SchedulingExceptionMessage valueOf(int code) {
    SchedulingExceptionMessage[] messages = values();
    int len = values().length;
    for (int i = 0; i < len; i++) {
      SchedulingExceptionMessage message = messages[i];
      if (message.code == code) {
        return message;
      }
    }
    throw new IllegalArgumentException("No matching constant for [" + code + "]");
  }


  @Override
  public String toString() {
    return Integer.toString(code);
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
