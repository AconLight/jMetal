package consts;

import results.ResultsSaver;

import java.util.ArrayList;
import java.util.function.Function;

public class ConstsGenerator {


    // by measure
    public static int[] measuresIdx = {2};
    public static int[] numbOfObjectives = {3};
    public static int[] numbOfFitnessHints = {2};

    // by file
    public static String file[] = {"data/bcw.data"};//, "data/glass.data"};
    public static int[] outlierSize = {18};//, 9};
    public static int[] normalSize = {82};//, 205};
    public static int[] initialNumberofVariables = {18};//, 9};
    public static int[] outlierLabel = {4};//, 6};

    // all times all
    public static double[] mutationAddProb = {0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28};//{0.10, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.20, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29, 0.30};
    public static double[] mutationRemProb = {0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28};
    public static double[] mutationChangeProb = {0.04, 0.08, 0.12, 0.16, 0.20, 0.24, 0.28};//{0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.20, 0.21};
    public static double[] crossProb = {0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7};
    public static int[] popSize = {12, 14, 16, 18, 20, 22, 24};

    public static double[] memremK = {0.5};


    public static int[] evaluations = {/*100, 200, 300, 400, 500, 600, 700, 800, 900, */1000, 1100, 1200, 1300, 1400/*, 1500, 1600, 1700*/};//, 1000, 1250, 1500, 1750, 2000


    private static int currentConstsIdx = 0;

    public static void restart() {
        currentConstsIdx = 0;
    }

    public static void setBest() {
        Consts.mutationAddProb = BestConsts.mutationAddProb;
        Consts.mutationRemProb = BestConsts.mutationRemProb;
        Consts.memesMutationAddProb = BestConsts.memesMutationAddProb;
        Consts.memesMutationRemProb = BestConsts.memesMutationRemProb;
        Consts.mutationChangeProb = BestConsts.mutationChangeProb;
        Consts.crossProb = BestConsts.crossProb;
        Consts.popSize = BestConsts.popSize;
    }

    public static void setForMemesPerc() {
        //Consts.evaluations = 200;
        Consts.measuresIdx = -1;
    }

    public static void setForDim() {
        Consts.evaluations = 600;
        Consts.measuresIdx = -1;
    }

