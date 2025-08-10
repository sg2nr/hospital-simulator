package com.hospital.rule.impl;

import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;
import com.hospital.rule.impl.utils.HealthStateMapBuilder;
import com.hospital.rule.impl.utils.RuleValidationUtils;

public class InsulinRule implements Rule {

  @Override
  public Map<HealthState, Integer> apply(Map<HealthState, Integer> patientsByState, Set<Drug> drugs) {
    RuleValidationUtils.validateRulePreconditions(patientsByState, drugs);
    if (drugs.contains(Drug.INSULIN) && drugs.contains(Drug.ANTIBIOTIC)) {
      int healthyCount = patientsByState.getOrDefault(HealthState.HEALTHY, 0);
      return HealthStateMapBuilder.from(patientsByState)
          .transition(HealthState.HEALTHY, HealthState.FEVER, healthyCount)
          .build();
    }

    if (drugs.contains(Drug.INSULIN)) {
      return HealthStateMapBuilder.from(patientsByState)
          .build();
    }

    int diabetesCount = patientsByState.getOrDefault(HealthState.DIABETES, 0);
    return HealthStateMapBuilder.from(patientsByState)
        .transition(HealthState.DIABETES, HealthState.DEAD, diabetesCount)
        .build();
  }
}
