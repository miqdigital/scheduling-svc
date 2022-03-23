package com.mediaiq.caps.platform.scheduling.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.http.HttpStatus;

import com.mediaiq.caps.platform.scheduling.config.ApplicationConfig;
import com.mediaiq.caps.platform.scheduling.util.Constants;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpCallbackServiceTest {

  @Mock
  OkHttpClient client;
  @Mock
  Call call;
  @Mock
  JobExecutionContext context;
  @Mock
  private ApplicationConfig mockApplicationConfig;
  private HttpCallbackService httpCallbackServiceUnderTest;
  private String strFireTime, strStartTime;
  private MeterRegistry meterRegistry;
  private JobDataMap jobDataMap = new JobDataMap();

  @Before
  public void setUp() {
    initMocks(this);
    strFireTime = "2020-01-01T01:00:00Z";
    strStartTime = "2020-01-01T01:02:00Z";
    jobDataMap.put(Constants.SCHEDULE_TASK_START_TIME_KEY, strStartTime);
    jobDataMap.put(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY, strFireTime);
    meterRegistry = new SimpleMeterRegistry();
    httpCallbackServiceUnderTest = new HttpCallbackService(mockApplicationConfig, meterRegistry);
    httpCallbackServiceUnderTest.setClient(client);
  }

  @Test
  public void testExecuteHttpCall() throws Exception {
    // Setup
    JobDetail jobDetail = Mockito.mock(JobDetail.class);
    JobKey jobKey = new JobKey("anyname", "anygroup");
    final Request mockRrequest = new Request.Builder().url("https://anyurl.com").build();
    Response mockResponse = new Response.Builder().request(mockRrequest).protocol(Protocol.HTTP_1_0)
        .code(HttpStatus.OK.value()) // status code
        .message("")
        .body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), "{}")).build();
    when(client.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenReturn(mockResponse);
    when(context.getJobDetail()).thenReturn(jobDetail);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(context.getMergedJobDataMap()).thenReturn(jobDataMap);

    // Run the test
    httpCallbackServiceUnderTest.executeHttpCall(mockRrequest, context);
  }

  @Test(expected = JobExecutionException.class)
  public void testExecuteHttpCall_ThrowsJobExecutionException() throws Exception {
    // Setup
    JobDetail jobDetail = Mockito.mock(JobDetail.class);
    JobKey jobKey = new JobKey("anyname", "anygroup");
    final Request mockRrequest = new Request.Builder().url("https://anyurl.com").build();
    Response mockResponse = new Response.Builder().request(mockRrequest).protocol(Protocol.HTTP_1_0)
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value()) // status code
        .message("")
        .body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), "{}")).build();
    when(client.newCall(any(Request.class))).thenReturn(call);
    when(call.execute()).thenReturn(mockResponse);
    when(context.getJobDetail()).thenReturn(jobDetail);
    when(jobDetail.getKey()).thenReturn(jobKey);
    when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
    // Run the test
    httpCallbackServiceUnderTest.executeHttpCall(mockRrequest, context);
  }
}
