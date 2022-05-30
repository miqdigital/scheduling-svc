package com.miqdigital.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {

  private Schedule scheduleUnderTest;

  @Before
  public void setUp() {
    scheduleUnderTest = new Schedule(Schedule.ScheduleType.CRONEXPRESSION, "value");
  }

  @Test
  public void testEquals() {
    // Setup
    Schedule expected = new Schedule(Schedule.ScheduleType.CRONEXPRESSION, "value");
    // Run the test
    final boolean result = scheduleUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    Schedule expected = new Schedule(Schedule.ScheduleType.CRONEXPRESSION, "value");
    // Run the test
    final int result = scheduleUnderTest.hashCode();

    // Verify the results
    assertEquals(expected.hashCode(), result);
  }

  @Test
  public void testToString() {
    // Setup
    String expected =
        "class Schedule {\n" + "    type: cronExpression\n" + "    value: value\n" + "}";
    // Run the test
    final String result = scheduleUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
