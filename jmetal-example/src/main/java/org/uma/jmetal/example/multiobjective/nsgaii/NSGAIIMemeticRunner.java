package org.uma.jmetal.example.multiobjective.nsgaii;

import consts.Consts;
import model.Observation;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.impl.MyCrossover;
import org.uma.jmetal.operator.mutation.impl.MyMutation;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.BreastCancerDiagnosisProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    int numb = 40;


    ArrayList<Observation> observations = loadData();
    int outliers = 0;
    for (Observation o : observations) {
      if (o.diagnosis == 4) {
        outliers++;
      }
    }
    System.out.println("outliers: " + outliers);
    float sumAcc = 0;
    float minAcc = 1;
    float maxAcc = 0;

    float sumCO = 0;
    float minCO = 1;
    float maxCO = 0;

    float sumFO = 0;
    float minFO = 1;
    float maxFO = 0;
    for (int i = 0; i < numb; i++) {
      ArrayList<Float> result = run(observations);
      float acc = (result.get(0) + result.get(1)) / (result.get(0) + result.get(1) + result.get(2) + result.get(3));
      float co = (result.get(0)) / (result.get(2) + result.get(0));
      float fo = (result.get(2)) / (result.get(2) + result.get(1));

      if (minAcc > acc) {
        minAcc = acc;
      }
      if (maxAcc < acc) {
        maxAcc = acc;
      }
      sumAcc += acc;

      if (minCO > co) {
        minCO = co;
      }
      if (maxCO < co) {
        maxCO = co;
      }
      sumCO += co;

      if (minFO > fo) {
        minFO = fo;
      }
      if (maxFO < fo) {
        maxFO = fo;
      }
      sumFO += fo;

      if ((i+1)%(Math.max(1, numb/10)) == 0) {
        System.out.println((int)(((i+1f)/numb*100)) + "% done");
      }

    }
    sumAcc = sumAcc/numb;
    sumCO = sumCO/numb;
    sumFO = sumFO/numb;

    System.out.println("minAcc: " + minAcc);
    System.out.println("avgAcc: " + sumAcc);
    System.out.println("maxAcc: " + maxAcc);
    System.out.println();

    System.out.println("minCO: " + minCO);
    System.out.println("avgCO: " + sumCO);
    System.out.println("maxCO: " + maxCO);
    System.out.println();

    System.out.println("minFO: " + minFO);
    System.out.println("avgFO: " + sumFO);
    System.out.println("maxFO: " + maxFO);
  }
  public static ArrayList<Float> run(ArrayList<Observation> observations2) throws JMetalException, FileNotFoundException {

    ArrayList<Observation> observations = new ArrayList<>();
    for (Observation o: observations2) {
      observations.add(o);
    }
    Problem problem;
    Algorithm<List<MemeticIntegerSolution>> algorithm;
    MyCrossover crossover;
    MyMutation mutation;
    SelectionOperator<List<MemeticIntegerSolution>, MemeticIntegerSolution> selection;
    String referenceParetoFront = "";

    problem = new BreastCancerDiagnosisProblem(observations);

    double crossoverProbability = 0.6;
    crossover = new MyCrossover(crossoverProbability);

    double mutationProbability = 0.2;
    int [] indexes = new int[100];
    for (int i = 0; i < 100; i++) {
      indexes[i] = i;
    }
    mutation = new MyMutation(mutationProbability, indexes);

    selection = new BinaryTournamentSelection<>(
            new RankingAndCrowdingDistanceComparator<>());

    int populationSize = 8;
    algorithm = new NSGAIIBuilder(problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(300)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<MemeticIntegerSolution> population = algorithm.getResult();




    int tp = 0;
    int tn = 0;
    int fp = 0;
    int fn = 0;
    int i = 0;
    ArrayList<Observation> toRem = new ArrayList<>();
    for (Integer val: population.get(0).getVariables()) {
      if (observations.get(val).diagnosis == 4) {
        tp++;
      }
      else {
        fp++;
      }
      toRem.add(observations.get(val));
      i++;
    }
    observations.removeAll(toRem);

    for (Observation o: observations) {
      if (o.diagnosis == 2) {
        tn++;
      }
      else {
        fn++;
      }
      i++;
    }



//    PlotFront plot = new PlotSmile(new ArrayFront(population).getMatrix()) ;
//    plot.plot();
    ArrayList<Float> res = new ArrayList<>();
    res.add(tp+0f);
    res.add(tn+0f);
    res.add(fp+0f);
    res.add(fn+0f);


    return res;
  }

  public static ArrayList<Observation> loadData() {
    ArrayList<Observation> observations = new ArrayList<>();
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(
              "data/bcw.data"));
      String line = reader.readLine();
      int i = 0;
      while (line != null) {
        if (!line.contains("?")) {
          observations.add(new Observation(line));
          i++;
        } else {
        }
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    ArrayList<Observation> normal = new ArrayList<>();
    ArrayList<Observation> outliers = new ArrayList<>();
    for (Observation o: observations) {
      if (o.diagnosis == 4) {
        outliers.add(o);
      } else {
        normal.add(o);
      }
    }
    Collections.shuffle(normal, new Random(2137));
    Collections.shuffle(outliers, new Random(2137));
    normal = new ArrayList<>(normal.subList(0, 82 * Consts.dataSizeFactor));
    outliers = new ArrayList<>(outliers.subList(0, 18 * Consts.dataSizeFactor));
    observations.clear();
    observations.addAll(normal);
    observations.addAll(outliers);
    Collections.shuffle(observations, new Random(2137));
    return observations;
  }
}
