package com.mediaiq.caps.platform.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HttpConfigBodyTest {

  private HttpConfigBody httpConfigBodyUnderTest;

  @Before
  public void setUp() {
    httpConfigBodyUnderTest = new HttpConfigBody("contentType", "content");
  }

  @Test
  public void testEquals() {
    // Setup
    HttpConfigBody expected = new HttpConfigBody("contentType", "content");
    // Run the test
    final boolean result = httpConfigBodyUnderTest.equals(expected);

    // Verify the results
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    HttpConfigBody expected = new HttpConfigBody("contentType", "content");
    // Run the test
    final int result = httpConfigBodyUnderTest.hashCode();

    // Verify the results
    assertEquals(expected.hashCode(), result);
  }

  @Test
  public void testToString() {
    // Setup
    String expected =
        "class HttpConfigBody {\n" + "    contentType: contentType\n" + "    content: content\n"
            + "}";
    // Run the test
    final String result = httpConfigBodyUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
