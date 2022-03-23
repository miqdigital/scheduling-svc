package com.mediaiq.caps.platform.scheduling.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.service.ScheduleTaskService;
import com.mediaiq.caps.platform.scheduling.util.EntityMocksHelper;

@RunWith(SpringRunner.class)
public class ScheduleTaskApiControllerTest {

  private ScheduleTask cronScheduleTask;
  @Mock
  private ScheduleTaskService mockScheduleTaskService;
  @InjectMocks
  private ScheduleTaskApiController scheduleTaskApiController;

  @Before
  public void setUp() {
    initMocks(this);
    cronScheduleTask = EntityMocksHelper.createCronScheduleTask();
  }

  @Test
  public void testIndex() {
    // Setup
    final ResponseEntity<String> expectedResult = new ResponseEntity<>("body", HttpStatus.OK);

    // Run the test
    final ResponseEntity<String> result = scheduleTaskApiController.index();
    // Verify the results
    assertEquals(expectedResult.getStatusCode(), result.getStatusCode());
  }

  @Test
  public void testSwagger() {
    // Setup
    final ResponseEntity<String> expectedResult = new ResponseEntity<>("body", HttpStatus.OK);

    // Run the test
    final ResponseEntity<String> result = scheduleTaskApiController.swagger();

    // Verify the results
    assertEquals(expectedResult.getStatusCode(), result.getStatusCode());
  }

  @Test
  public void testAddScheduleTask() {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ResponseEntity<ScheduleTask> expectedResult =
        new ResponseEntity<>(cronScheduleTask, HttpStatus.CREATED);
    when(mockScheduleTaskService.create(cronScheduleTask)).thenReturn(cronScheduleTask);

    // Run the test
    final ResponseEntity<ScheduleTask> result =
        scheduleTaskApiController.addScheduleTask(scheduleTaskReq);

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test
  public void testDeleteScheduleTask() {
    // Setup

    // Run the test
    scheduleTaskApiController.deleteScheduleTask(EntityMocksHelper.scheduleTaskId);

    // Verify the results
    verify(mockScheduleTaskService).delete(EntityMocksHelper.scheduleTaskId);
  }

  @Test
  public void testExecuteScheduleTask() {
    doNothing().when(mockScheduleTaskService).executeNow(EntityMocksHelper.scheduleTaskId);

    scheduleTaskApiController.executeScheduleTask(EntityMocksHelper.scheduleTaskId);
    verify(mockScheduleTaskService).executeNow(EntityMocksHelper.scheduleTaskId);
  }

  @Test
  public void testGetScheduleTask() {
    // Setup
    ScheduleTask cronScheduleTask = EntityMocksHelper.createCronScheduleTask();
    final ResponseEntity<List<ScheduleTask>> expectedResult =
        new ResponseEntity<>(Arrays.asList(this.cronScheduleTask), HttpStatus.OK);
    when(mockScheduleTaskService.getAll()).thenReturn(Arrays.asList(this.cronScheduleTask));

    // Run the test
    final ResponseEntity<List<ScheduleTask>> result = scheduleTaskApiController.getScheduleTask("");

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test
  public void testGetScheduleTaskById() throws Exception {
    // Setup
    final ResponseEntity<ScheduleTask> expectedResult =
        new ResponseEntity<>(cronScheduleTask, HttpStatus.OK);
    when(mockScheduleTaskService.get(EntityMocksHelper.scheduleTaskId)).thenReturn(
        cronScheduleTask);

    // Run the test
    final ResponseEntity<ScheduleTask> result =
        scheduleTaskApiController.getScheduleTaskById(EntityMocksHelper.scheduleTaskId);

    // Verify the results
    assertEquals(expectedResult, result);
  }

  @Test
  public void testUpdateScheduleTask() {
    // Setup
    final ScheduleTask scheduleTaskReq = cronScheduleTask;
    final ResponseEntity<ScheduleTask> expectedResult =
        new ResponseEntity<>(cronScheduleTask, HttpStatus.OK);
    when(mockScheduleTaskService.update(cronScheduleTask)).thenReturn(cronScheduleTask);

    // Run the test
    final ResponseEntity<ScheduleTask> result =
        scheduleTaskApiController.updateScheduleTask(scheduleTaskReq,
            EntityMocksHelper.scheduleTaskId);

    // Verify the results
    assertEquals(expectedResult, result);
  }
}
