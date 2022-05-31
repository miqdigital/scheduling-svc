package com.miqdigital.scheduling.server.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.miqdigital.scheduling.commons.model.SchedulingExceptionResponse;
import com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage;

@ControllerAdvice(basePackages = "com.miqdigital.scheduling.server")
public class SchedulingExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(SchedulingExceptionHandler.class);

  @ExceptionHandler(SchedulingException.ServiceException.class)
  public ResponseEntity handleSchedulerExceptionOfServer(HttpServletRequest request,
      SchedulingException.ServiceException ex) {
    logger.error("SchedulingException.ServiceException ", ex);
    return getResponseEntityServiceException(request, ex);
  }

  private ResponseEntity getResponseEntityServiceException(HttpServletRequest request,
      SchedulingException.ServiceException ex) {
    //TODO: Refactoring needed for throwing status code based on error message processing
    // make use of application defined status codes for this
    Throwable cause = ex.getCause();
    String message = ex.getMessage() + " Cause : " + cause;
    if (message.contains("will never fire")) {
      SchedulingExceptionMessage schedulingExceptionMessage =
          SchedulingExceptionMessage.NEVER_FIRE_SCHEDULE;
      SchedulingExceptionResponse body =
          new SchedulingExceptionResponse(HttpStatus.BAD_REQUEST, schedulingExceptionMessage,
              request.getRequestURI());
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    } else {
      SchedulingExceptionResponse body =
          new SchedulingExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, message,
              request.getRequestURI(), cause.getClass().getName());
      return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ExceptionHandler(SchedulingException.class)
  public ResponseEntity handleSchedulerExceptionOfClient(HttpServletRequest request,
      SchedulingException ex) {
    logger.error("SchedulingException ", ex);
    ResponseStatus responseStatus =
        AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
    HttpStatus status = responseStatus.code();
    String code = ex.getMessage();
    SchedulingExceptionMessage message = SchedulingExceptionMessage.valueOf(Integer.valueOf(code));
    SchedulingExceptionResponse body =
        new SchedulingExceptionResponse(status, message, request.getRequestURI());
    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
  public ResponseEntity handleValidationError(HttpServletRequest request, Exception ex) {
    logger.error("Exception ", ex);
    ResponseStatus responseStatus =
        AnnotationUtils.findAnnotation(SchedulingException.BadRequestException.class,
            ResponseStatus.class);
    HttpStatus status = responseStatus.code();
    StringBuilder validationError = new StringBuilder();

    if (ex instanceof MethodArgumentNotValidException) {
      BindingResult result = ((MethodArgumentNotValidException) ex).getBindingResult();
      List<FieldError> fieldErrors = result.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        validationError.append("{" + fieldError.getObjectName() + " " + fieldError.getField() + " "
            + fieldError.getCode() + "},");
      }
    } else {
      validationError.append(ex.getCause());
    }
    SchedulingExceptionResponse body =
        new SchedulingExceptionResponse(status, validationError.toString(), request.getRequestURI(),
            ex.getClass().toString());
    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleGenericException(HttpServletRequest request, Exception ex) {
    logger.error("Exception ", ex);
    //Mark ex as Server error
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String message = ex.getClass().toString();
    SchedulingExceptionResponse body =
        new SchedulingExceptionResponse(status, message, request.getRequestURI(), ex.toString());
    return new ResponseEntity<>(body, status);
  }
}
