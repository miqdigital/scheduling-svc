package com.miqdigital.scheduling.client.examples;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miqdigital.scheduling.client.SchedulingClient;
import com.miqdigital.scheduling.client.SchedulingClientImpl;
import com.miqdigital.scheduling.client.SchedulingConfig;
import com.miqdigital.scheduling.client.exception.SchedulingClientException;
import com.miqdigital.scheduling.client.model.ScheduleTask;



/**
 * The type Example runner.
 */
public class ExampleRunner {

  private static final Logger logger = LoggerFactory.getLogger(ExampleRunner.class);
  private static final ModelGeneratorExample model = new ModelGeneratorExample();
  private static SchedulingClient schedulingClient;

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws SchedulingClientException the scheduling client exception
   */
  public static void main(String[] args) throws SchedulingClientException {
    initializeClient("<your_api_key>");

    //1. Create a new schedule
    CreateScheduleExample createScheduleExample = new CreateScheduleExample(schedulingClient);
    String id = createScheduleExample.create(model.getCreateScheduleTask()).getId();
    ScheduleTask scheduleTask = schedulingClient.getScheduleTask(id);
    ZonedDateTime clientStartTime = scheduleTask.getTrigger().getStartDateTime()
        .withZoneSameInstant(ZoneId.of(scheduleTask.getZoneID()));
    logger.info("Schedule Task {}", scheduleTask);
    logger.info("Time in client timezone {}", clientStartTime);
    //    schedulingClient.executeScheduleTask(scheduleTask.getId());
  }

  private static void initializeClient(String authorizationKey) {
    SchedulingConfig schedulingConfig =
        new SchedulingConfig.SchedulingConfigBuilder().setAuthorizationKey(authorizationKey)
            .build();
    schedulingClient = new SchedulingClientImpl(schedulingConfig);
  }

}
