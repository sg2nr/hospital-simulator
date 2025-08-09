package com.hospital.service;

import java.util.EnumMap;
import java.util.HashMap;
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
 * 
 * <p>Rules are applied sequentially in the order provided. The output of each rule
 * becomes the input for the next rule, which means rule order can significantly
 * affect the final outcome.
 * 
 */
public class SimulatorEngine {

  private final List<Rule> rules;

  public SimulatorEngine(List<Rule> rules) {
    this.rules = rules;
  }

  /**
   * This method performs the simulation based on the given request.
   * @param request the simulation request containing initial patients and drugs
   * @return SimulationResponse containing the final state of patients after applying all rules
   */
  public SimulationResponse simulate(SimulationRequest request) {

    Map<HealthState, Integer> patientsByState = new EnumMap<>(request.initialPatients());
    Set<Drug> drugs = request.drugs();

    for (Rule rule : rules) {
      patientsByState = rule.apply(patientsByState, drugs);
    }

    return new SimulationResponse(patientsByState);
  }
}
