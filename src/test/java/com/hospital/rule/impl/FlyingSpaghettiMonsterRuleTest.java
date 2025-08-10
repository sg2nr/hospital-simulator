package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import com.hospital.domain.HealthState;
import com.hospital.rule.BinomialSampler;

class FlyingSpaghettiMonsterRuleTest {

  @Test
  void testApplyWhenTwoMillionDeadShouldReturnTwoHealthy() {
    // Given
    int deadCount = 2_000_000;
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.DEAD, deadCount, HealthState.HEALTHY, 0);

    // When
    BinomialSampler binomialSampler = Mockito.mock(BinomialSampler.class);
    Mockito.when(binomialSampler.sample(anyInt(), anyDouble())).thenReturn(2);
    FlyingSpaghettiMonsterRule rule = new FlyingSpaghettiMonsterRule(binomialSampler);
    Map<HealthState, Integer> result = rule.apply(initialCounts, Set.of());

    // Then
    int expectedResurrected = 2;
    assertEquals(deadCount - expectedResurrected, result.get(HealthState.DEAD));
    assertEquals(expectedResurrected, result.get(HealthState.HEALTHY));
  }

  @Test
  void testApplyWhenFlyingSpaghettiMonsterDoesNotResurrectShouldReturnAnyHealthy() {
    // Given
    int deadCount = 10;
    Map<HealthState, Integer> initialCounts = Map.of(HealthState.DEAD, deadCount, HealthState.HEALTHY, 0);

    // When
    BinomialSampler binomialSampler = Mockito.mock(BinomialSampler.class);
    Mockito.when(binomialSampler.sample(anyInt(), anyDouble())).thenReturn(0);
    FlyingSpaghettiMonsterRule rule = new FlyingSpaghettiMonsterRule(binomialSampler);
    Map<HealthState, Integer> result = rule.apply(initialCounts, Set.of());

    // Then
    assertEquals(deadCount, result.get(HealthState.DEAD));
    assertEquals(0, result.get(HealthState.HEALTHY));
  }

  @ParameterizedTest
  @EnumSource(value = HealthState.class, names = { "HEALTHY", "FEVER", "TUBERCULOSIS", "DIABETES" })
  void testApplyWhenHealthStateIsNotDeadShouldNotChangeThePatientsStates(HealthState currentHealthState) {
    // Given
    int count = 2_000_000;
    Map<HealthState, Integer> initialCounts = Map.of(currentHealthState, count);

    // When
    FlyingSpaghettiMonsterRule rule = new FlyingSpaghettiMonsterRule();
    Map<HealthState, Integer> result = rule.apply(initialCounts, Set.of());

    // Then
    assertEquals(count, result.get(currentHealthState));
    assertEquals(0, result.get(HealthState.DEAD));
  }

  @Test
  void testApplyWhenPatientsByStateIsEmptyShouldReturnMapWithPatientsTotalCountAsZero() {
    // Given
    Map<HealthState, Integer> initialCounts = Map.of();

    // When
    FlyingSpaghettiMonsterRule rule = new FlyingSpaghettiMonsterRule();
    Map<HealthState, Integer> result = rule.apply(initialCounts, Set.of());

    // Then
    assertEquals(0, result.getOrDefault(HealthState.HEALTHY, 0));
    assertEquals(0, result.getOrDefault(HealthState.DEAD, 0));
    assertEquals(0, result.getOrDefault(HealthState.FEVER, 0));
    assertEquals(0, result.getOrDefault(HealthState.TUBERCULOSIS, 0));
    assertEquals(0, result.getOrDefault(HealthState.DIABETES, 0));
  }
}
