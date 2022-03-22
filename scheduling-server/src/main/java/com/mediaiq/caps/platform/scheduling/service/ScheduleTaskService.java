package com.mediaiq.caps.platform.scheduling.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTaskExecutionHistoryResponse;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;

@Validated
public interface ScheduleTaskService {

  List<ScheduleTask> getAll();

  List<ScheduleTask> getAllForGroup(String groupId);

  List<ScheduleTask> getByIds(List<String> ids);

  ScheduleTask get(String scheduleTaskId);

  ScheduleTask create(ScheduleTask scheduleTask);

  ScheduleTask migrate(ScheduleTask scheduleTask);

  ScheduleTask update(ScheduleTask scheduleTask);

  void delete(String scheduleTaskId);

  void executeNow(String scheduleTaskId);

  List<ScheduleTaskExecutionHistoryResponse> getExecutionHistory(String groupName,
      String scheduleTaskId, ZonedDateTime start, ZonedDateTime end, String executionStatus);

  List<ZonedDateTime> getRunsInfo(Trigger trigger);
}
