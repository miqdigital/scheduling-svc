package com.mediaiq.caps.platform.scheduling;

import java.time.ZonedDateTime;

import com.mediaiq.caps.platform.scheduling.commons.model.CurlConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.Executor;
import com.mediaiq.caps.platform.scheduling.commons.model.Schedule;
import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;

public class Application {
  public static void main(String[] args) {
    String curlCommand = "curl -X GET 'http://httpbin.org'";
    CurlConfig curlConfig = CurlConfig.builder().curlCommand(curlCommand).build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.CURL).curlConfig(curlConfig).build();

    String cronExpression = "0/10 * * * * ?";
    Schedule schedule =
        Schedule.builder().type(Schedule.ScheduleType.CRONEXPRESSION).value(cronExpression).build();
    Trigger trigger = Trigger.builder().startDateTime(ZonedDateTime.now())
        .endDateTime(ZonedDateTime.now().plusMinutes(10)).schedule(schedule).build();
    ScheduleTask.builder().name("Task_every_10s").creator("creator_1")
        .description("sample_description").status(ScheduleTask.StatusEnum.ACTIVE).executor(executor)
        .trigger(trigger).build();

  }
}
