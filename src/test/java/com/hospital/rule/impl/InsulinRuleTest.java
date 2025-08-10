package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class InsulinRuleTest {

  @Test
  void testApplyWhenAllPatientsHaveDiabetesAndInsulinIsGivenShouldReturnAllDiabetes() {
    // Given
    int diabetesCount = 1_000;
    Set<Drug> drugs = Set.of(Drug.INSULIN);
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.DIABETES, diabetesCount);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(diabetesCount, result.get(HealthState.DIABETES));
    assertEquals(0, result.get(HealthState.HEALTHY));
  }

  @Test
  void testApplyWhenAllPatientsHaveDiabetesAndInsulinIsNotGivenShouldReturnAllDead() {
    // Given
    int diabetesCount = 1_000;
    Set<Drug> drugs = Set.of();
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.DIABETES, diabetesCount);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.get(HealthState.DIABETES));
    assertEquals(diabetesCount, result.get(HealthState.DEAD));
  }

  @Test
  void testApplyWhenAllPatientsAreHealthyAndInsulinAndAntibioticAreGivenShouldReturnAllFever() {
    // Given
    int healthyCount = 15_000;
    Set<Drug> drugs = Set.of(Drug.INSULIN, Drug.ANTIBIOTIC);
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.HEALTHY, healthyCount);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.get(HealthState.HEALTHY));
    assertEquals(healthyCount, result.get(HealthState.FEVER));
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "FEVER", "TUBERCULOSIS", "DEAD" })
  void testApplyWhenInsulinIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 10_000;
    Set<Drug> drugs = Set.of();
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "FEVER", "TUBERCULOSIS", "DEAD" })
  void testApplyWhenHealthStateIsNotDiabetesAndOnlyInsulinIsGivenShouldNotChangeHealthState(
      HealthState currentHealthState) {
    // Given
    int count = 50_000;
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);
    Set<Drug> drugs = Set.of(Drug.INSULIN);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @Test
  void testApplyWhenPatientsByStateIsEmptyShouldReturnMapWithPatientsTotalCountAsZero() {
    // Given
    Map<HealthState, Integer> initialCounts = Map.of();
    Set<Drug> drugs = Set.of(Drug.INSULIN);

    // When
    InsulinRule insulinRule = new InsulinRule();
    Map<HealthState, Integer> result = insulinRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.getOrDefault(HealthState.DIABETES, 0));
    assertEquals(0, result.getOrDefault(HealthState.HEALTHY, 0));
    assertEquals(0, result.getOrDefault(HealthState.FEVER, 0));
    assertEquals(0, result.getOrDefault(HealthState.TUBERCULOSIS, 0));
    assertEquals(0, result.getOrDefault(HealthState.DEAD, 0));
  }
}
