package com.mediaiq.caps.platform.scheduling.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTaskExecutionHistory;

public interface ScheduleTaskExecutionHistoryRepository
    extends JpaRepository<ScheduleTaskExecutionHistory, String> {

  @Query(value =
             " select * from SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " where "
                 + " steh.schedule_task_id = ?1 "
                 + " AND steh.start_time >= ?2 "
                 + " AND steh.start_time <= ?3 "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findByIdAndStartDateTimeBetween(String scheduleTaskId,
      ZonedDateTime start, ZonedDateTime end);

  @Query(value =
             " select * "
                 + " from SCHEDULE_TASK_INFO sti "
                 + " join SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " ON sti.groupname = ?1 "
                 + " AND steh.start_time >= ?2 "
                 + " AND steh.start_time <= ?3 "
                 + " AND sti.id = steh.schedule_task_id "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findByGroupAndStartDateTimeBetween(String groupName,
      ZonedDateTime start, ZonedDateTime end);

  @Query(value =
             " select * from SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " where "
                 + " steh.start_time >= ?1 "
                 + " AND steh.start_time <= ?2 "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findAllByStartDateTimeBetween(ZonedDateTime start,
      ZonedDateTime end);

  @Query(value =
             " select * from SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " where "
                 + " steh.schedule_task_id = ?1 "
                 + " AND steh.execution_status = ?2 "
                 + " AND steh.start_time >= ?3 "
                 + " AND steh.start_time <= ?4 "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findByIdAndExecutionStatusAndStartDateTimeBetween(
      String scheduleTaskId,
      String executionStatus,
      ZonedDateTime start, ZonedDateTime end);

  @Query(value =
             " select * "
                 + " from SCHEDULE_TASK_INFO sti "
                 + " join SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " ON sti.groupname = ?1 "
                 + " AND steh.execution_status = ?2 "
                 + " AND steh.start_time >= ?3 "
                 + " AND steh.start_time <= ?4 "
                 + " AND sti.id = steh.schedule_task_id "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findByGroupAndExecutionStatusAndStartDateTimeBetween(
      String groupName, String executionStatus, ZonedDateTime start,ZonedDateTime end);

  @Query(value =
             " select * from SCHEDULE_TASK_EXECUTION_HISTORY steh "
                 + " where "
                 + " steh.execution_status = ?1 "
                 + " AND steh.start_time >= ?2 "
                 + " AND steh.start_time <= ?3 "
                 + " ORDER BY steh.schedule_time DESC ",
         nativeQuery = true)
  List<ScheduleTaskExecutionHistory> findAllByExecutionStatusAndByStartDateTimeBetween(
      String executionStatus, ZonedDateTime start, ZonedDateTime end);

}
