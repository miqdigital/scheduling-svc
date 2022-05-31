package com.miqdigital.scheduling.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.SchedulerException;

import com.miqdigital.scheduling.commons.model.CurlConfig;
import com.miqdigital.scheduling.commons.model.Executor;
import com.miqdigital.scheduling.commons.model.Schedule;
import com.miqdigital.scheduling.commons.model.ScheduleTask;
import com.miqdigital.scheduling.commons.model.Trigger;
import com.miqdigital.scheduling.server.config.ApplicationConfig;
import com.miqdigital.scheduling.server.exception.SchedulingException;
import com.miqdigital.scheduling.server.repository.ScheduleTaskExecutionHistoryRepository;
import com.miqdigital.scheduling.server.repository.ScheduleTaskRepository;
import com.miqdigital.scheduling.server.service.SchedulerService;
import com.miqdigital.scheduling.server.util.EntityMocksHelper;

public class ScheduleTaskServiceImplTest {

  @Mock
  ScheduleTaskExecutionHistoryRepository mockScheduleTaskExecutionHistoryRepository;
  @Mock
  ApplicationConfig mockApplicationConfig;
  @Mock
  private ScheduleTaskRepository mockScheduleTaskRepository;
  @Mock
  private SchedulerService mockSchedulerService;

  private ScheduleTaskServiceImpl scheduleTaskServiceImplUnderTest;
  private ScheduleTask cronScheduleTask;


  @Before
  public void setUp() {
    initMocks(this);
    cronScheduleTask = EntityMocksHelper.createCronScheduleTask();
    scheduleTaskServiceImplUnderTest =
        new ScheduleTaskServiceImpl(mockSchedulerService, mockScheduleTaskRepository,
            mockScheduleTaskExecutionHistoryRepository, mockApplicationConfig);
  }

  @Test
  public void testGetAll() throws Exception {
    // Setup
    final List<ScheduleTask> expectedResult = Arrays.asList(cronScheduleTask);
    when(mockScheduleTaskRepository.findAll()).thenReturn(Arrays.asList(cronScheduleTask));

    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final List<ScheduleTask> result = scheduleTaskServiceImplUnderTest.getAll();

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test(expected = SchedulingException.class)
  public void testGetAll_SchedulerServiceGetLastExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final List<ScheduleTask> expectedResult = Arrays.asList(cronScheduleTask);
    when(mockScheduleTaskRepository.findAll()).thenReturn(Arrays.asList(cronScheduleTask));
    doThrow(new SchedulerException()).when(mockSchedulerService).getLastExecution(cronScheduleTask);

    // Run the test
    final List<ScheduleTask> result = scheduleTaskServiceImplUnderTest.getAll();
  }

  @Test(expected = SchedulingException.class)
  public void testGetAll_SchedulerServiceGetNextExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final List<ScheduleTask> expectedResult = Arrays.asList(cronScheduleTask);
    when(mockScheduleTaskRepository.findAll()).thenReturn(Arrays.asList(cronScheduleTask));
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    doThrow(SchedulerException.class).when(mockSchedulerService).getNextExecution(cronScheduleTask);

