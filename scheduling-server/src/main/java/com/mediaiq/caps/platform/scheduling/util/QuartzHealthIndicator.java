package com.mediaiq.caps.platform.scheduling.util;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class QuartzHealthIndicator implements HealthIndicator {
  
  @Autowired
  private SchedulerFactoryBean schedulerFactory;
  
  @Override
  public Health health() {
    try {
      schedulerFactory.getScheduler().getJobGroupNames();
    } catch (Exception e){
      return Health.down(e).withDetail("operationPerformed","scheduler.getJobGroupNames").build();
    }
    return Health.up().withDetail("operationPerformed","scheduler.getJobGroupNames").build();
  }
}