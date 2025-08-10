package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class ParacetamolRuleTest {

  @Test
  void testApplyWhenAllPatientsHaveFeverAndParacetamolIsGivenShouldReturnAllHealthy() {
    // Given
    int feverCount = 1_000;
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.FEVER, feverCount);

    // When
    ParacetamolRule paracetamolRule = new ParacetamolRule();
    Map<HealthState, Integer> result = paracetamolRule.apply(initialCounts, drugs);

    // Then
    int expectedHealthyCount = feverCount;
    assertEquals(expectedHealthyCount, result.get(HealthState.HEALTHY));
    assertEquals(0, result.get(HealthState.FEVER));
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "TUBERCULOSIS", "DIABETES", "DEAD" })
  void testApplyWhenHealthStateIsNotFeverAndParacetamolIsGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 1_000;
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    ParacetamolRule paracetamolRule = new ParacetamolRule();
    Map<HealthState, Integer> result = paracetamolRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  } 

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "TUBERCULOSIS", "DIABETES", "FEVER"})
  void testApplyWhenParacetamolAndAspirinAreGivenShouldReturnDead(HealthState currentHealthState) {
    // Given
    int count = 1_000;
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL, Drug.ASPIRIN);
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);
    
    // When
    ParacetamolRule paracetamolRule = new ParacetamolRule();
    Map<HealthState, Integer> result = paracetamolRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.get(currentHealthState));
    assertEquals(count, result.get(HealthState.DEAD));
  }

  @ParameterizedTest
  @EnumSource(HealthState.class)
  void testApplyWhenParacetamolIsNotGivenShouldNotChangeHealthState(HealthState currentHealthState) {
    // Given
    int count = 2_000;
    Set<Drug> drugs = Set.of();
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    ParacetamolRule paracetamolRule = new ParacetamolRule();
    Map<HealthState, Integer> result = paracetamolRule.apply(initialCounts, drugs);

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(count, result.values().stream().mapToInt(Integer::intValue).sum());
  }

  @Test
  void testApplyWhenPatientsBySteteIsEmptyShouldReturnMapWithPatientsTotalCountAsZero() {
    // Given
    Map<HealthState, Integer> initialCounts = Map.of();
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);

    // When
    ParacetamolRule paracetamolRule = new ParacetamolRule();
    Map<HealthState, Integer> result = paracetamolRule.apply(initialCounts, drugs);

    // Then
    assertEquals(0, result.getOrDefault(HealthState.HEALTHY, 0));
    assertEquals(0, result.getOrDefault(HealthState.FEVER, 0));
    assertEquals(0, result.getOrDefault(HealthState.TUBERCULOSIS, 0));
    assertEquals(0, result.getOrDefault(HealthState.DIABETES, 0));
  }  
}
