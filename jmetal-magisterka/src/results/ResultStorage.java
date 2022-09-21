package results;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultStorage {
    int siz = 0;

    float sumAcc = 0;
    float minAcc = 1;
    float maxAcc = 0;
    ArrayList<Float> smAcc = new ArrayList<>();
    float smeAcc = 0;

    float sumCO = 0;
    float minCO = 1;
    float maxCO = 0;
    ArrayList<Float> smCO = new ArrayList<>();
    float smeCO = 0;

    float sumFO = 0;
    float minFO = 1;
    float maxFO = 0;
    ArrayList<Float> smFO = new ArrayList<>();
    float smeFO = 0;

    float sumF1 = 0;
    float minF1 = 1;
    float maxF1 = 0;
    ArrayList<Float> smF1 = new ArrayList<>();
    float smeF1 = 0;

    ArrayList<Float> myAccs = new ArrayList<>();

    int numb;

    public ResultStorage(int numb) {
        this.numb = numb;
    }

    public void add(ArrayList<Float> result) {
        float acc = (result.get(0) + result.get(1)) / (result.get(0) + result.get(1) + result.get(2) + result.get(3));
        float co = (result.get(0)) / (result.get(2) + result.get(0));
        float fo = (result.get(0)) / (result.get(3) + result.get(0));
        float f1 = 2*(co*fo)/(co+fo);

        smAcc.add(acc);
        smCO.add(co);
        smFO.add(fo);
        smF1.add(f1);

        minAcc = Math.min(acc, minAcc);
        maxAcc = Math.max(acc, maxAcc);
        sumAcc += acc;

        minCO = Math.min(co, minCO);
        maxCO = Math.max(co, maxCO);
        sumCO += co;

        minFO = Math.min(fo, minFO);
        maxFO = Math.max(fo, maxFO);
        sumFO += fo;

        minF1 = Math.min(f1, minF1);
        maxF1 = Math.max(f1, maxF1);
        sumF1 += f1;
    }

    public void finish() {
        sumAcc = sumAcc/numb;
        sumCO = sumCO/numb;
        sumFO = sumFO/numb;
        sumF1 = sumF1/numb;


        smeAcc = 0;
        for (Float val: smAcc) {
            smeAcc += Math.abs(val-sumAcc);
        }
        smeAcc = (float) Math.sqrt(smeAcc/smAcc.size());


        smeCO = 0;
        for (Float val: smCO) {
            smeCO += Math.pow(val-sumCO, 2);
        }
        smeCO = (float) Math.sqrt(smeCO/smCO.size());


        smeFO = 0;
        for (Float val: smFO) {
            smeFO += Math.abs(val-sumFO);
        }
        smeFO = (float) Math.sqrt(smeFO/smFO.size());


        smeF1 = 0;
        for (Float val: smF1) {
            smeF1 += Math.abs(val-sumF1);
        }
        smeF1 = (float) Math.sqrt(smeF1/smF1.size());

        siz = smCO.size();

        smAcc.clear();
        smCO.clear();
        smFO.clear();
        smF1.clear();
    }

    public ArrayList<Float> getResults() {
        Float[] ret = {sumCO, smeCO, (float) Math.pow(smeCO, 2), (float) (smeCO/Math.sqrt(siz))};
        return new ArrayList<>(Arrays.asList(ret));
    }

    public void print() {
        System.out.println();
//        System.out.println("avg accuracy: " + sumAcc);
//        System.out.println("accuracy mean error: " + smeAcc);
//        System.out.println();

        System.out.println("avg precision: " + sumCO);
        System.out.println("precision mean error: " + smeCO);
        System.out.println();

//        System.out.println("avg recall: " + sumFO);
//        System.out.println("recall mean error: " + smeFO);
//        System.out.println();
//
//        System.out.println("avg f1 score: " + sumF1);
//        System.out.println("f1 score mean error: " + smeF1);
//        System.out.println();

    }
}
