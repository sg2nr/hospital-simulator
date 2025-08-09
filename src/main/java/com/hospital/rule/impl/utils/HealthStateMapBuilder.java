package com.hospital.rule.impl.utils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.hospital.domain.HealthState;

/**
 * Builder class for creating a map of HealthState and their corresponding
 * counts.
 * It initializes all health states to zero and allows transitions between
 * states.
 */
public class HealthStateMapBuilder {

  private final Map<HealthState, Integer> map;

  private HealthStateMapBuilder(Map<HealthState, Integer> source) {
    this.map = Arrays.stream(HealthState.values())
        .collect(Collectors.toMap(h -> h, h -> 0));
    this.map.putAll(source);
  }

  public static HealthStateMapBuilder from(Map<HealthState, Integer> source) {
    return new HealthStateMapBuilder(source);
  }

  /**
   * Transitions a specified count of patients from one health state to another.
   * If the count is greater than zero, it decreases the count of the fromState
   * and increases the count of the toState.
   * 
   * @param fromState the health state to transition from
   * @param toState   the health state to transition to
   * @param count     the number of patients to transition
   * @return
   */
  public HealthStateMapBuilder transition(HealthState fromState, HealthState toState, int count) {
    if (count > 0) {
      map.merge(fromState, -count, Integer::sum);
      map.merge(toState, count, Integer::sum);
    }
    return this;
  }

  /**
   * Kills all patients by transitioning all health states to DEAD.
   * 
   * @return HealthStateMapBuilder instance with all patients marked as DEAD.
   */
  public HealthStateMapBuilder killAll() {
    int total = map.values().stream().mapToInt(Integer::intValue).sum();
    map.replaceAll((state, count) -> state == HealthState.DEAD ? total : 0);
    return this;
  }

  public Map<HealthState, Integer> build() {
    return new EnumMap<>(map);
  }
}
