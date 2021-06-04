package org.uma.jmetal.example.multiobjective.nsgaii;

import consts.Consts;
import consts.ConstsGenerator;
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
import org.uma.jmetal.util.front.impl.ArrayFront;
import results.ResultStorage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class NSGAIIMemeticRunner extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws JMetalException, FileNotFoundException {

  }

  public static ArrayList<Float> constsRun() throws JMetalException, FileNotFoundException {
    int numb = Consts.numberOfRuns;
    ArrayList<Observation> observations = loadData();
    int outliers = 0;
    for (Observation o : observations) {
      if (o.diagnosis == Consts.outlierLabel) {
        outliers++;
      }
    }
    System.out.println("outliers: " + outliers);

    ResultStorage resultStorage = new ResultStorage(numb);

    for (int i = 0; i < numb; i++) {
      resultStorage.add(run(observations));
      if ((i+1)%(Math.max(1, numb/4)) == 0) {
        System.out.println((int)(((i+1f)/numb*100)) + "% done");
      }
    }

    resultStorage.finish();
    resultStorage.print();

    return resultStorage.getResults();
  }

  public static ArrayList<Float> normalRun() throws JMetalException, FileNotFoundException {
    ArrayList<Observation> observations = loadData();
    int outliers = 0;
    for (Observation o : observations) {
      if (o.diagnosis == Consts.outlierLabel) {
        outliers++;
      }
    }
    System.out.println("outliers: " + outliers);

    ResultStorage resultStorage = new ResultStorage(1);

    resultStorage.add(normalRun2(observations));

    resultStorage.finish();
    resultStorage.print();

    return resultStorage.getResults();
  }

  public static ArrayList<Float> normalRun2(ArrayList<Observation> observations2) throws JMetalException, FileNotFoundException {
    return null;
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
    problem = new BreastCancerDiagnosisProblem(observations);
    crossover = new MyCrossover();
    int [] indexes = new int[observations2.size()];
    for (int i = 0; i < observations2.size(); i++) {
      indexes[i] = i;
    }
    mutation = new MyMutation(indexes);

    selection = new BinaryTournamentSelection<>(
            new RankingAndCrowdingDistanceComparator<>());

    algorithm = new NSGAIIBuilder(problem, crossover, mutation, Consts.popSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(Consts.evaluations)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<MemeticIntegerSolution> population = algorithm.getResult();

    return calcResults(population, observations);
  }

  public static ArrayList<Float> calcResults(List<MemeticIntegerSolution> population, ArrayList<Observation> observations) {
    HashMap<Integer, Integer> samplesCount = new HashMap<>();

    for (int i = 0; i < Consts.outlierSize + Consts.normalSize; i++) {
      samplesCount.put(i, 0);
    }

    for (MemeticIntegerSolution sol: population) {
      for (Integer val: sol.getVariables()) {
        samplesCount.put(val, samplesCount.get(val) + 1);
      }
    }

    ArrayList<Integer> bests = new ArrayList<>();
    for (int i = 0; i < Consts.outlierSize; i++) {
      HashMap.Entry<Integer, Integer> entry = Collections.max(samplesCount.entrySet(), Comparator.comparing(Map.Entry::getValue));
      bests.add(entry.getKey());
      samplesCount.remove(entry.getKey());
    }

    int tp = 0;
    int tn = 0;
    int fp = 0;
    int fn = 0;
    int i = 0;
    ArrayList<Observation> toRem = new ArrayList<>();
    for (Integer val: bests) {
      if (observations.get(val).diagnosis == Consts.outlierLabel) {
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
      if (o.diagnosis != Consts.outlierLabel) {
        tn++;
      }
      else {
        fn++;
      }
      i++;
    }

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
              Consts.file));
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
      if (o.diagnosis == Consts.outlierLabel) {
        outliers.add(o);
      } else {
        normal.add(o);
      }
    }
    Collections.shuffle(normal, new Random(2137));
    Collections.shuffle(outliers, new Random(2137));
    normal = new ArrayList<>(normal.subList(0, Consts.normalSize));
    outliers = new ArrayList<>(outliers.subList(0, Consts.outlierSize));
    observations.clear();
    observations.addAll(normal);
    observations.addAll(outliers);
    Collections.shuffle(observations, new Random(2137));
    return observations;
  }
}
