package com.hospital.rule.impl;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;

import com.hospital.rule.BinomialSampler;

public class ApacheBinomialSampler implements BinomialSampler {

  @Override
  public int sample(int trials, double probability) {
    JDKRandomGenerator rng = new JDKRandomGenerator();
    BinomialDistribution binomialDistribution = new BinomialDistribution(rng, trials, probability);
    return binomialDistribution.sample();
  }
}
