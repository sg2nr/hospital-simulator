package com.hospital.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.hospital.rule.BinomialSampler;
import com.hospital.rule.Rule;
import com.hospital.rule.impl.AntibioticRule;
import com.hospital.rule.impl.AspirinRule;
import com.hospital.rule.impl.FlyingSpaghettiMonsterRule;
import com.hospital.rule.impl.InsulinRule;
import com.hospital.rule.impl.ParacetamolRule;

class CommandLineSimulatorTest {

  @ParameterizedTest
  @MethodSource("provideSimulationArgumentsAndRresults")
  void testCommandLineSimulation(String[] args, String expectedOutput) {
    // Given
    BinomialSampler binomialSampler = Mockito.mock(BinomialSampler.class);
    Mockito.when(binomialSampler.sample(anyInt(), anyDouble())).thenReturn(0);

    List<Rule> rules = List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule(binomialSampler));

    CommandLineSimulator simulator = new CommandLineSimulator(rules);

    // When
    String result = simulator.run(args);

    // Then
    assertEquals(expectedOutput, result);
  }

  static Stream<Arguments> provideSimulationArgumentsAndRresults() {
    return Stream.of(
        Arguments.of(new String[] { "D,D" }, "F:0,H:0,D:0,T:0,X:2"),
        Arguments.of(new String[] { "F", "P" }, "F:0,H:1,D:0,T:0,X:0"),
        Arguments.of(new String[] { "T,F,D", "An,I" }, "F:2,H:0,D:1,T:0,X:0"));
  }

  @Test
  void testCommandLineSimulatorWithNoArgumentsShouldThrowException() {    
    // Given
    CommandLineSimulator cli = new CommandLineSimulator(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule()));

    // When
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cli.run(new String[] {}));

    // Then
    assertEquals("No arguments provided for simulation.\nUsage: java -jar hospital-simulator.jar <patients> [<drugs>]",
        exception.getMessage());
  }
}
