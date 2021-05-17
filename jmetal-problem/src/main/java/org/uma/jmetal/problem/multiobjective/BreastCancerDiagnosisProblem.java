package org.uma.jmetal.problem.multiobjective;

import consts.Consts;
import model.Observation;
import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.*;
import java.util.function.Function;

public class BreastCancerDiagnosisProblem extends AbstractGenericProblem<MemeticIntegerSolution> {

    ArrayList<Observation> observations;
    Vector<Double> centroid;
    private static int initialNumberofVariables = Consts.initialNumberofVariables;
    private static int numbOfObjectives = Consts.numbOfObjectives;

    ArrayList<Function<MemeticIntegerSolution, Double>> measures;

    private void dataSetup(ArrayList<Observation> observations) {
        this.observations = observations;
        centroid = new Vector<Double>(observations.get(0).data.size());
        for (int i = 0; i < observations.get(0).data.size(); i++) {
            centroid.add(0d);
        }
        for (Observation o: observations) {
            int i = 0;
            for (Integer val: o.data) {
                centroid.set(i, centroid.get(i) + val);
                i++;
            }
        }
        for (int i = 0; i < observations.get(0).data.size(); i++) {
            centroid.set(i, centroid.get(i) / observations.size());
        }
    }

    private void problemSetup() {
        setNumberOfVariables(initialNumberofVariables);
        setNumberOfObjectives(numbOfObjectives);
        setNumberOfConstraints(0);
        setName("BreastCancerDiagnosisProblem");

        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0);
            upperLimit.add(100 * Consts.dataSizeFactor-1);
        }
    }

    private void measuresSetup() {
        this.measures = new ArrayList<>();

        this.measures.add((MemeticIntegerSolution solution) -> LOFMeasure(0,2, solution));
        this.measures.add((MemeticIntegerSolution solution) -> LOFMeasure(1,3, solution));
        this.measures.add((MemeticIntegerSolution solution) -> LOFMeasure(2,4, solution));
        this.measures.add((MemeticIntegerSolution solution) -> COFMeasure(3,2, solution));
        this.measures.add((MemeticIntegerSolution solution) -> COFMeasure(4,3, solution));
        this.measures.add((MemeticIntegerSolution solution) -> COFMeasure(5,4, solution));
//        this.measures.add((MemeticIntegerSolution solution) -> CDMeasure(2, solution));
//        this.measures.add((MemeticIntegerSolution solution) -> KNDMeasure(3, 4, solution));
        this.measures.add((MemeticIntegerSolution solution) -> ONMeasure(6, solution));

//        this.measures.add((MemeticIntegerSolution solution) -> COFMeasure(0,3, solution));
//        this.measures.add((MemeticIntegerSolution solution) -> ONMeasure(1, solution));


    }

    public BreastCancerDiagnosisProblem(ArrayList<Observation> observations)  {
        dataSetup(observations);
        measuresSetup();
        problemSetup();
    }

    @Override
    public void evaluate(MemeticIntegerSolution solution) {
        for (Function measure: measures) {
            measure.apply(solution);
        }
    }

    @Override
    public MemeticIntegerSolution createSolution() {
        MemeticIntegerSolution s = new MemeticIntegerSolution(0, 100 * Consts.dataSizeFactor-1, initialNumberofVariables, numbOfObjectives);
        for (int i = 0; i < s.getNumberOfVariables(); i++) {
            s.setVariable(i, JMetalRandom.getInstance().nextInt(0, 100 * Consts.dataSizeFactor-1));
        }
        return s;
    }


    // CALC FUNCTIONS
    private double vectorDistanceDI(Vector<Double> a, Vector<Integer> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i))*(a.get(i) - b.get(i));
        }
        return Math.sqrt(sum);
    }

    private double vectorDistanceII(Vector<Integer> a, Vector<Integer> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i))*(a.get(i) - b.get(i));
        }
        return Math.sqrt(sum);
    }

    private double kNearestDistance(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        double sum = 0;
        ArrayList<Double> distances = new ArrayList<>();
        for (int i = 0; i < observations.size(); i++) {
            distances.add(vectorDistanceII(a, observations.get(i).data));
        }
        Collections.sort(distances);
        for (int i = 0; i < k+1; i++) {
            sum += distances.get(i);
        }
        return sum;
    }

    private double kthDistance(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        ArrayList<Double> distances = new ArrayList<>();
        for (int i = 0; i < observations.size(); i++) {
            distances.add(vectorDistanceII(a, observations.get(i).data));
        }
        Collections.sort(distances);
        return distances.get(k);
    }

    private double reachabiltyDistance(int k, Vector<Integer> a, Vector<Integer> b, ArrayList<Observation> observations) {
        return Math.max(kthDistance(k, b, observations), vectorDistanceII(a, b));
    }

    private ArrayList<Observation> getKNearestSet(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        ArrayList<Observation> set = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < observations.size(); i++) {
            distances.add(vectorDistanceII(a, observations.get(i).data));
            indexes.add(i);
        }

        Collections.sort(indexes, (myA, myB) -> (int) Math.signum(distances.get(myA) - distances.get(myB)));
        for (int i = 1; i < k+1; i++) {
            set.add(observations.get(i));
        }
        int i = k+1;
        while (distances.get(i-1) == distances.get(i)) {
            set.add(observations.get(i));
            i++;
        }

        return set;
    }

    private double localReachabiltyDistance(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        double sum = 0;
        ArrayList<Observation> kNearestSet = getKNearestSet(k, a, observations);
        for (Observation o: kNearestSet) {
            sum += reachabiltyDistance(k, a, o.data, observations);
        }
        sum /= kNearestSet.size();

        return 1/ sum;
    }

    private double localOutlierFactor(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        double sum = 0;
        ArrayList<Observation> kNearestSet = getKNearestSet(k, a, observations);
        for (Observation o: kNearestSet) {
            sum += localReachabiltyDistance(k, o.data, observations);
        }
        sum /= kNearestSet.size() * localReachabiltyDistance(k, a, observations);

        return 1/ sum;
    }

    private double chainingDistance(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        double sum = 0;
        Vector<Integer> next = a;
        sum += kthDistance(1, next, observations) * 2*k / k*(k+1);
        next = getKNearestSet(1, a, observations).get(0).data;
        for (int i = 2; i <= k; i++) {
            sum += kthDistance(1, next, observations) * 2*(k + 1 - i) / k*(k+1);;
        }

        return sum;
    }

    private double connectivityOutlierFactor(int k, Vector<Integer> a, ArrayList<Observation> observations) {
        double distA = chainingDistance(k, a, observations);
        double distO = 0;
        ArrayList<Observation> kNearestSet = getKNearestSet(k, a, observations);
        for (Observation o: kNearestSet) {
            distO += chainingDistance(k, o.data, observations);
        }
        distO /= kNearestSet.size();

        return distA / distO;
    }


    // MEASURES WITHOUT HINTS
    private double ONMeasure(int measureIdx, MemeticIntegerSolution solution) {
        solution.setObjective(measureIdx, Math.abs(initialNumberofVariables-solution.getNumberOfVariables()));

        return 0;
    }


    // MEASURES WITH HINTS
    private double KNDMeasure(int measureIdx, int k, MemeticIntegerSolution solution) {

        ArrayList<Observation> nonOutliers = new ArrayList<>();
        for (Observation o: observations) {
            if (!solution.getVariables().contains(observations.indexOf(o))) {
                nonOutliers.add(o);
            }
        }
        double KNNearestDistanceSum = 0;
        int index = 0;
        for (Integer val: solution.getVariables()) {
            double KNNearestDistance = -kNearestDistance(k, observations.get(val).data, nonOutliers);
            KNNearestDistanceSum += KNNearestDistance;
            solution.setHint(index, measureIdx, KNNearestDistance);
            index++;
        }

        solution.setObjective(measureIdx, KNNearestDistanceSum / solution.getVariables().size());

        return 0;
    }

    private double CDMeasure(int measureIdx, MemeticIntegerSolution solution) {
        double CDistanceSum = 0;
        int index = 0;
        for (Integer val: solution.getVariables()) {
            double CDistance = -vectorDistanceDI(centroid, observations.get(val).data);
            CDistanceSum += CDistance;
            solution.setHint(index, measureIdx, CDistance);
            index++;
        }

        solution.setObjective(measureIdx, CDistanceSum / solution.getVariables().size());

        return 0;
    }

    private double LOFMeasure(int measureIdx, int k, MemeticIntegerSolution solution) {
        double LOFSum = 0;
        int index = 0;
        for (Integer val: solution.getVariables()) {
            double LOF = -localOutlierFactor(k, observations.get(val).data, observations);
            LOFSum += LOF;
            solution.setHint(index, measureIdx, LOF);
            index++;
        }

        solution.setObjective(measureIdx, LOFSum / solution.getVariables().size());

        return 0;
    }

    private double COFMeasure(int measureIdx, int k, MemeticIntegerSolution solution) {
        double COFSum = 0;
        int index = 0;
        for (Integer val: solution.getVariables()) {
            double COF = -connectivityOutlierFactor(k, observations.get(val).data, observations);
            COFSum += COF;
            solution.setHint(index, measureIdx, COF);
            index++;
        }

        solution.setObjective(measureIdx, COFSum / solution.getVariables().size());

        return 0;
    }


}
