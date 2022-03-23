package com.mediaiq.caps.platform.scheduling.commons.model;

public enum ExecutionStatusEnum {
  SUCCESS("SUCCESS"),
  FAILURE("FAILURE");

  private String value;

  ExecutionStatusEnum(String value) {
    this.value = value;
  }

  public static ExecutionStatusEnum fromValue(String text) {
    for (ExecutionStatusEnum b : ExecutionStatusEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}

