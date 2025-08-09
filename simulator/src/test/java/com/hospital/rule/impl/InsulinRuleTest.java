package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class InsulinRuleTest {

  @Test
  void testApplyWhenHealthStateIsDiabetesAndInsulinIsGivenShouldReturnDiabetes() {
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
  void testApplyWhenHealthStateIsHealthyAndInsulinAndAntibioticAreGivenShouldReturnFever() {
    // Given
    HealthState currentHealthState = HealthState.HEALTHY;
    Set<Drug> drugs = Set.of(Drug.INSULIN, Drug.ANTIBIOTIC);
    InsulinRule insulinRule = new InsulinRule();

    // When
    HealthState newHealthState = insulinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.FEVER, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({
      "HEALTHY", "FEVER", "DIABETES", "TUBERCULOSIS", "DEAD"
  })
  void testApplyWhenInsulinIsNoGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of();
    InsulinRule insulinRule = new InsulinRule();

    // When
    HealthState newHealthState = insulinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }
}
