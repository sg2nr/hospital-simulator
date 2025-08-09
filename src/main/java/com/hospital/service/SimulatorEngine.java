package com.hospital.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.domain.SimulationRequest;
import com.hospital.domain.SimulationResponse;
import com.hospital.rule.Rule;

/**
 * SimulatorEngine is responsible for simulating the application of rules
 * to a set of patients based on their health states and the drugs administered.
 * It processes the simulation request and applies each rule in sequence,
 * returning the final state of patients after all rules have been applied.
 */
public class SimulatorEngine {

  private final List<Rule> rules;

  public SimulatorEngine(List<Rule> rules) {
    this.rules = rules;
  }

  public SimulationResponse simulate(SimulationRequest request) {

    Map<HealthState, Integer> patientsByState = Map.copyOf(request.initialPatients());
    Set<Drug> drugs = request.drugs();

    for (Rule rule : rules) {
      patientsByState = rule.apply(patientsByState, drugs);
    }

    return new SimulationResponse(patientsByState);
  }
}
