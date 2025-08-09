package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class AntibioticRuleTest {

  @Test
  void testApplyWhenAllPatientsHaveTubercolisAndAntiobioticIsGivenShouldReturnAllHealthy() {
    // Given
    int tuberculosisCount = 1_000;
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.TUBERCULOSIS, tuberculosisCount);
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);

    // When
    AntibioticRule antibioticRule = new AntibioticRule();
    Map<HealthState, Integer> result = antibioticRule.apply(initialCounts, drugs);

    // Then
    int expectedHealthyCount = tuberculosisCount;
    assertEquals(expectedHealthyCount, result.get(HealthState.HEALTHY));
    assertEquals(0, result.get(HealthState.TUBERCULOSIS));
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "FEVER", "DIABETES", "DEAD" })
  void testApplyWhenHealthStateIsNotTuberculosisShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 1_000;
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);

    // When
    AntibioticRule antibioticRule = new AntibioticRule();
    Map<HealthState, Integer> result = antibioticRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @ParameterizedTest
  @EnumSource(HealthState.class)
  void testApplyWhenAntibioticIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 1_000;
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    AntibioticRule antibioticRule = new AntibioticRule();
    Map<HealthState, Integer> result = antibioticRule.apply(initialCounts, Set.of());

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }
}
