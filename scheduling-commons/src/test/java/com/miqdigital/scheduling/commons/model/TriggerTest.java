package com.miqdigital.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TriggerTest {

  @Mock
  private Schedule mockSchedule;

  private Trigger triggerUnderTest;

  @Before
  public void setUp() {
    initMocks(this);
    triggerUnderTest =
        new Trigger(ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            mockSchedule);
  }

  @Test
  public void testEquals() {
    // Setup
    Trigger expected =
        new Trigger(ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            mockSchedule);
    // Run the test
    final boolean result = triggerUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    Trigger expected =
        new Trigger(ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC")),
            mockSchedule);

    // Run the test
    final int result = triggerUnderTest.hashCode();

    // Verify the results
    assertEquals(expected.hashCode(), result);
  }

  @Test
  public void testToString() {
    // Setup
    String expected = "class Trigger {\n" + "    startDateTime: 2017-01-01T00:00Z[UTC]\n"
        + "    endDateTime: 2017-01-01T00:00Z[UTC]\n" + "    schedule: mockSchedule\n" + "}";
    // Run the test
    final String result = triggerUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
