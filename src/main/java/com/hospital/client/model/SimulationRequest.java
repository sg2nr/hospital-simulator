package com.hospital.client.model;

import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

public record SimulationRequest(Map<HealthState, Integer> initialPatients,
        Set<Drug> drugs) {

    public SimulationRequest {
        if (initialPatients == null) {
            throw new IllegalArgumentException("Invalid SimulationRequest: Initial patients map cannot be null.");
        }

        if (initialPatients.values().stream().anyMatch(count -> count < 0)) {
            throw new IllegalArgumentException("Invalid SimulationRequest: Patient counts cannot be negative.");
        }

        if (drugs == null) {
            drugs = Set.of();
        }
    }
}
