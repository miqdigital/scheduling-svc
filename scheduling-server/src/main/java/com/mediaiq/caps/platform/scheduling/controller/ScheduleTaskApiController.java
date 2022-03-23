package com.mediaiq.caps.platform.scheduling.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTaskExecutionHistoryResponse;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;
import com.mediaiq.caps.platform.scheduling.commons.utils.SchedulingExceptionMessage;
import com.mediaiq.caps.platform.scheduling.exception.SchedulingException;
import com.mediaiq.caps.platform.scheduling.service.ScheduleTaskService;
import com.mediaiq.caps.platform.scheduling.util.Utils;

@Controller
public class ScheduleTaskApiController implements ScheduleTaskApi {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ScheduleTaskService scheduleTaskService;

  public ResponseEntity<String> index() {
    return new ResponseEntity<>("Refer swagger doc at https://mediaiq.atlassian"
        + ".net/wiki/spaces/MIP/pages/1557791429/REST+API", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> swagger() {
    InputStream jsonInputStream =
        this.getClass().getClassLoader().getResourceAsStream("swagger.json");
    if (jsonInputStream == null) {
      logger.error("swagger.json not found");
      throw new SchedulingException.ServiceException("swagger.json not found",
          new FileNotFoundException());
    }
    String jsonDataSourceString = null;
    try {
      jsonDataSourceString = IOUtils.toString(jsonInputStream, StandardCharsets.UTF_8.name());
    } catch (IOException ex) {
      throw new SchedulingException.ServiceException("IOException ", ex);
    }
    return new ResponseEntity<>(jsonDataSourceString, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ScheduleTask> addScheduleTask(@Valid
  @RequestBody
      ScheduleTask scheduleTaskReq) {
    ScheduleTask scheduleTask = scheduleTaskService.create(scheduleTaskReq);
    Utils.convertScheduleTimezone(scheduleTask);
    return new ResponseEntity<>(scheduleTask, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ScheduleTask> migrateScheduleTask(@Valid ScheduleTask scheduleTaskReq) {
    ScheduleTask scheduleTask = scheduleTaskService.migrate(scheduleTaskReq);
    Utils.convertScheduleTimezone(scheduleTask);
    return new ResponseEntity<>(scheduleTask, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> deleteScheduleTask(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId) {
    scheduleTaskService.delete(scheduleTaskId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> executeScheduleTask(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId) {
    scheduleTaskService.executeNow(scheduleTaskId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<ScheduleTask>> getScheduleTask(String groupId) {
    List<ScheduleTask> scheduleTasks;
    if(StringUtils.isBlank(groupId)){
      scheduleTasks = scheduleTaskService.getAll();
    } else {
      scheduleTasks = scheduleTaskService.getAllForGroup(groupId);
    }
    scheduleTasks.forEach(Utils::convertScheduleTimezone);
    return new ResponseEntity<>(scheduleTasks, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<ScheduleTask>> getScheduleTask(List<String> scheduleTaskIds) {
    List<ScheduleTask> scheduleTasks = scheduleTaskService.getByIds(scheduleTaskIds);
    scheduleTasks.forEach(Utils::convertScheduleTimezone);
    return new ResponseEntity<>(scheduleTasks, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ScheduleTask> getScheduleTaskById(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId) {
    ScheduleTask scheduleTask = scheduleTaskService.get(scheduleTaskId);
    //TODO: Code refactoring needed for this to be moved to a proper class
    Utils.convertScheduleTimezone(scheduleTask);
    return new ResponseEntity<>(scheduleTask, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ScheduleTask> updateScheduleTask(
      @RequestBody
          ScheduleTask scheduleTaskReq,
      @PathVariable("scheduleTaskId")
          String scheduleTaskId) {
    if (scheduleTaskReq.getId() != null && scheduleTaskId.compareTo(scheduleTaskReq.getId()) != 0) {
      logger.error("Request body id {} doesn't match path param id {}", scheduleTaskReq.getId(),
          scheduleTaskId);
      throw new SchedulingException.BadRequestException(
          SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_MATCH);
    }
    scheduleTaskReq.setId(scheduleTaskId);
    ScheduleTask scheduleTaskUpdated = scheduleTaskService.update(scheduleTaskReq);
    Utils.convertScheduleTimezone(scheduleTaskUpdated);
    return new ResponseEntity<>(scheduleTaskUpdated, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<ScheduleTaskExecutionHistoryResponse>> getScheduleTaskRuns(
      String groupId, String scheduleTaskId, @NotNull ZonedDateTime startDateTime,
      @NotNull ZonedDateTime endDateTime, String executionStatus) {
    groupId = groupId != null ? groupId : "";
    scheduleTaskId = scheduleTaskId != null ? scheduleTaskId : "";
    executionStatus = executionStatus != null ? executionStatus : "";
    List<ScheduleTaskExecutionHistoryResponse> executionHistory = scheduleTaskService
        .getExecutionHistory(groupId, scheduleTaskId, startDateTime, endDateTime, executionStatus);
    return new ResponseEntity<>(executionHistory, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<ZonedDateTime>> getRunsInfo(@Valid Trigger trigger) {
    List<ZonedDateTime> runsInfo = scheduleTaskService.getRunsInfo(trigger);
    return new ResponseEntity<>(runsInfo, HttpStatus.OK);

  }

}
