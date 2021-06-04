package results;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ResultsSaver {
    private static String path = "results/";
    File file;
    String filePath;

    public ResultsSaver(String name) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println();
        String date = "" + dtf.format(now)
                .replace(".", "-")
                .replace(":", "-")
                .replace("/", "-");
        filePath = path + name + "_" + date + ".csv";
        file = new File(filePath);
    }

    public void saveRecord(String x, ArrayList<Float> ys) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(x);
        for (Float val: ys) {
            strings.add(new DecimalFormat("#.##").format(val));
        }
        String toWrite = String.join(", ", strings);
        System.out.println("toWrite");
        System.out.println(toWrite);
        try {
            FileWriter myWriter = new FileWriter(filePath, true);
            myWriter.append(toWrite + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
