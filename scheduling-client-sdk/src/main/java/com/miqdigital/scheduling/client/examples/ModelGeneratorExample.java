package com.miqdigital.scheduling.client.examples;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.miqdigital.scheduling.client.model.Executor;
import com.miqdigital.scheduling.client.model.HttpConfig;
import com.miqdigital.scheduling.client.model.HttpConfigBody;
import com.miqdigital.scheduling.client.model.Schedule;
import com.miqdigital.scheduling.client.model.ScheduleTask;
import com.miqdigital.scheduling.client.model.Trigger;

/**
 * The type Model generator example.
 */
public class ModelGeneratorExample {

  /**
   * Gets create schedule task.
   *
   * @return the create schedule task
   */
  public ScheduleTask getCreateScheduleTask() {
    //Create executor here
    HttpConfig httpConfig =
        HttpConfig.builder().method(HttpConfig.MethodEnum.POST).url("https://httpbin.org/post")
            .body(HttpConfigBody.builder().content("").contentType("application/json").build())
            .build();
    Executor executor =
        Executor.builder().type(Executor.ExecutorType.HTTP).httpConfig(httpConfig).build();

    //Create trigger here
    Schedule schedule =
        Schedule.builder().type(Schedule.ScheduleType.EVERY_N_MINUTES).value("10").build();

    ZonedDateTime start = ZonedDateTime.now().minusDays(10);
    ZonedDateTime end = ZonedDateTime.now().plusMinutes(15);

    Trigger trigger =
        Trigger.builder().schedule(schedule).startDateTime(start).endDateTime(end).build();

    //Return the ScheduleTask object
    return ScheduleTask.builder().creator("user@test.com")
        .description("Test schedule").executor(executor).name(UUID.randomUUID().toString())
        .group("test-group").trigger(trigger).status(ScheduleTask.StatusEnum.INACTIVE).build();
  }
}
