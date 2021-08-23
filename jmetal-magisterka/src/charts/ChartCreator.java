package charts;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ChartCreator extends Application {

    @Override public void start(Stage primaryStage) {
        Label label = new Label("This is the main GUI");
        VBox root = new VBox();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main");
        primaryStage.show();
        primaryStage.close();
        System.out.println("duppa");
    }

   public static void nextStage(int type, String path, String name, String xAxisName, String yAxisName, String y2AxisName, ArrayList xData, ArrayList yData, ArrayList y2Data) {
       System.out.println("next");
       Stage stage = new Stage();
        if (type == 0) {
            stage.setTitle(name);
            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(xAxisName);


            final NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(yAxisName);
            final BarChart<String, Number> lineChart =
                    new BarChart<String, Number>(xAxis, yAxis);

            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < xData.size(); i++) {
                series.getData().add(new XYChart.Data(xData.get(i), yData.get(i)));
            }

            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().addAll(series);
            stage.setScene(scene);
            stage.show();
            WritableImage image = scene.getRoot().snapshot(new SnapshotParameters(), null);

            File file = new File(path + name + ".png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (Exception s) {
            }
            //stage.close();
        } else if (type == 1) {
            stage.setTitle(name);
            final NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(xAxisName);


            final NumberAxis yAxis = new NumberAxis();
            final LineChart<Number, Number> lineChart =
                    new LineChart<Number, Number>(xAxis, yAxis);

            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < xData.size(); i++) {
                series.getData().add(new XYChart.Data(xData.get(i), ((Float) yData.get(i))*100));
            }

            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().addAll(series);
            stage.setScene(scene);
            stage.show();
            WritableImage image = scene.getRoot().snapshot(new SnapshotParameters(), null);

            File file = new File(path + name + ".png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (Exception s) {
            }
            //stage.close();
        } else if (type == 2) {
            stage.setTitle(name);
            final NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(xAxisName);


            final NumberAxis yAxis = new NumberAxis();
            final BubbleChart<Number, Number> lineChart =
                    new BubbleChart<Number, Number>(xAxis, yAxis);

            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < xData.size(); i++) {
                series.getData().add(new XYChart.Data(xData.get(i), ((Float) yData.get(i))*100, ((Float) y2Data.get(i))*50));
            }

            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().addAll(series);
            stage.setScene(scene);
            stage.show();
            WritableImage image = scene.getRoot().snapshot(new SnapshotParameters(), null);

            File file = new File(path + name + ".png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (Exception s) {
            }
            //stage.close();
        }
       stage.close();
    }
    public static void launchCharts() {
        Platform.setImplicitExit(false);
        launch();
    }

    public static void saveChart0(String path, String name, String xAxisName, String yAxisName, ArrayList xData, ArrayList yData) {
        Platform.runLater(new Thread(() -> nextStage(0, path, name, xAxisName, yAxisName, null, xData, yData, null)));
    }

    public static void saveChart1(String path, String name, String xAxisName, String yAxisName, ArrayList xData, ArrayList yData) {
        Platform.runLater(new Thread(() -> nextStage(1, path, name, xAxisName, yAxisName, null, xData, yData, null)));
    }

    public static void saveChart2(String path, String name, String xAxisName, String yAxisName, String y2AxisName, ArrayList xData, ArrayList yData, ArrayList y2Data) {
        Platform.runLater(new Thread(() -> nextStage(2, path, name, xAxisName, yAxisName, y2AxisName, xData, yData, y2Data)));
    }
}