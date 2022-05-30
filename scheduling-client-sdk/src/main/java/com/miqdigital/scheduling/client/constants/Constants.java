package com.miqdigital.scheduling.client.constants;

/**
 * The type Constants.
 */
public class Constants {
  /**
   * The constant key to get id of schedule task.
   */
  public static final String HTTP_HEADER_SCHEDULE_TASK_ID = "schedule-task-id";

  /**
   * The constant to get the start time of the schedule task.
   */
  public static final String HTTP_HEADER_SCHEDULE_TASK_START_TIME = "schedule-task-start-time";

  /**
   * The constant to get the next run time of schedule task.
   * This would not be available if the run is last run of called via execute-now
   */
  public static final String HTTP_HEADER_SCHEDULE_TASK_NEXT_RUN_TIME =
      "schedule-task-next-run-time";

  /**
   * The constant to get the schedule time of schedule task.
   */
  public static final String HTTP_HEADER_SCHEDULE_TASK_SCHEDULED_TIME =
      "schedule-task-scheduled-time";

  /**
   * The constant to get if this is the last run for the given schedule.
   * The value would always be false for execute-now schedules.
   */
  public static final String HTTP_HEADER_SCHEDULE_TASK_IS_LAST_RUN = "schedule-task-is-last-run";
}
