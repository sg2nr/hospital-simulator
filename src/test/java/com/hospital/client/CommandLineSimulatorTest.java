package com.hospital.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.hospital.rule.BinomialSampler;
import com.hospital.rule.impl.AntibioticRule;
import com.hospital.rule.impl.AspirinRule;
import com.hospital.rule.impl.FlyingSpaghettiMonsterRule;
import com.hospital.rule.impl.InsulinRule;
import com.hospital.rule.impl.ParacetamolRule;
import com.hospital.service.SimulatorEngine;

class CommandLineSimulatorTest {

  @ParameterizedTest
  @MethodSource("provideSimulationArgumentsAndResults")
  void testCommandLineSimulation(String[] args, String expectedOutput) {
    // Given
    BinomialSampler binomialSampler = Mockito.mock(BinomialSampler.class);
    Mockito.when(binomialSampler.sample(anyInt(), anyDouble())).thenReturn(0);

    SimulatorEngine simulatorEngine = new SimulatorEngine(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule()));

    CommandLineSimulator simulator = new CommandLineSimulator(simulatorEngine);

    // When
    String result = simulator.run(args);

    // Then
    assertEquals(expectedOutput, result);
  }

  static Stream<Arguments> provideSimulationArgumentsAndResults() {
    return Stream.of(
        // Example 1: Two patients with diabetes, no drugs
        Arguments.of(new String[] { "D,D" }, "F:0,H:0,D:0,T:0,X:2"),
        // Example 2: One patient with fever, given paracetamol
        Arguments.of(new String[] { "F", "P" }, "F:0,H:1,D:0,T:0,X:0"),
        // Example 3: One patient with tuberculosis, given antibiotic
        Arguments.of(new String[] { "T,F,D", "An,I" }, "F:2,H:0,D:1,T:0,X:0"),
        // More complex case with multiple states and drugs
        Arguments.of(new String[] { "F,F,H,D,T", "P,An" }, "F:0,H:4,D:0,T:0,X:1"),
        Arguments.of(new String[] { "F,D,T,H,X", "P,An,I" }, "F:0,H:3,D:1,T:0,X:1"),
        Arguments.of(new String[] { "H,H,H,H,H", "As,P,An,I" }, "F:0,H:0,D:0,T:0,X:5"),
        Arguments.of(new String[] { "H,H,H,H,H,F,D,D,D,T,T", "As,P,An,I" }, "F:0,H:0,D:0,T:0,X:11"));
  }

  @Test
  void testCommandLineSimulatorWithNoArgumentsShouldThrowException() {
    // Given
    SimulatorEngine simulatorEngine = new SimulatorEngine(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule()));
    CommandLineSimulator cli = new CommandLineSimulator(simulatorEngine);

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cli.run(new String[] {}));

    // Then
    assertEquals("No arguments provided for simulation.\nUsage: java -jar hospital-simulator.jar <patients> [<drugs>]",
        exception.getMessage());
  }

  @Test
  void testCommandLineSimulatorWithUnknownDrugShouldThrowIllegalArgumentException() {
    // Given
    SimulatorEngine simulatorEngine = new SimulatorEngine(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule()));
    CommandLineSimulator cli = new CommandLineSimulator(simulatorEngine);

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> cli.run(new String[] { "F", "new_drug" }));

    // Then
    assertEquals("Unknown drug: new_drug", exception.getMessage());
  }

  @Test
  void testCommandLineSimulatorWithInvalidHealthStateShouldThrowIllegalArgumentException() {
    // Given
    SimulatorEngine simulatorEngine = new SimulatorEngine(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule()));
    CommandLineSimulator cli = new CommandLineSimulator(simulatorEngine);

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> cli.run(new String[] { "F, New_HealthState", "P" }));

    // Then
    assertEquals("Invalid Health State: New_HealthState", exception.getMessage());
  }
}
