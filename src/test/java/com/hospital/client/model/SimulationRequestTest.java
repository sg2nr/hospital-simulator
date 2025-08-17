package com.hospital.client.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;

class SimulationRequestTest {

  @Test
  void testCreateSimulationRequestWithValidPatientsAndDrugsShouldReturnASimulationRequest() {
    // Given
    Map<HealthState, Integer> initialPatients = Map.of(HealthState.HEALTHY, 10);
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC, Drug.ASPIRIN);

    // When
    SimulationRequest request = new SimulationRequest(initialPatients, drugs);

    // Then
    assertEquals(drugs, request.drugs());
    assertEquals(10, request.initialPatients().get(HealthState.HEALTHY));
  }

  @Test
  void testCreateSimulationRequestWithNullInitialPatientsShouldThrowException() {
    // Given
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new SimulationRequest(null, drugs));

    // Then
    assertEquals("Invalid SimulationRequest: Initial patients map cannot be null.", exception.getMessage());
  }

  @Test
  void testCreateSimulationRequestWithNegativePatientCountShouldThrowException() {
    // Given
    Map<HealthState, Integer> initialPatients = Map.of(HealthState.TUBERCULOSIS, -1);
    Set<Drug> drugs = Set.of(Drug.ANTIBIOTIC);

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new SimulationRequest(initialPatients, drugs));

    // Then
    assertEquals("Invalid SimulationRequest: Patient counts cannot be negative.", exception.getMessage());
  }

  @Test
  void testCreateSimulationRequestWithNullDrugsShouldInitializeToEmptySet() {
    // Given
    Map<HealthState, Integer> initialPatients = Map.of(HealthState.HEALTHY, 10);

    // When
    SimulationRequest request = new SimulationRequest(initialPatients, null);

    // Then
    assertEquals(Set.of(), request.drugs());
    assertEquals(10, request.initialPatients().get(HealthState.HEALTHY));
  }
}
