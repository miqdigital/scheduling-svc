package com.miqdigital.scheduling.server.service;

import static com.miqdigital.scheduling.server.util.Constants.isoDateTime;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.miqdigital.scheduling.server.config.ApplicationConfig;
import com.miqdigital.scheduling.server.util.Constants;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The type Http callback service.
 */
@Service
public class HttpCallbackService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private Queue<Integer> activeTasksQueue;

  private OkHttpClient client;
  private MeterRegistry meterRegistry;
  private Counter.Builder runCounter;
  private Timer.Builder delayTimer;
  private Gauge.Builder activeTasks;

  /**
   * Instantiates a new Http callback service.
   */
  @Autowired
  public HttpCallbackService(ApplicationConfig applicationConfig, MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
    client = new OkHttpClient.Builder().eventListener(
            OkHttpMetricsEventListener.builder(meterRegistry, "okhttp.requests").build())
        .callTimeout(applicationConfig.getHttpJobCallBackTimeout(), TimeUnit.SECONDS).build();
    runCounter = Counter.builder("runCounter").description("count success/failure job runs");
    delayTimer = Timer.builder("delayTimer").description("count delay of job runs")
        .publishPercentileHistogram().sla(Duration.ofMinutes(5))
        .maximumExpectedValue(Duration.ofMinutes(15));
    activeTasksQueue = new ConcurrentLinkedDeque<>();
    activeTasks = Gauge.builder("activeTasks", activeTasksQueue, Collection::size);
  }

  public HttpCallbackService(OkHttpClient client) {
    this.client = client;
  }

  /**
   * Gets client.
   *
   * @return the client
   */
  public OkHttpClient getClient() {
    return client;
  }

  public void setClient(OkHttpClient client) {
    this.client = client;
  }

  /**
   * Execute.
   *
   * @param request the request
   * @param jobExecutionContext
   * @throws JobExecutionException the job execution exception
   */
  public void executeHttpCall(Request request, JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    boolean isSuccess = false;
    Response response = null;
    HttpStatus httpStatus = null;
    String jobGroup = jobExecutionContext.getJobDetail().getKey().getGroup();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    final String group = "group";
    request = request.newBuilder().tag(Tags.class, Tags.of(group, jobGroup)).build();
    logger.info("[{}.{}] Executing http job with request: {}", jobGroup, jobName, request);
    activeTasks.tags(group, jobGroup).register(meterRegistry);
    activeTasksQueue.add(1);
    for (int attempt = 0; attempt <= Constants.MAX_ATTEMPT; attempt++) {
      try {
        logger.info("[{}.{}] Attempt {}", jobGroup, jobName, attempt);
        response = client.newCall(request).execute();
        httpStatus = HttpStatus.resolve(response.code());
        String responseBody = response.body() == null ? "" : response.body().string();
        if (httpStatus.is2xxSuccessful()) {
          isSuccess = true;
          logger.info("[{}.{}] Request success with status: {} and response: {}", jobGroup, jobName,
              httpStatus.value(), responseBody);
          break;
        }
      } catch (IOException ex) {
        logger.warn("[{}.{}] Http call failed with exception {}", jobGroup, jobName, ex);
      }
      try {
        TimeUnit.SECONDS.sleep(Constants.RETRY_INTERVAL_SECONDS);
      } catch (InterruptedException ex) {
        activeTasksQueue.poll();
        logger.error("Caught exception ", ex);
        Thread.currentThread().interrupt();
        throw new JobExecutionException("HttpRequest failed with InterruptedException ");
      }
    }
    String scheduledTime = (String) jobExecutionContext.getMergedJobDataMap()
        .get(Constants.SCHEDULE_TASK_SCHEDULED_TIME_KEY);
    String startTime = (String) jobExecutionContext.getMergedJobDataMap()
        .get(Constants.SCHEDULE_TASK_START_TIME_KEY);
    long scheduledTimeSec = ZonedDateTime.parse(scheduledTime, isoDateTime).toEpochSecond();
    long startTimeSec = ZonedDateTime.parse(startTime, isoDateTime).toEpochSecond();
    long diff = startTimeSec - scheduledTimeSec;
    delayTimer.tags(group, jobGroup).register(meterRegistry).record(diff, TimeUnit.SECONDS);
    activeTasksQueue.poll();
    if (isSuccess == false) {
      String code = response != null ? String.valueOf(response.code()) : "";
      String message = response != null ? response.message() : "";
      runCounter.tags(group, jobGroup, "status", "failure").register(meterRegistry).increment(1.0);
      if (httpStatus == null) {
        throw new JobExecutionException("Unknown http status code observed " + code);
      } else {
        throw new JobExecutionException(
            "HttpRequest failed with status: " + code + " , reason: " + httpStatus.getReasonPhrase()
                + " ,response: " + message);
      }
    } else {
      runCounter.tags(group, jobGroup, "status", "success").register(meterRegistry).increment(1.0);
    }

  }
}
