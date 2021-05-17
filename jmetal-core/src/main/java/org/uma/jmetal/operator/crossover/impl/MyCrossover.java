package org.uma.jmetal.operator.crossover.impl;

import consts.Consts;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.*;

/**
 * This class allows to apply a SBX crossover operator using two parent solutions (Integer encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class MyCrossover implements CrossoverOperator<MemeticIntegerSolution> {

  private double crossoverProbability  ;

  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public MyCrossover() {
	  this(Consts.crossProb, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public MyCrossover(double crossoverProbability, RandomGenerator<Double> randomGenerator) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    }

    this.crossoverProbability = crossoverProbability ;
    this.randomGenerator = randomGenerator ;
  }

  /* Getters */
  @Override
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  /* Setters */
  public void setCrossoverProbability(double crossoverProbability) {
    this.crossoverProbability = crossoverProbability;
  }

  /** Execute() method */
  @Override
  public List<MemeticIntegerSolution> execute(List<MemeticIntegerSolution> solutions) {
    if (null == solutions) {
      throw new JMetalException("Null parameter") ;
    } else if (solutions.size() != 2) {
      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
    }

    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
  }

  /** doCrossover method */
  public List<MemeticIntegerSolution> doCrossover(
          double probability, MemeticIntegerSolution parent1, MemeticIntegerSolution parent2) {
    List<MemeticIntegerSolution> offspring = new ArrayList<MemeticIntegerSolution>(2);

    offspring.add((MemeticIntegerSolution) parent1.copy()) ;
    offspring.add((MemeticIntegerSolution) parent2.copy()) ;
    HashMap<Integer, ArrayList<Double>> varVals = new HashMap();
    for (int i = 0; i < parent1.getNumberOfVariables(); i++) {
      varVals.put(parent1.getVariable(i), parent1.variablesFitnessValues.get(i));
    }
    for (int i = 0; i < parent2.getNumberOfVariables(); i++) {
      varVals.put(parent2.getVariable(i), parent2.variablesFitnessValues.get(i));
    }

    if (randomGenerator.getRandomValue() <= probability) {
      ArrayList<Integer> bothVars = new ArrayList<>();
      bothVars.addAll(parent1.getVariables());
      bothVars.addAll(parent2.getVariables());
      ArrayList<Integer> genes = new ArrayList<>(new HashSet<>(bothVars));
      Collections.shuffle(genes);
      offspring.get(0).setVariables(genes.subList(0, genes.size()/2));
      offspring.get(1).setVariables(genes.subList(genes.size()/2, genes.size()));
    }
    offspring.get(0).variablesFitnessValues.clear();
    for (int i = 0; i < offspring.get(0).getNumberOfVariables(); i++) {
      offspring.get(0).variablesFitnessValues.add(varVals.get(offspring.get(0).getVariable(i)));
    }

    offspring.get(1).variablesFitnessValues.clear();
    for (int i = 0; i < offspring.get(1).getNumberOfVariables(); i++) {
      offspring.get(1).variablesFitnessValues.add(varVals.get(offspring.get(1).getVariable(i)));
    }

    return offspring;
  }

  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  public int getNumberOfGeneratedChildren() {
    return 2 ;
  }
}
