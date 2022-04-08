package com.mediaiq.caps.platform.scheduling.client.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The type Scheduling exception response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingExceptionResponse {

  private Date timestamp = new Date();
  private int status;
  private String error;
  private String code;
  private String message;
  private String exception;
  private String path;

  /**
   * Instantiates a new Scheduling exception response.
   */
  public SchedulingExceptionResponse() {
    //Default empty constructor
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * Gets status.
   *
   * @return the status
   */
  public int getStatus() {
    return status;
  }

  /**
   * Gets error.
   *
   * @return the error
   */
  public String getError() {
    return error;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Gets exception.
   *
   * @return the exception
   */
  public String getException() {
    return exception;
  }

  @Override
  public String toString() {
    return "SchedulingExceptionResponse{" + "timestamp=" + timestamp + ", status=" + status
        + ", error='" + error + '\'' + ", code='" + code + '\'' + ", message='" + message + '\''
        + ", exception='" + exception + '\'' + ", path='" + path + '\'' + '}';
  }
}
