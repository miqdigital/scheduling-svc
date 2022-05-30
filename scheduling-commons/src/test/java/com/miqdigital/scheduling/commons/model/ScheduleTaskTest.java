package com.miqdigital.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ScheduleTaskTest {

  @Mock
  Executor executor;
  @Mock
  Trigger trigger;
  private ScheduleTask scheduleTaskUnderTest;

  @Before
  public void setUp() {
    scheduleTaskUnderTest =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).build();

  }

  @Test
  public void testEquals() {
    // Setup
    ScheduleTask expected =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).build();

    // Run the test
    final boolean result = scheduleTaskUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    ScheduleTask expected =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).build();


    // Run the test
    final int result = scheduleTaskUnderTest.hashCode();

    // Verify the results
    assertEquals(expected.hashCode(), result);
  }

  @Test
  public void testToString() {
    // Setup
    String expected = "class ScheduleTask {\n" + "    id: null\n" + "    name: anytask\n"
        + "    group: anygroup\n" + "    description: anydescription\n"
        + "    creator: anycreator\n" + "    status: ACTIVE\n" + "    created: null\n"
        + "    updated: null\n" + "    lastExecution: null\n" + "    nextExecution: null\n"
        + "    trigger: null\n" + "    executor: null\n" + "    zoneId: null\n" + "}";
    // Run the test
    final String result = scheduleTaskUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
