package com.miqdigital.scheduling.client.service;

import java.time.ZonedDateTime;
import java.util.List;

import com.miqdigital.scheduling.client.model.ScheduleTask;
import com.miqdigital.scheduling.client.model.Trigger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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
   * @param scheduleTask the schedule task
   * @return the call
   */
  @POST("schedule-task")
  Call<ScheduleTask> createScheduleTask(
      @Body
          ScheduleTask scheduleTask);

  /**
   * Migrate schedule task call.
   *
   * @param scheduleTask the schedule task
   * @return the call
   */
  @POST("schedule-task/migrate")
  Call<ScheduleTask> migrateScheduleTask(
      @Body
          ScheduleTask scheduleTask);

  /**
   * Update schedule task call.
   *
   * @param id the id
   * @param scheduleTask the schedule task
   * @return the call
   */
  @PUT("schedule-task/{id}")
  Call<ScheduleTask> updateScheduleTask(
      @Path("id")
          String id,
      @Body
          ScheduleTask scheduleTask);

  /**
   * Gets all schedule task.
   *
   * @param groupId the group id
   * @return the all schedule task
   */
  @GET("schedule-task")
  Call<List<ScheduleTask>> getAllScheduleTask(
      @Query("groupId")
          String groupId);

  /**
   * Gets schedule task.
   *
   * @param scheduleTaskIds the schedule task ids
   * @return the schedule task
   */
  @GET("schedule-task/bulk")
  Call<List<ScheduleTask>> getScheduleTask(
      @Query("scheduleTaskIds")
          List<String> scheduleTaskIds);

  /**
   * Gets schedule task.
   *
   * @param scheduleId the schedule id
   * @return the schedule task
   */
  @GET("schedule-task/{id}")
  Call<ScheduleTask> getScheduleTask(
      @Path("id")
          String scheduleId);

  /**
   * Delete schedule task call.
   *
   * @param id the id
   * @return the call
   */
  @DELETE("schedule-task/{id}")
  Call<ResponseBody> deleteScheduleTask(
      @Path("id")
          String id);

  /**
   * Execute schedule task call.
   *
   * @param id the id
   * @return the call
   */
  @POST("schedule-task/{id}/execute-now")
  Call<ResponseBody> executeScheduleTask(
      @Path("id")
          String id);

  /**
   * Gets next runs info.
   *
   * @param trigger the trigger
   * @return the next runs info
   */
  @POST("schedule-task/runs-info")
  Call<List<ZonedDateTime>> getNextRunsInfo(
      @Body
      Trigger trigger);
}
