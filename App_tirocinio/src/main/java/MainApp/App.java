package MainApp;

import java.io.File;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    public File[] files=null;
    ChartClass CC;
    public Label notify;
    Button Clear;
    Button Procedi;
    
    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    private void initUI(Stage stage) {
        HomeClass HC=new HomeClass(stage);
        Scene home = HC.getScene();
        
        stage.setTitle("Charts viewer");
        stage.setScene(home);
        stage.show();  
    }

    public static void main(String[] args) {
        launch(args);
    }    
}

