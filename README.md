# Hospital Simulator

A Java application that simulates patient health state transitions in a hospital environment based on initial patient conditions and administered drugs.

## 📋 Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Usage Examples](#usage-examples)
- [Design Decisions & Assumptions](#design-decisions--assumptions)

## 🎯 Overview

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

## 🔧 Prerequisites

- **Java 21** or higher
- **Maven 3.8.0** or higher

### Verify Prerequisites
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

## 🚀 Quick Start

```bash
mvn clean package
java -jar target/hospital-simulator.jar F,H,D As,An
```

Expected output: `F:0,H:2,D:0,T:0,X:1`

## 🏗️ Building the Project

### Full Build with Tests
```bash
mvn clean verify
```

### Quick Build (Skip Tests)
```bash
mvn clean package -DskipTests
```

### Build Outputs
- `target/hospital-simulator.jar` - Executable JAR (shaded with dependencies)

## 🎮 Running the Application

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

## 📚 Usage Examples

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

## 🧠 Design Decisions & Assumptions
# TODO

## 📧 Contact

For any questions about this implementation, please feel free to reach out.

**Built with ❤️ using Java 21 and Maven**