package org.uma.jmetal.operator.mutation.impl;

import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.solution.sequencesolution.impl.CharSequenceSolution;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * This class implements a swap mutation. The solution type of the solution must be Permutation.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class MyMutation implements MutationOperator<MemeticIntegerSolution> {
  private double mutationProbability;
  private final int[] alphabet;

  /** Constructor */
  public MyMutation(double mutationProbability, int[] alphabet) {
    Check.probabilityIsValid(mutationProbability);
    this.mutationProbability = mutationProbability;
    this.alphabet = alphabet;
  }

  /* Execute() method */
  @Override
  public MemeticIntegerSolution execute(MemeticIntegerSolution solution) {
    Check.isNotNull(solution);

    doMutation(solution);
    return solution;
  }

  /** Performs the operation */
  public void doMutation(MemeticIntegerSolution solution) {
    boolean doMemes = true;

    if (doMemes) {

      // memetic remove
      for (int i = 0; i < solution.memesRemove.getVariables().size(); i++) {
        if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
          for (int j = 0; j < solution.getVariables().size(); j++) {
            if (solution.getVariable(j) == solution.memesRemove.getVariable(i)) {
              solution.remOneVar(j);
              solution.memesRemove.setVariable(i, null);
              solution.memesRemove.memesValues.set(i, null);
            }
          }
        }
      }

      // memetic add
      for (int i = 0; i < solution.memesAdd.getVariables().size(); i++) {
        if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
          if (!solution.getVariables().contains(solution.memesAdd.getVariable(i))) {
            solution.addOneVar(solution.memesAdd.getVariable(i));
          }
        }
      }

    } else {

      //     normal add
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
        int newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        while (solution.getVariables().contains(newValue)) {
          newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        }
        solution.addOneVar(newValue);
      }

      //     normal remove
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability && solution.getVariables().size() > 0) {
        solution.remOneVar(JMetalRandom.getInstance().nextInt(0, solution.getVariables().size() - 1));
      }

    }


    // normal change
    int size = solution.getVariables().size();
    for (int i = 0; i < size; i++) {
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability/4) {
        int positionToChange = JMetalRandom.getInstance().nextInt(0, size - 1);
        int newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        while (solution.getVariables().contains(newValue)) {
          newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        }
        solution.setVariable(positionToChange, newValue);
      }
    }
  }

  @Override
  public double getMutationProbability() {
    return mutationProbability;
  }
}

/*
minAcc: 0.72139305
avgAcc: 0.80872774
maxAcc: 0.835

minCO: 0.22222222
avgCO: 0.83194447
maxCO: 1.0

minFO: 0.0
avgFO: 0.036737576
maxFO: 0.16969697


____


minAcc: 0.7248157
avgAcc: 0.781083
maxAcc: 0.8225

minCO: 0.2361111
avgCO: 0.6402778
maxCO: 1.0

minFO: 0.0
avgFO: 0.07810237
maxFO: 0.16516517


 */
