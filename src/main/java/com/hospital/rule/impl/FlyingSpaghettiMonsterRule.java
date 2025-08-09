package com.hospital.rule.impl;

import java.util.Random;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

public class FlyingSpaghettiMonsterRule implements Rule {

  private static final double RESURRECTION_PROBABILITY = 1.0 / 1_000_000;

  private final Random random;

  public FlyingSpaghettiMonsterRule(Random random) {
    this.random = random;
  }

  @Override
  public HealthState apply(HealthState currentHealthState, Set<Drug> drugs) {
    if (currentHealthState == HealthState.DEAD && random.nextDouble() < RESURRECTION_PROBABILITY) {
      return HealthState.HEALTHY;
    }

    return currentHealthState;
  }
}
