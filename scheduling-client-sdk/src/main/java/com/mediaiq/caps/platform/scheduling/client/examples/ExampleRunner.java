package com.mediaiq.caps.platform.scheduling.client.examples;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mediaiq.caps.platform.scheduling.client.SchedulingClient;
import com.mediaiq.caps.platform.scheduling.client.SchedulingClientImpl;
import com.mediaiq.caps.platform.scheduling.client.SchedulingConfig;
import com.mediaiq.caps.platform.scheduling.client.exception.SchedulingClientException;
import com.mediaiq.caps.platform.scheduling.client.model.ScheduleTask;
import com.mediaiq.caps.platform.trackingtags.Department;
import com.mediaiq.caps.platform.trackingtags.Environment;
import com.mediaiq.caps.platform.trackingtags.Product;
import com.mediaiq.caps.platform.trackingtags.Service;
import com.mediaiq.caps.platform.trackingtags.Team;
import com.mediaiq.caps.platform.trackingtags.TrackingTags;

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
    initializeClient(Environment.LOCAL, "<your_api_key>");

    //1. Create a new schedule
    CreateScheduleExample createScheduleExample = new CreateScheduleExample(schedulingClient);
    String id = createScheduleExample.create(model.getCreateScheduleTask()).getId();
    ScheduleTask scheduleTask = schedulingClient.getScheduleTask(id);
    ZonedDateTime clientStartTime = scheduleTask.getTrigger().getStartDateTime()
        .withZoneSameInstant(ZoneId.of(scheduleTask.getZoneID()));
    logger.info("Schedule Task {}",scheduleTask);
    logger.info("Time in client timezone {}", clientStartTime);
//    schedulingClient.executeScheduleTask(scheduleTask.getId());
  }

  private static void initializeClient(Environment environment, String authorizationKey) {
    TrackingTags trackingTags =
        new TrackingTags.TrackingTagsBuilder().setEnvironment(Environment.INTEGRATION)
            .setTeam(Team.PLATFORMSERVICES).setProduct(Product.SCHEDULING_SERVICE)
            .setService(Service.SCHEDULING_SERVER).setDepartment(Department.TECH)
            .setOwner("platform@miqdigital.com").setFunction("IT").build();
    SchedulingConfig schedulingConfig = new SchedulingConfig.SchedulingConfigBuilder()
        .setAuthorizationKey(authorizationKey)
        .setEnvironment(environment)
    /*
     * set url will work only in case of LOCAL environment , in other environments URL will be 
     *                automatically overridden by predefined URLs of corresponding environments
     */
//      .setUrl("http://localhost:8080")
        .build();
    schedulingClient = new SchedulingClientImpl(schedulingConfig, trackingTags);
  }

}
