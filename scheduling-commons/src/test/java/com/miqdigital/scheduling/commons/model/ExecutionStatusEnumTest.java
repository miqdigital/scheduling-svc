package com.miqdigital.scheduling.commons.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionStatusEnumTest {

  @Test
  public void testToString() {
    // Run the test
    final String successResult = ExecutionStatusEnum.SUCCESS.toString();
    final String failureResult = ExecutionStatusEnum.FAILURE.toString();

    // Verify the results
    assertEquals("SUCCESS", successResult);
    assertEquals("FAILURE", failureResult);
  }

  @Test
  public void testFromValue() {
    assertEquals(ExecutionStatusEnum.SUCCESS, ExecutionStatusEnum.fromValue("SUCCESS"));
  }
}
