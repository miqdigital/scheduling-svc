package com.mediaiq.caps.platform.scheduling.client;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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
   * @param trackingTags the tracking tags
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask getScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Gets schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default ScheduleTask getScheduleTask(String scheduleTaskId) throws SchedulingClientException {
    return getScheduleTask(scheduleTaskId, getDefaultTrackingTagsHeaders());
  }

  /**
   * Gets schedule task.
   *
   * @param scheduleTaskIds the schedule task ids
   * @param trackingTags    the tracking tags
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ScheduleTask> getScheduleTask(List<String> scheduleTaskIds, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Gets schedule task.
   *
   * @param scheduleTaskIds the schedule task ids
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default List<ScheduleTask> getScheduleTask(List<String> scheduleTaskIds)
      throws SchedulingClientException {
    return getScheduleTask(scheduleTaskIds, getDefaultTrackingTagsHeaders());
  }

  /**
   * Gets all schedule task.
   *
   * @param trackingTags the tracking tags
   * @param groupId      the group id
   * @return the all schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ScheduleTask> getAllScheduleTask(Map<String, String> trackingTags, String groupId)
      throws SchedulingClientException;

  /**
   * Gets all schedule task.
   *
   * @param groupId the group id
   * @return the all schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default List<ScheduleTask> getAllScheduleTask(String groupId) throws SchedulingClientException {
    return getAllScheduleTask(getDefaultTrackingTagsHeaders(), groupId);
  }

  /**
   * Update schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @param trackingTags the tracking tags
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask updateScheduleTask(ScheduleTask scheduleTask, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Update schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default ScheduleTask updateScheduleTask(ScheduleTask scheduleTask)
      throws SchedulingClientException {
    return updateScheduleTask(scheduleTask, getDefaultTrackingTagsHeaders());
  }

  /**
   * Create schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @param trackingTags the tracking tags
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask createScheduleTask(ScheduleTask scheduleTask, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Create schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default ScheduleTask createScheduleTask(ScheduleTask scheduleTask)
      throws SchedulingClientException {
    return createScheduleTask(scheduleTask, getDefaultTrackingTagsHeaders());
  }

  /**
   * Migrate schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @param trackingTags the tracking tags
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  ScheduleTask migrateScheduleTask(ScheduleTask scheduleTask, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Migrate schedule task schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  default ScheduleTask migrateScheduleTask(ScheduleTask scheduleTask)
      throws SchedulingClientException {
    return migrateScheduleTask(scheduleTask, getDefaultTrackingTagsHeaders());
  }


  /**
   * Delete schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @param trackingTags the tracking tags
   * @throws SchedulingClientException the scheduling client exception
   */
  void deleteScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Delete schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @throws SchedulingClientException the scheduling client exception
   */
  default void deleteScheduleTask(String scheduleTaskId) throws SchedulingClientException {
    deleteScheduleTask(scheduleTaskId, getDefaultTrackingTagsHeaders());
  }

  /**
   * Execute schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @param trackingTags the tracking tags
   * @throws SchedulingClientException the scheduling client exception
   */
  void executeScheduleTask(String scheduleTaskId, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Execute schedule task.
   *
   * @param scheduleTaskId the schedule task id
   * @throws SchedulingClientException the scheduling client exception
   */
  default void executeScheduleTask(String scheduleTaskId) throws SchedulingClientException {
    executeScheduleTask(scheduleTaskId, getDefaultTrackingTagsHeaders());
  }

  /**
   * Gets next runs info.
   *
   * @param trigger      the trigger
   * @param trackingTags the tracking tags
   * @return the next runs info
   * @throws SchedulingClientException the scheduling client exception
   */
  List<ZonedDateTime> getNextRunsInfo(Trigger trigger, Map<String, String> trackingTags)
      throws SchedulingClientException;

  /**
   * Gets next runs info.
   *
   * @param trigger the trigger
   * @return the next runs info
   * @throws SchedulingClientException the scheduling client exception
   */
  default List<ZonedDateTime> getNextRunsInfo(Trigger trigger)
      throws SchedulingClientException {
    return getNextRunsInfo(trigger, getDefaultTrackingTagsHeaders());
  }

  /**
   * Gets default tracking tags.
   *
   * @return the default tracking tags
   */
  Map<String, String> getDefaultTrackingTagsHeaders();
}
