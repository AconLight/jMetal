package org.uma.jmetal.algorithm.memetic.impl;

import consts.Consts;
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
        for (int j = 0; j < 5; j++) {
            Collections.shuffle(solutionsCopy);
            for (int i = solutionsCopy.size() - 1; i > 0; i--) {
                MemeticIntegerSolution first = (MemeticIntegerSolution) solutions.get(i - 1);
                MemeticIntegerSolution second = (MemeticIntegerSolution) solutions.get(i);
                first.shareMemes(second);
            }
            ((MemeticIntegerSolution) solutions.get(solutionsCopy.size() - 1)).shareMemes(((MemeticIntegerSolution) solutions.get(0)));
        }

        return solutions;
    }
}
