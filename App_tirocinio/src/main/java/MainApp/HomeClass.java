/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Alessandro
 */
public class HomeClass {
    public File[] files;
    File acc;
    File sum;
    File dir;
    ChartClass CC;
    public Label notify;
    Button ClearFile;
    Button genARFF;
    Button ClearDir;
    Button generaGrafici;
    public Scene scene;
    public HomeClass(Stage stage){
        GridPane  root = new GridPane();
        notify=new Label("Non hai ancora selezionato alcun file");
        var label = new Label("Seleziona i file Accel.csv e Summary.csv dal tuo dispositivo \noppure una cartella contenente piu' file dello stesso tipo");
        var labelDir = new Label("");
        
        Button SearchDir=new Button("Select Directory");
        SearchDir.setOnAction((ActionEvent event) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            Stage sta = (Stage)stage.getScene().getWindow();
            File selectedDirectory = directoryChooser.showDialog(sta);
            if(selectedDirectory!=null){
              dir=selectedDirectory;
                ClearDir.setVisible(true);
                genARFF.setVisible(true);
                ClearFile.setVisible(false);
                generaGrafici.setVisible(false);
                notify.setText("Directory selezionata: "+dir.getName());  
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Non hai selezionato alcuna directory");
                alert.showAndWait();
            }
            
            
        });
                
        ClearDir = new Button("Clear Directory");
        ClearDir.setOnAction((ActionEvent event) -> {

            if(dir == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("I file selezionati sono errati o insufficienti");
                alert.showAndWait();
                
            }else{
                
                File[] accs = dir.listFiles(new FilenameFilter() {		
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
                File[] sums = dir.listFiles(new FilenameFilter() {		
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
                int c=0;
                if(accs.length==0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Nella cartella non ci sono file da ripulire");
                    alert.showAndWait();
                }
                
                else{
                    ClearFile CF=new ClearFile();
                    for(int i=0; i< accs.length; i++){
                        ArrayList esito=CF.generateCuts(accs[i],sums[i]);
                         if(esito!=null)c++;
                    }
                    if(c==0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Non sono stati individuati tagli per i file nella cartella");
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("I file sono stati tagliati correttamente");
                        alert.showAndWait();
                    }          
                }
            }
        });
        ClearFile = new Button("Clear File");
        ClearFile.setOnAction((ActionEvent event) -> {
                ClearFile CF=new ClearFile();
                CF.generateCuts(acc,sum);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("I file sono stati tagliati correttamente");
                alert.showAndWait();
        });
        generaGrafici = new Button("Ottieni grafici");
        generaGrafici.setOnAction((ActionEvent event) -> {
                CC=new ChartClass(stage,files, scene);
                Scene Charts= CC.getScene();
                stage.setScene(Charts);
        });
        var Search = new Button("Select File");
        Search.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File Dialog");
            FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("file csv","*.csv");
            fileChooser.getExtensionFilters().add(fileExtensions);
            Stage sta = (Stage)stage.getScene().getWindow();
            List<File> list=fileChooser.showOpenMultipleDialog(sta);
            File[] files=new File[2];
            int i=0;
            if(list!=null)
                for(File file:list){
                    files[i]=file;
                    System.out.println(file.getAbsolutePath());
                    i++;
                }
            if(i==2 && ((files[0].getName().contains("Accel") && files[1].getName().contains("Summary")) || (files[1].getName().contains("Accel") && files[0].getName().contains("Summary")))){
                this.files=files;
                notify.setText("File selezionati:\n"+files[0].getName()+"\n"+files[1].getName());
                ClearFile.setVisible(true);
                generaGrafici.setVisible(true);
                if(files[0].getName().contains("Accel")){
                    this.acc=files[0];
                    this.sum=files[1];
                }
                else{
                    this.acc=files[1];
                    this.sum=files[0];
                }
                System.out.println(files[0].getName().replaceFirst("[.][^.]+$", ""));
                System.out.println(files[1].getName().replaceFirst("[.][^.]+$", ""));
                ClearDir.setVisible(false);
                genARFF.setVisible(false);
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("I file selezionati sono errati o insufficienti");
                alert.showAndWait();
                                
            }
        });
        //test
        genARFF = new Button("genera Arff");
        genARFF.setOnAction((ActionEvent event) -> {
            generateARFF GA=new generateARFF(dir);
        });
        root.setHgap(8);
        root.setVgap(8);
        root.setAlignment(Pos.BASELINE_LEFT);
        root.setPadding(new Insets(50));
        root.add(Search, 0, 1);
        root.add(label, 2, 0,7,1);
        root.add(labelDir, 1, 1, 3, 1);
        root.add(notify, 0, 5,3, 5);
        root.add(genARFF,1,10);
        root.add(SearchDir, 0, 0);
        root.add(ClearFile, 0, 10);
        root.add(ClearDir, 0, 10);
        root.add(generaGrafici, 5, 10);
        ClearFile.setVisible(false);
        generaGrafici.setVisible(false);
        ClearDir.setVisible(false);
        genARFF.setVisible(false);
        var scene = new Scene(root, 600, 400);
        this.scene=scene;
    }
    public Scene getScene(){
        return scene;
    }
}
