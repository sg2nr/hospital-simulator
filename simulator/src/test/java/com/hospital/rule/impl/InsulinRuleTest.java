package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class InsulinRuleTest {

  @Test
  void testApplyWhenHealthStateIsDiabetesAndInsulinIsGiven() {
    // Given
    HealthState currentHealthState = HealthState.DIABETES;
    Set<Drug> drugs = Set.of(Drug.INSULIN);
    InsulinRule insulinRule = new InsulinRule();

    // When
    HealthState newHealthState = insulinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.DIABETES, newHealthState);
  }

  @Test
  void testApplyWhenHealthStateIsHealthyAndInsulinAndAntibioticAreGiven() {
    // Given
    HealthState currentHealthState = HealthState.HEALTHY;
    Set<Drug> drugs = Set.of(Drug.INSULIN, Drug.ANTIBIOTIC);
    InsulinRule insulinRule = new InsulinRule();

    // When
    HealthState newHealthState = insulinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.FEVER, newHealthState);
  }
}
