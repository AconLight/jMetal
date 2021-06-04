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
import results.ResultStorage;
import results.ResultsSaver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static consts.ConstsGenerator.resultsSaver;


public class NSGAIIMemeticRunnerStudy extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws JMetalException, FileNotFoundException {
//    NSGAIIMemeticRunner.constsRun();

    //set dataset
    //perParams();

    //set dataset
    normalAlgorithm();
    memesPlusNoMemes();
    memesPerc();

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
    Consts.outlierSize = 18;
    Consts.normalSize = 82;
    Consts.fullFeatureSet = true;
    Consts.outlierLabel = 4;
    Consts.file = "data/bcw.data";
    resultsSaver = new ResultsSaver("measures");
    resultsSaver.saveRecord("measures/bcw/lof_k1", NSGAIIMemeticRunner.normalRun());
  }

  public static void memesPlusNoMemes() throws JMetalException, FileNotFoundException {

  }

  public static void memesPerc() throws JMetalException, FileNotFoundException {

  }


}
