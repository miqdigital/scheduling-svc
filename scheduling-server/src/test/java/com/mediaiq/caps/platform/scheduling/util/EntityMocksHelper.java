package com.mediaiq.caps.platform.scheduling.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.TimeZone;

import com.mediaiq.caps.platform.scheduling.commons.model.CurlConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.Executor;
import com.mediaiq.caps.platform.scheduling.commons.model.HttpConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.Schedule;
import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;

public class EntityMocksHelper {

  public static final String CRON_EXPRESSION = "0 0/5 * * * ?";
  public static final String TIME_ZONE = "America/Los_Angeles";
  public static final ZonedDateTime startTime =
      ZonedDateTime.now(TimeZone.getTimeZone(TIME_ZONE).toZoneId());
  public static final ZonedDateTime endTime = startTime.plusMinutes(5);
  public static final String scheduleTaskId = "scheduleTaskId";

  public static ScheduleTask createCronScheduleTask() {
    HttpConfig httpConfig = HttpConfig.builder().url("anyURL").method(HttpConfig.MethodEnum.GET)
        .headers(new HashMap<>()).build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.HTTP).httpConfig(httpConfig).build();

    Schedule schedule =
        Schedule.builder().type(Schedule.ScheduleType.CRONEXPRESSION).value(CRON_EXPRESSION)
            .build();
    Trigger trigger =
        Trigger.builder().startDateTime(startTime).endDateTime(endTime).schedule(schedule).build();
    final ScheduleTask scheduleTask =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).build();
    scheduleTask.setId(scheduleTaskId);
    scheduleTask.setCreated(startTime);
    scheduleTask.setUpdated(endTime);
    scheduleTask.setZoneID(ZoneId.systemDefault().getId());
    return scheduleTask;
  }

  public static ScheduleTask createSimpleScheduleTask() {
    HttpConfig httpConfig = HttpConfig.builder().url("anyURL").method(HttpConfig.MethodEnum.GET)
        .headers(new HashMap<>()).build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.HTTP).httpConfig(httpConfig).build();


    Schedule schedule =
        Schedule.builder().type(Schedule.ScheduleType.EVERY_N_MINUTES).value("10").build();
    Trigger trigger =
        Trigger.builder().startDateTime(startTime).endDateTime(endTime).schedule(schedule).build();
    final ScheduleTask scheduleTask =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).build();
    scheduleTask.setId(scheduleTaskId);
    scheduleTask.setCreated(startTime);
    scheduleTask.setUpdated(endTime);
    scheduleTask.setZoneID(ZoneId.systemDefault().getId());
    return scheduleTask;
  }

  public static ScheduleTask createInvalidScheduleTask() {
    final ScheduleTask scheduleTask =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(null)
            .trigger(null).build();
    scheduleTask.setId(scheduleTaskId);
    scheduleTask.setCreated(startTime);
    scheduleTask.setUpdated(endTime);
    return scheduleTask;
  }

  public static ScheduleTask createInactiveScheduleTask() {
    String curlCommand = "curl -X GET 'http://httpbin.org'";
    CurlConfig curlConfig = CurlConfig.builder().curlCommand(curlCommand).build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.CURL).curlConfig(curlConfig).build();

    Schedule schedule =
        Schedule.builder().type(Schedule.ScheduleType.CRONEXPRESSION).value(CRON_EXPRESSION)
            .build();
    Trigger trigger =
        Trigger.builder().startDateTime(startTime).endDateTime(endTime).schedule(schedule).build();
    final ScheduleTask scheduleTask =
        ScheduleTask.builder().name("anytask").group("anygroup").creator("anycreator")
            .description("anydescription").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
            .trigger(trigger).status(ScheduleTask.StatusEnum.INACTIVE).build();
    scheduleTask.setId(scheduleTaskId);
    scheduleTask.setCreated(startTime);
    scheduleTask.setUpdated(endTime);
    return scheduleTask;
  }

}
