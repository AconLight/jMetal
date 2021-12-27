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
import java.util.stream.Collectors;

import static consts.ConstsGenerator.resultsSaver;
import static org.uma.jmetal.example.multiobjective.nsgaii.NSGAIIMemeticRunner.loadData;


public class NSGAIIMemeticRunnerStudy extends AbstractAlgorithmRunner {

  private static Function[] datasets = {/*(x) -> Consts.setBCW(), *//*(x) -> Consts.setNormal(),  */(x) -> Consts.setMusk()};

  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Thread thread = new Thread(() -> ChartCreator.launchCharts());
    thread.start();

    //Consts.setBCW();
    //perParams();

    for (Function dataset: datasets) {
      dataset.apply(null);
      normalAlgorithm();
//      memesPerc();
//      dataset.apply(null);
      perEvals();
    }

    // just for big dim
//    Consts.setMusk();
//    normalAlgorithm();

    //dimensionsPCA();
    System.out.println("memetic study finished ");
    thread.stop();

  }

  public static void perParams() throws JMetalException, FileNotFoundException {
    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 0;
    Consts.featuresMinus = 0;
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
    Consts.featuresMinus = 0;
    BreastCancerDiagnosisProblem problem = new BreastCancerDiagnosisProblem(loadData());
    ArrayList<BreastCancerDiagnosisProblem.IMeasure> measures = new ArrayList<>();
    ArrayList<String> measureNames = new ArrayList<>();
    measures.add((MemeticIntegerSolution solution) -> problem.CDMeasure(0, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.COFMeasure(0, 2, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.COFMeasure(0, 3, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.LOFMeasure(0, 2, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.LOFMeasure(0, 3, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.KNDMeasure(0, 1, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.KNDMeasure(0, 2, solution));
    measures.add((MemeticIntegerSolution solution) -> problem.KNDMeasure(0, 3, solution));
    measureNames.add("CDMeasure");
    measureNames.add("COFMeasure - k=2");
    measureNames.add("COFMeasure - k=3");
    measureNames.add("LOFMeasure - k=2");
    measureNames.add("LOFMeasure - k=3");
    measureNames.add("KNDMeasure - k=1");
    measureNames.add("KNDMeasure - k=2");
    measureNames.add("KNDMeasure - k=3");

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> measuresWithResults = new ArrayList<>();

    int idx = 0;
    for (BreastCancerDiagnosisProblem.IMeasure measure: measures) {
      problem.measures.clear();
      problem.measures.add((MemeticIntegerSolution solution) -> measure.calculate(solution));
      measuresWithResults.add(NormalAlgorithm.perform(problem, measureNames.get(idx)));
      idx++;
    }

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> chosenMeasures = NormalAlgorithm.chooseNMeasures(3, measuresWithResults);
    chosenMeasures = new ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>>(chosenMeasures.stream().filter(m -> m.getSecond().getSecond().size() != 0).collect(Collectors.toList()));

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
    System.out.println("normal algorithm finished ");
    for (String name: BestConsts.measures) {
      System.out.println(name);
    }
  }

  public static void perEvals() throws JMetalException, FileNotFoundException {
    resultsSaver = new ResultsSaver("per_eval/no_memes/" + Consts.file.split("data/")[1].split(".data")[0], 2);
    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.featuresMinus = 0;
    Consts.memesPerc = 0;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart2("no_memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of evaluations", "precision", "precision mean error");

    resultsSaver = new ResultsSaver("per_eval/memes/" + Consts.file.split("data/")[1].split(".data")[0], 2);
    System.out.println("with memes");
    ConstsGenerator.restart();
    Consts.memesPerc = Consts.bestMemesPerc;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart2("memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of evaluations", "precision", "precision mean error");

  }



  public static void memesPerc() throws JMetalException, FileNotFoundException {
    resultsSaver = new ResultsSaver("memes_perc/" + Consts.file.split("data/")[1].split(".data")[0], 2);
    System.out.println("memes perc");
    ConstsGenerator.restart();
    ConstsGenerator.setBest();
    ConstsGenerator.setForMemesPerc();
    Consts.featuresMinus = 0;
    float best = 0;
    for (int i = 0; i <= 100; i+=5) {
      Consts.memesPerc = i/100f;
      ArrayList<Float> res = NSGAIIMemeticRunner.constsRun();
      if (res.get(0) > best) {
        best = res.get(0);
        Consts.bestMemesPerc = Consts.memesPerc;
      }
      resultsSaver.saveRecord("" + i, res);
    }
    resultsSaver.saveChart2("memes_perc_" + Consts.file.split("/")[1].replace(".", "_"), "memes_perc", "precision", "precision mean error");
  }

  public static void dimensions() throws JMetalException, FileNotFoundException {
    ConstsGenerator.restart();
    ConstsGenerator.setBest();
    ConstsGenerator.setForDim();
    Consts.featuresNumb = 1000000;
    Consts.featuresMinus = 0;
    Consts.memesPerc = 0;
    Consts.featuresMinus = 0;
    resultsSaver = new ResultsSaver("dim/no_memes_" + Consts.file.split("data/")[1].split(".data")[0], 2);
    ArrayList<Float> res = null;
    while (Consts.featuresNumb > Consts.featuresMinus) {
      if (res != null) {
        resultsSaver.saveRecord("" + (Consts.featuresNumb - Consts.featuresMinus), res);
      }
      res = NSGAIIMemeticRunner.constsRun();
      Consts.featuresMinus++;
    }
    resultsSaver.saveRecord("" + (Consts.featuresNumb - Consts.featuresMinus), res);
    resultsSaver.saveChart2("dim_no_memes" + Consts.file.split("/")[1].replace(".", "_"), "memes_perc", "precision", "precision mean error");


    ConstsGenerator.restart();
    ConstsGenerator.setBest();
    ConstsGenerator.setForDim();
    Consts.featuresNumb = 1000000;
    Consts.featuresMinus = 0;
    Consts.memesPerc = Consts.bestMemesPerc;
    resultsSaver = new ResultsSaver("dim/memes_" + Consts.file.split("data/")[1].split(".data")[0], 2);
    res = null;
    while (Consts.featuresNumb > Consts.featuresMinus) {
      if (res != null) {
        resultsSaver.saveRecord("" + (Consts.featuresNumb - Consts.featuresMinus), res);
      }
      res = NSGAIIMemeticRunner.constsRun();
      Consts.featuresMinus++;
    }
    resultsSaver.saveRecord("" + (Consts.featuresNumb - Consts.featuresMinus), res);
    resultsSaver.saveChart2("dim_memes_" + Consts.file.split("/")[1].replace(".", "_"), "memes_perc", "precision", "precision mean error");
  }

  public static void dimensionsPCA() throws JMetalException, FileNotFoundException {
    resultsSaver = new ResultsSaver("dim_pca/no_memes_" + Consts.file.split("data/")[1].split(".csv")[0], 2);
    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 0;
    while (ConstsGenerator.prepareNextConstsByDim()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart1("dim_pca/no_memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of dimensions", "precision");

    resultsSaver = new ResultsSaver("dim_pca/memes_" + Consts.file.split("data/")[1].split(".csv")[0], 2);
    System.out.println("with memes");
    ConstsGenerator.restart();
    Consts.memesPerc = Consts.bestMemesPerc;
    while (ConstsGenerator.prepareNextConstsByDim()) {
      resultsSaver.saveRecord(ConstsGenerator.currentLabel, NSGAIIMemeticRunner.constsRun());
    }
    resultsSaver.saveChart1("dim_pca/memes_" + Consts.file.split("/")[1].replace(".", "_"), "number of dimensions", "precision");

  }


}
