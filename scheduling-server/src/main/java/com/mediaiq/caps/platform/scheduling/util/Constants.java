package com.mediaiq.caps.platform.scheduling.util;

import java.time.format.DateTimeFormatter;

public class Constants {

  public static final String SCHEDULE_TASK_START_TIME_KEY = "SCHEDULE_TASK_START_TIME_KEY";

  public static final String SCHEDULE_TASK_SCHEDULED_TIME_KEY = "SCHEDULE_TASK_SCHEDULED_TIME_KEY";

  //Max time upto which startTime can be behind Current Time
  public static final int MAX_DIFF_START_CURR_MINUTES = 2;

  //Max time difference between Start Time and End Time
  public static final int MAX_DIFF_START_END_YEAR = 3;

  public static final String HTTP_HEADER_SCHEDULE_TASK_ID = "schedule-task-id";

  public static final String HTTP_HEADER_SCHEDULE_TASK_START_TIME = "schedule-task-start-time";

  public static final String HTTP_HEADER_SCHEDULE_TASK_NEXT_RUN_TIME =
      "schedule-task-next-run-time";

  public static final String HTTP_HEADER_SCHEDULE_TASK_SCHEDULED_TIME =
      "schedule-task-scheduled-time";

  public static final String HTTP_HEADER_SCHEDULE_TASK_IS_LAST_RUN =
      "schedule-task-is-last-run";

  public static final String HTTP_HEADER_INSTANCE_HEADER =
      "internal_instance";

  public static final String JOB_RUN_WITH_EXECUTE_NOW = "job-run-with-execute-now";

  public static final DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
  public static final int MAX_ATTEMPT = 3;
  public static final int RETRY_INTERVAL_SECONDS = 20;
  public static final Integer MOCKPORT = 5000;
}
