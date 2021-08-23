package org.uma.jmetal.example.multiobjective.nsgaii;

import charts.ChartCreator;
import consts.BestConsts;
import consts.Consts;
import consts.ConstsGenerator;
import model.Observation;
import org.apache.commons.math3.util.Pair;
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
import results.ResultStorage;
import results.ResultsSaver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static consts.ConstsGenerator.resultsSaver;
import static org.uma.jmetal.example.multiobjective.nsgaii.NSGAIIMemeticRunner.loadData;


public class NSGAIIMemeticRunnerStudy extends AbstractAlgorithmRunner {

  private static Function[] datasets = {(x) -> Consts.setBCW(), (x) -> Consts.setNormal()};//, (x) -> Consts.setDim()};

  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Thread thread = new Thread(() -> ChartCreator.launchCharts());
    thread.start();

    Consts.setBCW();
    //perParams();

    for (Function dataset: datasets) {
      dataset.apply(null);
      normalAlgorithm();
      memesPerc();
      perEvals();

    }

//    Consts.setDim();
//    dimensions();

    thread.stop();

  }

  public static void perParams() throws JMetalException, FileNotFoundException {
    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 0;
    while (ConstsGenerator.prepareNextConstsByParamsOnePerRun()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }


    System.out.println("with memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 1;
    while (ConstsGenerator.prepareNextConstsByParamsOnePerRun()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }

  }

  public static void normalAlgorithm() throws JMetalException, FileNotFoundException {

    resultsSaver = new ResultsSaver("measures/" + Consts.file.split("data/")[1].split(".data")[0], 0);

    BreastCancerDiagnosisProblem problem = new BreastCancerDiagnosisProblem(loadData());
    ArrayList<BreastCancerDiagnosisProblem.IMeasure> measures = new ArrayList<>();
    ArrayList<String> measureNames = new ArrayList<>();
    measures.add((MemeticIntegerSolution solution) -> problem.CDMeasure(0, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.COFMeasure(0, 1, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.COFMeasure(0, 2, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.LOFMeasure(0, 1, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.LOFMeasure(0, 2, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.KNDMeasure(0, 1, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.KNDMeasure(0, 2, solution));
    measureNames.add("CDMeasure");
    measureNames.add("COFMeasure - k=1");
    measureNames.add("COFMeasure - k=2");
    measureNames.add("LOFMeasure - k=1");
    measureNames.add("LOFMeasure - k=2");
    measureNames.add("KNDMeasure - k=1");
    measureNames.add("KNDMeasure - k=2");

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> measuresWithResults = new ArrayList<>();

    int idx = 0;
    for (BreastCancerDiagnosisProblem.IMeasure measure: measures) {
      problem.measures.clear();
      problem.measures.add((MemeticIntegerSolution solution) -> measure.calculate(solution));
      measuresWithResults.add(NormalAlgorithm.perform(problem, measureNames.get(idx)));
      idx++;
    }

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> chosenMeasures = NormalAlgorithm.chooseNMeasures(2, measuresWithResults);

    BestConsts.measures.clear();
    for (Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>> chosen: chosenMeasures) {
      if (BestConsts.measures.stream().noneMatch(s -> s.contains(chosen.getFirst().split(" - k=")[0]))) {
        BestConsts.measures.add(chosen.getFirst());
        ArrayList<Float> ys = new ArrayList<>();
        ys.add(chosen.getSecond().getFirst().get(0) / (chosen.getSecond().getFirst().get(0) + chosen.getSecond().getFirst().get(2) + 0f));
        resultsSaver.saveRecord(chosen.getFirst(), ys);
      }
    }
    resultsSaver.saveChart0("chosenMeasures", "measure name", "precision");
    System.out.println();
  }

  public static void perEvals() throws JMetalException, FileNotFoundException {
    resultsSaver = new ResultsSaver("per_eval/no_memes/" + Consts.file.split("data/")[1].split(".data")[0], 2);
    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 0;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart2("no_memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of evaluations", "precision", "precision mean error");

    resultsSaver = new ResultsSaver("per_eval/memes/" + Consts.file.split("data/")[1].split(".data")[0], 2);
    System.out.println("with memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 1;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart2("memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of evaluations", "precision", "precision mean error");

  }



  public static void memesPerc() throws JMetalException, FileNotFoundException {

  }

  public static void dimensions() throws JMetalException, FileNotFoundException {

  }


}
