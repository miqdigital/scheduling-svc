package com.mediaiq.caps.platform.scheduling.service.impl;

import static com.mediaiq.caps.platform.scheduling.util.Constants.JOB_RUN_WITH_EXECUTE_NOW;
import static com.mediaiq.caps.platform.scheduling.util.Constants.MAX_DIFF_START_CURR_MINUTES;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.jobs.NativeJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.mediaiq.caps.platform.scheduling.commons.model.CurlConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.Executor;
import com.mediaiq.caps.platform.scheduling.commons.model.HttpConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.MessagingConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.Schedule;
import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;
import com.mediaiq.caps.platform.scheduling.commons.utils.SchedulingExceptionMessage;
import com.mediaiq.caps.platform.scheduling.exception.SchedulingException;
import com.mediaiq.caps.platform.scheduling.jobs.HttpJob;
import com.mediaiq.caps.platform.scheduling.jobs.MessagingJob;
import com.mediaiq.caps.platform.scheduling.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SchedulerFactoryBean schedulerFactory;

  @Override
  public ScheduleTask create(final ScheduleTask scheduleTask) throws SchedulerException {
    Scheduler scheduler = schedulerFactory.getScheduler();
    JobDetail jobDetail = createJobDetail(scheduleTask);
    //keep replace as false to throw exception in case job with same name and group exists
    scheduler.addJob(jobDetail, false);
    return scheduleTask;
  }

  @Override
  public void delete(final ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    Scheduler scheduler = schedulerFactory.getScheduler();
    JobKey jobKey = JobKey.jobKey(jobId, jobGroup);
    TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
    scheduler.unscheduleJob(triggerKey);
    scheduler.deleteJob(jobKey);
  }

  @Override
  public void updateJob(final ScheduleTask scheduleTask) throws SchedulerException {
    Scheduler scheduler = schedulerFactory.getScheduler();
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    JobKey jobKey = JobKey.jobKey(jobId, jobGroup);

    //throw exception if job doesn't already exist
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    if(jobDetail == null){
      logger.error("No Job exists for group {} and id {} in quartz",jobGroup,jobId);
      throw new SchedulingException.NotFoundException(SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_EXIST);
    }
    JobDetail jobDetailupdated = createJobDetail(scheduleTask);
    //keep replace as true to replace existing Job
    scheduler.addJob(jobDetailupdated, true);
  }

  @Override
  public void schedule(final ScheduleTask scheduleTask) throws SchedulerException {
    Scheduler scheduler = schedulerFactory.getScheduler();
    org.quartz.Trigger cronTrigger = createTriggerForJob(scheduleTask);
    scheduler.scheduleJob(cronTrigger);
  }

  @Override
  public void pause(ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    JobKey jobKey = new JobKey(jobId, jobGroup);
    Scheduler scheduler = schedulerFactory.getScheduler();
    scheduler.pauseJob(jobKey);
  }

  @Override
  public void resume(ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    JobKey jobKey = new JobKey(jobId, jobGroup);
    Scheduler scheduler = schedulerFactory.getScheduler();
    scheduler.resumeJob(jobKey);
  }

  @Override
  public void executeNow(final ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    JobKey jobKey = new JobKey(jobId, jobGroup);
    Scheduler scheduler = schedulerFactory.getScheduler();
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.putAsString(JOB_RUN_WITH_EXECUTE_NOW,true);
    scheduler.triggerJob(jobKey,jobDataMap);
  }

  public ZonedDateTime getLastExecution(ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    Scheduler scheduler = schedulerFactory.getScheduler();
    TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);

    Date previousFireTime = null;
    org.quartz.Trigger trigger = scheduler.getTrigger(triggerKey);
    //By default keep lastExecution as epoch 0 i.e. 1st Jan 1970
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
    if (trigger != null && trigger.getPreviousFireTime() != null) {
      previousFireTime = trigger.getPreviousFireTime();
      zonedDateTime = ZonedDateTime.ofInstant(previousFireTime.toInstant(), ZoneId.systemDefault());
    }
    return zonedDateTime;
  }

  public ZonedDateTime getNextExecution(ScheduleTask scheduleTask) throws SchedulerException {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    Scheduler scheduler = schedulerFactory.getScheduler();
    TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);

    Date nextFireTime = null;
    org.quartz.Trigger trigger = scheduler.getTrigger(triggerKey);
    //By default keep nextExecution as epoch 0 i.e. 1st Jan 1970
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
    if (trigger != null && trigger.getNextFireTime() != null) {
      nextFireTime = trigger.getNextFireTime();
      zonedDateTime = ZonedDateTime.ofInstant(nextFireTime.toInstant(), ZoneId.systemDefault());
    }
    return zonedDateTime;
  }

  @Override
  public void reSchedule(final ScheduleTask scheduleTask) throws SchedulerException {
    Scheduler scheduler = schedulerFactory.getScheduler();
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
    org.quartz.Trigger cronTrigger = createTriggerForJob(scheduleTask);
    Date nextScheduleDate = null;
    //After the last execution (i.e. after endDateTime) trigger gets deleted , hence schedule new
    if (scheduler.getTrigger(triggerKey) == null) {
      nextScheduleDate = scheduler.scheduleJob(cronTrigger);
      logger.debug("Create trigger with trigger {} ", cronTrigger);
    } else {
      logger.debug("Replace trigger key {} with trigger {} ", triggerKey, cronTrigger);
      nextScheduleDate = scheduler.rescheduleJob(triggerKey, cronTrigger);
    }
    logger.info("Next trigger {} at date {}", scheduleTask.getId(), nextScheduleDate);
  }

  private JobDetail createJobDetail(ScheduleTask scheduleTask) {
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    Executor executor = scheduleTask.getExecutor();
    if (executor == null) {
      throw new SchedulingException.BadRequestException(SchedulingExceptionMessage.MISSING_CONFIG);
    }
    Executor.ExecutorType executorType = executor.getType();
    Class<? extends Job> jobClass;
    JobDetail jobDetail = null;
    switch (executorType) {
      case CURL:
        jobClass = NativeJob.class;
        jobDetail =
            JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroup).storeDurably().build();
        CurlConfig curlConfig = executor.getCurlConfig();
        if (curlConfig == null) {
          throw new SchedulingException.BadRequestException(
              SchedulingExceptionMessage.MISSING_CONFIG);
        }
        String command = curlConfig.getCurlCommand();
        jobDetail.getJobDataMap().put(NativeJob.PROP_COMMAND, command);
        jobDetail.getJobDataMap().put(NativeJob.PROP_CONSUME_STREAMS, "true");
        break;
      case HTTP:
        jobClass = HttpJob.class;
        jobDetail =
            JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroup).storeDurably().build();
        HttpConfig httpConfig = executor.getHttpConfig();
        if (httpConfig == null) {
          throw new SchedulingException.BadRequestException(
              SchedulingExceptionMessage.MISSING_CONFIG);
        }
        jobDetail.getJobDataMap().put(jobClass.getName(), httpConfig);
        break;
      case MESSAGING:
        jobClass = MessagingJob.class;
        jobDetail =
            JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroup).storeDurably().build();
        MessagingConfig messagingConfig = executor.getMessagingConfig();
        if (messagingConfig == null) {
          throw new SchedulingException.BadRequestException(
              SchedulingExceptionMessage.MISSING_CONFIG);
        }
        jobDetail.getJobDataMap().put(jobClass.getName(), messagingConfig);
        break;
      default:
        throw new SchedulingException.BadRequestException(
            SchedulingExceptionMessage.INVALID_EXECUTOR_TYPE);
    }
    return jobDetail;
  }

  private org.quartz.Trigger createTriggerForJob(ScheduleTask scheduleTask)
      throws SchedulerException {
    int intervalinMin = 0;
    String jobId = scheduleTask.getId();
    String jobGroup = scheduleTask.getGroup();
    Scheduler scheduler = schedulerFactory.getScheduler();
    JobKey jobKey = JobKey.jobKey(jobId, jobGroup);

    TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
    Trigger scheduleTaskTrigger = scheduleTask.getTrigger();
    Schedule.ScheduleType scheduleType = scheduleTaskTrigger.getSchedule().getType();

    ZoneId zone = scheduleTaskTrigger.getStartDateTime().getZone();
    //Never start in back date, set starttime = first execution time for the schedule
    ZonedDateTime startDateTime = getFirstValidExecution(scheduleTask, ZonedDateTime.now());
    ZonedDateTime endDateTime = scheduleTaskTrigger.getEndDateTime();
    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
    org.quartz.Trigger jobTrigger = null;
    switch (scheduleType) {
      case CRONEXPRESSION:
        String cronExpression = scheduleTaskTrigger.getSchedule().getValue();
        jobTrigger =
            getCronTrigger(triggerKey, cronExpression, zone, startDateTime, endDateTime, jobDetail);
        break;
      case EVERY_N_MINUTES:
        intervalinMin = Integer.parseInt(scheduleTaskTrigger.getSchedule().getValue());
        jobTrigger =
            getSimpleTrigger(triggerKey, intervalinMin, startDateTime, endDateTime, jobDetail);
        break;
      case EVERY_N_MONTHS:
        int intervalInMonths = Integer.parseInt(scheduleTaskTrigger.getSchedule().getValue());
        jobTrigger =
            getCalendarTrigger(triggerKey, intervalInMonths, zone, startDateTime, endDateTime,
                jobDetail);
        break;
      case PREDEFINEDEXPRESSION:
        intervalinMin = getIntervalInMinutes(scheduleTaskTrigger.getSchedule().getValue());
        jobTrigger =
            getSimpleTrigger(triggerKey, intervalinMin, startDateTime, endDateTime, jobDetail);
        break;
      default:
        throw new SchedulingException.BadRequestException(
            SchedulingExceptionMessage.INVALID_SCHEDULE_TYPE);
    }
    return jobTrigger;
  }

  private org.quartz.Trigger getCalendarTrigger(TriggerKey triggerKey, int intervalInMonths,
      ZoneId zone, ZonedDateTime startDateTime, ZonedDateTime endDateTime, JobDetail jobDetail) {

    ScheduleBuilder<CalendarIntervalTrigger> scheduleBuilder =
        CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
            .inTimeZone(TimeZone.getTimeZone(zone)).withIntervalInMonths(intervalInMonths)
            .withMisfireHandlingInstructionFireAndProceed();

    return TriggerBuilder.newTrigger().withIdentity(triggerKey)
        .startAt(Date.from(startDateTime.toInstant())).endAt(Date.from(endDateTime.toInstant()))
        .withSchedule(scheduleBuilder)
        .forJob(jobDetail).build();
  }

  private org.quartz.Trigger getSimpleTrigger(TriggerKey triggerKey, int intervalinMin,
      ZonedDateTime startDateTime, ZonedDateTime endDateTime, JobDetail jobDetail) {

    ScheduleBuilder<SimpleTrigger> schedBuilder =
        SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(intervalinMin)
            .withMisfireHandlingInstructionFireNow()
            .repeatForever();
    return TriggerBuilder.newTrigger().withIdentity(triggerKey)
        .startAt(Date.from(startDateTime.toInstant())).endAt(Date.from(endDateTime.toInstant()))
        .withSchedule(schedBuilder)
        .forJob(jobDetail)
        .build();
  }

  private org.quartz.Trigger getCronTrigger(TriggerKey triggerKey, String cronExpression,
      ZoneId zone, ZonedDateTime startDateTime, ZonedDateTime endDateTime, JobDetail jobDetail) {
    CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
        .withMisfireHandlingInstructionFireAndProceed().inTimeZone(TimeZone.getTimeZone(zone));
    return TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobDetail)
        .startAt(Date.from(startDateTime.toInstant())).endAt(Date.from(endDateTime.toInstant()))
        .withSchedule(cronScheduleBuilder).build();
  }

  private int getIntervalInMinutes(String predefinedExpressionStr) {
    Schedule.PreDefinedExpression predefinedExpression = null;
    try {
      predefinedExpression = Schedule.PreDefinedExpression.fromValue(predefinedExpressionStr);
    } catch (IllegalArgumentException exception) {
      logger.error("Invalid {} predefined expression {}", predefinedExpressionStr, exception);
      throw new SchedulingException.BadRequestException(
          SchedulingExceptionMessage.INVALID_PREDEFINED_EXPRESSION);
    }
    int intervalInMinutes = 0;
    switch (predefinedExpression) {
      case EVERY15MIN:
        intervalInMinutes = 15;
        break;
      case HOURLY:
        intervalInMinutes = 60;
        break;
      case EVERY3HOUR:
        intervalInMinutes = 3 * 60;
        break;
      case DAILY:
        intervalInMinutes = 24 * 60;
        break;
      case WEEKLY:
        intervalInMinutes = 7 * 24 * 60;
        break;
      case FORTNIGHTLY:
        intervalInMinutes = 15 * 24 * 60;
        break;
      case MONTHLY:
        intervalInMinutes = 30 * 24 * 60;
        break;
      default:
        logger.error("Invalid {} predefined expression", predefinedExpressionStr);
        throw new SchedulingException.BadRequestException(
            SchedulingExceptionMessage.INVALID_PREDEFINED_EXPRESSION);
    }
    return intervalInMinutes;
  }

  @Override
  public ZonedDateTime getFirstValidExecution(ScheduleTask scheduledTask, ZonedDateTime currentTime)
      throws SchedulerException {
    try {
      int executionLimit = 100;
      //Get the execution which is greater than the current time with tolerance
      ZonedDateTime baseStartTime = currentTime.minusMinutes(MAX_DIFF_START_CURR_MINUTES);
      Trigger scheduledTaskTrigger = scheduledTask.getTrigger();
      List<ZonedDateTime> executionTimes = getExecutionTimes(scheduledTaskTrigger, executionLimit);
      int sizeOfExecution = executionTimes.size();

      while (sizeOfExecution > 1 && !executionTimes.get(sizeOfExecution-1).isAfter(baseStartTime)) {
        Trigger trigger = Trigger.builder().startDateTime(executionTimes.get(sizeOfExecution - 1))
            .endDateTime(scheduledTaskTrigger.getEndDateTime())
            .schedule(scheduledTaskTrigger.getSchedule()).build();
        executionTimes = getExecutionTimes(trigger, executionLimit);
        sizeOfExecution = executionTimes.size();
      }

      for (ZonedDateTime executeTime : executionTimes) {
        if (executeTime.isAfter(baseStartTime)) {
          return executeTime;
        }
      }
    } catch (ParseException e) {
      throw new SchedulerException("Exception while getting execution times for the schedule", e);
    }
    throw new SchedulerException("This schedule will never fire");
  }

  @Override
  public List<ZonedDateTime> getExecutionTimes(Trigger trigger, int limit) throws ParseException {
    int intervalInMinutes = 0;
    String value = trigger.getSchedule().getValue();
    ZonedDateTime stTime = trigger.getStartDateTime();
    ZonedDateTime endTime = trigger.getEndDateTime();

    Date startDateTime = Date.from(stTime.toInstant());
    Date endDateTime = Date.from(trigger.getEndDateTime().toInstant());
    ZoneId zone = stTime.getZone();
    List<ZonedDateTime> result = null;


    Schedule.ScheduleType type = trigger.getSchedule().getType();
    switch (type) {
      case CRONEXPRESSION:
        result = getExecutionTimesForCronTrigger(startDateTime, endDateTime, zone, value, limit);
        break;
      case PREDEFINEDEXPRESSION:
        intervalInMinutes = getIntervalInMinutes(value);
        result = getExecutionTimesForSimpleTrigger(stTime, endTime, intervalInMinutes, limit);
        break;
      case EVERY_N_MONTHS:
        int intervalInMonths = Integer.parseInt(value);
        result =
            getExecutionTimesForCalendarTrigger(stTime, endTime, zone, intervalInMonths, limit);
        break;
      case EVERY_N_MINUTES:
        intervalInMinutes = Integer.valueOf(value);
        result = getExecutionTimesForSimpleTrigger(stTime, endTime, intervalInMinutes, limit);
        break;
      default:
        throw new SchedulingException.BadRequestException(
            SchedulingExceptionMessage.INVALID_SCHEDULE_TYPE);
    }
    return result;
  }


  private List<ZonedDateTime> getExecutionTimesForSimpleTrigger(ZonedDateTime startDateTime,
      ZonedDateTime endDateTime, int intervalInMinutes, int limit) {
    int current = 0;
    List<ZonedDateTime> result = new ArrayList<>(limit);
    ZonedDateTime next = startDateTime;

    while (current <= limit && next != null && next.compareTo(endDateTime) <= 0) {
      current++;
      result.add(next);
      next = next.plusMinutes(intervalInMinutes);
    }
    return result;
  }

  private List<ZonedDateTime> getExecutionTimesForCalendarTrigger(ZonedDateTime stTime,
      ZonedDateTime endTime,ZoneId zoneId, int intervalInMonths, int limit) {
    List<Date> executionTimes = new ArrayList<>();
    CalendarIntervalTriggerImpl calendarIntervalTrigger = new CalendarIntervalTriggerImpl();
    calendarIntervalTrigger.setStartTime(Date.from(stTime.toInstant()));
    calendarIntervalTrigger.setEndTime(Date.from(endTime.toInstant()));
    calendarIntervalTrigger.setTimeZone(TimeZone.getTimeZone(zoneId));
    calendarIntervalTrigger.setRepeatInterval(intervalInMonths);
    calendarIntervalTrigger.setRepeatIntervalUnit(DateBuilder.IntervalUnit.MONTH);

    int current = 0;
    Date nextFireTime = calendarIntervalTrigger.computeFirstFireTime(new AnnualCalendar());
    while (current < limit && nextFireTime != null
        && nextFireTime.compareTo(calendarIntervalTrigger.getEndTime()) <= 0) {
      current++;
      executionTimes.add(nextFireTime);
      nextFireTime = calendarIntervalTrigger.getFireTimeAfter(nextFireTime);
    }
    List<ZonedDateTime> result = new ArrayList<>(executionTimes.size());
    executionTimes.forEach(
        curr -> result.add(ZonedDateTime.ofInstant(curr.toInstant(), ZoneId.systemDefault())));
    return result;
  }

  private List<ZonedDateTime> getExecutionTimesForCronTrigger(Date startDateTime, Date endDateTime,
      ZoneId zone, String cronExpression, int limit) throws ParseException {
    List<Date> executionTimes = new ArrayList<>();
    int current = 0;
    CronExpression cron = new CronExpression(cronExpression);
    TimeZone timeZone = TimeZone.getTimeZone(zone);
    cron.setTimeZone(timeZone);
    if(cron.isSatisfiedBy(startDateTime)) {
      executionTimes.add(startDateTime);
    }
    Date next = cron.getNextValidTimeAfter(startDateTime);
    while (current <= limit && next != null && next.compareTo(endDateTime) <= 0) {
      current++;
      executionTimes.add(next);
      next = cron.getNextValidTimeAfter(next);
    }
    List<ZonedDateTime> result = new ArrayList<>(executionTimes.size());
    executionTimes.forEach(curr -> {
      result.add(ZonedDateTime.ofInstant(curr.toInstant(), ZoneId.systemDefault()));
    });
    return result;
  }

}
