package com.miqdigital.scheduling.commons.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SchedulingExceptionMessageTest {

  @Test
  public void testToString() {
    // Run the test
    final String schedule_task_id_does_not_matchResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_MATCH.toString();
    final String missing_configResult = SchedulingExceptionMessage.MISSING_CONFIG.toString();
    final String invalid_executor_typeResult =
        SchedulingExceptionMessage.INVALID_EXECUTOR_TYPE.toString();
    final String invalid_schedule_typeResult =
        SchedulingExceptionMessage.INVALID_SCHEDULE_TYPE.toString();
    final String invalid_predefined_expressionResult =
        SchedulingExceptionMessage.INVALID_PREDEFINED_EXPRESSION.toString();
    final String invalid_schedule_expression_result =
        SchedulingExceptionMessage.INVALID_SCHEDULE_EXPRESSION.toString();
    final String schedule_task_is_not_scheduledResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_IS_NOT_SCHEDULED.toString();
    final String schedule_task_is_in_active_statusResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_IS_IN_ACTIVE_STATUS.toString();
    final String schedule_task_is_not_in_active_statusResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_IS_NOT_IN_ACTIVE_STATUS.toString();
    final String min_interval_constraint_violationResult =
        SchedulingExceptionMessage.MIN_INTERVAL_CONSTRAINT_VIOLATION.toString();
    final String start_time_in_back_dateResult =
        SchedulingExceptionMessage.START_TIME_IN_BACK_DATE.toString();
    final String end_time_start_time_diff_violationResult =
        SchedulingExceptionMessage.END_TIME_START_TIME_DIFF_VIOLATION.toString();
    final String start_time_after_end_timeResult =
        SchedulingExceptionMessage.START_TIME_AFTER_END_TIME.toString();
    final String invalid_execution_statusResult =
        SchedulingExceptionMessage.INVALID_EXECUTION_STATUS.toString();
    final String schedule_task_id_does_not_existResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_EXIST.toString();
    final String schedule_task_already_existResult =
        SchedulingExceptionMessage.SCHEDULE_TASK_ALREADY_EXIST.toString();

    // Verify the results
    assertEquals("40001", schedule_task_id_does_not_matchResult);
    assertEquals("40002", missing_configResult);
    assertEquals("40003", invalid_executor_typeResult);
    assertEquals("40004", invalid_schedule_typeResult);
    assertEquals("40005", invalid_predefined_expressionResult);
    assertEquals("40006", invalid_schedule_expression_result);
    assertEquals("40007", schedule_task_is_not_scheduledResult);
    assertEquals("40008", schedule_task_is_in_active_statusResult);
    assertEquals("40009", schedule_task_is_not_in_active_statusResult);
    assertEquals("40011", min_interval_constraint_violationResult);
    assertEquals("40012", start_time_in_back_dateResult);
    assertEquals("40013", end_time_start_time_diff_violationResult);
    assertEquals("40014", start_time_after_end_timeResult);
    assertEquals("40015", invalid_execution_statusResult);
    assertEquals("40401", schedule_task_id_does_not_existResult);
    assertEquals("40901", schedule_task_already_existResult);
  }

  @Test
  public void testValueOf() {
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_MATCH,
        SchedulingExceptionMessage.valueOf(40001));
    assertEquals(SchedulingExceptionMessage.MISSING_CONFIG,
        SchedulingExceptionMessage.valueOf(40002));
    assertEquals(SchedulingExceptionMessage.INVALID_EXECUTOR_TYPE,
        SchedulingExceptionMessage.valueOf(40003));
    assertEquals(SchedulingExceptionMessage.INVALID_SCHEDULE_TYPE,
        SchedulingExceptionMessage.valueOf(40004));
    assertEquals(SchedulingExceptionMessage.INVALID_PREDEFINED_EXPRESSION,
        SchedulingExceptionMessage.valueOf(40005));
    assertEquals(SchedulingExceptionMessage.INVALID_SCHEDULE_EXPRESSION,
        SchedulingExceptionMessage.valueOf(40006));
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_IS_NOT_SCHEDULED,
        SchedulingExceptionMessage.valueOf(40007));
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_IS_IN_ACTIVE_STATUS,
        SchedulingExceptionMessage.valueOf(40008));
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_IS_NOT_IN_ACTIVE_STATUS,
        SchedulingExceptionMessage.valueOf(40009));
    assertEquals(SchedulingExceptionMessage.MIN_INTERVAL_CONSTRAINT_VIOLATION,
        SchedulingExceptionMessage.valueOf(40011));
    assertEquals(SchedulingExceptionMessage.START_TIME_IN_BACK_DATE,
        SchedulingExceptionMessage.valueOf(40012));
    assertEquals(SchedulingExceptionMessage.END_TIME_START_TIME_DIFF_VIOLATION,
        SchedulingExceptionMessage.valueOf(40013));
    assertEquals(SchedulingExceptionMessage.START_TIME_AFTER_END_TIME,
        SchedulingExceptionMessage.valueOf(40014));
    assertEquals(SchedulingExceptionMessage.INVALID_EXECUTION_STATUS,
        SchedulingExceptionMessage.valueOf(40015));
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_EXIST,
        SchedulingExceptionMessage.valueOf(40401));
    assertEquals(SchedulingExceptionMessage.SCHEDULE_TASK_ALREADY_EXIST,
        SchedulingExceptionMessage.valueOf(40901));

  }
}
