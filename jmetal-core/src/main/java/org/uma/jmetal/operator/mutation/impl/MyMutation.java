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
    int sequenceLength = solution.getNumberOfVariables();

    for (int i = 0; i < sequenceLength; i++) {
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
        int positionToChange = JMetalRandom.getInstance().nextInt(0, sequenceLength - 1);
        int newValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        //while (newValue)
        solution.setVariable(positionToChange, newValue);
      }
    }
  }

  @Override
  public double getMutationProbability() {
    return mutationProbability;
  }
}
