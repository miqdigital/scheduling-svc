package com.mediaiq.caps.platform.scheduling.util;

import static com.mediaiq.caps.platform.scheduling.util.Constants.JOB_RUN_WITH_EXECUTE_NOW;
import static com.mediaiq.caps.platform.scheduling.util.Constants.isoDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;
import com.mediaiq.caps.platform.scheduling.exception.SchedulingException;

/**
 * The type Utils.
 */
public class Utils {
  /**
   * Convert schedule time to the user defined tz.
   *
   * @param scheduleTask the schedule task
   */
  public static void convertScheduleTimezone(ScheduleTask scheduleTask) {
    try {
      if (scheduleTask.getZoneID() != null) {
        ZoneId zoneId = ZoneId.of(scheduleTask.getZoneID());
        Trigger trigger = scheduleTask.getTrigger();
        //Update the start and end time in the timezone with zone id for which request came
        ZonedDateTime startDateTime = trigger.getStartDateTime();
        ZonedDateTime endDateTime = trigger.getEndDateTime();
        ZonedDateTime sameInstantStart = startDateTime.withZoneSameInstant(zoneId);
        ZonedDateTime sameInstantEnd = endDateTime.withZoneSameInstant(zoneId);
        trigger.setStartDateTime(sameInstantStart);
        trigger.setEndDateTime(sameInstantEnd);
      }
    } catch (NullPointerException e) {
      throw new SchedulingException.ServiceException("Null schedule task received for conversion",
          e);
    }
  }

  /**
   * Generates meta header map for callbacks.
   *
   * @param jobExecutionContext JobExecutionContext
   * @return Header Map
   */
  public static Map<String, String> getHeaderMap(JobExecutionContext jobExecutionContext) {
    Map<String, String> headersMap = new HashMap<>();
    JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
    String startTime =
        (String) mergedJobDataMap.getOrDefault(Constants.SCHEDULE_TASK_START_TIME_KEY, "none");
    String scheduledTime =
        (String) mergedJobDataMap.getOrDefault(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY, "none");

    headersMap.put(Constants.HTTP_HEADER_SCHEDULE_TASK_ID,
        jobExecutionContext.getJobDetail().getKey().getName());
    headersMap.put(Constants.HTTP_HEADER_SCHEDULE_TASK_START_TIME, startTime);
    headersMap.put(Constants.HTTP_HEADER_SCHEDULE_TASK_SCHEDULED_TIME, scheduledTime);
    Date nextFireTime = jobExecutionContext.getNextFireTime();
    headersMap.put(Constants.HTTP_HEADER_SCHEDULE_TASK_IS_LAST_RUN,
        Boolean.toString(isLastRun(jobExecutionContext)));
    if (nextFireTime != null) {
      headersMap.put(Constants.HTTP_HEADER_SCHEDULE_TASK_NEXT_RUN_TIME,
          ZonedDateTime.ofInstant(nextFireTime.toInstant(), ZoneId.systemDefault())
              .truncatedTo(ChronoUnit.SECONDS).format(isoDateTime));
    }
    return headersMap;
  }

  /**
   * Checks if the given run is last run or not, only if the execution is scheduled.
   *
   * @param jobExecutionContext JobExecutionContext
   * @return boolean
   */
  private static boolean isLastRun(JobExecutionContext jobExecutionContext) {
    boolean isExecuteNow =
        jobExecutionContext.getMergedJobDataMap().getBooleanValue(JOB_RUN_WITH_EXECUTE_NOW);
    Date nextFireTime = jobExecutionContext.getNextFireTime();
    return nextFireTime == null && !isExecuteNow;
  }
}
