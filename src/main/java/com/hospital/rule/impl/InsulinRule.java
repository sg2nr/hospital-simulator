package com.hospital.rule.impl;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class InsulinRule implements Rule {

  @Override
  public HealthState apply(HealthState currenHealthState, Set<Drug> drugs) {

    if (drugs.contains(Drug.INSULIN)) {
      if (currenHealthState == HealthState.HEALTHY && drugs.contains(Drug.ANTIBIOTIC)) {
        return HealthState.FEVER;
      }

      if (currenHealthState == HealthState.DIABETES) {
        return HealthState.DIABETES;
      }
    }

    return currenHealthState;
  }
}
