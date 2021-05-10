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
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author Alessandro
 */
public class CalcFeatures {
    //aggiungi time, var, passaggi per 0, genere(?)
    File acc;
    File sum;
    String es;
    String[] line;
    int Xzero=0;
    int Yzero=0;
    int Zzero=0;
    
    public CalcFeatures(File a, File s,String es){
        this.acc=a;
        this.sum=s;
        CSVReader reader;
        double time=0;
        double meanX=0;
        double meanY=0;
        double meanZ=0;
        double varX=0;
        double varY=0;
        double varZ=0;
        double meanHB=0;
        double meanBR=0;
        double meanPos=0;
        
        try{
                reader = new CSVReader(new FileReader(acc));
                
                List<String[]> list=reader.readAll();
                int size= list.size()-1;
                
                double [] xVal=new double[size];
                double [] yVal=new double[size];
                double [] zVal=new double[size];
                
                
                int i=0;
                Iterator it =list.iterator();
                it.next();
                while(it.hasNext()) {
                    String[] line =(String [])it.next();
                    //System.out.println(line[1]+" "+line[2]+" "+line[3]+" ");
                    xVal[i]=Double.valueOf(line[1]);
                    yVal[i]=Double.valueOf(line[2]);
                    zVal[i]=Double.valueOf(line[3]);
                    i++;   
                }
                
                
                time=(double)i/100;
                meanX=StatUtils.mean(xVal);
                meanY=StatUtils.mean(yVal);
                meanZ=StatUtils.mean(zVal);
                System.out.println("OOOOO");
                calcZeros();
                varX=StatUtils.populationVariance(xVal);
                varY=StatUtils.populationVariance(yVal);
                varZ=StatUtils.populationVariance(zVal);
                System.out.println("X: "+meanX+" Y: "+meanY+" Z: "+meanZ+" ");
                
        } catch (CsvException | IOException e) {
                System.out.print("ECCEZIONE"+e);
                e.printStackTrace();
        }
        try{
            reader = new CSVReader(new FileReader(sum));

            List<String[]> list=reader.readAll();
            int size= list.size()-1;
            double [] HBVal=new double[size];
            double [] BRVal=new double[size];
            double [] PosVal=new double[size];
            int i=0;
            Iterator it =list.iterator();
            it.next();
            while(it.hasNext()) {
                String[] line =(String [])it.next();
                //System.out.println(line[1]+" "+line[2]+" "+line[3]+" ");
                HBVal[i]=Double.valueOf(line[1]);
                BRVal[i]=Double.valueOf(line[2]);
                PosVal[i]=Double.valueOf(line[4]);
                i++;

            }
            meanHB=StatUtils.mean(HBVal);
            meanBR=StatUtils.mean(BRVal);
            meanPos=StatUtils.mean(PosVal);
            System.out.println("HB: "+meanHB+" BR: "+meanBR+" Pos: "+meanPos);
                
        } catch (CsvException | IOException e) {
            System.out.print("ECCEZIONE"+e);
            e.printStackTrace();
        }
        String[] result={String.valueOf(meanX),
                        String.valueOf(meanY),
                        String.valueOf(meanZ),
                        String.valueOf(meanHB),
                        String.valueOf(meanBR),
                        String.valueOf(meanPos),
                        String.valueOf(Xzero),
                        String.valueOf(Yzero),
                        String.valueOf(Zzero),
                        String.valueOf(varX),
                        String.valueOf(varY),
                        String.valueOf(varZ),
                        String.valueOf(time),
                        "Uomo",
                        es};
        for(int j=0; j<result.length;j++){
            System.out.print(result[j]+"  ");
        }
        System.out.println();
        this.line=result;
    }
    public void calcZeros(){
        CSVReader reader;
        int window=11;
        int range=1;
        ArrayList meansX=new ArrayList();
        ArrayList meansY=new ArrayList();
        ArrayList meansZ=new ArrayList();
        double meanX=0;
        double meanY=0;
        double meanZ=0;
        double fix=0;//window/range/2;
        try {
            reader = new CSVReader(new FileReader(acc));
            
            String [] nextLine= reader.readNext();
            
            double [] Xdata= new double[window];            
            double [] Ydata= new double[window];
            double [] Zdata= new double[window];
          
            double i=0;
            int j=0;
            //calcola primo punto
            //riempire la finestra la prima volta
            if(nextLine!=null) {
                while (((nextLine = reader.readNext())!= null && j<window)) {
                        double x=Double.valueOf(nextLine[1]);
                        double y=Double.valueOf(nextLine[2]);
                        double z=Double.valueOf(nextLine[3]);
                        
                        Xdata[j]=x;
                        Ydata[j]=y;
                        Zdata[j]=z;					
                        j++;
                        i++;
                }
            }
            //calcolo il valore medio del nuovo grafico di medie
            meanX=StatUtils.mean(Xdata);
            meanY=StatUtils.mean(Ydata);
            meanZ=StatUtils.mean(Zdata);
            meansX.add(meanX);
            meansY.add(meanY);
            meansZ.add(meanZ);
            
            //calcola i restanti
            while(nextLine!=null) {
                j=0;
                while (((nextLine = reader.readNext())!= null && j<range)) {
                        double x=Double.valueOf(nextLine[1]);
                        double y=Double.valueOf(nextLine[2]);
                        double z=Double.valueOf(nextLine[3]);
                        Xdata[(int)(i%window)]=x;
                        Ydata[(int)(i%window)]=y;
                        Zdata[(int)(i%window)]=z;					
                        j++;
                        i++;
                }
                double temp;
                temp=StatUtils.mean(Xdata);
                meanX=meanX+temp;
                meansX.add(temp);
                temp=StatUtils.mean(Ydata);
                meanY=meanY+temp;
                meansY.add(temp);
                temp=StatUtils.mean(Zdata);
                meanZ=meanZ+temp;
                meansZ.add(temp);
                
            }
            meanX=meanX/i;
            meanY=meanY/i;
            meanZ=meanZ/i;
        }catch(IOException | CsvValidationException e){
            
        }
        for (int j = 0; j < meansX.size(); j++) {    
            meansX.set(j, (double)meansX.get(j)-meanX);
            meansY.set(j, (double)meansY.get(j)-meanY);
            meansZ.set(j, (double)meansZ.get(j)-meanZ);
            if(j>0){
                if((double)meansX.get(j)*(double)meansX.get(j-1)<0) Xzero++;
                if((double)meansY.get(j)*(double)meansY.get(j-1)<0) Yzero++;
                if((double)meansZ.get(j)*(double)meansZ.get(j-1)<0) Zzero++;
            }
        }
        System.out.println("FINE FUNZ");

    }
    public String[] getFeatures(){
        return line;
    }
         
}
