package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class AntibioticRuleTest {

  @Test
  void testApplyWhenHealthStateIsTuberculosisAndAntibioticIsGivenShouldReturnHealthy() {
    // Given
    HealthState currentHealthState = HealthState.TUBERCULOSIS;
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);
    AntibioticRule antibioticRule = new AntibioticRule();

    // When
    HealthState newHealthState = antibioticRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.HEALTHY, newHealthState);
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "FEVER", "DIABETES", "DEAD" })
  void testApplyWhenHealthStateIsNotTuberculosisShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);
    AntibioticRule antibioticRule = new AntibioticRule();

    // When
    HealthState newHealthState = antibioticRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }

  @ParameterizedTest
  @EnumSource(HealthState.class)
  void testApplyWhenAntibioticIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of();
    AntibioticRule antibioticRule = new AntibioticRule();

    // When
    HealthState newHealthState = antibioticRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }
}
