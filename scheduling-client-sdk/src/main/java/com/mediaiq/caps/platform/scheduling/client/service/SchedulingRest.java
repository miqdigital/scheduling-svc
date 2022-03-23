package com.mediaiq.caps.platform.scheduling.client.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.mediaiq.caps.platform.scheduling.client.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.client.model.Trigger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The interface Scheduling rest.
 */
public interface SchedulingRest {

  /**
   * Create schedule task call.
   *
   * @param trackingHeaders the tracking headers
   * @param scheduleTask the schedule task
   * @return the call
   */
  @POST("schedule-task")
  Call<ScheduleTask> createScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Body
          ScheduleTask scheduleTask);

  /**
   * Migrate schedule task call.
   *
   * @param trackingHeaders the tracking headers
   * @param scheduleTask    the schedule task
   * @return the call
   */
  @POST("schedule-task/migrate")
  Call<ScheduleTask> migrateScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Body
          ScheduleTask scheduleTask);

  /**
   * Update schedule task call.
   *
   * @param trackingHeaders the tracking headers
   * @param id the id
   * @param scheduleTask the schedule task
   * @return the call
   */
  @PUT("schedule-task/{id}")
  Call<ScheduleTask> updateScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Path("id")
          String id,
      @Body
          ScheduleTask scheduleTask);

  /**
   * Gets all schedule task.
   *
   * @param trackingHeaders the tracking headers
   * @param groupId         the group id
   * @return the all schedule task
   */
  @GET("schedule-task")
  Call<List<ScheduleTask>> getAllScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Query("groupId")
          String groupId);

  /**
   * Gets schedule task.
   *
   * @param trackingHeaders the tracking headers
   * @param scheduleTaskIds the schedule task ids
   * @return the schedule task
   */
  @GET("schedule-task/bulk")
  Call<List<ScheduleTask>> getScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Query("scheduleTaskIds")
          List<String> scheduleTaskIds);

  /**
   * Gets schedule task.
   *
   * @param trackingHeaders the tracking headers
   * @param scheduleId the schedule id
   * @return the schedule task
   */
  @GET("schedule-task/{id}")
  Call<ScheduleTask> getScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Path("id")
          String scheduleId);

  /**
   * Delete schedule task call.
   *
   * @param trackingHeaders the tracking headers
   * @param id the id
   * @return the call
   */
  @DELETE("schedule-task/{id}")
  Call<ResponseBody> deleteScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Path("id")
          String id);

  /**
   * Execute schedule task call.
   *
   * @param trackingHeaders the tracking headers
   * @param id the id
   * @return the call
   */
  @POST("schedule-task/{id}/execute-now")
  Call<ResponseBody> executeScheduleTask(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Path("id")
          String id);

  /**
   * Gets next runs info.
   *
   * @param trackingHeaders the tracking headers
   * @param trigger         the trigger
   * @return the next runs info
   */
  @POST("schedule-task/runs-info")
  Call<List<ZonedDateTime>> getNextRunsInfo(
      @HeaderMap
          Map<String, String> trackingHeaders,
      @Body
          Trigger trigger);
}
