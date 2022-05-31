package com.miqdigital.scheduling.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.miqdigital.scheduling.commons.model.Schedule;
import com.miqdigital.scheduling.commons.model.ScheduleTask;
import com.miqdigital.scheduling.server.exception.SchedulingException;
import com.miqdigital.scheduling.server.util.EntityMocksHelper;

public class SchedulerServiceImplTest {

  private ScheduleTask cronScheduleTask;
  private ScheduleTask simpleScheduleTask;
  private ScheduleTask invalidScheduleTask;
  @Mock
  private SchedulerFactoryBean mockSchedulerFactory;

  @Mock
  private Scheduler mockScheduler;

  @InjectMocks
  private SchedulerServiceImpl schedulerServiceImplUnderTest;

  @Before
  public void setUp() {
    initMocks(this);
    cronScheduleTask = EntityMocksHelper.createCronScheduleTask();
    simpleScheduleTask = EntityMocksHelper.createSimpleScheduleTask();
    invalidScheduleTask = EntityMocksHelper.createInvalidScheduleTask();
    when(mockSchedulerFactory.getScheduler()).thenReturn(mockScheduler);
  }

  @Test
  public void testCreate() throws Exception {
    // Setup
    final ScheduleTask expectedResult = cronScheduleTask;
    final ScheduleTask expectedResult2 = simpleScheduleTask;

    // Run the test
    final ScheduleTask result = schedulerServiceImplUnderTest.create(cronScheduleTask);
    final ScheduleTask result2 = schedulerServiceImplUnderTest.create(simpleScheduleTask);

    // Verify the results
    assertEquals(expectedResult, result);
    assertEquals(expectedResult2, result2);
  }

  @Test(expected = SchedulingException.BadRequestException.class)
  public void testCreate_ThrowsBadRequestException() throws Exception {
    // Run the test
    schedulerServiceImplUnderTest.create(invalidScheduleTask);
  }

