package com.hospital.rule.impl;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class AspirinRule implements Rule {

  @Override
  public HealthState apply(HealthState currenHealthState, Set<Drug> drugs) {
    if (currenHealthState == HealthState.FEVER && drugs.contains(Drug.ASPIRIN)) {
      return HealthState.HEALTHY;
    }
    return currenHealthState;
  }
}
