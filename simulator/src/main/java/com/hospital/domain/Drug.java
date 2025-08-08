package com.hospital.domain;

public enum Drug {

  ASPIRIN("As"),
  PARACETAMOL("P"),
  INSULIN("I"),
  ANTIBIOTIC("An");

  private final String code;

  Drug(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static Drug fromCode(String code) {
    for (Drug drug : values()) {
      if (drug.code.equalsIgnoreCase(code))
        return drug;
    }
    throw new IllegalArgumentException("Unknown drug: " + code);
  }
}
