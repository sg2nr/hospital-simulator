package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class ParacetamolRuleTest {

  @Test
  void testApplyWhenHealthStateIsFeverAndParacetamolIsGivenShouldReturnHealthy() {
    // Given
    HealthState currentHealthState = HealthState.FEVER;
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);
    ParacetamolRule paracetamolRule = new ParacetamolRule();

    // When
    HealthState newHealthState = paracetamolRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.HEALTHY, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({
      "HEALTHY", "FEVER", "TUBERCULOSIS", "DIABETES", "DEAD"
  })
  void testApplyWhenParacetamolAndAspirinAreGivenShouldReturnDead(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL, Drug.ASPIRIN);
    ParacetamolRule paracetamolRule = new ParacetamolRule();

    // When
    HealthState newHealthState = paracetamolRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.DEAD, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({
      "HEALTHY", "TUBERCULOSIS", "DIABETES", "DEAD"
  })
  void testApplyWhenHealthStateIsNotFeverAndParacetamolIsGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);
    ParacetamolRule paracetamolRule = new ParacetamolRule();

    // When
    HealthState newHealthState = paracetamolRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({
      "HEALTHY", "FEVER", "TUBERCULOSIS", "DIABETES", "DEAD"
  })
  void testApplyWhenParacetamolIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of();
    ParacetamolRule paracetamolRule = new ParacetamolRule();

    // When
    HealthState newHealthState = paracetamolRule.apply(currentHealthState, drugs);  

    // Then
    assertEquals(currentHealthState, newHealthState);
  }
}
