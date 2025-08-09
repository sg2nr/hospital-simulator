package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class FlyingSpaghettiMonsterRuleTest {

  @Test
  void testApplyWhenHealthStateIsDeadAndFlyingSpaghettiMonsterResurrectsShouldReturnHealthy() {
    // Given
    HealthState currentHealthState = HealthState.DEAD;
    Set<Drug> drugs = Set.of();

    Random mockRandom = Mockito.mock(Random.class);
    Mockito.when(mockRandom.nextDouble()).thenReturn(0.0); // Simulate the condition for resurrection
    FlyingSpaghettiMonsterRule flyingSpaghettiMonsterRule = new FlyingSpaghettiMonsterRule(mockRandom);

    // When
    HealthState newHealthState = flyingSpaghettiMonsterRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.HEALTHY, newHealthState);
  }

  @Test
  void testApplyWhenHealthStateIsDeadAndFlyingSpaghettiMonsterDoesNotResurrectShouldReturnDead() {
    // Given
    HealthState currentHealthState = HealthState.DEAD;
    Set<Drug> drugs = Set.of();

    Random mockRandom = Mockito.mock(Random.class);
    Mockito.when(mockRandom.nextDouble()).thenReturn(1.0); // Simulate the condition for no resurrection
    FlyingSpaghettiMonsterRule flyingSpaghettiMonsterRule = new FlyingSpaghettiMonsterRule(mockRandom);

    // When
    HealthState newHealthState = flyingSpaghettiMonsterRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(HealthState.DEAD, newHealthState);
  }

  @ParameterizedTest
  @CsvSource({
      "HEALTHY", "FEVER", "TUBERCULOSIS", "DIABETES"
  })
  void testApplyWhenHealthStateIsNotDeadAndFlyingSpaghettiMonsterDoesNotResurrectShouldNotChangeHealthState(
      HealthState currentHealthState) {
    // Given
    Set<Drug> drugs = Set.of();

    Random mockRandom = Mockito.mock(Random.class);
    Mockito.when(mockRandom.nextDouble()).thenReturn(1.0); // Simulate the condition for no resurrection
    FlyingSpaghettiMonsterRule flyingSpaghettiMonsterRule = new FlyingSpaghettiMonsterRule(mockRandom);

    // When
    HealthState newHealthState = flyingSpaghettiMonsterRule.apply(currentHealthState, drugs);

    // Then
    assertEquals(currentHealthState, newHealthState);
  }
}
