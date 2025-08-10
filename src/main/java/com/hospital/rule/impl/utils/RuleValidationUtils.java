package com.hospital.rule.impl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

public final class RuleValidationUtils {

  private RuleValidationUtils() {
    // Utility class, no instantiation needed
  }

  public static void validateRulePreconditions(Map<HealthState, Integer> patientsByState, Set<Drug> drugs) {

    List<String> errors = new ArrayList<>();
    if (patientsByState == null) {
      errors.add("Information about patients health state cannot be null.");
    }

    if (drugs == null) {
      errors.add("Information about drugs cannot be null.");
    }

    if (patientsByState != null && patientsByState.values().stream().anyMatch(count -> count < 0)) {
      errors.add("Patient counts cannot be negative.");
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException(String.join("\n", errors));
    }
  }
}
