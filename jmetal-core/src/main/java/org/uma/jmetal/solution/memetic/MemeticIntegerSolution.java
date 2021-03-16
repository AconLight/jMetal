package org.uma.jmetal.solution.memetic;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;

import java.util.List;

public class MemeticIntegerSolution extends DefaultIntegerSolution {
    public DefaultIntegerSolution memes;

    public MemeticIntegerSolution(List<Pair<Integer, Integer>> bounds, int numberOfObjectives, List<Pair<Integer, Integer>> memesBounds, int numberOfMemes) {
        super(bounds, numberOfObjectives);
        memes = new DefaultIntegerSolution(memesBounds, numberOfMemes);
    }
}
