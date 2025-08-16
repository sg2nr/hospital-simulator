package com.hospital.domain;

public enum HealthState {

  HEALTHY("H"),
  FEVER("F"),
  DIABETES("D"),
  TUBERCULOSIS("T"),
  DEAD("X");

  private final String code;

  HealthState(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static HealthState fromCode(String code) {
    for (HealthState state : values()) {
      if (state.code.equalsIgnoreCase(code))
        return state;
    }
    throw new IllegalArgumentException("Invalid Health State: " + code);
  }
}
