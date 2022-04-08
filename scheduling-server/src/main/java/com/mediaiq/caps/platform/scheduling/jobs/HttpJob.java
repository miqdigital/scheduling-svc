package com.mediaiq.caps.platform.scheduling.jobs;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mediaiq.caps.platform.scheduling.commons.model.HttpConfig;
import com.mediaiq.caps.platform.scheduling.config.ApplicationConfig;
import com.mediaiq.caps.platform.scheduling.service.HttpCallbackService;
import com.mediaiq.caps.platform.scheduling.util.QuartzJob;
import com.mediaiq.caps.platform.scheduling.util.Utils;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@QuartzJob
@DisallowConcurrentExecution
public class HttpJob implements Job {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  HttpCallbackService httpCallbackService;

  @Autowired
  ApplicationConfig applicationConfig;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    String jobGroup = jobExecutionContext.getJobDetail().getKey().getGroup();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    logger.info("Http Job execution started for name: {} and group: {}", jobName, jobGroup);
    JobDetail jobDetail = jobExecutionContext.getJobDetail();
    HttpConfig httpConfig = (HttpConfig) jobDetail.getJobDataMap().get(HttpJob.class.getName());

    RequestBody requestBody = null;
    if (httpConfig.getMethod() == HttpConfig.MethodEnum.POST
        || httpConfig.getMethod() == HttpConfig.MethodEnum.PATCH
        || httpConfig.getMethod() == HttpConfig.MethodEnum.PUT) {
      requestBody = RequestBody.create(null, new byte[0]);
    }

    if (httpConfig.getBody() != null) {
      requestBody = RequestBody.create(MediaType.parse(httpConfig.getBody().getContentType()),
          httpConfig.getBody().getContent());
    }

    //Populating meta-data in headers for end systems
    Map<String, String> headersMap = httpConfig.getHeaders();
    headersMap.putAll(Utils.getHeaderMap(jobExecutionContext));

    Request request = new Request.Builder().url(httpConfig.getUrl()).headers(Headers.of(headersMap))
        .method(httpConfig.getMethod().toString(), requestBody).build();
    httpCallbackService.executeHttpCall(request, jobExecutionContext);
    logger.info("Http Job execution completed for name: {} and group: {}", jobName, jobGroup);
  }
}
