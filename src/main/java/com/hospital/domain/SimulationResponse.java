package com.hospital.domain;

import java.util.Map;

public record SimulationResponse(
    Map<HealthState, Integer> patientsByState
) {}
