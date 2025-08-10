package com.hospital.client.model;

import java.util.Map;

import com.hospital.domain.HealthState;

public record SimulationResponse(
    Map<HealthState, Integer> patientsByState
) {}
