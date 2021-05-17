package consts;

import java.util.function.Function;

public class ConstsGenerator {


    // by measure
    public static int[] measuresIdx = {0, 1};
    public static int[] numbOfObjectives = {3, 3};
    public static int[] numbOfFitnessHints = {2, 2};

    // by file
    public static String file[] = {"data/glass.data", "data/bcw.data"};
    public static int[] outlierSize = {9, 18};
    public static int[] normalSize = {205, 82};
    public static int[] initialNumberofVariables = {9, 9};
    public static int[] outlierLabel = {6, 4};

    // all times all
    public static double[] mutationAddProb = {0.2};
    public static double[] mutationRemProb = {0.2};
    public static double[] mutationChangeProb = {0.05};
    public static double[] crossProb = {0.6};
    public static int[] popSize = {12};


    public static int[] evaluations = {300};


    private static int currentConstsIdx = 0;

    public static boolean prepareNextConstsByParams() {
        int allPossibilities = file.length * measuresIdx.length * mutationAddProb.length * mutationRemProb.length * mutationChangeProb.length * crossProb.length * popSize.length;
        currentConstsIdx++;
        if (currentConstsIdx >= allPossibilities) {
            return false;
        }

        for (int fileVal = 0; fileVal < file.length; fileVal++) {
            for (int measuresIdxVal = 0; measuresIdxVal < measuresIdx.length; measuresIdxVal++) {
                for (int mutationAddProbVal = 0; mutationAddProbVal < mutationAddProb.length; mutationAddProbVal++) {
                    for (int mutationRemProbVal = 0; mutationRemProbVal < mutationRemProb.length; mutationRemProbVal++) {
                        for (int mutationChangeProbVal = 0; mutationChangeProbVal < mutationChangeProb.length; mutationChangeProbVal++) {
                            for (int crossProbVal = 0; crossProbVal < crossProb.length; crossProbVal++) {
                                for (int popSizeVal = 0; popSizeVal < popSize.length; popSizeVal++) {
                                    Consts.measuresIdx = measuresIdx[measuresIdxVal];
                                    Consts.numbOfObjectives = numbOfObjectives[measuresIdxVal];
                                    Consts.numbOfFitnessHints = numbOfFitnessHints[measuresIdxVal];

                                    Consts.outlierSize = outlierSize[fileVal];
                                    Consts.normalSize = normalSize[fileVal];
                                    Consts.initialNumberofVariables = initialNumberofVariables[fileVal];
                                    Consts.outlierLabel = outlierLabel[fileVal];
                                    Consts.file = file[fileVal];

                                    Consts.mutationAddProb = mutationAddProb[mutationAddProbVal];
                                    Consts.mutationRemProb = mutationRemProb[mutationRemProbVal];
                                    Consts.mutationChangeProb = mutationChangeProb[mutationChangeProbVal];
                                    Consts.crossProb = crossProb[crossProbVal];
                                    Consts.popSize = popSize[popSizeVal];

                                }
                            }
                        }
                    }
                }
            }
        }


        return true;
    }


    public static boolean prepareNextConstsByEvaluation() {
        int allPossibilities = evaluations.length;
        currentConstsIdx++;
        if (currentConstsIdx >= allPossibilities) {
            return false;
        }


        return true;
    }
}
