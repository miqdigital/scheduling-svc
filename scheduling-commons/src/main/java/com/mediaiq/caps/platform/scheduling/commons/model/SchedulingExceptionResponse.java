package com.mediaiq.caps.platform.scheduling.commons.model;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mediaiq.caps.platform.scheduling.commons.utils.SchedulingExceptionMessage;

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
   * Instantiates a new Scheduling exception response.
   *
   * @param status the status
   * @param message the message
   * @param path the path
   */
  public SchedulingExceptionResponse(HttpStatus status, SchedulingExceptionMessage message,
      String path) {
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.code = Integer.toString(message.getCode());
    this.message = message.getMessage();
    this.path = path;
  }

  /**
   * Instantiates a new Scheduling exception response.
   *
   * @param status the status
   * @param message the message
   * @param path the path
   * @param exception the exception
   */
  public SchedulingExceptionResponse(HttpStatus status, String message, String path,
      String exception) {
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
    this.path = path;
    this.exception = exception;
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
