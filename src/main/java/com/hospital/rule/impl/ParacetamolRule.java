package com.hospital.rule.impl;

import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;
import com.hospital.rule.impl.utils.HealthStateMapBuilder;
import com.hospital.rule.impl.utils.RuleValidationUtils;

public class ParacetamolRule implements Rule {
  
  @Override
  public Map<HealthState, Integer> apply(Map<HealthState, Integer> patientsByState, Set<Drug> drugs) {
    RuleValidationUtils.validateRulePreconditions(patientsByState, drugs);
    if (drugs.contains(Drug.PARACETAMOL)) {
      if (drugs.contains(Drug.ASPIRIN)) {

        return HealthStateMapBuilder.from(patientsByState)
            .killAll()
            .build();
      }

      int feverCount = patientsByState.getOrDefault(HealthState.FEVER, 0);
      return HealthStateMapBuilder.from(patientsByState)
          .transition(HealthState.FEVER, HealthState.HEALTHY, feverCount)
          .build();
    }
    return HealthStateMapBuilder.from(patientsByState).build();
  }
}
