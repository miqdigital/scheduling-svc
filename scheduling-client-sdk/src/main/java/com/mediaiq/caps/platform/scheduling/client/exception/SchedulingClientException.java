package com.mediaiq.caps.platform.scheduling.client.exception;

import com.mediaiq.caps.platform.scheduling.client.model.SchedulingExceptionResponse;

/**
 * The type Scheduling exception.
 */
public class SchedulingClientException extends Exception {

  private transient SchedulingExceptionResponse response; //NOSONAR

  /**
   * Instantiates a new Scheduling exception.
   */
  public SchedulingClientException() {
    super();
  }

  /**
   * Instantiates a new Scheduling exception.
   *
   * @param response the response
   */
  public SchedulingClientException(SchedulingExceptionResponse response) {
    super(response.toString());
    this.response = response;
  }

  /**
   * Instantiates a new Scheduling exception.
   *
   * @param message the message
   */
  public SchedulingClientException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Scheduling exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public SchedulingClientException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Gets response.
   *
   * @return the response
   */
  public SchedulingExceptionResponse getResponse() {
    return response;
  }
}
