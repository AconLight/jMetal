package org.uma.jmetal.solution.memetic;

import org.uma.jmetal.solution.AbstractSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.HashMap;

public class MemeticIntegerSolution extends AbstractSolution<Integer> implements IntegerSolution {
    public MemeticIntegerSolution memesAdd;
    public MemeticIntegerSolution memesRemove;
    private int lowerBound, upperBound;


    public MemeticIntegerSolution(int lowerBound, int upperBound, int initialNumberOfVariables, int numberOfObjectives) {
        super(initialNumberOfVariables, numberOfObjectives);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        memesAdd = new MemeticIntegerSolution(lowerBound, upperBound, initialNumberOfVariables, numberOfObjectives, true);
        memesRemove = new MemeticIntegerSolution(lowerBound, upperBound, initialNumberOfVariables, numberOfObjectives, true);
    }

    public MemeticIntegerSolution(int lowerBound, int upperBound, int initialNumberOfVariables, int numberOfObjectives, boolean noMoreMemes) {
        super(initialNumberOfVariables, numberOfObjectives);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void addOneVar(int value) {

    }

    public void remOneVar(int value) {

    }

    /** Copy constructor */
    public MemeticIntegerSolution(MemeticIntegerSolution solution) {
        super(solution.getNumberOfVariables(), solution.getNumberOfObjectives());
        memesAdd = new MemeticIntegerSolution(solution.memesAdd, true);
        memesRemove = new MemeticIntegerSolution(solution.memesRemove, true);

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            setVariable(i, solution.getVariable(i));
        }

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
            setConstraint(i, solution.getConstraint(i));
        }

        upperBound = solution.upperBound;
        lowerBound = solution.lowerBound;

        attributes = new HashMap<>(solution.attributes);
    }

    /** Copy constructor */
    public MemeticIntegerSolution(MemeticIntegerSolution solution, boolean noMoreMemes) {
        super(solution.getNumberOfVariables(), solution.getNumberOfObjectives());

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            setVariable(i, solution.getVariable(i));
        }

        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }

        for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
            setConstraint(i, solution.getConstraint(i));
        }

        upperBound = solution.upperBound;
        lowerBound = solution.lowerBound;

        attributes = new HashMap<>(solution.attributes);
    }

    @Override
    public Solution<Integer> copy() {
        return new MemeticIntegerSolution(this);
    }

    @Override
    public Integer getLowerBound(int index) {
        return null;
    }

    @Override
    public Integer getUpperBound(int index) {
        return null;
    }
}
