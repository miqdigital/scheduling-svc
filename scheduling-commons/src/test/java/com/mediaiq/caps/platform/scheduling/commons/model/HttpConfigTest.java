package com.mediaiq.caps.platform.scheduling.commons.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class HttpConfigTest {

  @Mock
  private HttpConfigBody mockBody;

  private HttpConfig httpConfigUnderTest;

  @Before
  public void setUp() {
    initMocks(this);
    httpConfigUnderTest =
        new HttpConfig("url", HttpConfig.MethodEnum.GET, new HashMap<>(), mockBody);
  }

  @Test
  public void testEquals() {
    // Setup
    HttpConfig expected =
        new HttpConfig("url", HttpConfig.MethodEnum.GET, new HashMap<>(), mockBody);
    // Run the test
    final boolean result = httpConfigUnderTest.equals(expected);
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    // Setup
    HttpConfig expected =
        new HttpConfig("url", HttpConfig.MethodEnum.GET, new HashMap<>(), mockBody);
    // Run the test
    assertEquals(expected.hashCode(), httpConfigUnderTest.hashCode());
  }

  @Test
  public void testToString() {
    // Setup
    String expected =
        "class HttpConfig {\n" + "    url: url\n" + "    method: GET\n" + "    headers: {}\n"
            + "    body: mockBody\n" + "}";
    // Run the test
    final String result = httpConfigUnderTest.toString();

    // Verify the results
    assertEquals(expected, result);
  }
}
