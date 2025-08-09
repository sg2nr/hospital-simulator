package com.hospital.rule;

import java.util.Map;
import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

/**
 * Rule interface defines the contract for applying health rules to patients based on their current health states and available drugs.
 */
public interface Rule {

  /**
   * Applies the rule to the patients givent their current health states and the drugs available.
   * 
   * @param patientsByState a map of health states to the number of patients in that state
   * @param drugs a set of drugs that may affect the health states
   * @return a map of health states to the updated number of patients in that state after applying the rule
   */
  Map<HealthState, Integer> apply(Map<HealthState, Integer> patientsByState, Set<Drug> drugs);  
}
