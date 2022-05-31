package com.miqdigital.scheduling.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.miqdigital.scheduling.commons.model.ScheduleTask;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, String> {
  @Modifying(clearAutomatically = true,
             flushAutomatically = true)
  @Query(value =
             "update scheduleTask t set t.name = ?1, t.description = ?2, t.creator = ?3, t.status"
                 + " = " + "?4 where t.id = ?5",
         nativeQuery = true)
  int updateScheduleTaskInfo(String name, String description, String creator, String status,
      String scheduleTaskId);

  @Query(value = "select count(st) from SCHEDULE_TASK_INFO st "
      + "where st.name like ?1 AND st.groupname like ?2",
         nativeQuery = true)
  int countByNameAndGroup(String name, String group);


  @Query(value = "select * from SCHEDULE_TASK_INFO st where st.name like ?1 AND "
      + "st.groupname like ?2",
         nativeQuery = true)
  ScheduleTask findByNameAndGroup(String name, String group);

  @Query(value = "select * from SCHEDULE_TASK_INFO st where " + "st.groupname like ?1",
         nativeQuery = true)
  List<ScheduleTask> findByGroup(String group);
}
