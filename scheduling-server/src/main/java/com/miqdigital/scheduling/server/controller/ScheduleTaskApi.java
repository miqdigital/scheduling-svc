package com.miqdigital.scheduling.server.controller;

import java.time.ZonedDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.miqdigital.scheduling.commons.model.ScheduleTask;
import com.miqdigital.scheduling.commons.model.ScheduleTaskExecutionHistoryResponse;
import com.miqdigital.scheduling.commons.model.Trigger;

public interface ScheduleTaskApi {

  @RequestMapping(value = "/")
  ResponseEntity<String> index();

  @RequestMapping(value = "/swagger",
                  produces = {"application/json", "*/*"},
                  consumes = {"application/json"},
                  method = RequestMethod.GET)
  ResponseEntity<String> swagger();

  @RequestMapping(value = "/schedule-task/migrate",
                  produces = {"application/json", "*/*"},
                  consumes = {"application/json"},
                  method = RequestMethod.POST)
  ResponseEntity<ScheduleTask> migrateScheduleTask(@Valid

  @RequestBody
      ScheduleTask body);

  @RequestMapping(value = "/schedule-task",
                  produces = {"application/json", "*/*"},
                  consumes = {"application/json"},
                  method = RequestMethod.POST)
  ResponseEntity<ScheduleTask> addScheduleTask(@Valid
  @RequestBody
      ScheduleTask body);


  @RequestMapping(value = "/schedule-task/{scheduleTaskId}",
                  produces = {"*/*"},
                  method = RequestMethod.DELETE)
  ResponseEntity<Void> deleteScheduleTask(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId);


  @RequestMapping(value = "/schedule-task/{scheduleTaskId}/execute-now",
                  produces = {"*/*"},
                  method = RequestMethod.POST)
  ResponseEntity<Void> executeScheduleTask(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId);


  @RequestMapping(value = "/schedule-task",
                  produces = {"application/json", "*/*"},
                  method = RequestMethod.GET)
  ResponseEntity<List<ScheduleTask>> getScheduleTask(
      @RequestParam(value = "groupId",
                    required = false)
          String groupId);

  @RequestMapping(value = "/schedule-task/bulk",
                  produces = {"application/json", "*/*"},
                  method = RequestMethod.GET)
  ResponseEntity<List<ScheduleTask>> getScheduleTask(
      @RequestParam(value = "scheduleTaskIds",
                    required = true)
          List<String> scheduleTaskIds);

  @RequestMapping(value = "/schedule-task/{scheduleTaskId}",
                  produces = {"application/json", "*/*"},
                  method = RequestMethod.GET)
  ResponseEntity<ScheduleTask> getScheduleTaskById(
      @PathVariable("scheduleTaskId")
          String scheduleTaskId);


  @RequestMapping(value = "/schedule-task/{scheduleTaskId}",
                  
                  produces = {"application/json", "*/*"},
                  consumes = {"application/json"},
                  method = RequestMethod.PUT)
  ResponseEntity<ScheduleTask> updateScheduleTask(@Valid
  @RequestBody
      ScheduleTask body,
      @PathVariable("scheduleTaskId")
          String scheduleTaskId);

  @RequestMapping(value = {"/schedule-task/runs/group", "/schedule-task/runs/group/{groupId}"},
                  produces = {"application/json", "*/*"},
                  method = RequestMethod.GET)
  ResponseEntity<List<ScheduleTaskExecutionHistoryResponse>> getScheduleTaskRuns(
      @PathVariable(name = "groupId",
                    required = false)
          String groupId,
      @RequestParam(value = "scheduleTaskId",
                    required = false)
          String scheduleTaskId, @NotNull
  @RequestParam(value = "startDateTime")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      ZonedDateTime startDateTime, @NotNull
  @RequestParam(value = "endDateTime")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      ZonedDateTime endDateTime,
      @RequestParam(value = "executionStatus",
                    required = false)
          String executionStatus);

  @RequestMapping(value = "/schedule-task/runs-info",
                  produces = {"application/json", "*/*"},
                  consumes = {"application/json"},
                  method = RequestMethod.POST)
  ResponseEntity<List<ZonedDateTime>> getRunsInfo(@Valid
  @RequestBody
      Trigger trigger);
}
