/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author Alessandro
 */
public class ClearFile {
    public class Range {
        public int x;
        public int y;
        public Range(int x, int y){
            this.x=x;
            this.y=y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }     
        public int dif(){
            return y-x;
        }
    }
    public ArrayList generateCuts(File fileacc, File filesum){
        File dir=fileacc.getParentFile(); 
        String name = fileacc.getName().replaceFirst("[.][^.]+$", "");
        String namesum = filesum.getName().replaceFirst("[.][^.]+$", "");
        ArrayList<File> cuts=new ArrayList<File>();
        ArrayList cut=this.cut(fileacc);
        int k=0;
        
        //File NewCut=new File(dir.getAbsolutePath()+"\\"+name+"_cut1.csv");
        if(!cut.isEmpty()){
            try {
                CSVReader reader = new CSVReader(new FileReader(fileacc));
                CSVReader readersum = new CSVReader(new FileReader(filesum));
                String [] nextLine;
                String[] headerRecord = reader.readNext();
                String[] headerRecordSum = readersum.readNext();
                java.nio.file.Path newdir=Paths.get(dir.getAbsolutePath()+"\\FileCut");
                Files.createDirectories(newdir);
                for(int i=0; i<cut.size(); i++){

                    Writer writer = Files.newBufferedWriter(Paths.get(dir.getAbsolutePath()+"\\FileCut\\"+name+"_cut"+i+".csv"));
                    cuts.add(new File(dir.getAbsolutePath()+"\\"+name+"_cut"+i+".csv"));
                    CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END
                    );
                    csvWriter.writeNext(headerRecord);
                    while(k<(int)cut.get(i)/**100*/){
                        nextLine = reader.readNext();
                        k++;
                    }
                    System.out.println("Comincio a scrivere da "+ k);
                    i++;
                    while(k<(int)cut.get(i)/**100*/){
                        nextLine = reader.readNext();
                        k++;
                        csvWriter.writeNext(nextLine);
                        writer.flush();
                    }
                    System.out.println("Finisco a scrivere a "+ k);
                }
                k=0;
                for(int i=0; i<cut.size(); i++){
                    Writer writer = Files.newBufferedWriter(Paths.get(dir.getAbsolutePath()+"\\FileCut\\"+namesum+"_cut"+i+".csv"));
                    cuts.add(new File(dir.getAbsolutePath()+"\\"+namesum+"_cut"+i+".csv"));
                    CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END
                    );
                    csvWriter.writeNext(headerRecordSum);
                    while(k<(int)cut.get(i)/100){
                        nextLine = readersum.readNext();
                        k++;
                    }
                    System.out.println("Comincio a scrivere da "+ k);
                    i++;
                    while(k<(int)cut.get(i)/100){
                        nextLine = readersum.readNext();
                        k++;
                        csvWriter.writeNext(nextLine);
                        writer.flush();
                    }
                    System.out.println("Finisco a scrivere a "+ k);



                }
            } catch (CsvValidationException | IOException e) {
                    System.out.print("ECCEZIONE"+e);
                    e.printStackTrace();
            }
        }
        else{
            return null;
        }
        return cuts;
    }
    
    public ArrayList cut(File file){
        
        CSVReader reader;
        
        int window=11;
        int range=1;
        ArrayList varX=new ArrayList();
        ArrayList varY=new ArrayList();
        ArrayList varZ=new ArrayList();
        try {
            //Calcolo tutte le medie
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
            
            
            i=0;
            int j=0;
            while (j<size-window) {
                double mediax=StatUtils.mean(xVal,j,window);
                double mediay=StatUtils.mean(yVal,j,window);
                double mediaz=StatUtils.mean(zVal,j,window);
                xMeans[(int)i]=mediax;
                yMeans[(int)i]=mediay;
                zMeans[(int)i]=mediaz;
                j=j+range;
                i++;
            }
            range=50;
            window=300;
            j=0;
            i=0;
            while (j<xMeans.length-window) {
                double varx=StatUtils.populationVariance(xMeans,j,window);
                double vary=StatUtils.populationVariance(yMeans,j,window);
                double varz=StatUtils.populationVariance(zMeans,j,window);
                System.out.println("**"+i+ " Vsr X Y Z "+ varx+"*"+vary+"*"+varz+"NUM VAL "+xVal.length );
                varX.add(varx);
                varY.add(vary);
                varZ.add(varz);
                j=j+range;
                i++;
            }
            //(150+i*50)
        }catch (CsvException | IOException e) {
            System.out.print("ECCEZIONE"+e);
            e.printStackTrace();
        }
        
        
        
        // 3 cicli che mi prendono gli intervalli di riposo per ogni asse
        double i=0;
        ArrayList intX=new ArrayList();
        ArrayList intY=new ArrayList();
        ArrayList intZ=new ArrayList();
        double start=0,end=0;
        int limit=3;
        int count=1;
        int sizeX=varX.size();
        int sizeY=varY.size();
        int sizeZ=varZ.size();
        System.out.println("DURATA VAR X: "+sizeX+"DURATA Mean X: ");
        System.out.println("DURATA VAR Y: "+sizeY+"DURATA Mean Y: ");
        System.out.println("DURATA VAR Z: "+sizeZ+"DURATA Mean Z: ");
        
        while(i<varX.size()){
            start=i;
            while(i<varX.size() && (double)varX.get((int)i)<100){
                i++; 
            }
            end=i;
            if((end-start)>limit){
                intX.add(new Range(150+(int)start*50,150+(int)end*50));
            }
            i++;
            count++;
        }
        i=0;
        count=1;
        while(i<varY.size()){
            start=i;
            while(i<varY.size() && (double)varY.get((int)i)<100){
                i++; 
            }
            end=i;
            if((end-start)>limit){
                intY.add(new Range(155+(int)start*50,155+(int)end*50));
            }
            i++;
            count++;
        }
        i=0;
        count=1;
        while(i<varZ.size()){
            start=i;
                while(i<varZ.size() && (double)varZ.get((int)i)<100){
                    i++; 
                }
                end=i;
                if((end-start)>limit){
                    intZ.add(new Range(155+(int)start*50,155+(int)end*50));
                }
                i++;
                count++;
        }
        
        
        //print di controllo
        for(int k=0; k<intX.size(); k++){
            
            Range r=(Range)intX.get(k);
            System.out.println("Range X:"+ r.getX() +"--"+r.getY());
        }
        for(int k=0; k<intY.size(); k++){
            
            Range r=(Range)intY.get(k);
            System.out.println("Range Y:"+ r.getX() +"--"+r.getY());
        }
        for(int k=0; k<intZ.size(); k++){
            
            Range r=(Range)intZ.get(k);
            System.out.println("Range Z:"+ r.getX() +"--"+r.getY());
        }
        //**CAMBIATO
        // Tagli Partendo dal movimento con durata piu lunga, e prendo gli altri movimenti con durata simile
        // Funziona su un file contenenti esercizi dello stesso tipo o comunque con durata simile
        //MODIFICA: prendo tutti i tagli e confronto i tagli con i successivi per capire se sono simili; 
            //se si prendo l'ultimo e il precedente, e ricomincio;
            //altrimenti prendo come possibile nuovo esercizio l'ultimo e ricomincio dal seguente
        
        ArrayList Esercizi =new ArrayList<Range>();
        int k=0;
        ArrayList cuts=new ArrayList();
        
        while(!intX.isEmpty() && !intY.isEmpty() && !intY.isEmpty() ){
            Range rX=(Range)intX.get(0);
            Range rY=(Range)intY.get(0);
            Range rZ=(Range)intZ.get(0);
            System.out.println("PRESI INT:"+ "Range X:"+ rX.getX() +"--"+rX.getY());
            System.out.println("PRESI INT:"+ "Range Y:"+ rY.getX() +"--"+rY.getY());
            System.out.println("PRESI INT:"+ "Range Z:"+ rZ.getX() +"--"+rZ.getY());
            int min=Math.min(rX.getY(),rY.getY());
            int max=Math.max(rX.getX(),rY.getX());
            if(max>min){
                System.out.println("NON trovato "+ max +"  "+ min);
                if(min==rX.getY())
                    intX.remove(rX);
                else
                    intY.remove(rY);
            }
            else{
                Range es=new Range(max, min);
                min=Math.min(es.getY(),rZ.getY());
                max=Math.max(es.getX(),rZ.getX());
                if(max>min){
                    System.out.println("NON trovato "+ max +"  "+ min);
                    if(min==rX.getY())
                        intX.remove(rX);
                    else if(min==rY.getY())
                        intY.remove(rY);
                        else
                            intZ.remove(rZ);
                }
                else{
                    System.out.println("trovato"+ max +"  "+ min);
                    Range newEs=new Range(max,min);
                    if(min==rX.getY())
                        intX.remove(rX);
                    else if(min==rY.getY())
                        intY.remove(rY);
                        else
                            intZ.remove(rZ);
                    Esercizi.add(newEs);
                }
            }
        }
       
        //ho tutti i possibili esercizi in Esercizi
        for(k=0;k<Esercizi.size()-1;k++){
            Range es1=(Range)Esercizi.get(k);
            Range es2=(Range)Esercizi.get(k+1);
            cuts.add(es1.getY());
            cuts.add(es2.getX());
            
            System.out.println("ES "+k+" da+es.getY() "+es1.getY()+" " +es2.getX());
        }
        
        return cuts;
    }
    
        
}
