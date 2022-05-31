package com.miqdigital.scheduling.server.service.impl;

import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.END_TIME_START_TIME_DIFF_VIOLATION;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.INVALID_EXECUTION_STATUS;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.INVALID_SCHEDULE_EXPRESSION;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.MIN_INTERVAL_CONSTRAINT_VIOLATION;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.SCHEDULE_GROUP_UPDATE_ERROR;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.SCHEDULE_TASK_ALREADY_EXIST;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.SCHEDULE_TASK_ID_DOES_NOT_EXIST;
import static com.miqdigital.scheduling.commons.utils.SchedulingExceptionMessage.START_TIME_AFTER_END_TIME;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.miqdigital.scheduling.commons.model.ExecutionStatusEnum;
import com.miqdigital.scheduling.commons.model.Executor;
import com.miqdigital.scheduling.commons.model.Schedule;
import com.miqdigital.scheduling.commons.model.ScheduleTask;
import com.miqdigital.scheduling.commons.model.ScheduleTaskExecutionHistory;
import com.miqdigital.scheduling.commons.model.ScheduleTaskExecutionHistoryResponse;
import com.miqdigital.scheduling.commons.model.Trigger;
import com.miqdigital.scheduling.server.config.ApplicationConfig;
import com.miqdigital.scheduling.server.exception.SchedulingException;
import com.miqdigital.scheduling.server.repository.ScheduleTaskExecutionHistoryRepository;
import com.miqdigital.scheduling.server.repository.ScheduleTaskRepository;
import com.miqdigital.scheduling.server.service.ScheduleTaskService;
import com.miqdigital.scheduling.server.service.SchedulerService;
import com.miqdigital.scheduling.server.util.Constants;
import com.miqdigital.scheduling.server.util.Utils;

