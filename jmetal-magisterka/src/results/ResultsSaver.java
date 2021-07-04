package results;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResultsSaver {
    private static String pathAll = "results/all/";
    private static String pathLast = "results/last/";

    File fileAll;
    File fileLast;
    String filePathAll;
    String filePathLast;

    public ResultsSaver(String name) {
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

    public void saveRecord(String x, ArrayList<Float> ys) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(x);
        for (Float val: ys) {
            strings.add(new DecimalFormat("#.##").format(val).replace(",", "."));
        }
        String toWrite = String.join(", ", strings);
//        System.out.println("toWrite");
//        System.out.println(toWrite);
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
    }
}
