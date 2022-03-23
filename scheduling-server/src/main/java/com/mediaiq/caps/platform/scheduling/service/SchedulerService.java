package com.mediaiq.caps.platform.scheduling.service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;

@Validated
public interface SchedulerService {
  ScheduleTask create(final ScheduleTask scheduleTask) throws SchedulerException;

  void updateJob(final ScheduleTask scheduleTask) throws SchedulerException;

  void delete(final ScheduleTask scheduleTask) throws SchedulerException;

  void schedule(final ScheduleTask scheduleTask) throws SchedulerException;

  void reSchedule(final ScheduleTask scheduleTask) throws SchedulerException;

  void pause(final ScheduleTask scheduleTask) throws SchedulerException;

  void resume(final ScheduleTask scheduleTask) throws SchedulerException;

  void executeNow(final ScheduleTask scheduleTask) throws SchedulerException;

  ZonedDateTime getLastExecution(final ScheduleTask scheduleTask) throws SchedulerException;

  ZonedDateTime getNextExecution(final ScheduleTask scheduleTask) throws SchedulerException;

  /**
   * Get the first execution after current time without any misfire
   * @param scheduledTask ScheduleTask
   * @return ZonedDateTime
   * @throws SchedulerException
   */
  ZonedDateTime getFirstValidExecution(final ScheduleTask scheduledTask,
      final ZonedDateTime currentTime) throws SchedulerException;

  /**
   * Get list of execution times
   * @param trigger trigger for the schedule
   * @param limit number of records to get
   * @return List of execution times
   * @throws ParseException
   */
  List<ZonedDateTime> getExecutionTimes(Trigger trigger, int limit) throws ParseException;

  /**
   * Get max 50 list of execution times
   * @param trigger trigger for the schedule
   * @return list fo execution times
   * @throws ParseException
   */
  default List<ZonedDateTime> getExecutionTimes(Trigger trigger) throws ParseException {
    return getExecutionTimes(trigger,50);
  }

}

