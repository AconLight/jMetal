package org.uma.jmetal.problem.multiobjective;

import model.Observation;
import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BreastCancerDiagnosisProblem extends AbstractGenericProblem<MemeticIntegerSolution> {

    ArrayList<Observation> observations;
    Vector<Double> centroid;
    private static int initialNumberofVariables = 20;

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
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);
        setName("BreastCancerDiagnosisProblem");

        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0);
            upperLimit.add(99);
        }

    }

    @Override
    public void evaluate(MemeticIntegerSolution solution) {
        double sum = 0;
        int index = 0;
        //System.out.println(solution.getVariables().size());
        for (Integer val: solution.getVariables()) {
            double vDistance = -vectorDistance(centroid, observations.get(val).data);
            solution.setHint(index, 0, vDistance);
            sum += vDistance;
            index++;
        }
        double cDistance = sum / solution.getVariables().size();

        solution.setObjective(0, cDistance);
        solution.setObjective(1, Math.abs(45-solution.getNumberOfVariables()));

    }

    @Override
    public MemeticIntegerSolution createSolution() {
        MemeticIntegerSolution s = new MemeticIntegerSolution(0, 99, initialNumberofVariables, 2);
        for (int i = 0; i < s.getNumberOfVariables(); i++) {
            s.setVariable(i, JMetalRandom.getInstance().nextInt(0, 99));
        }
        return s;
    }

    private double vectorDistance(Vector<Double> a, Vector<Integer> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i))*(a.get(i) - b.get(i));
        }
        return Math.sqrt(sum);
    }
}
