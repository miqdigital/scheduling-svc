package com.mediaiq.caps.platform.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mediaiq.caps.platform.scheduling.config.ApplicationConfig;



@ComponentScan(basePackages = {"com.mediaiq.caps.platform.scheduling"})
@EntityScan("com.mediaiq.caps.platform.scheduling")
@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
public class SchedulingServerStartUp implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  Environment environment;
  @Autowired
  ApplicationConfig applicationConfig;

  public static void main(String[] args) {
    System.setProperty("spring.profiles.default", "local");
    SpringApplication.run(SchedulingServerStartUp.class, args);
  }


  @Override
  public void run(String... args) {
    String[] activeProfiles = environment.getActiveProfiles();
    String[] defaultProfiles = environment.getDefaultProfiles();
    logger.info("Default profile is {}", defaultProfiles[0]);
    if (activeProfiles.length > 0) {
      logger.info("Active profile is {}", activeProfiles[0]);
    }
  }
}