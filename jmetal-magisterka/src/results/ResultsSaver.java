package results;

import charts.ChartCreator;
import consts.Consts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Function;

public class ResultsSaver {
    private static String pathAll = "results/all/";
    private static String pathLast = "results/last/";
    private static String pathCharts = "results/charts/";

    File fileAll;
    File fileLast;
    String filePathAll;
    String filePathLast;
    String myName;

    public static void main(String[] args) throws FileNotFoundException {

    }

    public ResultsSaver(String name) {
        myName = name;
        type = -1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println();
        String date = "" + dtf.format(now)
                .replace(".", "-")
                .replace(":", "-")
                .replace("/", "-");
        filePathAll = pathAll + name + "_" + date + ".csv";;
        filePathLast = pathLast + name + ".csv";
        fileAll = new File(filePathAll);
        fileLast = new File(filePathLast);

        try {
            FileWriter myWriter = new FileWriter(filePathLast, false);
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultsSaver(String name, int type) {
        myName = name;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println();
        String date = "" + dtf.format(now)
                .replace(".", "-")
                .replace(":", "-")
                .replace("/", "-");
        filePathAll = pathAll + name + "_" + date + ".csv";;
        filePathLast = pathLast + name + ".csv";
        fileAll = new File(filePathAll);
        fileLast = new File(filePathLast);

        try {
            FileWriter myWriter = new FileWriter(filePathLast, false);
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.type = type;
        if (type == 0) {
            xDataString = new ArrayList<String>();
            yData = new ArrayList<Float>();
        } else if (type == 1) {
            xDataNumber = new ArrayList<Float>();
            yData = new ArrayList<Float>();
        } else if(type == 2) {
            xDataNumber = new ArrayList<Float>();
            yData = new ArrayList<Float>();
            y2Data = new ArrayList<Float>();
        }

    }

    public int type;
    public ArrayList xDataString;
    public ArrayList xDataNumber;
    public ArrayList yData;
    public ArrayList y2Data;

    public void saveRecord(String x, ArrayList<Float> ys) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(x);
        for (Float val: ys) {
            strings.add(new DecimalFormat("#.##").format(val).replace(",", "."));
        }
        String toWrite = String.join(", ", strings);
        try {
            FileWriter myWriter = new FileWriter(filePathAll, true);
            myWriter.append(toWrite + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filePathLast, true);
            myWriter.append(toWrite + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (type == 0) {
            xDataString.add(x);
            yData.add(ys.get(0));
        } else if (type == 1) {
            xDataNumber.add(Float.parseFloat(x));
            yData.add(ys.get(0));
        } else if(type == 2) {
            xDataNumber.add(Float.parseFloat(x));
            yData.add(ys.get(0));
            y2Data.add(ys.get(1));
        }
    }
    //słupkowy z nazwami
    public void saveChart0(String name, String xAxisName, String yAxisName) {
        ChartCreator.saveChart0(pathCharts, myName, xAxisName, yAxisName, xDataString, yData);
    }
    // liniowy x-y
    public void saveChart1(String name, String xAxisName, String yAxisName) {
        ChartCreator.saveChart1(pathCharts, myName, xAxisName, yAxisName, xDataNumber, yData);
    }
    // bombelkowy x-y+y_err
    public void saveChart2(String name, String xAxisName, String yAxisName, String y2AxisName) {
        ChartCreator.saveChart2(pathCharts, myName, xAxisName, yAxisName, y2AxisName, xDataNumber, yData, y2Data);
    }
}
