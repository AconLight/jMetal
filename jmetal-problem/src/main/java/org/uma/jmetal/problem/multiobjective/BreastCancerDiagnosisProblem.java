package org.uma.jmetal.problem.multiobjective;

import model.Observation;
import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;

import java.util.ArrayList;
import java.util.List;

public class BreastCancerDiagnosisProblem extends AbstractIntegerProblem {

    ArrayList<Observation> observations;

    public BreastCancerDiagnosisProblem(ArrayList<Observation> observations)  {
        this.observations = observations;
        setNumberOfVariables(5);
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);
        setName("BreastCancerDiagnosisProblem");

        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0);
            upperLimit.add(99);
        }

        setVariableBounds(lowerLimit, upperLimit);
    }

    @Override
    public void evaluate(IntegerSolution solution) {
        int f1 = 0;
        int f2 = 1;
        for (Integer val: solution.getVariables()) {
            f1 += val;
            f2 *=f1;
        }

        solution.setObjective(0, f1-f2);
        solution.setObjective(1, f1-f2);

    }

    @Override
    public IntegerSolution createSolution() {
        MemeticIntegerSolution s = new MemeticIntegerSolution(0, 99, 5, 2);
        for (int i = 0; i < s.getNumberOfVariables(); i++) {
            s.setVariable(i, i);
        }
        return s;
    }
}
