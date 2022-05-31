package com.miqdigital.scheduling.client.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miqdigital.scheduling.client.exception.SchedulingClientException;
import com.miqdigital.scheduling.client.model.SchedulingExceptionResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * The type Response processor.
 */
public class ResponseProcessor {

  private static final ObjectMapper mapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private ResponseProcessor() {
    //Empty constructor
  }

  /**
   * Process t.
   *
   * @param <T> the type parameter
   * @param response the response
   * @return the t
   * @throws SchedulingClientException the scheduling client exception
   */
  public static <T> T process(Response<T> response) throws SchedulingClientException {
    /*If Response if empty throw null pointer exception*/
    if (response == null) {
      throw new SchedulingClientException("Server returned an empty response");
    }

    /*Check if status code was 2XX*/
    if (response.isSuccessful()) {
      return response.body();
    } else {
      /*Check if status was 4** user error then parse body and throw exception*/
      getErrorResponse(response.errorBody(), response.code());
    }
    return null;
  }

  private static void getErrorResponse(ResponseBody errorResponse, int code)
      throws SchedulingClientException {
    if (errorResponse == null) {
      throw new SchedulingClientException("Response body is empty, statusCode: " + code);
    }
    SchedulingExceptionResponse schedulingExceptionResponse = null;

    //Read error string
    String errorString;
    try {
      errorString = errorResponse.string();
    } catch (IOException e) {
      throw new SchedulingClientException("Server returned an invalid response");
    }

    //Cast and throw
    try {
      schedulingExceptionResponse =
          mapper.readValue(errorString, SchedulingExceptionResponse.class);
      throw new SchedulingClientException(schedulingExceptionResponse);
    } catch (JsonProcessingException e) {
      throw new SchedulingClientException("Server returned an invalid response: " + errorString);
    }
  }
}