@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  ApplicationConfig applicationConfig;
  SchedulerService schedulerService;
  ScheduleTaskExecutionHistoryRepository scheduleTaskExecutionHistoryRepository;
  ScheduleTaskRepository scheduleTaskRepository;

  public ScheduleTaskServiceImpl(SchedulerService schedulerService,
      ScheduleTaskRepository scheduleTaskRepository,
      ScheduleTaskExecutionHistoryRepository scheduleTaskExecutionHistoryRepository,
      ApplicationConfig applicationConfig) {
    this.schedulerService = schedulerService;
    this.scheduleTaskRepository = scheduleTaskRepository;
    this.scheduleTaskExecutionHistoryRepository = scheduleTaskExecutionHistoryRepository;
    this.applicationConfig = applicationConfig;
  }

  static <T> void setIfNotNull(Consumer<T> setter, Supplier<T> getter) {
    T value = getter.get();
    if (value != null)
      setter.accept(value);
  }

  @Override
  public List<ScheduleTask> getAll() {
    List<ScheduleTask> scheduleTasks = scheduleTaskRepository.findAll();
    scheduleTasks.forEach(this::setExecutionInfo);
    return scheduleTasks;
  }

  @Override
  public List<ScheduleTask> getAllForGroup(String groupId) {
    List<ScheduleTask> scheduleTasks = scheduleTaskRepository.findByGroup(groupId);
    scheduleTasks.forEach(this::setExecutionInfo);
    return scheduleTasks;
  }

  @Override
  public ScheduleTask get(String scheduleTaskId) {
    ScheduleTask scheduleTask = scheduleTaskRepository.findById(scheduleTaskId).orElse(null);
    validateScheduleTaskExist(scheduleTask, scheduleTaskId);
    setExecutionInfo(scheduleTask);
    return scheduleTask;
  }

  @Override
  public List<ScheduleTask> getByIds(List<String> ids) {
    List<ScheduleTask> scheduleTasks = scheduleTaskRepository.findAllById(ids);
    scheduleTasks.forEach(this::setExecutionInfo);
    return scheduleTasks;
  }

  @Override
  @Transactional
  public ScheduleTask create(ScheduleTask scheduleTaskReq) {
    logger.info("Create ScheduleTask {}", scheduleTaskReq);
    ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    //Validations
    validateName(scheduleTaskReq);
    validateMinFrequency(scheduleTaskReq.getTrigger());
    validateStartEndTimeDiff(scheduleTaskReq);

    scheduleTaskReq.setCreated(now);
    scheduleTaskReq.setUpdated(scheduleTaskReq.getCreated());
    ZonedDateTime startDateTime = scheduleTaskReq.getTrigger().getStartDateTime();
    scheduleTaskReq.setZoneID(startDateTime.getZone().getId());
    ScheduleTask savedScheduleTask = null;
    try {
      savedScheduleTask = scheduleTaskRepository.save(scheduleTaskReq);
      schedulerService.create(scheduleTaskReq);
      schedulerService.schedule(scheduleTaskReq);
      if (scheduleTaskReq.getStatus().equals(ScheduleTask.StatusEnum.INACTIVE)) {
        schedulerService.pause(scheduleTaskReq);
      }
      setExecutionInfo(savedScheduleTask);
    } catch (SchedulerException e) {
      logger.error("Failed to create {} task {}", scheduleTaskReq.getName(), e);
      throw new SchedulingException.ServiceException("Failed to create task", e);
    }
    logger.info("Task created {} id {}", savedScheduleTask.getName(), savedScheduleTask.getId());
    return savedScheduleTask;
  }

  @Override
  public ScheduleTask migrate(ScheduleTask scheduleTaskReq) {
    logger.info("Create ScheduleTask {}", scheduleTaskReq);
    ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    //Validations
    validateName(scheduleTaskReq);
    validateMinFrequency(scheduleTaskReq.getTrigger());

    scheduleTaskReq.setCreated(now);
    scheduleTaskReq.setUpdated(scheduleTaskReq.getCreated());
    ZonedDateTime startDateTime = scheduleTaskReq.getTrigger().getStartDateTime();
    scheduleTaskReq.setZoneID(startDateTime.getZone().getId());
    ScheduleTask savedScheduleTask = null;
    try {
      savedScheduleTask = scheduleTaskRepository.save(scheduleTaskReq);
      schedulerService.create(scheduleTaskReq);
      setExecutionInfo(savedScheduleTask);
    } catch (SchedulerException e) {
      logger.error("Failed to create {} task {}", scheduleTaskReq.getName(), e);
      throw new SchedulingException.ServiceException("Failed to create task", e);
    }
    logger.info("Task created {} id {}", savedScheduleTask.getName(), savedScheduleTask.getId());
    return savedScheduleTask;
  }

  @Override
  @Transactional
  public ScheduleTask update(final ScheduleTask scheduleTaskReq) {
    logger.debug("Update ScheduleTask {}", scheduleTaskReq);
    ZonedDateTime now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    ScheduleTask currentScheduleTask =
        scheduleTaskRepository.findById(scheduleTaskReq.getId()).orElse(null);
    Utils.convertScheduleTimezone(currentScheduleTask);
    validateScheduleTaskExist(currentScheduleTask, scheduleTaskReq.getId());

    Trigger updatedTrigger = scheduleTaskReq.getTrigger();
    Executor updatedExecutor = scheduleTaskReq.getExecutor();

    //Validate updated name
    if (!scheduleTaskReq.getName().equals(currentScheduleTask.getName())) {
      logger.info("Name updated to {}", scheduleTaskReq.getName());
      validateName(scheduleTaskReq);
    }

    //Validate updated Trigger
    Trigger currentTrigger = currentScheduleTask.getTrigger();
    if (!updatedTrigger.getSchedule().equals(currentTrigger.getSchedule())) {
      logger.info("Validate schedule {}", updatedTrigger.getSchedule());
      validateMinFrequency(scheduleTaskReq.getTrigger());
    }

    if (!updatedTrigger.getEndDateTime().equals(currentTrigger.getEndDateTime())) {
      logger.info("Validate endDate {}", updatedTrigger.getEndDateTime());
      validateStartEndTimeDiff(scheduleTaskReq);
    }

    if (!scheduleTaskReq.getGroup().equals(currentScheduleTask.getGroup())) {
      logger.warn("Schedule task group cannot be updated {}", scheduleTaskReq);
      throw new SchedulingException.BadRequestException(SCHEDULE_GROUP_UPDATE_ERROR);
    }

    try {
      //If executor is changed update jobdetails
      if (!updatedExecutor.equals(currentScheduleTask.getExecutor())) {
        schedulerService.updateJob(scheduleTaskReq);
        logger.info("Executor updated {}", updatedExecutor);
      }

      //If trigger is changed, reschedule job
      if (!updatedTrigger.equals(currentTrigger)) {
        schedulerService.reSchedule(scheduleTaskReq);
        logger.info("Trigger updated {}", scheduleTaskReq.getTrigger());
      }

      //If status is changed , pause/resume job
      if (scheduleTaskReq.getStatus() != null && !scheduleTaskReq.getStatus()
          .equals(currentScheduleTask.getStatus())) {
        if (scheduleTaskReq.getStatus() == ScheduleTask.StatusEnum.ACTIVE) {
          schedulerService.resume(scheduleTaskReq);
          logger.info("Task active {}", scheduleTaskReq.getId());
        }
        if (scheduleTaskReq.getStatus() == ScheduleTask.StatusEnum.INACTIVE) {
          schedulerService.pause(scheduleTaskReq);
          logger.info("Task inactive {}", scheduleTaskReq.getId());
        }
        logger.info("Status updated to {}", scheduleTaskReq.getStatus());
      }
    } catch (SchedulerException e) {
      logger.error("Failed to update {} task {}", scheduleTaskReq.getId(), e);
      throw new SchedulingException.ServiceException("Failed to update task", e);
    }

    scheduleTaskReq.setUpdated(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    scheduleTaskReq.setZoneID(updatedTrigger.getStartDateTime().getZone().getId());
    ScheduleTask scheduleTaskNew = scheduleTaskRepository.save(scheduleTaskReq);
    setExecutionInfo(scheduleTaskNew);
    logger.info("Task updated {}", scheduleTaskNew.getId());
    return scheduleTaskNew;
  }

  @Override
  @Transactional
  public void delete(String scheduleTaskId) {
    logger.debug("Delete ScheduleTask {}", scheduleTaskId);
    ScheduleTask scheduleTask = scheduleTaskRepository.findById(scheduleTaskId).orElse(null);
    validateScheduleTaskExist(scheduleTask, scheduleTaskId);
    try {
      schedulerService.delete(scheduleTask);
      scheduleTaskRepository.delete(scheduleTask);
    } catch (SchedulerException e) {
      logger.error("Failed to delete {} task {}", scheduleTaskId, e);
      throw new SchedulingException.ServiceException("Failed to delete task", e);
    }
    logger.info("Task deleted {}", scheduleTaskId);
  }

  @Override
  public void executeNow(String scheduleTaskId) {
    logger.debug("Execute now ScheduleTask {}", scheduleTaskId);
    ScheduleTask scheduleTask = scheduleTaskRepository.findById(scheduleTaskId).orElse(null);
    validateScheduleTaskExist(scheduleTask, scheduleTaskId);
    try {
      schedulerService.executeNow(scheduleTask);
    } catch (SchedulerException e) {
      logger.error("Failed to execute {} task {}", scheduleTaskId, e);
      throw new SchedulingException.ServiceException("Failed to execute task", e);
    }
    logger.info("Task executed {}", scheduleTaskId);
  }

  @Override
  public List<ScheduleTaskExecutionHistoryResponse> getExecutionHistory(String groupName,
      String scheduleTaskId, ZonedDateTime start, ZonedDateTime end, String executionStatus) {
    executionStatus = executionStatus.toUpperCase();
    logger.debug("ScheduleTask Execution History of group {} and ScheduleTask id {} "
            + "from {} till {} with status {} ", groupName, scheduleTaskId, start, end,
        executionStatus);
    List<ScheduleTaskExecutionHistory> result = null;
    List<ScheduleTaskExecutionHistoryResponse> scheduleTaskExecutionHistoryResponse;
    if (StringUtils.isEmpty(executionStatus)) {
      if (StringUtils.isEmpty(scheduleTaskId) && StringUtils.isEmpty(groupName)) {
        result = scheduleTaskExecutionHistoryRepository.findAllByStartDateTimeBetween(start, end);
      } else if (StringUtils.isEmpty(scheduleTaskId) == false) {
        result =
            scheduleTaskExecutionHistoryRepository.findByIdAndStartDateTimeBetween(scheduleTaskId,
                start, end);
      } else {
        result =
            scheduleTaskExecutionHistoryRepository.findByGroupAndStartDateTimeBetween(groupName,
                start, end);
      }
    } else {
      ExecutionStatusEnum executionStatusEnum;
      try {
        executionStatusEnum = ExecutionStatusEnum.valueOf(executionStatus.toUpperCase());
      } catch (IllegalArgumentException exception) {
        logger.error("Error while converting executionStatus {}, exception", executionStatus,
            exception);
        throw new SchedulingException.BadRequestException(INVALID_EXECUTION_STATUS);
      }
      if (StringUtils.isEmpty(scheduleTaskId) && StringUtils.isEmpty(groupName)) {
        result =
            scheduleTaskExecutionHistoryRepository.findAllByExecutionStatusAndByStartDateTimeBetween(
                executionStatus, start, end);
      } else if (StringUtils.isEmpty(scheduleTaskId) == false) {
        result =
            scheduleTaskExecutionHistoryRepository.findByIdAndExecutionStatusAndStartDateTimeBetween(
                scheduleTaskId, executionStatus, start, end);
      } else {
        result =
            scheduleTaskExecutionHistoryRepository.findByGroupAndExecutionStatusAndStartDateTimeBetween(
                groupName, executionStatus, start, end);
      }
    }
    scheduleTaskExecutionHistoryResponse = new ArrayList<>(result.size());
    for (ScheduleTaskExecutionHistory scheduleTaskExecutionHistory : result) {
      scheduleTaskExecutionHistoryResponse.add(
          new ScheduleTaskExecutionHistoryResponse(scheduleTaskExecutionHistory));
    }

    return scheduleTaskExecutionHistoryResponse;
  }

  @Override
  public List<ZonedDateTime> getRunsInfo(Trigger trigger) {
    List<ZonedDateTime> executionTimes;
    try {
      validateMinFrequency(trigger);
      executionTimes = schedulerService.getExecutionTimes(trigger);
    } catch (ParseException e) {
      logger.error("Failed to getRunsInfo for trigger {} exception {}", trigger, e);
      throw new SchedulingException.BadRequestException(INVALID_SCHEDULE_EXPRESSION);
    }
    return executionTimes;
  }

  private void setExecutionInfo(ScheduleTask scheduleTask) {
    ZonedDateTime lastExecution = null;
    ZonedDateTime nextExecution = null;
    try {
      lastExecution = schedulerService.getLastExecution(scheduleTask);
      nextExecution = schedulerService.getNextExecution(scheduleTask);
    } catch (SchedulerException e) {
      logger.error("Failed to get execution info {} task {}", scheduleTask.getId(), e);
      throw new SchedulingException.ServiceException("Failed to get execution info", e);
    }
    scheduleTask.setLastExecution(lastExecution);
    scheduleTask.setNextExecution(nextExecution);
    logger.debug("Task execution info returned {}", scheduleTask.getId());
  }

  private void validateScheduleTaskExist(ScheduleTask scheduleTask, String id) {
    if (scheduleTask == null) {
      logger.warn("Scheduled task does not exist {}", id);
      throw new SchedulingException.NotFoundException(SCHEDULE_TASK_ID_DOES_NOT_EXIST);
    }
  }

  private void validateName(ScheduleTask scheduleTaskReq) {
    String name = scheduleTaskReq.getName();
    String group = scheduleTaskReq.getGroup();
    int count = scheduleTaskRepository.countByNameAndGroup(name, group);
    if (count != 0) {
      logger.warn("Task with same name {} for group: {} already exist", name, group);
      throw new SchedulingException.ConflictException(SCHEDULE_TASK_ALREADY_EXIST);
    }
  }

  //If EndTime - CurrentTime > 3 years, then throw Exception
  private void validateStartEndTimeDiff(ScheduleTask scheduleTaskReq) {
    ZonedDateTime startTime =
        scheduleTaskReq.getTrigger().getStartDateTime().withZoneSameInstant(ZoneId.systemDefault())
            .truncatedTo(ChronoUnit.MINUTES);
    ZonedDateTime endTime =
        scheduleTaskReq.getTrigger().getEndDateTime().withZoneSameInstant(ZoneId.systemDefault())
            .truncatedTo(ChronoUnit.MINUTES);

    ZonedDateTime maxAllowedEndTime =
        ZonedDateTime.now().plusYears(Constants.MAX_DIFF_START_END_YEAR);
    if (startTime.compareTo(endTime) >= 0) {
      logger.warn("start time {} is after end time {}", startTime, endTime);
      throw new SchedulingException.BadRequestException(START_TIME_AFTER_END_TIME);
    }
    if (endTime.compareTo(maxAllowedEndTime) > 0) {
      logger.warn("End Time {} is after max allowed end time {}", endTime, maxAllowedEndTime);
      throw new SchedulingException.BadRequestException(END_TIME_START_TIME_DIFF_VIOLATION);
    }
  }

  /**
   * Creating an trigger with endtime as max allowed endtime from now and based on that
   * we will get next 4 runs and calculate the frequency as difference between second and third run
   * as cron always calculates time from 0th minute, so consider
   * Schedule : A
   * -- Frequency : 7 minute
   * -- started at 10:50
   * will have runs at [10:56 , 11:00, 11:07]
   * so the difference between 1st and 2nd run < 5 min,
   * hence always compare 2 consecutive differences
   *
   * @param trigger Trigger whose only schedule will be used to calculate frequency
   */
  private void validateMinFrequency(Trigger trigger) {
    Schedule schedule = trigger.getSchedule();
    if (StringUtils.isEmpty(schedule.getValue())) {
      logger.warn("Schedule.value expression is empty");
      throw new SchedulingException.BadRequestException(INVALID_SCHEDULE_EXPRESSION);
    }
    ZonedDateTime startDateTime = trigger.getStartDateTime();
    Trigger frequencyTrigger = Trigger.builder().schedule(schedule).startDateTime(startDateTime)
        .endDateTime(startDateTime.plusYears(Constants.MAX_DIFF_START_END_YEAR)).build();
    int minSchedulingIntervalInMin = applicationConfig.getMinSchedulingInterval();

    try {
      List<ZonedDateTime> executionTimes = schedulerService.getExecutionTimes(frequencyTrigger, 4);
      if (executionTimes.size() > 2) {
        long firstDiff = ChronoUnit.MINUTES.between(executionTimes.get(0), executionTimes.get(1));
        long secondDiff = ChronoUnit.MINUTES.between(executionTimes.get(1), executionTimes.get(2));

        if (firstDiff < minSchedulingIntervalInMin && secondDiff < minSchedulingIntervalInMin) {
          logger.warn("Difference between consecutive execution {} , {} , {} is less than "
                  + "min allowed interval {}.", executionTimes.get(0), executionTimes.get(1),
              executionTimes.get(2), minSchedulingIntervalInMin);
          throw new SchedulingException.BadRequestException(MIN_INTERVAL_CONSTRAINT_VIOLATION);
        }
      }
      logger.debug("cron expression validated");
    } catch (ParseException e) {
      logger.warn("Cron Expression is invalid {}", schedule.getValue());
      throw new SchedulingException.BadRequestException(INVALID_SCHEDULE_EXPRESSION);
    }
  }

}
