package com.mediaiq.caps.platform.scheduling.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

import com.mediaiq.caps.platform.scheduling.commons.model.ScheduleTask;
import com.mediaiq.caps.platform.scheduling.commons.model.Trigger;

public class UtilsTest {

  @Test
  public void testConvertScheduleTimezone() {
    // Setup
    String zone = "-07:00";
    ZoneId zoneId = ZoneId.of(zone);
    ZonedDateTime start = ZonedDateTime.now();
    ZonedDateTime end = start.plusHours(2);
    ZonedDateTime startTransformed = start.withZoneSameInstant(zoneId);
    ZonedDateTime endTransformed = end.withZoneSameInstant(zoneId);
    Trigger trigger = Trigger.builder().startDateTime(start).endDateTime(end).build();
    final ScheduleTask scheduleTask = ScheduleTask.builder().trigger(trigger).build();
    scheduleTask.setZoneID(zone);
    // Run the test
    Utils.convertScheduleTimezone(scheduleTask);

    // Verify the results
    Assert.assertEquals(startTransformed, scheduleTask.getTrigger().getStartDateTime());
    Assert.assertEquals(endTransformed, scheduleTask.getTrigger().getEndDateTime());
  }
}
