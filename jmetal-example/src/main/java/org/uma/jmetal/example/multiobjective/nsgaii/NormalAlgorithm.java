package org.uma.jmetal.example.multiobjective.nsgaii;

import consts.Consts;
import model.Observation;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.problem.multiobjective.BreastCancerDiagnosisProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;

import java.util.*;
import java.util.function.Function;

public class NormalAlgorithm {


    public static Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>> perform(BreastCancerDiagnosisProblem problem, String measureName) {
        ArrayList<MemeticIntegerSolution> calculated = new ArrayList<>();
        for (int i = 0; i < Consts.normalSize + Consts.outlierSize; i++) {
            MemeticIntegerSolution solution = createSolution(0, Consts.normalSize + Consts.outlierSize-1, i);
            problem.measures.get(0).apply(solution);
            calculated.add(solution);
        }
        Collections.sort(calculated, (a, b) -> (int) Math.signum(a.getObjective(0) - b.getObjective(0)));
        return calcResults(new ArrayList<>(calculated.subList(0, Consts.initialNumberofVariables)), problem.observations, measureName);
    }

    public static Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>> calcResults(ArrayList<MemeticIntegerSolution> population, ArrayList<Observation> observations2, String measureName) {
        ArrayList<Observation> observations = new ArrayList<>();
        for (Observation o: observations2) {
            observations.add(o);
        }
        ArrayList<Integer> tps = new ArrayList<>();
        ArrayList<Integer> bests = new ArrayList<>();
        for (MemeticIntegerSolution solution: population) {
            bests.add(solution.getVariable(0));
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
                tps.add(val);
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

        Collections.sort(tps);

        return new Pair<>(measureName, new Pair<>(res, tps));
    }

    public static ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> chooseNMeasures(int r, ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> measuresWithResults) {
        ArrayList<Pair<int[], Integer>> chosen = new ArrayList<>();
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(measuresWithResults.size(), r);
        while (iterator.hasNext()) {
            final int[] combination = iterator.next();
            HashSet<Integer> sum = new HashSet<>();
            String names = "";
            for (int comb: combination) {
                names += measuresWithResults.get(comb).getFirst() + " ---- ";
                sum.addAll(measuresWithResults.get(comb).getSecond().getSecond());
            }
            chosen.add(new Pair<>(combination, sum.size()));
        }
        ArrayList<Pair<String, Pair<ArrayList<Float>, ArrayList<Integer>>>> chosenMeasures = new ArrayList<>();
        Collections.sort(chosen, (a, b) -> (int) Math.signum(b.getSecond() - a.getSecond()));
        HashSet<Integer> allBests = new HashSet<>();
        int bestVal = chosen.get(0).getSecond();
        int i = 0;
        while (i < chosen.size()) {
            if (bestVal > chosen.get(i).getSecond()) {
                break;
            }
            for (int val: chosen.get(i).getFirst()) {
                allBests.add(val);
            }
            i++;
        }
        for (int comb: allBests) {
            chosenMeasures.add(measuresWithResults.get(comb));
        }
        return chosenMeasures;

    }



    public static MemeticIntegerSolution createSolution(int lowerBound, int upperBound, int val) {
        MemeticIntegerSolution solution = new MemeticIntegerSolution(lowerBound, upperBound, 1, Consts.numbOfObjectives);
        solution.setVariable(0, val);
        return solution;
    }
}
