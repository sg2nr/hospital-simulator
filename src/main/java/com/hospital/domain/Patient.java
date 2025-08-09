package com.hospital.domain;

public class Patient {

  private HealthState healthState;

  public Patient(HealthState healthState) {
    this.healthState = healthState;
  }

  public void setHealthState(HealthState healthState) {
    this.healthState = healthState;
  }

  public HealthState getHealthState() {
    return healthState;
  }
}
