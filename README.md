# Hospital Simulator

A Java application that simulates patient health state transitions in a hospital environment based on initial patient conditions and administered drugs.

## Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Usage Examples](#usage-examples)
- [Design Decisions & Assumptions](#design-decisions--assumptions)
- [Development](#development)

## Overview

The Hospital Simulator processes patients with various health conditions and simulates how their states change when different drugs are administered. The application follows a rule-based system where each drug has specific effects on patient health states.

### Health States
- **F** - Fever
- **H** - Healthy  
- **D** - Diabetes
- **T** - Tuberculosis
- **X** - Dead

### Available Drugs
- **As** - Aspirin
- **An** - Antibiotic
- **I** - Insulin
- **P** - Paracetamol

## Prerequisites

- **Java 21** or higher
- **Maven 3.6.3** or higher

### Verify Prerequisites
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

## Building the Project

### Full Build with Tests
```bash
mvn clean package
```

### Quick Build (Skip Tests)
```bash
mvn clean package -DskipTests
```

### Build Outputs
- `target/hospital-simulator.jar` - Executable JAR

## Running the Application

### Command Syntax
```bash
java -jar target/hospital-simulator.jar <patients> [<drugs>]
```

### Parameters
- **`<patients>`** (required): Comma-separated list of patient health state codes
- **`<drugs>`** (optional): Comma-separated list of drug codes to administer

### Output Format
Results are displayed as: `F:x,H:x,D:x,T:x,X:x` where:
- **F** = Number of Fever patients
- **H** = Number of Healthy patients  
- **D** = Number of Diabetes patients
- **T** = Number of Tuberculosis patients
- **X** = Number of Dead patients

## Usage Examples

### Basic Examples
```bash
# Two fever patients, one healthy - no treatment
java -jar target/hospital-simulator.jar F,F,H
# Output: F:2,H:1,D:0,T:0,X:0

# Single diabetes patient - no treatment
java -jar target/hospital-simulator.jar D
# Output: F:0,H:0,D:0,T:0,X:1
```

### With Drug Treatment
```bash
# Fever patients treated with Aspirin
java -jar target/hospital-simulator.jar F,F As
# Output: F:0,H:2,D:0,T:0,X:0

# Mixed conditions with multiple drugs
java -jar target/hospital-simulator.jar F,D,T As,An,I
# Output: F:2,H:0,D:1,T:0,X:0

# Complex scenario
java -jar target/hospital-simulator.jar F,F,H,D,D,T As,An,P,I
# Output: F:0,H:0,D:0,T:0,X:6
```

### Error Cases
```bash
# No arguments
java -jar target/hospital-simulator.jar
# Output: 
# Error: No arguments provided for simulation.
# Usage: java -jar hospital-simulator.jar <patients> [<drugs>]

# Invalid health state
java -jar target/hospital-simulator.jar Z
# Output: Error: Invalid Health State: Z

# Unknown drug
java -jar target/hospital-simulator.jar F,F O
# Output: Error: Unknown drug: O
```

## Design Decisions & Assumptions

### Assumptions

#### Single Health State per Patient
Each patient can only be in one health state at a time. States are mutually exclusive (`FEVER`, `HEALTHY`, `DIABETES`, `TUBERCULOSIS`, `DEAD`).

#### Simultaneous Drug Application
All drugs are applied simultaneously. For example, if a patient has `FEVER` and the input includes both `ASPIRIN` and `PARACETAMOL`, the combined effect is resolved in a single simulation step.

#### Order Independence
The order of drugs in the input does not affect the outcome (`ASPIRIN,PARACETAMOL` == `PARACETAMOL,ASPIRIN`).

#### Rule Conflicts
When multiple rules may apply, they are processed in a **deterministic order** (the engine applies rules in the order in which they are registered).

### Core Decisions

#### Stateless Engine
The simulation engine does not retain state between runs. Each simulation is self-contained and driven solely by its input.

#### Probability-based Rules
Some rules may rely on randomness (e.g., `FlyingSpaghettiMonsterRule` with a resurrection probability). To ensure deterministic testing, randomness is abstracted via the `BinomialSampler` interface, which can be mocked or replaced with a deterministic implementation.

#### Extensibility
New health states or drugs can be introduced without modifying the simulation engine itself. Only new `Rule` implementations are required.

### Architectural Decisions

#### Separation of Concerns

The **business logic** is isolated from the **client layer** (currently a CLI). The `SimulatorEngine` can be integrated with other front-ends:

* **Current**: Command-line interface (`CommandLineSimulator`)
* **Future**: REST API controller, web UI, batch processor, etc.

Any client only needs to send a `SimulationRequest` and handle a `SimulationResponse`.

```
+--------+       +----------------+       +-------------+       +-----------+
| Client | ----> | SimulatorEngine| ----> |   Rules     | ----> | Response  |
+--------+       +----------------+       +-------------+       +-----------+
                      |                         ^
                      | applies each Rule       |
                      +-------------------------+
```

#### Immutability of Core Domain Objects
The `SimulationRequest` is implemented as a Java `record`, which enforces immutability by design.
Validation logic is applied within the constructor to guarantee that only valid requests can be instantiated. This prevents propagation of invalid states across the system and shifts error detection as early as possible, closer to the API boundary.

#### Rule-based Engine

At the core of the design is the `Rule` interface:

```java
public interface Rule {
    Map<HealthState, Integer> apply(Map<HealthState, Integer> patientsByState, Set<Drug> drugs);
}
```

Each rule encapsulates a **single domain effect** (e.g., *Antibiotic cures Tuberculosis*, *Paracetamol + Aspirin kills all patients*).
This makes the system easy to extend: new behaviors are added by implementing a `Rule` and registering it with the engine.

#### Composition over Inheritance

Rules are injected into the engine as a `List<Rule>`. The engine is agnostic to their concrete implementations; it simply executes them in sequence.
This design follows the **Open/Closed Principle** and favors composition over inheritance.

#### Strategy Pattern

Rules are interchangeable strategies. The engine applies them consistently, without knowing their internal logic.

#### Builder Pattern

`HealthStateMapBuilder` is used to create fluent and readable state transitions, reducing boilerplate.

#### Dependency Injection

Rules can depend on external services or utilities. For instance, `FlyingSpaghettiMonsterRule` relies on `Apache Commons Math` via an injectable `BinomialSampler`.

#### Testability

* `Rule` is designed as a pure function: `(patientsByState, drugs) -> newPatientsByState`, making unit testing straightforward.
* Probabilistic behavior is abstracted, enabling deterministic tests with mocks or stubs.
* The engine can be tested end-to-end using sample inputs and expected outputs.

### Error Handling

* **Fail Fast**: Invalid inputs cause immediate termination with a clear error message.
* **Validation**: Inputs are validated upfront through `RuleValidationUtils`, ensuring consistency and avoiding duplication.
* **Precondition Checks**: Each rule verifies its inputs before applying logic.
* **Logging**: Errors are written to `stderr`.

### Future Extensions

The architecture supports:

* New **rules**, deterministic or probabilistic;
* New **clients** (REST API, GUI, batch processor).

### Technical Stack

#### Java 21

This project leverages modern Java features such as records, which promote immutability and enable safer validation. Java 21 also provides strong performance, long-term support, and a mature ecosystem, ensuring a stable and future-proof foundation.

#### Maven

Build automation and dependency management are handled with Maven.

#### Testing

* **JUnit 5**: Provides the testing framework, with parameterized tests used to validate rules and scenarios across multiple inputs.
* **Mockito**: Enables mocking of probabilistic behaviors and isolating dependencies, ensuring deterministic unit tests.

#### SLF4J

Provides a simple and standardized logging API. The slf4j-simple implementation is used to log simulation events, including executed rules and intermediate patient states, helping with debugging

#### Apache Commons Math

Provides reliable mathematical and statistical utilities (e.g., probability distributions), reducing the need for custom implementations and ensuring correctness in simulation logic.

## Development

### Adding New Drugs
1. Create a new rule class implementing `Rule` interface
2. Add the rule to `CommandLineSimulator.main()` method
3. Add corresponding tests
4. Update documentation

### Adding New Health States  
1. Add enum value to `HealthState`
2. Update `formatResponse()` method in `CommandLineSimulator`
3. Add any necessary business rules
4. Update tests and documentation

---

## Contact

For any questions about this implementation, please feel free to reach out.

**Built with ❤️ using Java 21 and Maven**