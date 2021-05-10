/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Alessandro
 */
public class HrBrPosChart {
    Scene PostureScene;
    final LineChart<Number,Number> PostureChart;
    Scene HeartbeatScene;
    final LineChart<Number,Number> HeartbeatChart;
    Scene BreathrateScene;
    final LineChart<Number,Number> BreathrateChart;
    //1 HR,2 BR,4 Pos
    public HrBrPosChart(File file) {
        
        final NumberAxis PxAxis = new NumberAxis();
        final NumberAxis HxAxis = new NumberAxis();
        final NumberAxis BxAxis = new NumberAxis();
        final NumberAxis PyAxis = new NumberAxis();
        PyAxis.setLabel("Degrees (°)");
        PxAxis.setLabel("Time (s)");
        HxAxis.setLabel("Time (s)");
        BxAxis.setLabel("Time (s)");
        PostureChart = new LineChart<Number,Number>(PxAxis,PyAxis);
        PostureChart.setTitle("Posture Chart");
        final NumberAxis HyAxis = new NumberAxis();
        HyAxis.setLabel("Beats per minute");
        HeartbeatChart = new LineChart<Number,Number>(HxAxis,HyAxis);  
        HeartbeatChart.setTitle("Heart beat chart");
        final NumberAxis ByAxis = new NumberAxis();
        ByAxis.setLabel("Breaths per minutes");
        BreathrateChart = new LineChart<Number,Number>(BxAxis,ByAxis);  
        BreathrateChart.setTitle("Breath rate chart");
        XYChart.Series seriesP = new XYChart.Series();
        seriesP.setName("Posture degrees");
        XYChart.Series seriesH = new XYChart.Series();
        seriesH.setName("Heart beats");
        XYChart.Series seriesB = new XYChart.Series();
        seriesB.setName("Breath rate");
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            String [] nextLine;
            double i=0;
            while (((nextLine = reader.readNext())!= null)) {
                    if(i!=0) {
                            double x=Double.valueOf(nextLine[4]);
                            seriesP.getData().add(new XYChart.Data(i,x));
                            double y=Double.valueOf(nextLine[1]);
                            seriesH.getData().add(new XYChart.Data(i,y));
                            double z=Double.valueOf(nextLine[2]);
                            seriesB.getData().add(new XYChart.Data(i,z));
                            //System.out.println(x+" "+y+" "+z);
                    }
                    i++;
                    
            }
        } catch (CsvValidationException | IOException e) {
                System.out.print("ECCEZIONE"+e);
                e.printStackTrace();
        }
        PostureScene  = new Scene(PostureChart,800,600);
        PostureChart.getData().add(seriesP);
        HeartbeatScene  = new Scene(HeartbeatChart,800,600);
        HeartbeatChart.getData().add(seriesH);
        BreathrateScene  = new Scene(BreathrateChart,800,600);
        BreathrateChart.getData().add(seriesB);
    }

    public LineChart<Number, Number> getBreathrateChart() {
        return BreathrateChart;
    }

    public Scene getBreathrateScene() {
        return BreathrateScene;
    }

    public LineChart<Number, Number> getHeartbeatChart() {
        return HeartbeatChart;
    }

    public Scene getHeartbeatScene() {
        return HeartbeatScene;
    }

    public LineChart<Number, Number> getPostureChart() {
        return PostureChart;
    }

    public Scene getPostureScene() {
        return PostureScene;
    }
    
    
}
