package consts;

import java.util.ArrayList;
import java.util.function.Function;

public class Consts {

    public static boolean fullFeatureSet = true;
    public static int measuresIdx = 2;
    public static int numbOfObjectives = 4;
    public static int numbOfFitnessHints = 3;

//    public static int outlierSize = 18;
//    public static int normalSize = 82;
//    public static int outlierLabel = 4;
//    public static String file = "data/bcw.data";

    public static int outlierSize = 9;
    public static int normalSize = 205;
    public static int outlierLabel = 6;
    public static String file = "data/glass.data";

    public static int initialNumberofVariables = outlierSize;

    public static double mutationAddProb = 0.08;
    public static double mutationRemProb = 0.08;
    public static double mutationChangeProb = 0.11;

    public static double memesMutationAddProb = 0.24;
    public static double memesMutationRemProb = 0.24;


    public static double crossProb = 0.8;

    public static int popSize = 18;

    public static int evaluations = 300;

    public static int numberOfRuns = 5;

    public static double memesPerc = 1;


    public static Object setBCW() {
        Consts.fullFeatureSet = true;
        Consts.outlierSize = 18;
        Consts.normalSize = 82;
        Consts.outlierLabel = 4;
        Consts.file = "data/bcw.data";
        Consts.initialNumberofVariables = 18;
        return null;
    }

    public static Object setNormal() {
        Consts.fullFeatureSet = true;
        Consts.outlierSize = 9;
        Consts.normalSize = 205;
        Consts.outlierLabel = 6;
        Consts.file = "data/glass.data";
        Consts.initialNumberofVariables = Consts.outlierSize*6;
        return null;
    }

    public static Object setDim() {
        Consts.fullFeatureSet = true;
        Consts.outlierSize = 9;
        Consts.normalSize = 205;
        Consts.outlierLabel = 6;
        Consts.file = "data/glass.data";
        Consts.initialNumberofVariables = Consts.outlierSize*6;
        return null;
    }





}
