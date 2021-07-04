package org.uma.jmetal.example.multiobjective.nsgaii;

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

import static consts.ConstsGenerator.resultsSaver;
import static org.uma.jmetal.example.multiobjective.nsgaii.NSGAIIMemeticRunner.loadData;


public class NSGAIIMemeticRunnerStudy extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws JMetalException, FileNotFoundException {
//    NSGAIIMemeticRunner.constsRun();

    //set dataset
    //perParams();
    perEvals();

    //set dataset
    //normalAlgorithm();
    memesPlusNoMemes();
    memesPerc();

  }

  public static void perEvals() throws JMetalException, FileNotFoundException {

    Consts.fullFeatureSet = true;

    Consts.outlierSize = 18;
    Consts.normalSize = 82;
    Consts.outlierLabel = 4;
    Consts.file = "data/bcw.data";
    Consts.initialNumberofVariables = 18;

    System.out.println("without memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 0;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      NSGAIIMemeticRunner.constsRun();
    }


    System.out.println("with memes");
    ConstsGenerator.restart();
    Consts.memesPerc = 1;
    while (ConstsGenerator.prepareNextConstsByEvaluation()) {
      NSGAIIMemeticRunner.constsRun();
    }

  }

  public static void perParams() throws JMetalException, FileNotFoundException {

    Consts.fullFeatureSet = true;

    Consts.outlierSize = 18;
    Consts.normalSize = 82;
    Consts.outlierLabel = 4;
    Consts.file = "data/bcw.data";
    Consts.initialNumberofVariables = 18;

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
    Consts.fullFeatureSet = true;

    Consts.outlierSize = 18;
    Consts.normalSize = 82;
    Consts.outlierLabel = 4;
    Consts.file = "data/bcw.data";

    Consts.outlierSize = 9;
    Consts.normalSize = 205;
    Consts.outlierLabel = 6;
    Consts.file = "data/glass.data";

    Consts.initialNumberofVariables = Consts.outlierSize*6;

    //resultsSaver = new ResultsSaver("measures");
    //resultsSaver.saveRecord("measures/bcw/lof_k1", NSGAIIMemeticRunner.normalRun());
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
    measureNames.add("COFMeasure, k=1");
    measureNames.add("COFMeasure, k=2");
    measureNames.add("LOFMeasure, k=1");
    measureNames.add("LOFMeasure, k=2");
    measureNames.add("KNDMeasure, k=1");
    measureNames.add("KNDMeasure, k=2");

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> measuresWithResults = new ArrayList<>();

    int idx = 0;
    for (BreastCancerDiagnosisProblem.IMeasure measure: measures) {
      problem.measures.clear();
      problem.measures.add((MemeticIntegerSolution solution) -> measure.calculate(solution));
      measuresWithResults.add(NormalAlgorithm.perform(problem, measureNames.get(idx)));
      idx++;
    }

    ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> chosenMeasures = NormalAlgorithm.chooseNMeasures(2, measuresWithResults);

    System.out.println();
  }

  public static void memesPlusNoMemes() throws JMetalException, FileNotFoundException {

  }

  public static void memesPerc() throws JMetalException, FileNotFoundException {

  }


}
