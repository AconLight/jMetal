package org.uma.jmetal.example.multiobjective.nsgaii;

import model.Observation;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.lab.visualization.plot.PlotFront;
import org.uma.jmetal.lab.visualization.plot.impl.PlotSmile;
import org.uma.jmetal.operator.crossover.impl.IntegerSBXCrossover;
import org.uma.jmetal.operator.crossover.impl.MyCrossover;
import org.uma.jmetal.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal.operator.mutation.impl.MyMutation;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.BreastCancerDiagnosisProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.solution.sequencesolution.impl.IntSequenceSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.front.impl.ArrayFront;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to configure and run the NSGA-II algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIMemeticRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException Invoking command:
   *                               java org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    ArrayList<Observation> observations = loadData();

    Problem problem;
    Algorithm<List<MemeticIntegerSolution>> algorithm;
    MyCrossover crossover;
    MyMutation mutation;
    SelectionOperator<List<MemeticIntegerSolution>, MemeticIntegerSolution> selection;
    String referenceParetoFront = "";

    problem = new BreastCancerDiagnosisProblem(observations);

    double crossoverProbability = 0.9;
    crossover = new MyCrossover(crossoverProbability);

    double mutationProbability = 0.6;
    int [] indexes = new int[100];
    for (int i = 0; i < 100; i++) {
      indexes[i] = i;
    }
    mutation = new MyMutation(mutationProbability, indexes);

    selection = new BinaryTournamentSelection<>(
            new RankingAndCrowdingDistanceComparator<>());

    int populationSize = 100 ;
    algorithm = new NSGAIIBuilder(problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(1000)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<MemeticIntegerSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    //JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

//    printFinalSolutionSet(population);
//    if (!referenceParetoFront.equals("")) {
//      printQualityIndicators(population, referenceParetoFront);
//    }
    System.out.println("elo");
    System.out.println("" + population.get(0).getVariables().toString());




    PlotFront plot = new PlotSmile(new ArrayFront(population).getMatrix()) ;
    plot.plot();
  }

  public static ArrayList<Observation> loadData() {
    ArrayList<Observation> observations = new ArrayList<>();
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(
              "data/bcw.data"));
      String line = reader.readLine();
      int i = 0;
      while (line != null && i < 100) {
        if (!line.contains("?")) {
          observations.add(new Observation(line));
          i++;
        }
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return observations;
  }
}
