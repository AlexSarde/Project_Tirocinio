/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Alessandro
 */
public class ChartClass {
    public Scene scene;
    File acc;
    File sum;
    public ChartClass(Stage stage, File[] files, Scene homeScene){
        
        //CREA tutti i grafici
        if(files[0].getName().contains("Accel")){
            this.acc=files[0];
            this.sum=files[1];
        }
        else{
            this.acc=files[1];
            this.sum=files[0];
        }
        String name = acc.getName().replaceFirst("[.][^.]+$", "");
        //1
        AccChart accs=new AccChart(acc);
        System.out.println("ACC fatto");
        //2
        HrBrPosChart HBP=new HrBrPosChart(sum);
        System.out.println("HRBRPos fatto");
        //3 
        VarChart vars=new VarChart(acc);
        System.out.println("VAR fatto");
        GridPane  root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.BASELINE_LEFT);
        Button AccChart = new Button("Accelerazione sugli assi");
        AccChart.setOnAction((ActionEvent event) -> {
            Stage sta = new Stage();
            sta.setScene(accs.getScene());
            sta.show();
        });
        Button HBChart = new Button("Battiti al minuto");
        HBChart.setOnAction((ActionEvent event) -> {
            Stage sta = new Stage();
            sta.setScene(HBP.getHeartbeatScene());
            sta.show();
        });
        Button BRChart = new Button("Respiri al minuto");
        BRChart.setOnAction((ActionEvent event) -> {
            Stage sta = new Stage();
            sta.setScene(HBP.getBreathrateScene());
            sta.show();
        });
        Button Posture = new Button("Grafico Postura");
        Posture.setOnAction((ActionEvent event) -> {
            Stage sta = new Stage();
            sta.setScene(HBP.getPostureScene());
            sta.show();
        });
        Button varChart = new Button("Grafici Varianze");
        varChart.setOnAction((ActionEvent event) -> {  
            Stage sta = new Stage();
            sta.setScene(vars.getScene());
            sta.show();
        });
        Button MediaChart = new Button("Grafici Media Mobile");
        MediaChart.setOnAction((ActionEvent event) -> {  
            Stage sta = new Stage();
            sta.setScene(vars.getMediaScene());
            sta.show();
        });
        Button MedianaChart = new Button("Grafici Mediana Mobile");
        MedianaChart.setOnAction((ActionEvent event) -> {  
            Stage sta = new Stage();
            sta.setScene(vars.getMedianaScene());
            sta.show();
        });
        Button Indietro = new Button("Torna alla selezione file");
        Indietro.setOnAction((ActionEvent event) -> {  
            stage.setScene(homeScene);
        });
        Button Salva = new Button("Salva grafici in una cartella");
        Salva.setOnAction((ActionEvent event) -> {   
            System.out.print(acc.getParent());
            String dirpath=acc.getParent();
            try {
                java.nio.file.Path newdir=Paths.get(dirpath+"\\"+name+"_Charts\\");
                Files.createDirectories(newdir);
                Scene ch=accs.getScene();
                Scene chBR= HBP.getBreathrateScene();
                Scene chHR= HBP.getHeartbeatScene();
                Scene chPO= HBP.getPostureScene();
                Scene chVar= vars.getScene();
                WritableImage image1 = ch.snapshot(null);
                WritableImage image2 = chBR.snapshot(null);
                WritableImage image3 = chHR.snapshot(null);
                WritableImage image4 = chPO.snapshot(null);
                WritableImage image5 = chVar.snapshot(null);
                File file1 = new File(dirpath+"\\"+name+"_Charts\\"+"\\ACC3axisCH.png");
                File file2 = new File(dirpath+"\\"+name+"_Charts\\"+"\\BreathrateCH.png");
                File file3 = new File(dirpath+"\\"+name+"_Charts\\"+"\\HeartbeatCH.png");
                File file4 = new File(dirpath+"\\"+name+"_Charts\\"+"\\PostureCH.png");
                File file5 = new File(dirpath+"\\"+name+"_Charts\\"+"\\VarianceCH.png");
                ImageIO.write(SwingFXUtils.fromFXImage(image1, null), "PNG", file1);
                ImageIO.write(SwingFXUtils.fromFXImage(image2, null), "PNG", file2);
                ImageIO.write(SwingFXUtils.fromFXImage(image3, null), "PNG", file3);
                ImageIO.write(SwingFXUtils.fromFXImage(image4, null), "PNG", file4);
                ImageIO.write(SwingFXUtils.fromFXImage(image5, null), "PNG", file5);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Le immagini sono state salvate correttamente");
                alert.showAndWait();
            }catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Qualcosa e' andato storto");
                alert.showAndWait();
            }
        });
        AccChart.setPrefSize(200, 24);
        HBChart.setPrefSize(200, 24);
        BRChart.setPrefSize(200, 24);
        Posture.setPrefSize(200, 24);
        varChart.setPrefSize(200, 24);
        MediaChart.setPrefSize(200, 24);
        MedianaChart.setPrefSize(200, 24);
        Indietro.setPrefSize(200, 24);
        Salva.setPrefSize(200, 24);
        root.add(AccChart, 0, 0);
        root.add(HBChart, 0, 1);
        root.add(BRChart, 0, 2);
        root.add(Posture, 0, 3);
        root.add(varChart, 0, 4);
        root.add(MedianaChart, 0, 5);
        root.add(MediaChart, 0, 6);
        root.add(Indietro, 0, 15);
        root.add(Salva, 15, 15);
        var scene = new Scene(root, 600, 400);   
        this.scene=scene;
    }
    public Scene getScene(){
        return scene;
    }
}
   