    public static boolean prepareNextConstsByParams() {
        int allPossibilities = memremK.length*file.length * measuresIdx.length * mutationAddProb.length * mutationRemProb.length * mutationChangeProb.length * crossProb.length * popSize.length;
        currentConstsIdx++;
        if (currentConstsIdx > allPossibilities) {
            return false;
        }
        int i = 0;
        for (int fileVal = 0; fileVal < file.length; fileVal++) {
            for (int measuresIdxVal = 0; measuresIdxVal < measuresIdx.length; measuresIdxVal++) {
                for (int mutationAddProbVal = 0; mutationAddProbVal < mutationAddProb.length; mutationAddProbVal++) {
                    for (int mutationRemProbVal = 0; mutationRemProbVal < mutationRemProb.length; mutationRemProbVal++) {
                        for (int mutationChangeProbVal = 0; mutationChangeProbVal < mutationChangeProb.length; mutationChangeProbVal++) {
                            for (int crossProbVal = 0; crossProbVal < crossProb.length; crossProbVal++) {
                                for (int popSizeVal = 0; popSizeVal < popSize.length; popSizeVal++) {
                                    for (int memesShareVal = 0; memesShareVal < memremK.length; memesShareVal++) {
                                        i++;
                                        if (i < currentConstsIdx) {
                                            continue;
                                        } else if (i == currentConstsIdx) {
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
                                            //Consts.memremK = memremK[memesShareVal];
                                            System.out.println("\nConsts.mutationChangeProb: " + Consts.mutationChangeProb);
                                            System.out.println("Consts.mutationAddProb: " + Consts.mutationAddProb);
                                            System.out.println("Consts.mutationRemProb: " + Consts.mutationRemProb);
                                            System.out.println("Consts.popSize: " + Consts.popSize);
                                            //System.out.println("Consts.memremK: " + Consts.memremK);
                                        }
                                    }
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
        Consts.measuresIdx = -1;
        Consts.numbOfObjectives = BestConsts.measures.size() + 1;
        Consts.numbOfFitnessHints = Consts.numbOfObjectives - 1;

        int allPossibilities = evaluations.length;
        currentConstsIdx++;
        if (currentConstsIdx > allPossibilities) {
            return false;
        }
        int i = 0;
        for (int evalVal = 0; evalVal < evaluations.length; evalVal++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                Consts.evaluations = evaluations[evalVal];
                System.out.println("\nConsts.evaluations: " + Consts.evaluations);
                currentLabel = "" + Consts.evaluations;
            }
        }

        return true;
    }

    public static boolean prepareNextConstsByDim() {
        Consts.measuresIdx = -1;
        Consts.numbOfObjectives = BestConsts.measures.size() + 1;
        Consts.numbOfFitnessHints = Consts.numbOfObjectives - 1;
        Consts.outlierLabel = 1;
        Consts.fullFeatureSet = true;
        Consts.outlierSize = 15;
        Consts.normalSize = 85;


        ArrayList<Function> dims = Consts.getDimsFromRight("featuremusk.csv", 3, 20);
        int allPossibilities = dims.size();
        currentConstsIdx++;
        if (currentConstsIdx > allPossibilities) {
            return false;
        }
        int i = 0;
        for (int dim = 0; dim < dims.size(); dim++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                dims.get(i-1).apply(null);
                System.out.println("\ndimensions: " + (i));
                currentLabel = "" + (i);
            }
        }

        return true;
    }


    public static ResultsSaver resultsSaver;
    public static String currentLabel;

    public static boolean prepareNextConstsByParamsOnePerRun() {
        int allPossibilities = mutationAddProb.length + mutationChangeProb.length + crossProb.length + popSize.length;
        currentConstsIdx++;
        if (currentConstsIdx > allPossibilities + 1) {
            return false;
        }
        int i = 0;

        String ifMemes = "";
        String ifMemes2 = "";
        if (Consts.memesPerc == 0) {
            ifMemes = "no_memes/";
            ifMemes2 = "no_memes";
        } else {
            ifMemes = "memes/";
            ifMemes2 = "memes";
        }

        if (currentConstsIdx-1 == 0) {
            resultsSaver = new ResultsSaver("params/" + ifMemes + "mutation_add_remove_prob", 0);
        } else if (currentConstsIdx-1 == mutationAddProb.length) {
            resultsSaver.saveChart1("params_" + ifMemes2 + "_mutation_add_remove_prob", "mutation_add_remove_prob", "precision");
            resultsSaver = new ResultsSaver("params/" + ifMemes + "mutation_change_prob", 0);
        } else if (currentConstsIdx-1 == mutationAddProb.length + mutationChangeProb.length) {
            resultsSaver.saveChart1("params_" + ifMemes2 + "_mutation_change_prob", "mutation_change_prob", "precision");
            resultsSaver = new ResultsSaver("params/" + ifMemes + "cross_prob", 0);
        } else if (currentConstsIdx-1 == mutationAddProb.length + mutationChangeProb.length + crossProb.length) {
            resultsSaver.saveChart1("params_" + ifMemes2 + "_cross_prob", "cross_prob", "precision");
            resultsSaver = new ResultsSaver("params/" + ifMemes + "pop_size", 0);
        } else if (currentConstsIdx-1 == mutationAddProb.length + mutationChangeProb.length + crossProb.length + popSize.length) {
            resultsSaver.saveChart1("params_" + ifMemes2 + "_popSize", "popSize", "precision");
            return false;
        }

        for (int mutationAddProbVal = 0; mutationAddProbVal < mutationAddProb.length; mutationAddProbVal++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                setBest();
                Consts.mutationAddProb = mutationAddProb[mutationAddProbVal];
                Consts.memesMutationAddProb = mutationAddProb[mutationAddProbVal];
                Consts.mutationRemProb = mutationRemProb[mutationAddProbVal];
                Consts.memesMutationRemProb = mutationRemProb[mutationAddProbVal];
                System.out.println("\nmutationAddRemProb: " + Consts.mutationAddProb);
                currentLabel = "" + Consts.mutationAddProb;
            }

        }

        for (int mutationChangeProbVal = 0; mutationChangeProbVal < mutationChangeProb.length; mutationChangeProbVal++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                setBest();
                Consts.mutationChangeProb = mutationChangeProb[mutationChangeProbVal];
                System.out.println("\nmutationChangeProb: " + Consts.mutationChangeProb);
                currentLabel = "" + Consts.mutationChangeProb;
            }
        }

        for (int crossProbVal = 0; crossProbVal < crossProb.length; crossProbVal++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                setBest();
                Consts.crossProb = crossProb[crossProbVal];
                System.out.println("\ncrossProb: " + Consts.crossProb);
                currentLabel = "" + Consts.crossProb;
            }
        }

        for (int popSizeVal = 0; popSizeVal < popSize.length; popSizeVal++) {
            i++;
            if (i < currentConstsIdx) {
                continue;
            } else if (i == currentConstsIdx) {
                setBest();
                Consts.popSize = popSize[popSizeVal];
                System.out.println("\npopSize: " + Consts.popSize);
                currentLabel = "" + Consts.popSize;
            }
        }

        return true;
    }
}
