package com.hospital.rule.impl;

import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.BinomialSampler;
import com.hospital.rule.Rule;
import com.hospital.rule.impl.utils.HealthStateMapBuilder;
import com.hospital.rule.impl.utils.RuleValidationUtils;

public class FlyingSpaghettiMonsterRule implements Rule {

  private static final double RESURRECTION_PROBABILITY = 1.0 / 1_000_000;

  private final BinomialSampler binomialSampler;

  public FlyingSpaghettiMonsterRule() {
    this.binomialSampler = new ApacheBinomialSampler();
  }

  public FlyingSpaghettiMonsterRule(BinomialSampler binomialSampler) {
    this.binomialSampler = binomialSampler;
  }

  @Override
  public Map<HealthState, Integer> apply(Map<HealthState, Integer> patientsByState, Set<Drug> drugs) {
    RuleValidationUtils.validateRulePreconditions(patientsByState, drugs);
    int deadCount = patientsByState.getOrDefault(HealthState.DEAD, 0);   

    if (deadCount == 0) {
      return HealthStateMapBuilder.from(patientsByState).build();
    }

    int resurrected = binomialSampler.sample(deadCount, RESURRECTION_PROBABILITY);
    
    return HealthStateMapBuilder.from(patientsByState)
        .transition(HealthState.DEAD, HealthState.HEALTHY, resurrected)
        .build();
  }
}
