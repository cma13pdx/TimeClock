/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cmanson
 */
public class TimeClock extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TimeClockFXML.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Time Clock");
        stage.setScene(scene);
        stage.getScene().getStylesheets().add(getClass().getResource("timeClock.css").toExternalForm());
        stage.show();
    }

    
    public static void main(String[] args) { launch(args); }
}