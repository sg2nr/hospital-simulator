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
