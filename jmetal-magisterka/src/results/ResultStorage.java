package results;

import java.util.ArrayList;

public class ResultStorage {
    float sumAcc = 0;
    float minAcc = 1;
    float maxAcc = 0;

    float sumCO = 0;
    float minCO = 1;
    float maxCO = 0;

    float sumFO = 0;
    float minFO = 1;
    float maxFO = 0;

    int numb;

    public ResultStorage(int numb) {
        this.numb = numb;
    }

    public void add(ArrayList<Float> result) {
        float acc = (result.get(0) + result.get(1)) / (result.get(0) + result.get(1) + result.get(2) + result.get(3));
        float co = (result.get(0)) / (result.get(2) + result.get(0));
        float fo = (result.get(2)) / (result.get(2) + result.get(1));
        if (minAcc > acc) {
            minAcc = acc;
        }
        if (maxAcc < acc) {
            maxAcc = acc;
        }
        sumAcc += acc;
        if (minCO > co) {
            minCO = co;
        }
        if (maxCO < co) {
            maxCO = co;
        }
        sumCO += co;

        if (minFO > fo) {
            minFO = fo;
        }
        if (maxFO < fo) {
            maxFO = fo;
        }
        sumFO += fo;
    }

    public void finish() {
        sumAcc = sumAcc/numb;
        sumCO = sumCO/numb;
        sumFO = sumFO/numb;
    }

    public void print() {
        System.out.println("minAcc: " + minAcc);
        System.out.println("avgAcc: " + sumAcc);
        System.out.println("maxAcc: " + maxAcc);
        System.out.println();

        System.out.println("minCO: " + minCO);
        System.out.println("avgCO: " + sumCO);
        System.out.println("maxCO: " + maxCO);
        System.out.println();

        System.out.println("minFO: " + minFO);
        System.out.println("avgFO: " + sumFO);
        System.out.println("maxFO: " + maxFO);
    }
}