  @Test(expected = SchedulerException.class)
  public void testCreate_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler)
        .addJob(any(JobDetail.class), anyBoolean());

    // Run the test
    schedulerServiceImplUnderTest.create(cronScheduleTask);
    schedulerServiceImplUnderTest.create(simpleScheduleTask);
  }

  @Test
  public void testDelete() throws Exception {
    // Run the test
    schedulerServiceImplUnderTest.delete(cronScheduleTask);
    schedulerServiceImplUnderTest.delete(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).deleteJob(any(JobKey.class));
    verify(mockScheduler, times(2)).unscheduleJob(any(TriggerKey.class));
  }

  @Test(expected = SchedulerException.class)
  public void testDelete_ThrowsSchedulerException() throws Exception {
    //    doThrow(SchedulerException.class).when(mockScheduler).unscheduleJob(any(TriggerKey.class));
    when(mockScheduler.unscheduleJob(any(TriggerKey.class))).thenThrow(SchedulerException.class);
    // Run the test
    schedulerServiceImplUnderTest.delete(cronScheduleTask);
  }

  @Test
  public void testUpdateJob() throws Exception {
    // Setup
    JobKey jobKey = new JobKey("anykey", "anygroup");
    JobDetail jobDetail = mock(JobDetail.class);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(jobDetail);

    // Run the test
    schedulerServiceImplUnderTest.updateJob(cronScheduleTask);
    schedulerServiceImplUnderTest.updateJob(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).addJob(any(JobDetail.class), anyBoolean());
  }

  @Test(expected = SchedulingException.class)
  public void testUpdateJob_ThrowsSchedulingException() throws Exception {
    doThrow(new SchedulerException()).when(mockScheduler)
        .addJob(any(JobDetail.class), anyBoolean());
    // Run the test
    schedulerServiceImplUnderTest.updateJob(cronScheduleTask);
  }

  @Test
  public void testGetExecutionTimes() throws Exception {
    // Setup
    final com.miqdigital.scheduling.commons.model.Trigger trigger =
        new com.miqdigital.scheduling.commons.model.Trigger(
            ZonedDateTime.now().minusDays(10), ZonedDateTime.now().plusDays(10),
            new Schedule(Schedule.ScheduleType.EVERY_N_MINUTES, "1440"));

    // Run the test
    final List<ZonedDateTime> result = schedulerServiceImplUnderTest.getExecutionTimes(trigger);

    // Verify the results
    assertEquals(21, result.size());
  }

  @Test(expected = ParseException.class)
  public void testGetExecutionTimes_ThrowsParseException() throws Exception {
    // Setup
    final com.miqdigital.scheduling.commons.model.Trigger trigger =
        new com.miqdigital.scheduling.commons.model.Trigger(
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z")),
            ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z")),
            new Schedule(Schedule.ScheduleType.CRONEXPRESSION, "value"));

    // Run the test
    schedulerServiceImplUnderTest.getExecutionTimes(trigger);
  }

  @Test
  public void testGetFirstValidExecution() throws Exception {
    // Setup
    com.miqdigital.scheduling.commons.model.Trigger trigger =
        com.miqdigital.scheduling.commons.model.Trigger.builder()
            .startDateTime(ZonedDateTime.now().minusDays(10))
            .endDateTime(ZonedDateTime.now().plusMinutes(20)).schedule(
                Schedule.builder().type(Schedule.ScheduleType.EVERY_N_MINUTES).value("10").build())
            .build();

    final ScheduleTask scheduledTask = new ScheduleTask();
    scheduledTask.setTrigger(trigger);

    // Run the test
    ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime result =
        schedulerServiceImplUnderTest.getFirstValidExecution(scheduledTask, now);

    // Verify the results
    assertEquals(0L, ChronoUnit.MINUTES.between(now, result));
  }

  @Test(expected = SchedulerException.class)
  public void testGetFirstValidExecution_ThrowsSchedulerException() throws Exception {
    // Setup
    com.miqdigital.scheduling.commons.model.Trigger trigger =
        com.miqdigital.scheduling.commons.model.Trigger.builder()
            .startDateTime(ZonedDateTime.now().minusDays(10))
            .endDateTime(ZonedDateTime.now().minusMinutes(20)).schedule(
                Schedule.builder().type(Schedule.ScheduleType.EVERY_N_MINUTES).value("10").build())
            .build();

    final ScheduleTask scheduledTask = new ScheduleTask();
    scheduledTask.setTrigger(trigger);

    // Run the test
    ZonedDateTime now = ZonedDateTime.now();
    final ZonedDateTime result =
        schedulerServiceImplUnderTest.getFirstValidExecution(scheduledTask, now);
  }

  @Test
  public void testSchedule() throws Exception {
    // Setup
    JobKey jobKey = new JobKey("anykey", "anygroup");
    JobDetail jobDetail = mock(JobDetail.class);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(jobDetail);
    // Run the test
    schedulerServiceImplUnderTest.schedule(cronScheduleTask);
    schedulerServiceImplUnderTest.schedule(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).scheduleJob(any(org.quartz.Trigger.class));
  }

  @Test(expected = SchedulerException.class)
  public void testSchedule_ThrowsSchedulerException() throws Exception {
    // Setup
    JobKey jobKey = new JobKey("anykey", "anygroup");
    JobDetail jobDetail = mock(JobDetail.class);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(jobDetail);

    doThrow(new SchedulerException()).when(mockScheduler)
        .scheduleJob(any(org.quartz.Trigger.class));
    // Run the test
    schedulerServiceImplUnderTest.schedule(cronScheduleTask);
  }

  @Test
  public void testPause() throws Exception {
    // Run the test
    schedulerServiceImplUnderTest.pause(cronScheduleTask);
    schedulerServiceImplUnderTest.pause(simpleScheduleTask);


    // Verify the results
    verify(mockScheduler, times(2)).pauseJob(any(JobKey.class));
  }

  @Test(expected = SchedulerException.class)
  public void testPause_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler).pauseJob(any(JobKey.class));
    // Run the test
    schedulerServiceImplUnderTest.pause(cronScheduleTask);
    schedulerServiceImplUnderTest.pause(simpleScheduleTask);
  }

  @Test
  public void testResume() throws Exception {
    // Run the test
    schedulerServiceImplUnderTest.resume(cronScheduleTask);
    schedulerServiceImplUnderTest.resume(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).resumeJob(any(JobKey.class));
  }

  @Test(expected = SchedulerException.class)
  public void testResume_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler).resumeJob(any(JobKey.class));
    // Run the test
    schedulerServiceImplUnderTest.resume(cronScheduleTask);
    schedulerServiceImplUnderTest.resume(simpleScheduleTask);
  }



  @Test
  public void testExecuteNow() throws Exception {
    // Run the test
    schedulerServiceImplUnderTest.executeNow(cronScheduleTask);
    schedulerServiceImplUnderTest.executeNow(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).triggerJob(any(JobKey.class), any(JobDataMap.class));
  }

  @Test(expected = SchedulerException.class)
  public void testExecuteNow_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler)
        .triggerJob(any(JobKey.class), any(JobDataMap.class));
    // Run the test
    schedulerServiceImplUnderTest.executeNow(cronScheduleTask);
    schedulerServiceImplUnderTest.executeNow(simpleScheduleTask);
  }

  @Test
  public void testGetLastExecution() throws Exception {
    // Setup
    Trigger trigger = mock(Trigger.class);
    when(mockScheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);

    // Run the test
    final ZonedDateTime result = schedulerServiceImplUnderTest.getLastExecution(cronScheduleTask);

    // Verify the results
    verify(trigger).getPreviousFireTime();
  }

  @Test(expected = SchedulerException.class)
  public void testGetLastExecution_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler).getTrigger(any(TriggerKey.class));
    // Run the test
    schedulerServiceImplUnderTest.getLastExecution(cronScheduleTask);
    schedulerServiceImplUnderTest.getLastExecution(simpleScheduleTask);
  }

  @Test
  public void testGetNextExecution() throws Exception {
    // Setup
    Trigger trigger = mock(Trigger.class);
    when(mockScheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);

    // Run the test
    schedulerServiceImplUnderTest.getNextExecution(cronScheduleTask);
    schedulerServiceImplUnderTest.getNextExecution(simpleScheduleTask);

    // Verify the results
    verify(trigger, times(2)).getNextFireTime();
  }

  @Test(expected = SchedulerException.class)
  public void testGetNextExecution_ThrowsSchedulerException() throws Exception {
    // Setup
    doThrow(new SchedulerException()).when(mockScheduler).getTrigger(any(TriggerKey.class));
    // Run the test
    schedulerServiceImplUnderTest.getNextExecution(cronScheduleTask);
    schedulerServiceImplUnderTest.getNextExecution(simpleScheduleTask);
  }

  @Test
  public void testReSchedule() throws Exception {
    // Setup
    Trigger trigger = mock(Trigger.class);
    JobKey jobKey = new JobKey(cronScheduleTask.getId(), cronScheduleTask.getGroup());
    JobDetail jobDetail = mock(JobDetail.class);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(jobDetail);
    when(mockScheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);

    // Run the test
    schedulerServiceImplUnderTest.reSchedule(cronScheduleTask);
    schedulerServiceImplUnderTest.reSchedule(simpleScheduleTask);

    // Verify the results
    verify(mockScheduler, times(2)).rescheduleJob(any(), any());
  }

  @Test(expected = SchedulerException.class)
  public void testReSchedule_ThrowsSchedulerException() throws Exception {
    // Setup
    Trigger trigger = mock(Trigger.class);
    JobKey jobKey = new JobKey(cronScheduleTask.getId(), cronScheduleTask.getGroup());
    JobDetail jobDetail = mock(JobDetail.class);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(jobDetail);
    when(mockScheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);
    doThrow(new SchedulerException()).when(mockScheduler).rescheduleJob(any(), any());
    // Run the test
    schedulerServiceImplUnderTest.reSchedule(cronScheduleTask);
    schedulerServiceImplUnderTest.reSchedule(simpleScheduleTask);
  }
}
