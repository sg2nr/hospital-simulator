package com.hospital.client;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.hospital.client.model.SimulationRequest;
import com.hospital.client.model.SimulationResponse;
import com.hospital.domain.Drug;
import com.hospital.domain.HealthState;
import com.hospital.rule.impl.AntibioticRule;
import com.hospital.rule.impl.AspirinRule;
import com.hospital.rule.impl.FlyingSpaghettiMonsterRule;
import com.hospital.rule.impl.InsulinRule;
import com.hospital.rule.impl.ParacetamolRule;
import com.hospital.service.SimulatorEngine;

/**
 * Command-line interface for the hospital patient simulator.
 * 
 * <p>This class provides a command-line wrapper around the {@link SimulatorEngine}
 * to simulate patient health state changes based on initial conditions and applied drugs.
 * It handles argument parsing, delegates simulation to the engine, and formats the results
 * for console output.
 * 
 * <h2>Usage</h2>
 * <pre>
 * java -jar hospital-simulator.jar &lt;patients&gt; [&lt;drugs&gt;]
 * </pre>
 * 
 * <h3>Arguments</h3>
 * <ul>
 * <li><strong>patients</strong> (required): Comma-separated list of patient health state codes
 *     (e.g., "F,H,D,T,X" for Fever, Healthy, Diabetes, Tuberculosis, Dead)</li>
 * <li><strong>drugs</strong> (optional): Comma-separated list of drug codes to administer
 *     (e.g., "As,An,I,P" for Aspirin, Antibiotic, Insulin, Paracetamol)</li>
 * </ul>
 * 
 * <h3>Examples</h3>
 * <pre>
 * // Simulate 2 fever patients and 1 healthy patient with no drugs
 * java -jar hospital-simulator.jar "F,F,H"
 * 
 * // Simulate patients with aspirin and antibiotic treatment
 * java -jar hospital-simulator.jar "F,D,T" "As,An"
 * </pre>
 * 
 * <h2>Output Format</h2>
 * <p>Results are formatted as: {@code F:x,H:x,D:x,T:x,X:x} where:
 * <ul>
 * <li>F = Fever patients</li>
 * <li>H = Healthy patients</li>
 * <li>D = Diabetes patients</li>
 * <li>T = Tuberculosis patients</li>
 * <li>X = Dead patients</li>
 * </ul>
 * 
 * @version 1.0
 * @since 1.0
 */
public class CommandLineSimulator {

  private static final String NO_ARGUMENTS_ERROR_MESSAGE = """
      No arguments provided for simulation.
      Usage: java -jar hospital-simulator.jar <patients> [<drugs>]""";

  private final SimulatorEngine simulatorEngine;

  public CommandLineSimulator(SimulatorEngine simulatorEngine) {
    this.simulatorEngine = simulatorEngine;
  }

  public static void main(String[] args) {
    CommandLineSimulator cli = new CommandLineSimulator(new SimulatorEngine(List.of(
        new AspirinRule(),
        new AntibioticRule(),
        new InsulinRule(),
        new ParacetamolRule(),
        new FlyingSpaghettiMonsterRule())));

    try {
      String result = cli.run(args);
      System.out.println(result);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  public String run(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException(NO_ARGUMENTS_ERROR_MESSAGE);
    }

    SimulationRequest request = createRequest(args);
    SimulationResponse response = simulatorEngine.simulate(request);

    return formatResponse(response);
  }

  private SimulationRequest createRequest(String[] args) {
    // Parse patients (first argument)
    Map<HealthState, Integer> patientsByState = parsePatients(args[0]);

    // Parse drugs (if provided)
    Set<Drug> drugs = args.length > 1 ? parseDrugs(args[1]) : Set.of();

    return new SimulationRequest(patientsByState, drugs);
  }

  private Map<HealthState, Integer> parsePatients(String input) {
    Map<HealthState, Integer> patientsByState = new EnumMap<>(HealthState.class);
    for (String token : input.split(",")) {
      HealthState state = HealthState.fromCode(token.trim());
      patientsByState.merge(state, 1, Integer::sum);
    }
    return patientsByState;
  }

  private Set<Drug> parseDrugs(String input) {
    return Arrays.stream(input.split(","))
        .map(String::trim)
        .map(Drug::fromCode)
        .collect(Collectors.toSet());
  }

  private String formatResponse(SimulationResponse response) {
    Map<HealthState, Integer> counts = response.patientsByState();
    return String.format("F:%d,H:%d,D:%d,T:%d,X:%d",
        counts.getOrDefault(HealthState.FEVER, 0),
        counts.getOrDefault(HealthState.HEALTHY, 0),
        counts.getOrDefault(HealthState.DIABETES, 0),
        counts.getOrDefault(HealthState.TUBERCULOSIS, 0),
        counts.getOrDefault(HealthState.DEAD, 0));
  }
}
