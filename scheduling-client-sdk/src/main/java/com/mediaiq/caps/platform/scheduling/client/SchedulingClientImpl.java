package com.mediaiq.caps.platform.scheduling.client;

import static com.mediaiq.caps.platform.scheduling.client.constants.ErrorMessage.FAILED_API_CALL_ERROR_MESSAGE;
import static com.mediaiq.caps.platform.scheduling.client.constants.ErrorMessage.SCHEDULE_ID_NULL_ERROR_MESSAGE;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediaiq.caps.platform.scheduling.client.exception.SchedulingClientException;
import com.mediaiq.caps.platform.scheduling.client.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.client.model.Trigger;
import com.mediaiq.caps.platform.scheduling.client.service.SchedulingRest;
import com.mediaiq.caps.platform.scheduling.client.utils.ResponseProcessor;
import com.mediaiq.caps.platform.trackingtags.Environment;
import com.mediaiq.caps.platform.trackingtags.TrackingTags;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * The type Scheduling client.
 */
public class SchedulingClientImpl implements SchedulingClient {

  private SchedulingRest schedulingRest;
  private Map<String, String> defaultTrackingTagsHeaders;

  /**
   * Instantiates a new Scheduling client.
   *
   * @param schedulingConfig the scheduling config
   * @param defaultTrackingTags the default tracking tags
   */
  public SchedulingClientImpl(SchedulingConfig schedulingConfig, TrackingTags defaultTrackingTags) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    OkHttpClient client = getOkHttpClient(schedulingConfig);
    Retrofit retrofit =
        new Retrofit.Builder().baseUrl(schedulingConfig.getUrl())
            .client(client).addConverterFactory(JacksonConverterFactory.create(mapper)).build();
    schedulingRest = retrofit.create(SchedulingRest.class);
    this.defaultTrackingTagsHeaders = defaultTrackingTags.generateHeaders();
  }

  @Override
  public ScheduleTask getScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException {
    if (StringUtils.isEmpty(scheduleTaskId)) {
      throw new IllegalArgumentException(SCHEDULE_ID_NULL_ERROR_MESSAGE);
    }
    Call<ScheduleTask> scheduleTaskCall =
        schedulingRest.getScheduleTask(trackingTags, scheduleTaskId);
    Response<ScheduleTask> scheduleTaskResponse = null;
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public List<ScheduleTask> getAllScheduleTask(Map<String, String> trackingTags, String groupId)
      throws SchedulingClientException {
    Call<List<ScheduleTask>> scheduleTaskCall =
        schedulingRest.getAllScheduleTask(trackingTags, groupId);
    Response<List<ScheduleTask>> scheduleTaskResponse = null;
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public List<ScheduleTask> getScheduleTask(List<String> scheduleTaskIds,
      Map<String, String> trackingTags) throws SchedulingClientException {
    Call<List<ScheduleTask>> scheduleTaskCall =
        schedulingRest.getScheduleTask(trackingTags, scheduleTaskIds);
    Response<List<ScheduleTask>> scheduleTaskResponse = null;
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public ScheduleTask updateScheduleTask(ScheduleTask scheduleTask,
      Map<String, String> trackingTags) throws SchedulingClientException {
    String scheduleTaskId = scheduleTask.getId();
    if (StringUtils.isEmpty(scheduleTaskId)) {
      throw new IllegalArgumentException(SCHEDULE_ID_NULL_ERROR_MESSAGE);
    }
    Response<ScheduleTask> scheduleTaskResponse;
    Call<ScheduleTask> scheduleTaskCall =
        schedulingRest.updateScheduleTask(trackingTags, scheduleTaskId, scheduleTask);
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public ScheduleTask createScheduleTask(ScheduleTask scheduleTask,
      Map<String, String> trackingTags) throws SchedulingClientException {
    Call<ScheduleTask> scheduleTaskCall =
        schedulingRest.createScheduleTask(trackingTags, scheduleTask);
    Response<ScheduleTask> scheduleTaskResponse = null;
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public ScheduleTask migrateScheduleTask(ScheduleTask scheduleTask,
      Map<String, String> trackingTags) throws SchedulingClientException {
    Call<ScheduleTask> scheduleTaskCall =
        schedulingRest.migrateScheduleTask(trackingTags, scheduleTask);
    Response<ScheduleTask> scheduleTaskResponse = null;
    try {
      scheduleTaskResponse = scheduleTaskCall.execute();
      ResponseProcessor.process(scheduleTaskResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return scheduleTaskResponse.body();
  }

  @Override
  public void deleteScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException {
    if (StringUtils.isEmpty(scheduleTaskId)) {
      throw new IllegalArgumentException(SCHEDULE_ID_NULL_ERROR_MESSAGE);
    }
    Call<ResponseBody> responseBodyCall =
        schedulingRest.deleteScheduleTask(trackingTags, scheduleTaskId);
    Response<ResponseBody> responseBodyResponse;
    try {
      responseBodyResponse = responseBodyCall.execute();
      ResponseProcessor.process(responseBodyResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
  }

  @Override
  public void executeScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException {
    if (StringUtils.isEmpty(scheduleTaskId)) {
      throw new IllegalArgumentException(SCHEDULE_ID_NULL_ERROR_MESSAGE);
    }
    Call<ResponseBody> responseBodyCall =
        schedulingRest.executeScheduleTask(trackingTags, scheduleTaskId);
    Response<ResponseBody> responseBodyResponse;
    try {
      responseBodyResponse = responseBodyCall.execute();
      ResponseProcessor.process(responseBodyResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
  }

  @Override
  public List<ZonedDateTime> getNextRunsInfo(Trigger trigger,
      Map<String, String> trackingTags) throws SchedulingClientException {
    Call<List<ZonedDateTime>> responseBodyCall = schedulingRest.getNextRunsInfo(trackingTags, trigger);
    Response<List<ZonedDateTime>> responseBodyResponse;
    try {
      responseBodyResponse = responseBodyCall.execute();
      ResponseProcessor.process(responseBodyResponse);
    } catch (IOException e) {
      throw new SchedulingClientException(FAILED_API_CALL_ERROR_MESSAGE, e);
    }
    return responseBodyResponse.body();
  }

  @Override
  public Map<String, String> getDefaultTrackingTagsHeaders() {
    return defaultTrackingTagsHeaders;
  }

  //Utility methods go here

  private OkHttpClient getOkHttpClient(SchedulingConfig schedulingConfig) {
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    String authorizationKey = schedulingConfig.getAuthorizationKey() != null ?
        schedulingConfig.getAuthorizationKey() :
        "";
    httpClient.addInterceptor(chain -> {
      Request original = chain.request();
      Request request = original.newBuilder().method(original.method(), original.body()).headers(
          original.headers().newBuilder().add("api-gateway-token", authorizationKey).build())
          .build();
      return chain.proceed(request);
    });
    return httpClient.build();
  }
  
}
