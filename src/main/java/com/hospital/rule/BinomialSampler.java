package com.hospital.rule;

/**
 * Interface for sampling from a binomial distribution.
 * This interface abstracts the sampling logic, allowing for different implementations.
 */
public interface BinomialSampler {

  int sample(int trials, double probability);
}
