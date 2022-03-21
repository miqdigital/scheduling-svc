package com.mediaiq.caps.platform.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CurlConfigTest {

  private CurlConfig curlConfigUnderTest;
  private String curlCommand = "curlCommand";

  @Before
  public void setUp() {
    curlConfigUnderTest = new CurlConfig(curlCommand);
  }

  @Test
  public void testEquals() {
    // Setup
    CurlConfig expected = new CurlConfig(curlCommand);
    // Run the test
    assertTrue(curlConfigUnderTest.equals(curlConfigUnderTest));
    assertTrue(expected.equals(curlConfigUnderTest));
  }

  @Test
  public void testHashCode() {
    // Setup
    CurlConfig expected = new CurlConfig(curlCommand);
    // Run the test
    assertEquals(expected.hashCode(), curlConfigUnderTest.hashCode());
  }

  @Test
  public void testToString() {
    // Setup
    String expected = "class CurlConfig {\n" + "    curlCommand: curlCommand\n" + "}";
    // Run the test
    final String result = curlConfigUnderTest.toString();
    // Verify the results
    assertEquals(expected, result);
  }

}
