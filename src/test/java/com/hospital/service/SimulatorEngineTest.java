package com.hospital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.domain.SimulationRequest;
import com.hospital.domain.SimulationResponse;
import com.hospital.rule.Rule;
import com.hospital.rule.impl.AntibioticRule;
import com.hospital.rule.impl.AspirinRule;
import com.hospital.rule.impl.FlyingSpaghettiMonsterRule;
import com.hospital.rule.impl.InsulinRule;
import com.hospital.rule.impl.ParacetamolRule;

class SimulatorEngineTest {

  // From example #1
  @Test
  void testSimulateWhenAllRulesAreAppliedToTwoDiabetesAndNoDrugsShouldReturnTwoDiabetes() {
    // Given
    SimulationRequest request = new SimulationRequest(Map.of(HealthState.DIABETES, 2), Set.of());

    List<Rule> rules = List.of(new AspirinRule(), new AntibioticRule(), new InsulinRule(), new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule(new Random()));
    SimulatorEngine simulatorEngine = new SimulatorEngine(rules);

    // When
    SimulationResponse response = simulatorEngine.simulate(request);

    // Then
    assertEquals(2, response.patientsByState().get(HealthState.DIABETES));
    assertEquals(0, response.patientsByState().get(HealthState.HEALTHY));
    assertEquals(0, response.patientsByState().get(HealthState.FEVER));
    assertEquals(0, response.patientsByState().get(HealthState.TUBERCULOSIS));
    assertEquals(0, response.patientsByState().get(HealthState.DEAD));
  }

  // From example #2
  @Test
  void testSimulateWhenAllRulesAreAppliedToOneFeverAndParacetamolShouldReturnOneHealthy() {
    // Given
    SimulationRequest request = new SimulationRequest(Map.of(HealthState.FEVER, 1), Set.of(Drug.PARACETAMOL));

    List<Rule> rules = List.of(new AspirinRule(), new AntibioticRule(), new InsulinRule(), new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule(new Random()));
    SimulatorEngine simulatorEngine = new SimulatorEngine(rules);

    // When
    SimulationResponse response = simulatorEngine.simulate(request);

    // Then
    assertEquals(1, response.patientsByState().get(HealthState.HEALTHY));
    assertEquals(0, response.patientsByState().get(HealthState.FEVER));
    assertEquals(0, response.patientsByState().get(HealthState.DIABETES));
    assertEquals(0, response.patientsByState().get(HealthState.TUBERCULOSIS));
    assertEquals(0, response.patientsByState().get(HealthState.DEAD));
  }

  // From example #3
  @Test
  void testSimulateWhenAllRulesAreAppliedToTuberculosisAndFeverAndDiabetesAndAntibioticAndInsulinShouldReturnTwoFeverAndOneDiabetes() {
    // Given
    SimulationRequest request = new SimulationRequest(
        Map.of(HealthState.TUBERCULOSIS, 1, HealthState.FEVER, 1, HealthState.DIABETES, 1),
        Set.of(Drug.ANTIBIOTIC, Drug.INSULIN));

    List<Rule> rules = List.of(new AspirinRule(), new AntibioticRule(), new InsulinRule(), new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule(new Random()));
    SimulatorEngine simulatorEngine = new SimulatorEngine(rules);

    // When
    SimulationResponse response = simulatorEngine.simulate(request);

    // Then
    assertEquals(2, response.patientsByState().get(HealthState.FEVER));
    assertEquals(0, response.patientsByState().get(HealthState.HEALTHY));
    assertEquals(1, response.patientsByState().get(HealthState.DIABETES));
    assertEquals(0, response.patientsByState().get(HealthState.TUBERCULOSIS));
    assertEquals(0, response.patientsByState().get(HealthState.DEAD));
  }

  @Test
  void testSimulateWhenAllRulesAreAppliedToHealthyAndTuberculosisAndFeverAndDiabetesAndAntibioticAndInsulinShouldReturnThreeFeverAndOneDiabetes() {
    // Given
    SimulationRequest request = new SimulationRequest(
            Map.of(HealthState.TUBERCULOSIS, 1, HealthState.FEVER, 1, HealthState.DIABETES, 1, HealthState.HEALTHY, 1),
            Set.of(Drug.ANTIBIOTIC, Drug.INSULIN));

    List<Rule> rules = List.of(new AspirinRule(), new AntibioticRule(), new InsulinRule(), new ParacetamolRule(),
            new FlyingSpaghettiMonsterRule(new Random()));
    SimulatorEngine simulatorEngine = new SimulatorEngine(rules);

    // When
    SimulationResponse response = simulatorEngine.simulate(request);

    // Then
    assertEquals(3, response.patientsByState().get(HealthState.FEVER));
    assertEquals(0, response.patientsByState().get(HealthState.HEALTHY));
    assertEquals(1, response.patientsByState().get(HealthState.DIABETES));
    assertEquals(0, response.patientsByState().get(HealthState.TUBERCULOSIS));
    assertEquals(0, response.patientsByState().get(HealthState.DEAD));
  }

}
