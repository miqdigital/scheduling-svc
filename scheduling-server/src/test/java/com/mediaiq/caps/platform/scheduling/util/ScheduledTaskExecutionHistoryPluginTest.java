package com.mediaiq.caps.platform.scheduling.util;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.springframework.context.ApplicationContext;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTaskExecutionHistory;
import com.mediaiq.caps.platform.scheduling.repository.ScheduleTaskExecutionHistoryRepository;
import com.mediaiq.caps.platform.scheduling.service.SchedulerService;

public class ScheduledTaskExecutionHistoryPluginTest {

  @Mock
  ScheduleTaskExecutionHistoryRepository mockScheduleTaskExecutionHistoryRepository;
  @Mock
  JobExecutionContext context;
  @Mock
  ApplicationContext applicationContext;
  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  @Mock
  private SchedulerService mockSchedulerService;
  @Mock
  private Scheduler mockScheduler;
  private ScheduledTaskExecutionHistoryPlugin scheduledTaskExecutionHistoryPluginUnderTest;
  private Date startTime, fireTime;
  private String strFireTime, strStartTime;

  @Before
  public void setUp() throws ParseException {
    initMocks(this);
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    strFireTime = "2020-01-01T01:00:00Z";
    strStartTime = "2020-01-01T01:02:00Z";
    fireTime = df.parse(strFireTime);
    startTime = df.parse(strStartTime);
    scheduledTaskExecutionHistoryPluginUnderTest = new ScheduledTaskExecutionHistoryPlugin();
  }

  @Test
  public void testJobToBeExecuted() {

    JobDataMap jobDataMap = new JobDataMap();
    when(context.getScheduledFireTime()).thenReturn(fireTime);
    when(context.getFireTime()).thenReturn(startTime);
    when(context.getMergedJobDataMap()).thenReturn(jobDataMap);

    // Run the test
    scheduledTaskExecutionHistoryPluginUnderTest.jobToBeExecuted(context);

    // Verify the results
    String strActualFireTime = (String) jobDataMap.get(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY);
    ZonedDateTime actualFireTime = ZonedDateTime.parse(strActualFireTime, Constants.isoDateTime);

    String strActualStartTime = (String) jobDataMap.get(Constants.SCHEDULE_TASK_START_TIME_KEY);
    ZonedDateTime actualStartTime = ZonedDateTime.parse(strActualStartTime, Constants.isoDateTime);

    assertTrue(actualFireTime.toInstant().equals(fireTime.toInstant()));
    assertTrue(actualStartTime.toInstant().equals(startTime.toInstant()));
  }

  @Test
  public void testJobWasExecuted() {
    // Setup
    scheduledTaskExecutionHistoryPluginUnderTest.setApplicationContext(applicationContext);
    final JobExecutionException jobException = new JobExecutionException("msg", false);
    JobDetailImpl jobDetail = new JobDetailImpl();
    JobKey jobKey = new JobKey("name", "group");
    jobDetail.setKey(jobKey);
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("jobName", "jobName");
    jobDetail.setJobDataMap(jobDataMap);

    jobDataMap.put(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY, strFireTime);
    jobDataMap.put(Constants.SCHEDULE_TASK_START_TIME_KEY, strStartTime);


    when(context.getJobDetail()).thenReturn(jobDetail);
    when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
    when(applicationContext.getBean(ScheduleTaskExecutionHistoryRepository.class)).thenReturn(
        mockScheduleTaskExecutionHistoryRepository);
    // Run the test
    scheduledTaskExecutionHistoryPluginUnderTest.jobWasExecuted(context, jobException);

    // Verify the results
    verify(mockScheduleTaskExecutionHistoryRepository).save(
        any(ScheduleTaskExecutionHistory.class));
  }
}
