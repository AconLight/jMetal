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

    // normal add
    if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
      int newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
      while (solution.getVariables().contains(newValue)) {
        newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
      }
      solution.addOneVar(newValue);
    }


    // normal change
    int size = solution.getVariables().size();
    for (int i = 0; i < size; i++) {
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
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
