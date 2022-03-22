package com.mediaiq.caps.platform.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTaskExecutionHistoryResponseTest {

  private ScheduleTaskExecutionHistoryResponse scheduleTaskExecutionHistoryResponseUnderTest;

  @Before
  public void setUp() {
    scheduleTaskExecutionHistoryResponseUnderTest =
        new ScheduleTaskExecutionHistoryResponse(0, "scheduleTaskId",
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ExecutionStatusEnum.SUCCESS, "errorMessage");
  }

  @Test
  public void testEquals() {
    // Setup
    ScheduleTaskExecutionHistoryResponse expected =
        new ScheduleTaskExecutionHistoryResponse(0, "scheduleTaskId",
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ExecutionStatusEnum.SUCCESS, "errorMessage");
    // Run the test
    final boolean result = scheduleTaskExecutionHistoryResponseUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    ScheduleTaskExecutionHistoryResponse expected =
        new ScheduleTaskExecutionHistoryResponse(0, "scheduleTaskId",
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ExecutionStatusEnum.SUCCESS, "errorMessage");

    // Run the test
    final int result = scheduleTaskExecutionHistoryResponseUnderTest.hashCode();

    // Verify the results
    assertEquals(expected.hashCode(), result);
  }

  @Test
  public void testToString() {
    // Setup
    String expected = "class ScheduleTaskExecutionHistoryResponse {\n" + "    executionId: 0\n"
        + "    scheduleTaskId: scheduleTaskId\n" + "    scheduleDateTime: 2017-01-01T00:00Z[UTC]\n"
        + "    startDateTime: 2017-01-01T00:00Z[UTC]\n"
        + "    endDateTime: 2017-01-01T00:00Z[UTC]\n" + "    executionStatus: SUCCESS\n"
        + "    errorMessage: errorMessage\n" + "}";
    // Run the test
    final String result = scheduleTaskExecutionHistoryResponseUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
