package com.hospital.domain;

import java.util.Map;
import java.util.Set;

public record SimulationRequest(Map<HealthState, Integer> initialPatients,
    Set<Drug> drugs) {

}
