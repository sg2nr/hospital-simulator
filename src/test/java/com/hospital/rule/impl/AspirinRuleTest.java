package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class AspirinRuleTest {

  @Test
  void testApplyWhenHealthStateIsFeverAndAspirinIsGivenShouldReturnHealthy() {
    // Given
    HealthState currentHealthState = HealthState.FEVER;
    Set<Drug> drugs = Set.of(Drug.ASPIRIN);
    AspirinRule aspirinRule = new AspirinRule();

    // When
    HealthState newHealthState = aspirinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.HEALTHY, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({ "HEALTHY", "TUBERCULOSIS", "DIABETES", "DEAD" })
  void testApplyWhenHealthStateIsNotFeverShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of(Drug.ASPIRIN);
    AspirinRule aspirinRule = new AspirinRule();

    // When
    HealthState newHealthState = aspirinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({ "HEALTHY", "FEVER", "TUBERCULOSIS", "DIABETES", "DEAD" })
  void testApplyWhenAspirinIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of();
    AspirinRule aspirinRule = new AspirinRule();

    // When
    HealthState newHealthState = aspirinRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }
}
