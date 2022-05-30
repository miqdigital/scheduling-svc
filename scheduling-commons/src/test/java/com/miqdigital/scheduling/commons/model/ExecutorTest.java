package com.miqdigital.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ExecutorTest {

  @Mock
  private CurlConfig mockCurlConfig;
  @Mock
  private HttpConfig mockHttpConfig;

  private Executor executorUnderTest;

  @Before
  public void setUp() {
    initMocks(this);
    executorUnderTest =
        new Executor(Executor.ExecutorType.CURL, mockCurlConfig, mockHttpConfig);
  }

  @Test
  public void testEquals() {
    // Setup
    Executor expected =
        new Executor(Executor.ExecutorType.CURL, mockCurlConfig, mockHttpConfig);
    // Run the test
    final boolean result = executorUnderTest.equals(expected);
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    Executor expected =
        new Executor(Executor.ExecutorType.CURL, mockCurlConfig, mockHttpConfig);
    // Run the test
    assertEquals(expected.hashCode(), executorUnderTest.hashCode());
  }

  @Test
  public void testToString() {
    // Setup
    String expected = "class Executor {\n" + "    type: curl\n" + "    curlConfig: mockCurlConfig\n"
        + "    httpConfig: mockHttpConfig\n" + "}";
    // Run the test
    final String result = executorUnderTest.toString();
    // Verify the results
    assertEquals(expected, result);
  }
}
