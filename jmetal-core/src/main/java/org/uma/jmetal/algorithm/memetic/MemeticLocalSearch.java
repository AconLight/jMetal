package org.uma.jmetal.algorithm.memetic;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;

import java.util.List;

public interface MemeticLocalSearch<S> {
    public List<S> localSearch(List<S> solutions);
}
