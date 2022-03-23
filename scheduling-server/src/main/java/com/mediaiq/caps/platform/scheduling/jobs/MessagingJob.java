package com.mediaiq.caps.platform.scheduling.jobs;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import com.mediaiq.caps.platform.Event;
import com.mediaiq.caps.platform.EventMetadata;
import com.mediaiq.caps.platform.scheduling.commons.model.HttpConfig;
import com.mediaiq.caps.platform.scheduling.commons.model.MessagingConfig;
import com.mediaiq.caps.platform.scheduling.config.ApplicationConfig;
import com.mediaiq.caps.platform.scheduling.service.HttpCallbackService;
import com.mediaiq.caps.platform.scheduling.util.QuartzJob;
import com.mediaiq.caps.platform.scheduling.util.Utils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@QuartzJob
@DisallowConcurrentExecution
public class MessagingJob implements Job {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  HttpCallbackService httpCallbackService;

  @Autowired
  ApplicationConfig applicationConfig;

  private static final String EVENT_JSON = "{\"topicName\": \"%s\",\"event\": %s}";

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    String jobGroup = jobExecutionContext.getJobDetail().getKey().getGroup();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    logger.info("Http Job execution started for name: {} and group: {}", jobName, jobGroup);
    JobDetail jobDetail = jobExecutionContext.getJobDetail();
    MessagingConfig messagingConfig =
        (MessagingConfig) jobDetail.getJobDataMap().get(MessagingJob.class.getName());

    //Populating meta-data in headers for end systems
    Map<String, String> headersMap = Utils.getHeaderMap(jobExecutionContext);

    if(StringUtils.isEmpty(messagingConfig.getTopicName())){
      messagingConfig.setTopicName(applicationConfig.getMessagingDefaultPublishTopic());
    }
    if(StringUtils.isEmpty(messagingConfig.getType())){
      messagingConfig.setType(jobGroup);
    }
    String payloadString = getEventPublishJson(messagingConfig, headersMap);
    String requestString = String.format(EVENT_JSON, messagingConfig.getTopicName(), payloadString);
    RequestBody requestBody =
        RequestBody.create(MediaType.parse(ContentType.APPLICATION_JSON.toString()), requestString);

    Request request = new Request.Builder().url(applicationConfig.getMessagingPublishEndpoint())
        .method(HttpConfig.MethodEnum.POST.toString(), requestBody).build();
    httpCallbackService.executeHttpCall(request, jobExecutionContext);
    logger.info("Http Job execution completed for name: {} and group: {}", jobName, jobGroup);
  }

  public String getEventPublishJson(MessagingConfig messagingConfig, Map<String, String> headersMap)
      throws JobExecutionException {
    try {
      Value.Builder valueBuilder = Value.newBuilder();
      JsonFormat.parser().merge(messagingConfig.getPayload(), valueBuilder);
      Event event = Event.newBuilder()
          .setMeta(EventMetadata.newBuilder().putAllCustomMeta(headersMap).build())
          .setType(messagingConfig.getType()).setPayload(valueBuilder).build();
      return JsonFormat.printer().print(event);
    } catch (InvalidProtocolBufferException e) {
      throw new JobExecutionException(e);
    }
  }
}
