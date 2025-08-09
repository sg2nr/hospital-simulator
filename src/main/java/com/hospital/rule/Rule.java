package com.hospital.rule;

import java.util.Set;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

public interface Rule {

  HealthState apply(HealthState currenHealthState, Set<Drug> drugs);
  
}
