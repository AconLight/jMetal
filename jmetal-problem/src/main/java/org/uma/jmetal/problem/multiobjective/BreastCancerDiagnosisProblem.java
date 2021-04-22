package org.uma.jmetal.problem.multiobjective;

import consts.Consts;
import model.Observation;
import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.*;

public class BreastCancerDiagnosisProblem extends AbstractGenericProblem<MemeticIntegerSolution> {

    ArrayList<Observation> observations;
    Vector<Double> centroid;
    private static int initialNumberofVariables = Consts.initialNumberofVariables;
    private static int numbOfObjectives = Consts.numbOfObjectives;

    public BreastCancerDiagnosisProblem(ArrayList<Observation> observations)  {
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

    @Override
    public void evaluate(MemeticIntegerSolution solution) {
        double cSum = 0;
        double k1Sum = 0;
        double k2Sum = 0;
        double k3Sum = 0;
        int index = 0;
        ArrayList<Observation> nonOutliers = new ArrayList<>();
        for (Observation o: observations) {
            if (!solution.getVariables().contains(observations.indexOf(o))) {
                nonOutliers.add(o);
            }
        }

        for (Integer val: solution.getVariables()) {
            double vDistance = -vectorDistanceDI(centroid, observations.get(val).data);
            double k1NearestDistance = -kNearestDistance(1, observations.get(val).data, nonOutliers);
            double k2NearestDistance = -kNearestDistance(2, observations.get(val).data, nonOutliers);
            double k3NearestDistance = -kNearestDistance(3, observations.get(val).data, nonOutliers);

            solution.setHint(index, 0, vDistance);
            solution.setHint(index, 1, k1NearestDistance);
            solution.setHint(index, 2, k2NearestDistance);
            solution.setHint(index, 3, k3NearestDistance);

            cSum += vDistance;
            k1Sum += k1NearestDistance;
            k2Sum += k2NearestDistance;
            k3Sum += k3NearestDistance;

            index++;
        }

        double cDistance = cSum / solution.getVariables().size();
        double k1Distance = k1Sum / solution.getVariables().size();
        double k2Distance = k2Sum / solution.getVariables().size();
        double k3Distance = k3Sum / solution.getVariables().size();

        solution.setObjective(0, cDistance);
        solution.setObjective(1, k1Distance);
        solution.setObjective(2, k2Distance);
        solution.setObjective(3, k3Distance);
        solution.setObjective(4, Math.abs(initialNumberofVariables-solution.getNumberOfVariables()));

    }

    @Override
    public MemeticIntegerSolution createSolution() {
        MemeticIntegerSolution s = new MemeticIntegerSolution(0, 100 * Consts.dataSizeFactor-1, initialNumberofVariables, numbOfObjectives);
        for (int i = 0; i < s.getNumberOfVariables(); i++) {
            s.setVariable(i, JMetalRandom.getInstance().nextInt(0, 100 * Consts.dataSizeFactor-1));
        }
        return s;
    }

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
        for (int i = 0; i < k+4; i++) {
            sum += distances.get(i);
        }
        return sum;
    }

}
