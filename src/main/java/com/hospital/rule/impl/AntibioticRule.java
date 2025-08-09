package com.hospital.rule.impl;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class AntibioticRule implements Rule {

  @Override
  public HealthState apply(HealthState currenHealthState, Set<Drug> drugs) {
    if (currenHealthState == HealthState.TUBERCULOSIS && drugs.contains(Drug.ANTIBIOTIC)) {
      return HealthState.HEALTHY;
    }
    return currenHealthState;
  }
}
