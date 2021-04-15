package org.uma.jmetal.algorithm.memetic.impl;

import org.uma.jmetal.algorithm.memetic.MemeticLocalSearch;
import org.uma.jmetal.solution.memetic.MemeticIntegerSolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MemeticLocalSearchVariableRecommendation<S> implements MemeticLocalSearch {

    @Override
    public List localSearch(List solutions) {
        ArrayList<MemeticIntegerSolution> solutionsCopy = new ArrayList<>();
        for (int i = 0; i < solutions.size(); i++) {
            solutionsCopy.add((MemeticIntegerSolution) solutions.get(i));
        }
        Collections.shuffle(solutionsCopy);
        for (int i = 0; i < solutionsCopy.size()/2; i++) {
            MemeticIntegerSolution first = (MemeticIntegerSolution) solutions.get(2*i);
            MemeticIntegerSolution second = (MemeticIntegerSolution) solutions.get(2*i+1);
            first.shareMemes(second);
        }
        return solutions;
    }
}
