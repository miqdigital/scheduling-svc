package com.mediaiq.caps.platform.scheduling.client.examples;

import com.mediaiq.caps.platform.scheduling.client.SchedulingClient;
import com.mediaiq.caps.platform.scheduling.client.exception.SchedulingClientException;
import com.mediaiq.caps.platform.scheduling.client.model.ScheduleTask;

/**
 * The type Create schedule example.
 */
public class CreateScheduleExample {

  private SchedulingClient schedulingClient;

  /**
   * Instantiates a new Create schedule example.
   *
   * @param schedulingClient the scheduling client
   */
  public CreateScheduleExample(SchedulingClient schedulingClient) {
    this.schedulingClient = schedulingClient;
  }

  /**
   * Create schedule task.
   *
   * @param scheduleTask the schedule task
   * @return the schedule task
   * @throws SchedulingClientException the scheduling client exception
   */
  public ScheduleTask create(ScheduleTask scheduleTask) throws SchedulingClientException {
    return schedulingClient.createScheduleTask(scheduleTask);
  }

}
