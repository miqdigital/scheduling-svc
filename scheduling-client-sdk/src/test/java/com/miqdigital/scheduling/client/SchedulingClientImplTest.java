package com.miqdigital.scheduling.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static retrofit2.Response.success;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.miqdigital.scheduling.client.exception.SchedulingClientException;
import com.miqdigital.scheduling.client.model.ScheduleTask;
import com.miqdigital.scheduling.client.model.Trigger;
import com.miqdigital.scheduling.client.service.SchedulingRest;

import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SchedulingClientImplTest {

  @Captor
  ArgumentCaptor<HashMap> hashMapArgumentCaptor;
  @Captor
  ArgumentCaptor<String> stringArgumentCaptor;
  @Captor
  ArgumentCaptor<ScheduleTask> scheduleTaskArgumentCaptor;
  @Captor
  ArgumentCaptor<Trigger> triggerArgumentCaptor;
  @Captor
  ArgumentCaptor<List> listArgumentCaptor;
  @Mock
  SchedulingConfig schedulingConfig;
  @Mock
  Call mockedCall;
  @Mock
  SchedulingRest schedulingRest;
  @Mock
  ScheduleTask scheduleTask;
  @Mock
  ResponseBody responseBody;
  @Mock
  Trigger trigger;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldCheckIsEmptyInGetScheduleTask() {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    try {
      schedulingClient.getScheduleTask("");
    } catch (Exception e) {
      assertEquals("Schedule task id can not be empty", e.getMessage());
    }
  }

  @Test
  public void shouldCheckGetScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ScheduleTask(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    schedulingClient.getScheduleTask("1100");

    verify(schedulingRest).getScheduleTask(stringArgumentCaptor.capture());
    assertEquals("1100", stringArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInGetScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.getScheduleTask("1100");
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }
  }

  @Test
  public void shouldCheckGetScheduleTaskForScheduleTaskIds()
      throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getScheduleTask(Mockito.any(List.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ArrayList<ScheduleTask>(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    List<String> scheduleTaskIds = new ArrayList<>();
    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);

    scheduleTaskIds.add("1100");

    schedulingClient.getScheduleTask(scheduleTaskIds);

    verify(schedulingRest).getScheduleTask(listArgumentCaptor.capture());
    assertEquals(scheduleTaskIds, listArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInGetScheduleTaskForScheduleTaskIds() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getScheduleTask(Mockito.any(List.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    List<String> scheduleTaskIds = new ArrayList<>();
    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);

    scheduleTaskIds.add("1100");

    try {
      schedulingClient.getScheduleTask(scheduleTaskIds);
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }
  }

  @Test
  public void shouldCheckGetAllScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getAllScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ArrayList<ScheduleTask>(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    schedulingClient.getAllScheduleTask("1100");

    verify(schedulingRest).getAllScheduleTask(stringArgumentCaptor.capture());
    assertEquals("1100", stringArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInGetAllScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getAllScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.getAllScheduleTask("1100");
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }

  }

  @Test
  public void shouldCheckIsEmptyInUpdateScheduleTask() {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);

    try {
      schedulingClient.updateScheduleTask(new ScheduleTask());
    } catch (Exception e) {
      assertEquals("Schedule task id can not be empty", e.getMessage());

    }
  }

  @Test
  public void shouldCheckUpdateScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.updateScheduleTask(Mockito.any(String.class),
        Mockito.any(ScheduleTask.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ScheduleTask(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    when(scheduleTask.getId()).thenReturn("1100");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);

    schedulingClient.updateScheduleTask(scheduleTask);

    verify(schedulingRest).updateScheduleTask(stringArgumentCaptor.capture(),
        scheduleTaskArgumentCaptor.capture());
    assertEquals(scheduleTask, scheduleTaskArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInUpdateScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.updateScheduleTask(Mockito.any(String.class),
        Mockito.any(ScheduleTask.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    when(scheduleTask.getId()).thenReturn("1100");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.updateScheduleTask(scheduleTask);
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }


  }

  @Test
  public void shouldCheckCreateScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.createScheduleTask(Mockito.any(ScheduleTask.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ScheduleTask(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);

    schedulingClient.createScheduleTask(scheduleTask);

    verify(schedulingRest).createScheduleTask(scheduleTaskArgumentCaptor.capture());
    assertEquals(scheduleTask, scheduleTaskArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInCreateScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.createScheduleTask(Mockito.any(ScheduleTask.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.createScheduleTask(scheduleTask);
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }
  }

  @Test
  public void shouldCheckMigrateScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.migrateScheduleTask(Mockito.any(ScheduleTask.class))).thenReturn(
        mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ScheduleTask(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);

    schedulingClient.migrateScheduleTask(scheduleTask);

    verify(schedulingRest).migrateScheduleTask(scheduleTaskArgumentCaptor.capture());
    assertEquals(scheduleTask, scheduleTaskArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInMigrateScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.migrateScheduleTask(Mockito.any(ScheduleTask.class))).thenReturn(
        mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.migrateScheduleTask(scheduleTask);
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }
  }

  @Test
  public void shouldCheckIsEmptyInDeleteScheduleTask() {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);

    try {
      schedulingClient.deleteScheduleTask("");
    } catch (Exception e) {
      assertEquals("Schedule task id can not be empty", e.getMessage());
    }
  }

  @Test
  public void shouldCheckDeleteScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.deleteScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(responseBody,
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    schedulingClient.deleteScheduleTask("1100");

    verify(schedulingRest).deleteScheduleTask(stringArgumentCaptor.capture());
    assertEquals("1100", stringArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInDeleteScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.deleteScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.deleteScheduleTask("1100");
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());

    }
  }

  @Test
  public void shouldCheckIsEmptyInExecuteScheduleTask() {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    try {
      schedulingClient.executeScheduleTask("");
    } catch (Exception e) {
      assertEquals("Schedule task id can not be empty", e.getMessage());
    }
  }

  @Test
  public void shouldCheckExecuteScheduleTask() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.executeScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(responseBody,
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    schedulingClient.executeScheduleTask("1100");

    verify(schedulingRest).executeScheduleTask(stringArgumentCaptor.capture());
    assertEquals("1100", stringArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInExecuteScheduleTask() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.executeScheduleTask(Mockito.any(String.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.executeScheduleTask("1100");
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());

    }
  }


  @Test
  public void shouldCheckGetNextRunsInfo() throws IOException, SchedulingClientException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getNextRunsInfo(Mockito.any(Trigger.class))).thenReturn(mockedCall);

    when(mockedCall.execute()).thenReturn(success(new ArrayList<ZonedDateTime>(),
        (new okhttp3.Response.Builder()).code(200).message("OK").protocol(Protocol.HTTP_1_1)
            .request((new okhttp3.Request.Builder()).url("http://localhost/").build()).build()));

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    schedulingClient.getNextRunsInfo(trigger);

    verify(schedulingRest).getNextRunsInfo(triggerArgumentCaptor.capture());

    assertEquals(trigger, triggerArgumentCaptor.getValue());
  }

  @Test
  public void shouldCheckIOExceptionInGetNextRunsInfo() throws IOException {
    when(schedulingConfig.getUrl()).thenReturn("http://localhost:8080");

    when(schedulingRest.getNextRunsInfo(Mockito.any(Trigger.class))).thenReturn(mockedCall);
    when(mockedCall.execute()).thenThrow(new IOException());

    SchedulingClientImpl schedulingClient = new SchedulingClientImpl(schedulingConfig);
    ReflectionTestUtils.setField(schedulingClient, "schedulingRest", schedulingRest);
    try {
      schedulingClient.getNextRunsInfo(trigger);
    } catch (SchedulingClientException e) {
      assertEquals("Failed to make API call", e.getMessage());
    }

  }



}