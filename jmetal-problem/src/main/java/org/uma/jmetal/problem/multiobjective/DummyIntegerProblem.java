package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.ArrayList;
import java.util.List;

public class DummyIntegerProblem extends AbstractIntegerProblem {

    public DummyIntegerProblem()  {
        setNumberOfVariables(3);
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);
        setName("DummyIntegerProblem");

        List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0);
            upperLimit.add(10);
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
        solution.setObjective(1, f2*f1 - (f1+f2));

    }
}
