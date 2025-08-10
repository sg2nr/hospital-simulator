package com.hospital.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

public record SimulationRequest(Map<HealthState, Integer> initialPatients,
        Set<Drug> drugs) {

    public SimulationRequest {
        List<String> errors = new ArrayList<>();

        if (initialPatients == null) {
            errors.add("Initial patients map cannot be null.");
        } else if (initialPatients.values().stream().anyMatch(count -> count < 0)) {
            errors.add("Patient counts cannot be negative.");
        }

        if (drugs == null) {
            drugs = Set.of();
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid SimulationRequest: " + String.join("\n", errors));
        }
    }
}
