package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class AspirinRuleTest {

  @Test
  void testApplyWhenAllPatientsHaveFeverAndAspirinIsGivenShouldReturnAllHealthy() {
    // Given
    int feverCount = 1_000;
    Set<Drug> drugs = Set.of(Drug.ASPIRIN);
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.FEVER, feverCount);

    // When
    AspirinRule aspirinRule = new AspirinRule();
    Map<HealthState, Integer> result = aspirinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(feverCount, result.get(HealthState.HEALTHY));
    assertEquals(0, result.get(HealthState.FEVER));
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "TUBERCULOSIS", "DIABETES", "DEAD" })
  void testApplyWhenHealthStateIsNotFeverAndAspirinIsGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 300_000;
    Set<Drug> drugs = Set.of(Drug.ASPIRIN);
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    AspirinRule aspirinRule = new AspirinRule();
    Map<HealthState, Integer> result = aspirinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(0, result.get(HealthState.FEVER));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @ParameterizedTest
  @EnumSource(HealthState.class)
  void testApplyWhenAspirinIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 10_000;
    Set<Drug> drugs = Set.of();
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    AspirinRule aspirinRule = new AspirinRule();
    Map<HealthState, Integer> result = aspirinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @Test
  void testApplyWhenPatientsByStateIsEmptyShouldReturnMapWithPatientsTotalCountAsZero() {
    // Given
    Map<HealthState, Integer> initialCounts = Map.of();
    Set<Drug> drugs = Set.of(Drug.ASPIRIN);

    // When
    AspirinRule aspirinRule = new AspirinRule();
    Map<HealthState, Integer> result = aspirinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.get(HealthState.HEALTHY));
    assertEquals(0, result.get(HealthState.FEVER));
    assertEquals(0, result.get(HealthState.DIABETES));
    assertEquals(0, result.get(HealthState.TUBERCULOSIS));
    assertEquals(0, result.get(HealthState.DEAD));
  }
}
