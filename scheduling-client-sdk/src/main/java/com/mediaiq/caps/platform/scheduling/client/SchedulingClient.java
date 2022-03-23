package com.mediaiq.caps.platform.scheduling.client;

import java.time.ZonedDateTime;
import java.util.List;

import com.mediaiq.caps.platform.scheduling.client.exception.SchedulingClientException;
import com.mediaiq.caps.platform.scheduling.client.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.client.model.Trigger;

/**
 * The interface Scheduling client.
 */
public interface SchedulingClient {
  /**
   * Gets schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask getScheduleTask(String scheduleTaskId) throws SchedulingClientException;


  /**
   * Gets schedule task.
   *
   * @param scheduleTaskIds the schedule task ids
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ScheduleTask> getScheduleTask(List<String> scheduleTaskIds) throws SchedulingClientException;


  /**
   * Gets all schedule task.
   *
   * @param groupId the group id
   * @return the all schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ScheduleTask> getAllScheduleTask(String groupId) throws SchedulingClientException;


  /**
   * Update schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask updateScheduleTask(ScheduleTask scheduleTask) throws SchedulingClientException;


  /**
   * Create schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask createScheduleTask(ScheduleTask scheduleTask) throws SchedulingClientException;


  /**
   * Migrate schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask migrateScheduleTask(ScheduleTask scheduleTask) throws SchedulingClientException;


  /**
   * Delete schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @throws SchedulingClientException the scheduling client exception
   */
  void deleteScheduleTask(String scheduleTaskId) throws SchedulingClientException;


  /**
   * Execute schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @throws SchedulingClientException the scheduling client exception
   */
  void executeScheduleTask(String scheduleTaskId) throws SchedulingClientException;


  /**
   * Gets next runs info.
   *
   * @param trigger the trigger
   * @return the next runs info
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ZonedDateTime> getNextRunsInfo(Trigger trigger) throws SchedulingClientException;

}
