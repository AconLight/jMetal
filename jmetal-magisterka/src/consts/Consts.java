package consts;

import java.util.ArrayList;
import java.util.function.Function;

public class Consts {


    public static int measuresIdx = 0;
    public static int numbOfObjectives = 5;
    public static int numbOfFitnessHints = 4;
    public static int outlierSize = 9;//18;
    public static int normalSize = 205;//82;
    public static int initialNumberofVariables = outlierSize;
    public static boolean fullFeatureSet = true;
    public static int outlierLabel = 6;//4;
    public static String file = "data/glass.data";

    public static double mutationAddProb = 0.2;
    public static double mutationRemProb = 0.2;
    public static double mutationChangeProb = 0.05;

    public static double crossProb = 0.6;

    public static int popSize = 12;

    public static int evaluations = 800;

    public static int numberOfRuns = 10;





}
