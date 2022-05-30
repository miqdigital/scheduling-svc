package com.miqdigital.scheduling.integration.utils;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * The type Time utils.
 */
public class TimeUtils {

  public static final DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  /**
   * Get current time string.
   *
   * @return the string
   */
  public static String getCurrentTime() {
    return ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS)
        .format(isoDateTime);
  }

  public static String getAbsoluteCron(String date) {
    ZonedDateTime zdtInstanceAtOffset = ZonedDateTime.parse(date).plusMinutes(3);
    //    ZonedDateTime zdt = ZonedDateTime.ofInstant(zdtInstanceAtOffset.toInstant(), ZoneId.of("UTC"));
    String cron =
        "0 " + zdtInstanceAtOffset.getMinute() + " " + zdtInstanceAtOffset.getHour() + " * * ?";
    return cron;
  }

  /**
   * Get current time plus minutes string.
   *
   * @param minutes the minutes
   * @return the string
   */
  public static String getCurrentTimePlusMinutes(int minutes) {
    return ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS)
        .plusMinutes(minutes).format(isoDateTime);
  }

  public static String getCurrentTimePlusMinutes(int minutes, String tzId) {
    return ZonedDateTime.now(ZoneId.of(tzId)).truncatedTo(ChronoUnit.SECONDS).plusMinutes(minutes)
        .format(isoDateTime);
  }

  public static String getCurrentDatePlusMonths(int months) {
    return ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS)
        .plusMonths(months).format(isoDateTime);
  }

  public static long getMillis(String date) throws ParseException {
    ZonedDateTime zdtInstanceAtOffset = ZonedDateTime.parse(date);
    return zdtInstanceAtOffset.toInstant().toEpochMilli();
  }

}
