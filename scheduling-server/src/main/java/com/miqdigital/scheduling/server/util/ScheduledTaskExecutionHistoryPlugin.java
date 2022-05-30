package com.miqdigital.scheduling.server.util;

import static com.miqdigital.scheduling.server.util.Constants.isoDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.miqdigital.scheduling.commons.model.ExecutionStatusEnum;
import com.miqdigital.scheduling.commons.model.ScheduleTaskExecutionHistory;
import com.miqdigital.scheduling.server.repository.ScheduleTaskExecutionHistoryRepository;

/**
 * Persists each ScheduledTask's execution in database
 */
@Service
public class ScheduledTaskExecutionHistoryPlugin
    implements SchedulerPlugin, JobListener, ApplicationContextAware {

  public static ApplicationContext applicationContext;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  ScheduleTaskExecutionHistoryRepository scheduleTaskExecutionHistoryRepository;
  private String name;

  public ScheduledTaskExecutionHistoryPlugin() {
    logger.info("Execution history plugin initialized");
  }

  public void initialize(String pname, Scheduler scheduler, ClassLoadHelper classLoadHelper)
      throws SchedulerException {
    this.name = pname;
    scheduler.getListenerManager().addJobListener(this, EverythingMatcher.allJobs());
  }

  public void start() {
    //do Nothing
  }

  public void shutdown() {
    //do Nothing
  }


  public String getName() {
    return name;
  }

  /**
   * @see org.quartz.JobListener#jobToBeExecuted(JobExecutionContext)
   */
  public void jobToBeExecuted(JobExecutionContext context) {
    Date startTime = context.getFireTime();
    Date scheduledTime = context.getScheduledFireTime();
    String zonedStartTime = ZonedDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault())
        .truncatedTo(ChronoUnit.SECONDS).format(isoDateTime);
    String zonedScheduledTime =
        ZonedDateTime.ofInstant(scheduledTime.toInstant(), ZoneId.systemDefault())
            .truncatedTo(ChronoUnit.SECONDS).format(isoDateTime);

    context.getMergedJobDataMap().put(Constants.SCHEDULE_TASK_START_TIME_KEY, zonedStartTime);
    context.getMergedJobDataMap()
        .put(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY, zonedScheduledTime);
  }

  /**
   * @see org.quartz.JobListener#jobWasExecuted(JobExecutionContext, JobExecutionException)
   */
  public void jobWasExecuted(JobExecutionContext jobExecutionContext,
      JobExecutionException jobException) {
    String scheduleTaskId = jobExecutionContext.getJobDetail().getKey().getName();
    String currentFireTime = (String) jobExecutionContext.getMergedJobDataMap()
        .get(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY);
    String startTime = (String) jobExecutionContext.getMergedJobDataMap()
        .get(Constants.SCHEDULE_TASK_START_TIME_KEY);
    String endTime = ZonedDateTime.now().format(isoDateTime);
    boolean isSuccess = true;
    String errorMessage = null;
    if (jobException != null) {
      errorMessage = jobException.getMessage();
      isSuccess = false;
    }
    jobCompleted(scheduleTaskId, currentFireTime, startTime, endTime, isSuccess, errorMessage);
  }

  /**
   * @see org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
   */
  public void jobExecutionVetoed(JobExecutionContext context) {
    //do Nothing
  }

  /**
   * Job completed
   */
  private void jobCompleted(String scheduleTaskId, String fireTime, String startDateTime,
      String endDateTime, boolean isSuccess, String message) {
    ExecutionStatusEnum executionStatus =
        isSuccess ? ExecutionStatusEnum.SUCCESS : ExecutionStatusEnum.FAILURE;

    ZonedDateTime currentFireTime = ZonedDateTime.parse(fireTime, isoDateTime);
    ZonedDateTime startTime = ZonedDateTime.parse(startDateTime, isoDateTime);
    ZonedDateTime endTime = ZonedDateTime.parse(endDateTime, isoDateTime);

    ScheduleTaskExecutionHistory scheduleTaskExecutionHistory =
        ScheduleTaskExecutionHistory.builder().scheduleTaskId(scheduleTaskId)
            .executionStatus(executionStatus).scheduleDateTime(currentFireTime)
            .startDateTime(startTime).endDateTime(endTime).errorMessage(message).build();
    applicationContext.getBean(ScheduleTaskExecutionHistoryRepository.class)
        .save(scheduleTaskExecutionHistory);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
