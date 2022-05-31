package com.miqdigital.scheduling.server.config;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @PostConstruct
  public void init() {
    logger.info("Initailizing Spring Quartz Scheduler...");
  }

  @Bean
  public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    logger.debug("Configuring Job factory");
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean quartzScheduler(ApplicationContext applicationContext,
      QuartzProperties qp) {
    SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

    Properties props = new Properties();
    props.putAll(qp.getProperties());

    quartzScheduler.setQuartzProperties(props);
    quartzScheduler.setOverwriteExistingJobs(true);

    quartzScheduler.setJobFactory(springBeanJobFactory(applicationContext));
    quartzScheduler.setDataSource(dataSource());

    return quartzScheduler;
  }

  @Bean
  @ConfigurationProperties("spring.datasource.quartz-data-source")
  @QuartzDataSource
  public DataSource dataSource() {
    logger.info("Initializing Quartz Data Source");
    return DataSourceBuilder.create().build();
  }


}
