/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;

/**
 *
 * @author Alessandro
 */
public class VarChart {
    Scene scene;
    Scene Mediascene;
    Scene Medianascene;
    final LineChart<Number,Number> VarChart;
    final LineChart<Number,Number> MediaChart;
    final LineChart<Number,Number> MedianaChart;
    public VarChart(File file){
        //Var Chart
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        VarChart = new LineChart<Number,Number>(xAxis,yAxis);
        VarChart.setTitle("Variance of acceleration on 3 axis");
        System.out.println("OOOOOOOOOO");
        XYChart.Series seriesX = new XYChart.Series();
        seriesX.setName("Var on x");
        XYChart.Series seriesY = new XYChart.Series();
        seriesY.setName("Var on Y");
        XYChart.Series seriesZ = new XYChart.Series();
        seriesZ.setName("Var on Z");
        //mediaChart
        final NumberAxis MediaxAxis = new NumberAxis();
        final NumberAxis MediayAxis = new NumberAxis();
        MediaChart = new LineChart<Number,Number>(MediaxAxis,MediayAxis);
        MediaChart.setTitle("Mean of acceleration on 3 axis");
        System.out.println("OOOOOOOOOO");
        XYChart.Series MediaseriesX = new XYChart.Series();
        MediaseriesX.setName("Mean on x");
        XYChart.Series MediaseriesY = new XYChart.Series();
        MediaseriesY.setName("Mean on Y");
        XYChart.Series MediaseriesZ = new XYChart.Series();
        MediaseriesZ.setName("Mean on Z");
        //Mediana chart
        final NumberAxis MedianxAxis = new NumberAxis();
        final NumberAxis MedianyAxis = new NumberAxis();
        MedianaChart = new LineChart<Number,Number>(MedianxAxis,MedianyAxis);
        MedianaChart.setTitle("Median of acceleration on 3 axis");
        System.out.println("OOOOOOOOOO");
        XYChart.Series MedianaseriesX = new XYChart.Series();
        MedianaseriesX.setName("Median on x");
        XYChart.Series MedianaseriesY = new XYChart.Series();
        MedianaseriesY.setName("Median on Y");
        XYChart.Series MedianaseriesZ = new XYChart.Series();
        MedianaseriesZ.setName("Median on Z");
        
        
        CSVReader reader;
        int window=11;
        int range=1;
        double fix=0;//window/range/2;
        try {
            reader = new CSVReader(new FileReader(file));
            List<String[]> list=reader.readAll();
            int size= list.size()-1;

            double [] xVal=new double[size];
            double [] yVal=new double[size];
            double [] zVal=new double[size];
            double i=0;
            
            Iterator it =list.iterator();
            it.next();
            while(it.hasNext()) {
                String[] line =(String [])it.next();
                //System.out.println(line[1]+" "+line[2]+" "+line[3]+" ");
                xVal[(int)i]=Double.valueOf(line[1]);
                yVal[(int)i]=Double.valueOf(line[2]);
                zVal[(int)i]=Double.valueOf(line[3]);
                i++;   
            }
            int meansize=((size-window)/range)+1;
            double [] xMeans=new double[meansize];
            double [] yMeans=new double[meansize];
            double [] zMeans=new double[meansize];
          
            
            int j=0;
            i=0;
            //mediana e media
            Median m=new Median();
            while (j<size-window) {
                
                m.setData(xVal,j,window);
                MedianaseriesX.getData().add(new XYChart.Data(i/100-fix,m.evaluate()));
                m.setData(yVal,j,window);
                MedianaseriesY.getData().add(new XYChart.Data(i/100-fix,m.evaluate()));
                m.setData(zVal,j,window);
                MedianaseriesZ.getData().add(new XYChart.Data(i/100-fix,m.evaluate()));
                //media
                double mediax=StatUtils.mean(xVal,j,window);
                double mediay=StatUtils.mean(yVal,j,window);
                double mediaz=StatUtils.mean(zVal,j,window);
                xMeans[(int)i]=mediax;
                yMeans[(int)i]=mediay;
                zMeans[(int)i]=mediaz;
                MediaseriesX.getData().add(new XYChart.Data(i/100-fix,mediax));
                MediaseriesY.getData().add(new XYChart.Data(i/100-fix,mediay));
                MediaseriesZ.getData().add(new XYChart.Data(i/100-fix,mediaz));
                j=j+range;
                i++;
            }
            System.out.println("Numero Punti mean (tot-11) "+meansize );
            //varianze
            i=0;
            j=0;
            range=50;
            window=300;
            fix=0;
            while (j<xMeans.length-window) {
                double varx=StatUtils.populationVariance(xMeans,j,window);
                double vary=StatUtils.populationVariance(yMeans,j,window);
                double varz=StatUtils.populationVariance(zMeans,j,window);
                System.out.println("**"+i+ " Vsr X Y Z "+ varx+"*"+vary+"*"+varz+"NUM VAL "+xVal.length );
                seriesX.getData().add(new XYChart.Data((150+i*50)/100,varx));
                seriesY.getData().add(new XYChart.Data((150+i*50)/100,vary));
                seriesZ.getData().add(new XYChart.Data((150+i*50)/100,varz));
                j=j+range;
                i++;
            }
            System.out.println("Numero Punti "+i+"PUNTI TOTALI"+ i*range );
            
            
            
        } catch ( IOException | CsvException e) {
            System.out.print("ECCEZIONE"+e);
            e.printStackTrace();
        }
        
        scene  = new Scene(VarChart,800,600);
        VarChart.getData().addAll(seriesX,seriesY,seriesZ);
        VarChart.setCreateSymbols(false);
        seriesX.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        seriesY.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        seriesZ.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        
        Mediascene  = new Scene(MediaChart,800,600);
        MediaChart.getData().addAll(MediaseriesX,MediaseriesY,MediaseriesZ);
        MediaChart.setCreateSymbols(false);
        MediaseriesX.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        MediaseriesY.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        MediaseriesZ.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        
        Medianascene  = new Scene(MedianaChart,800,600);
        MedianaChart.getData().addAll(MedianaseriesX,MedianaseriesY,MedianaseriesZ);
        MedianaChart.setCreateSymbols(false);
        MedianaseriesX.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        MedianaseriesY.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        MedianaseriesZ.getNode().setStyle("-fx-stroke-width: 1px; -fx-effect: null;");
        
       
    }
    public Scene getScene(){
        return scene;
    }
    public Scene getMedianaScene(){
        return Medianascene;
    }
    public Scene getMediaScene(){
        return Mediascene;
    }
    public LineChart getVarChart(){
        return VarChart;
    }
    public LineChart getMediaChart(){
        return MediaChart;
    }
    public LineChart getMedianaChart(){
        return MedianaChart;
    }
}

