package com.hospital.rule.impl;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class ParacetamolRule implements Rule {

  @Override
  public HealthState apply(HealthState currenHealthState, Set<Drug> drugs) {

    if (drugs.contains(Drug.PARACETAMOL)) {

      if (drugs.contains(Drug.ASPIRIN)) {
        return HealthState.DEAD;
      }

      if (currenHealthState == HealthState.FEVER) {
        return HealthState.HEALTHY;
      }
    }
    return currenHealthState;
  }
}
