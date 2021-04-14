package model;

import java.util.Vector;

public class Observation {
    private String delimeter = ",";

    public int id;
    public Vector<Integer> data;
    public int diagnosis;

    public Observation(String line) {
        String[] valStr = line.split(delimeter);
        this.id = Integer.valueOf(valStr[0]);
        this.diagnosis = Integer.valueOf(valStr[10]);;
        this.data = new Vector(9);
        for (int i = 1; i <= 9; i++) {
            this.data.add(Integer.valueOf(valStr[i]));
        }
    }
}
