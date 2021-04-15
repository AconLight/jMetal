package org.uma.jmetal.solution.memetic;

import org.uma.jmetal.solution.AbstractSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.*;

public class MemeticIntegerSolution extends AbstractSolution<Integer> implements IntegerSolution {

    public MemeticIntegerSolution memesAdd;
    public MemeticIntegerSolution memesRemove;
    public ArrayList<ArrayList<Double>> variablesFitnessValues;
    public ArrayList<Double> memesValues;

    private int lowerBound, upperBound;

    public static int numberOfFitnessHints = 1;

    public MemeticIntegerSolution(int lowerBound, int upperBound, int initialNumberOfVariables, int numberOfObjectives) {
        super(initialNumberOfVariables, numberOfObjectives);
        variablesFitnessValues = new ArrayList<>();
        for (int i = 0; i < initialNumberOfVariables; i++) {
            variablesFitnessValues.add(new ArrayList<Double>(Collections.nCopies(numberOfFitnessHints, 0d)));
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        memesAdd = new MemeticIntegerSolution(lowerBound, upperBound, 0, numberOfObjectives, true);
        memesRemove = new MemeticIntegerSolution(lowerBound, upperBound, 0, numberOfObjectives, true);
    }

    public MemeticIntegerSolution(int lowerBound, int upperBound, int initialNumberOfVariables, int numberOfObjectives, boolean noMoreMemes) {
        super(initialNumberOfVariables, numberOfObjectives);
        for (int i = 0; i < numberOfFitnessHints; i++) {
            getVariables().add(null);
        }
        memesValues = new ArrayList<Double>(Collections.nCopies(numberOfFitnessHints, null));
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void setVariables(List<Integer> vars) {
        this.getVariables().clear();
        this.getVariables().addAll(vars);
    }

    public void addOneVar(Integer value) {
        getVariables().add(value);
        variablesFitnessValues.add(new ArrayList<Double>(Collections.nCopies(numberOfFitnessHints, 0d)));
    }

    public void remOneVar(int index) {
        getVariables().remove(index);
        if (variablesFitnessValues != null) {
            variablesFitnessValues.remove(index);
        }
    }

    public void shareMemes(MemeticIntegerSolution s) {
        if (s.memesAdd != null && memesAdd != null) {
            for (int i = 0; i < s.memesAdd.memesValues.size(); i++) {
                if (s.memesAdd.memesValues.get(i) < memesAdd.memesValues.get(i)) {
                    memesAdd.setVariable(i, s.memesAdd.getVariable(i));
                    memesAdd.memesValues.set(i, s.memesAdd.memesValues.get(i));
                }
            }
        }
    }

    public void setHint(int varIndex, int hintIndex, double value) {
        variablesFitnessValues.get(varIndex).set(hintIndex, value);
        if (memesAdd.getVariable(hintIndex) == null) {
            memesAdd.setVariable(hintIndex, getVariable(varIndex));
            memesAdd.memesValues.set(hintIndex, value);
        } else {
            if (memesAdd.memesValues.get(hintIndex) > value) {
                memesAdd.setVariable(hintIndex, getVariable(varIndex));
                memesAdd.memesValues.set(hintIndex, value);
            }
        }
        if (memesRemove.getVariable(hintIndex) == null) {
            memesRemove.setVariable(hintIndex, getVariable(varIndex));
            memesRemove.memesValues.set(hintIndex, value);
        } else {
            if (memesRemove.memesValues.get(hintIndex) < value) {
                memesRemove.setVariable(hintIndex, getVariable(varIndex));
                memesRemove.memesValues.set(hintIndex, value);
            }
        }
    }

    public ArrayList<Integer> cutGenesToNumb(int numb) {
        List<Integer> firstGenes = getVariables().subList(0, numb);
        List<Integer> secondGenes = getVariables().subList(numb, getNumberOfVariables());
        for (int i = 0; i <firstGenes.size(); i++) {
            setVariable(i, firstGenes.get(i));
        }
        return new ArrayList<>(secondGenes);
    }

    /** Copy constructor */
    public MemeticIntegerSolution(MemeticIntegerSolution solution) {
        this(solution.lowerBound, solution.upperBound, solution.getNumberOfVariables(), solution.getNumberOfObjectives());
        variablesFitnessValues = copyVariablesFitnessValues(solution.variablesFitnessValues);
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
        this(solution.lowerBound, solution.upperBound, solution.getNumberOfVariables(), solution.getNumberOfObjectives(), true);

//        variablesFitnessValues = copyVariablesFitnessValues(solution.variablesFitnessValues);
        memesValues = copyMemesValues(solution.memesValues);

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

    private ArrayList<ArrayList<Double>> copyVariablesFitnessValues(ArrayList<ArrayList<Double>> variablesFitnessValues) {
        if (variablesFitnessValues == null) {
            return null;
        }
        ArrayList<ArrayList<Double>> res = new ArrayList<>();
        for (ArrayList<Double> arr: variablesFitnessValues) {
            ArrayList<Double> arrCopy = new ArrayList<>();
            for (Double val: arr) {
                arrCopy.add(0d + val);
            }
            res.add(arrCopy);
        }

        return res;
    }

    private ArrayList<Double> copyMemesValues(ArrayList<Double> memesValues) {
        if (memesValues == null) return null;
        ArrayList<Double> res = new ArrayList<>();
        for (Double val: memesValues) {
            res.add(0d + val);
        }
        return res;
    }
}
