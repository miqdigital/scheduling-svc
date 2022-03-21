package com.mediaiq.caps.platform.scheduling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mediaiq.caps.platform.scheduling.commons.utils.SchedulingExceptionMessage;

public abstract class SchedulingException extends RuntimeException {

  SchedulingException(String message) {
    super(message);
  }

  SchedulingException(String message, Throwable cause) {
    super(message, cause);
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public static class NotFoundException extends SchedulingException {
    public NotFoundException(SchedulingExceptionMessage message) {
      super(message.toString());
    }
  }


  @ResponseStatus(value = HttpStatus.CONFLICT)
  public static class ConflictException extends SchedulingException {
    public ConflictException(SchedulingExceptionMessage message) {
      super(message.toString());
    }
  }


  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public static class BadRequestException extends SchedulingException {
    public BadRequestException(SchedulingExceptionMessage message) {
      super(message.toString());
    }
  }


  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public static class ServiceException extends SchedulingException {
    public ServiceException(String message, Throwable cause) {
      super(message, cause);
    }
  }


  public static class UnImplementedException extends SchedulingException {
    public UnImplementedException(String message) {
      super(message);
    }
  }
}
