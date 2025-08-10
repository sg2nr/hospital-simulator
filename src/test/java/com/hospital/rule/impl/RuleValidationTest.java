package com.hospital.rule.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.Rule;

class RuleValidationTest {

  static Stream<Rule> allRules() {
    return Stream.of(
        new AntibioticRule(),
        new FlyingSpaghettiMonsterRule(),
        new InsulinRule(),
        new ParacetamolRule());
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenPatientsByStateIsNullShouldThrowIllegalArgumentException(Rule rule) {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> rule.apply(null, Set.of(Drug.PARACETAMOL)));
    assertEquals("Information about patients health state cannot be null.", exception.getMessage());
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenDrugsIsNullShouldThrowIllegalArgumentException(Rule rule) {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> rule.apply(Map.of(HealthState.HEALTHY, 1), null));
    assertEquals("Information about drugs cannot be null.", exception.getMessage());
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenPatientsByStateHasNegativeCountShouldThrowIllegalArgumentException(Rule rule) {
    Map<HealthState, Integer> patientsByState = Map.of(HealthState.HEALTHY, -1);
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> rule.apply(patientsByState, Set.of(Drug.PARACETAMOL)));
    assertEquals("Patient counts cannot be negative.", exception.getMessage());
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenAllPreconditionsAreMetShouldNotThrowException(Rule rule) {
    Map<HealthState, Integer> patientsByState = Map.of(HealthState.HEALTHY, 10);
    Set<Drug> drugs = Set.of(Drug.PARACETAMOL);

    assertDoesNotThrow(() -> rule.apply(patientsByState, drugs));
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenAllPatientsByStateHasNegativeCountAndDrugsAreNullShouldThrowException(Rule rule) {
    Map<HealthState, Integer> patientsByState = Map.of(HealthState.HEALTHY, -1);
    Set<Drug> drugs = null;
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> rule.apply(patientsByState, drugs));

    String expectedMessage = """
        Information about drugs cannot be null.
        Patient counts cannot be negative.""";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @ParameterizedTest
  @MethodSource("allRules")
  void testApplyWhenAllPatientsByStateIsNullAndDrugsAreNullShouldThrowException(Rule rule) {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> rule.apply(null, null));

    String expectedMessage = """
        Information about patients health state cannot be null.
        Information about drugs cannot be null.""";
    assertEquals(expectedMessage, exception.getMessage());
  }
}
