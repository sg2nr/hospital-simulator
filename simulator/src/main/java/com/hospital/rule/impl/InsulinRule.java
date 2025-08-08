package com.hospital.rule.impl;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class InsulinRule implements Rule {

  @Override
  public HealthState apply(HealthState currenHealthState, Set<Drug> drugs) {
    
    if (currenHealthState == HealthState.HEALTHY && drugs.contains(Drug.INSULIN) && drugs.contains(Drug.ANTIBIOTIC)) {
      return HealthState.FEVER;
    }

    if (currenHealthState == HealthState.DIABETES && drugs.contains(Drug.INSULIN)) {
      return HealthState.DIABETES;
    }

    return currenHealthState;
  }
}
