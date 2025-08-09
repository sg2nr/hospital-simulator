package com.hospital.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.domain.SimulationRequest;
import com.hospital.domain.SimulationResponse;
import com.hospital.rule.Rule;

public class SimulatorEngine {

  private final List<Rule> rules;

  public SimulatorEngine(List<Rule> rules) {
    this.rules = rules;
  }

  public SimulationResponse simulate(SimulationRequest request) {

    Map<HealthState, Integer> patientsByState = new HashMap<>();

    patientsByState.put(HealthState.HEALTHY, 0);
    patientsByState.put(HealthState.FEVER, 0);
    patientsByState.put(HealthState.DIABETES, 0);
    patientsByState.put(HealthState.TUBERCULOSIS, 0);
    patientsByState.put(HealthState.DEAD, 0);

    // Initialize the patientsByState with the initial patients from the request
    patientsByState.putAll(request.initialPatients());

    Set<HealthState> currentStates = request.initialPatients().keySet();
    Set<Drug> drugs = request.drugs();

    for (Rule rule : rules) {
      // O(s)
      Map<HealthState, HealthState> newStateMap = applyRuleToStates(rule, currentStates, drugs);

      // O(s)
      for (Map.Entry<HealthState, HealthState> entry : newStateMap.entrySet()) {
        HealthState oldState = entry.getKey();
        HealthState newState = entry.getValue();

        // Update the new state count
        Integer patientsInOldState = patientsByState.get(oldState);
        patientsByState.merge(newState, patientsInOldState, Integer::sum);

        // Reset the previous state count
        patientsByState.computeIfPresent(oldState, (k, v) -> 0);

        currentStates = Set.copyOf(patientsByState.keySet());
      }
    }

    return new SimulationResponse(patientsByState);
  }

  private Map<HealthState, HealthState> applyRuleToStates(
      Rule rules, Set<HealthState> states, Set<Drug> drugs) {
    Map<HealthState, HealthState> newStateMap = new HashMap<>();

    for (HealthState currentState : states) {
      HealthState newState = rules.apply(currentState, drugs);

      if (newState != currentState) {
        newStateMap.put(currentState, newState);
      }
    }

    return newStateMap;
  }
}
