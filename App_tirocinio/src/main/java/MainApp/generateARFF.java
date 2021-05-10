/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Alessandro
 */
public class generateARFF {
    File dir;
    public generateARFF(File dir){
        this.dir= dir;
        File newcsv=new File(dir.getAbsolutePath()+"\\fileAARF"+".csv");
        File [] files=dir.listFiles();
        
        //crea file csv e inserisci header meanX, meanY, ...
        String [] headerRecord= {"meanX", "meanY","meanZ","meanHB","meanBR","MeanPos","Xzero","Yzero","Zzero","VarX","VarY","VarZ","Time","Genere","Class"};
        try{
            Writer writer = Files.newBufferedWriter(Paths.get(dir.getAbsolutePath()+"\\fileAARF"+".csv"));
            
            CSVWriter csvWriter = new CSVWriter(writer,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
            );
            csvWriter.writeNext(headerRecord);
        
        
            for(File file: files){
                File[] accs = file.listFiles(new FilenameFilter() {		
                    //apply a filter
                    @Override
                    public boolean accept(File dir, String name) {
                        boolean result;
                        if(name.contains("Accel")&& name.contains(".csv")){
                            result=true;
                        }
                        else{
                            result=false;
                        }
                        return result;
                    }
                });
                File[] sums = file.listFiles(new FilenameFilter() {		
                    //apply a filter
                    @Override
                    public boolean accept(File dir, String name) {
                        boolean result;
                        if(name.contains("Summary") && name.contains(".csv") ){
                            result=true;
                        }
                        else{
                            result=false;
                        }
                        return result;
                    }
                });
                if(accs.length!=0){
                    for(int i=0; i< accs.length; i++){
                        CalcFeatures cf=new CalcFeatures(accs[i],sums[i],file.getName());
                        String [] line=cf.getFeatures();
                        csvWriter.writeNext(line);
                        writer.flush();
                    }
                }
            }
            writer.close();
            CSVLoader loader = new CSVLoader();
            loader.setSource(newcsv);
            Instances data = loader.getDataSet();

            // save ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(dir.getAbsolutePath()+"\\fileAARF"+".arff"));
            //saver.setDestination(new File(dir.getAbsolutePath()+"\\fileAARF"+".arff"));
            saver.writeBatch();
        }catch(IOException e){
            
        }
    }   
}