    // Run the test
    final List<ScheduleTask> result = scheduleTaskServiceImplUnderTest.getAll();
  }

  @Test
  public void testGet() throws Exception {
    // Setup
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));

    // Configure SchedulerService.getLastExecution(...).
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(
        any(ZonedDateTime.class));
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(
        any(ZonedDateTime.class));

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.get("scheduleTaskId");

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test(expected = SchedulingException.class)
  public void testGet_SchedulerServiceGetLastExecutionThrowsSchedulerException() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    doThrow(new SchedulerException()).when(mockSchedulerService).getLastExecution(cronScheduleTask);

    // Run the test
    scheduleTaskServiceImplUnderTest.get("scheduleTaskId");
  }

  @Test(expected = SchedulingException.class)
  public void testGet_SchedulerServiceGetNextExecutionThrowsSchedulerException() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));

    // Configure SchedulerService.getLastExecution(...).
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    doThrow(new SchedulerException()).when(mockSchedulerService).getNextExecution(cronScheduleTask);

    // Run the test
    scheduleTaskServiceImplUnderTest.get("scheduleTaskId");
  }

  @Test
  public void testCreate() throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.countByNameAndGroup(anyString(), anyString())).thenReturn(0);
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.create(cronScheduleTask)).thenReturn(cronScheduleTask);

    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.create(scheduleTaskReq);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockSchedulerService).create(cronScheduleTask);
    verify(mockSchedulerService).schedule(cronScheduleTask);
  }

  @Test(expected = SchedulingException.class)
  public void testCreate_SchedulerServiceCreateThrowsSchedulerException() throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.countByNameAndGroup(anyString(), anyString())).thenReturn(0);
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    doThrow(new SchedulerException()).when(mockSchedulerService).create(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.create(scheduleTaskReq);
  }

  @Test(expected = SchedulingException.class)
  public void testCreate_SchedulerServiceScheduleThrowsSchedulerException() throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.countByNameAndGroup(anyString(), anyString())).thenReturn(0);
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.create(cronScheduleTask)).thenReturn(cronScheduleTask);
    doThrow(SchedulerException.class).when(mockSchedulerService).schedule(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.create(scheduleTaskReq);
  }

  @Test(expected = SchedulingException.class)
  public void testCreate_SchedulerServiceGetLastExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.countByNameAndGroup(anyString(), anyString())).thenReturn(0);
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.create(cronScheduleTask)).thenReturn(cronScheduleTask);
    doThrow(SchedulerException.class).when(mockSchedulerService).getLastExecution(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.create(scheduleTaskReq);
  }

  @Test(expected = SchedulingException.class)
  public void testCreate_SchedulerServiceGetNextExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.countByNameAndGroup(anyString(), anyString())).thenReturn(0);
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.create(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    doThrow(SchedulerException.class).when(mockSchedulerService).getNextExecution(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.create(scheduleTaskReq);
  }

  @Test
  public void testUpdate() throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskReq);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockScheduleTaskRepository).save(cronScheduleTask);
  }

  @Test
  public void testUpdateExecutor() throws Exception {
    // Setup
    CurlConfig curlConfig = CurlConfig.builder().curlCommand("anyCommand").build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.CURL).curlConfig(curlConfig).build();
    final ScheduleTask scheduleTaskUpdateJob =
        cronScheduleTask.toBuilder().executor(executor).build();
    scheduleTaskUpdateJob.setId(EntityMocksHelper.scheduleTaskId);
    final ScheduleTask expectedResult = scheduleTaskUpdateJob;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskUpdateJob)).thenReturn(scheduleTaskUpdateJob);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskUpdateJob);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockScheduleTaskRepository).save(scheduleTaskUpdateJob);
    verify(mockSchedulerService).updateJob(scheduleTaskUpdateJob);
  }

  @Test
  public void testUpdateSchedule() throws Exception {
    // Setup
    Schedule schedule = Schedule.builder().type(Schedule.ScheduleType.PREDEFINEDEXPRESSION)
        .value(Schedule.PreDefinedExpression.EVERY15MIN.toString()).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(60)).schedule(schedule).build();
    final ScheduleTask scheduleTaskUpdateTrigger =
        cronScheduleTask.toBuilder().trigger(trigger).build();

    scheduleTaskUpdateTrigger.setId(EntityMocksHelper.scheduleTaskId);
    final ScheduleTask expectedResult = scheduleTaskUpdateTrigger;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskUpdateTrigger)).thenReturn(
        scheduleTaskUpdateTrigger);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskUpdateTrigger);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockScheduleTaskRepository).save(scheduleTaskUpdateTrigger);
    verify(mockSchedulerService).reSchedule(scheduleTaskUpdateTrigger);
  }

  @Test
  public void testUpdateStatus() throws Exception {
    // Setup
    final ScheduleTask scheduleTaskInactive =
        cronScheduleTask.toBuilder().status(ScheduleTask.StatusEnum.INACTIVE).build();

    scheduleTaskInactive.setId(EntityMocksHelper.scheduleTaskId);
    final ScheduleTask expectedResult = scheduleTaskInactive;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskInactive)).thenReturn(scheduleTaskInactive);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    when(mockSchedulerService.getNextExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskInactive);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockScheduleTaskRepository).save(scheduleTaskInactive);
    verify(mockSchedulerService).pause(scheduleTaskInactive);
  }


  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServiceUpdateJobThrowsSchedulerException() throws Exception {
    // Setup
    CurlConfig curlConfig = CurlConfig.builder().curlCommand("anyCommand").build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.CURL).curlConfig(curlConfig).build();
    final ScheduleTask scheduleTaskUpdateJob =
        cronScheduleTask.toBuilder().executor(executor).build();
    scheduleTaskUpdateJob.setZoneID("+07:00");
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskUpdateJob)).thenReturn(scheduleTaskUpdateJob);
    doThrow(SchedulerException.class).when(mockSchedulerService).updateJob(scheduleTaskUpdateJob);

    // Run the test
    scheduleTaskServiceImplUnderTest.update(scheduleTaskUpdateJob);
  }

  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServiceReScheduleThrowsSchedulerException() throws Exception {
    // Setup
    //TODO: Need proper assertions and repository mocks are not working
    //TODO: Assertions to verify if the method which is throwing excetion is even called
    Schedule schedule = Schedule.builder().type(Schedule.ScheduleType.PREDEFINEDEXPRESSION)
        .value(Schedule.PreDefinedExpression.EVERY15MIN.toString()).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(60)).schedule(schedule).build();
    final ScheduleTask scheduleTaskUpdateTrigger =
        cronScheduleTask.toBuilder().trigger(trigger).build();
    scheduleTaskUpdateTrigger.setZoneID("-07:00");
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.ofNullable(cronScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskUpdateTrigger)).thenReturn(
        scheduleTaskUpdateTrigger);
    doThrow(SchedulerException.class).when(mockSchedulerService)
        .reSchedule(scheduleTaskUpdateTrigger);

    // Run the test
    scheduleTaskServiceImplUnderTest.update(scheduleTaskUpdateTrigger);
  }

  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServiceResumeThrowsSchedulerException() throws Exception {
    // Setup
    ScheduleTask inactiveScheduleTask = EntityMocksHelper.createInactiveScheduleTask();
    final ScheduleTask scheduleTaskActive =
        inactiveScheduleTask.toBuilder().status(ScheduleTask.StatusEnum.ACTIVE).build();
    scheduleTaskActive.setZoneID("-07:00");
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(inactiveScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskActive)).thenReturn(scheduleTaskActive);
    doThrow(SchedulerException.class).when(mockSchedulerService).resume(scheduleTaskActive);

    // Run the test
    scheduleTaskServiceImplUnderTest.update(scheduleTaskActive);
  }

  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServicePauseThrowsSchedulerException() throws Exception {
    // Setup
    ScheduleTask activeScheduleTask = cronScheduleTask;
    final ScheduleTask scheduleTaskInactive =
        activeScheduleTask.toBuilder().status(ScheduleTask.StatusEnum.INACTIVE).build();
    scheduleTaskInactive.setZoneID("+07:00");
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(activeScheduleTask));
    when(mockScheduleTaskRepository.save(scheduleTaskInactive)).thenReturn(scheduleTaskInactive);
    doThrow(SchedulerException.class).when(mockSchedulerService).pause(scheduleTaskInactive);

    // Run the test
    scheduleTaskServiceImplUnderTest.update(scheduleTaskInactive);
  }

  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServiceGetLastExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    doThrow(SchedulerException.class).when(mockSchedulerService).getLastExecution(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskReq);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockSchedulerService).updateJob(cronScheduleTask);
    verify(mockSchedulerService).reSchedule(cronScheduleTask);
    verify(mockSchedulerService).resume(cronScheduleTask);
    verify(mockSchedulerService).pause(cronScheduleTask);
  }

  @Test(expected = SchedulingException.class)
  public void testUpdate_SchedulerServiceGetNextExecutionThrowsSchedulerException()
      throws Exception {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ScheduleTask expectedResult = cronScheduleTask;
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    when(mockScheduleTaskRepository.save(cronScheduleTask)).thenReturn(cronScheduleTask);
    when(mockSchedulerService.getLastExecution(cronScheduleTask)).thenReturn(ZonedDateTime.now());
    doThrow(SchedulerException.class).when(mockSchedulerService).getNextExecution(cronScheduleTask);

    // Run the test
    final ScheduleTask result = scheduleTaskServiceImplUnderTest.update(scheduleTaskReq);

    // Verify the results
    assertEquals(expectedResult, result);
    verify(mockSchedulerService).updateJob(cronScheduleTask);
    verify(mockSchedulerService).reSchedule(cronScheduleTask);
    verify(mockSchedulerService).resume(cronScheduleTask);
    verify(mockSchedulerService).pause(cronScheduleTask);
  }

  @Test
  public void testDelete() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));

    // Run the test
    scheduleTaskServiceImplUnderTest.delete("scheduleTaskId");

    // Verify the results
    verify(mockSchedulerService).delete(cronScheduleTask);
    verify(mockScheduleTaskRepository).delete(cronScheduleTask);
  }

  @Test(expected = SchedulingException.class)
  public void testDelete_SchedulerServiceThrowsSchedulerException() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    doThrow(SchedulerException.class).when(mockSchedulerService).delete(cronScheduleTask);

    // Run the test
    scheduleTaskServiceImplUnderTest.delete("scheduleTaskId");

    // Verify the results
    verify(mockScheduleTaskRepository).delete(cronScheduleTask);
  }

  @Test
  public void testExecuteNow() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));

    // Run the test
    scheduleTaskServiceImplUnderTest.executeNow("scheduleTaskId");

    // Verify the results
    verify(mockSchedulerService).executeNow(cronScheduleTask);
  }

  @Test(expected = SchedulingException.class)
  public void testExecuteNow_SchedulerServiceThrowsSchedulerException() throws Exception {
    // Setup
    when(mockScheduleTaskRepository.findById(anyString())).thenReturn(
        Optional.of(cronScheduleTask));
    doThrow(SchedulerException.class).when(mockSchedulerService).executeNow(cronScheduleTask);

    // Run the test
    scheduleTaskServiceImplUnderTest.executeNow("scheduleTaskId");

    // Verify the results
  }

  @Test
  public void getRunsInfo() throws Exception {
    //Setup
    Schedule schedule = Schedule.builder().type(Schedule.ScheduleType.PREDEFINEDEXPRESSION)
        .value(Schedule.PreDefinedExpression.EVERY15MIN.toString()).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(60)).schedule(schedule).build();
    ZonedDateTime zonedDateTime =
        ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
    List<ZonedDateTime> expected = Arrays.asList(zonedDateTime);
    when(mockSchedulerService.getExecutionTimes(trigger)).thenReturn(Arrays.asList(zonedDateTime));
    // Run the test
    List<ZonedDateTime> runsInfo = scheduleTaskServiceImplUnderTest.getRunsInfo(trigger);

    // Verify the results
    assertEquals(runsInfo, expected);
  }

  @Test(expected = SchedulingException.BadRequestException.class)
  public void getRunsInfo_MinFrequencyValidationException() throws Exception {
    //Setup
    Schedule schedule = Schedule.builder().type(Schedule.ScheduleType.PREDEFINEDEXPRESSION)
        .value(Schedule.PreDefinedExpression.EVERY15MIN.toString()).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(60)).schedule(schedule).build();
    ZonedDateTime zonedDateTime =
        ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
    List<ZonedDateTime> expected =
        Arrays.asList(zonedDateTime, zonedDateTime.plusMinutes(15), zonedDateTime.plusMinutes(30));
    when(mockApplicationConfig.getMinSchedulingInterval()).thenReturn(10000);
    when(mockSchedulerService.getExecutionTimes(any(Trigger.class), anyInt())).thenReturn(expected);
    // Run the test
    scheduleTaskServiceImplUnderTest.getRunsInfo(trigger);
  }

  @Test(expected = SchedulingException.BadRequestException.class)
  public void getRunsInfo_ThrowsSchedulingException() throws Exception {
    //Setup
    Schedule schedule = Schedule.builder().type(Schedule.ScheduleType.PREDEFINEDEXPRESSION)
        .value(Schedule.PreDefinedExpression.EVERY15MIN.toString()).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(60)).schedule(schedule).build();
    ZonedDateTime zonedDateTime =
        ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
    doThrow(ParseException.class).when(mockSchedulerService).getExecutionTimes(trigger);

    // Run the test
    scheduleTaskServiceImplUnderTest.getRunsInfo(trigger);
  }

}
