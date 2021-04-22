package model;

import consts.Consts;

import java.util.Vector;

public class Observation {
    private String delimeter = ",";

    public int id;
    public Vector<Integer> data;
    public int diagnosis;

    public Observation(String line) {
        String[] valStr = line.split(delimeter);
        this.id = Integer.valueOf(valStr[0]);
        this.diagnosis = Integer.valueOf(valStr[10]);
        int dataCap = 4;
        if (Consts.fullFeatureSet) {
            dataCap = 9;
        }
        this.data = new Vector(dataCap);
        for (int i = 1; i <= dataCap; i++) {
            this.data.add(Integer.valueOf(valStr[i]));
        }
    }
}
