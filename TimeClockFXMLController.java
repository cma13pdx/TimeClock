/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author cmanson
 */
public class TimeClockFXMLController implements Initializable {

    private static final Logger logger = Logger.getLogger(TimeClock.class.getName());
    private static Boolean clockedIn = false;
    private static Boolean running = true;
    private Time clock;
    ObservableList<TimeSheet> data;
    public DBSearch db;
    @FXML
    private Button clockIn;
    @FXML
    private Button clockOut;
    @FXML
    private Button clockSS;
    @FXML
    private TextField startClock;
    @FXML
    private TextField stopClock;
    @FXML
    private ChoiceBox appChoice;
    @FXML
    private TableView<TimeSheet> timeSheet;
    @FXML
    private TableColumn<TimeSheet, Integer> idCol;
    @FXML
    private TableColumn<TimeSheet, String> applicationCol;
    @FXML
    private TableColumn<TimeSheet, String> sundayCol;
    @FXML
    private TableColumn<TimeSheet, String> mondayCol;
    @FXML
    private TableColumn<TimeSheet, String> tuesdayCol;
    @FXML
    private TableColumn<TimeSheet, String> wednesdayCol;
    @FXML
    private TableColumn<TimeSheet, String> thursdayCol;
    @FXML
    private TableColumn<TimeSheet, String> fridayCol;
    @FXML
    private TableColumn<TimeSheet, String> saturdayCol;
    @FXML
    private TableColumn<TimeSheet, String> totalCol;
    @FXML
    private TableColumn<TimeSheet, Boolean> activeApp;

    public TimeClockFXMLController() throws SQLException, Exception {
        this.data = FXCollections.observableArrayList(DBSearch.getWeekTimeList());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        clock = new Time();
        try {
            clockedIn = DBSearch.clockedIn();
        } catch (Exception ex) {
            Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (clockedIn) {
            startStopClock();
            try {
                startClock.setText(DBSearch.openTime());
                startClock.setEditable(false);
            } catch (Exception ex) {
                Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            clockIn.setText("Switch");
        } else {
            startStartClock();
            clockOut.setDisable(true);
        }

        
        idCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, Integer>("idNum"));
        applicationCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("appName"));
        sundayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("sundayValue"));
        mondayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("mondayValue"));
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("tuesdayValue"));
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("wednesdayValue"));
        thursdayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("thursdayValue"));
        fridayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("fridayValue"));
        saturdayCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("saturdayValue"));
        totalCol.setCellValueFactory(new PropertyValueFactory<TimeSheet, String>("totalValue"));
        activeApp.setCellValueFactory(new PropertyValueFactory<TimeSheet, Boolean>("activeApp"));
        timeSheet.setItems(data);
        try {
            updateAppChoice();
        } catch (Exception ex) {
            Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateTableHighlight();
    }

    @FXML
    private void handleStopStartClock(ActionEvent e) {
        if (clockedIn) {
            if (running) {
                stopStopClock();
                clockSS.setText("Auto");
                running = false;
            } else {
                startStopClock();
                clockSS.setText("Edit");
                running = true;
            }
        } else {
            if (running) {
                stopStartClock();
                clockSS.setText("Auto");
                running = false;
            } else {
                startStartClock();
                clockSS.setText("Edit");
                running = true;
            }
        }
    }

    @FXML
    private void handleClockIn(ActionEvent e) {
        
        if (appChoice.getValue() != null) {
            if (clockedIn) {
                DBSearch.clockOut(stopClock.getText());
                startClock.setEditable(true);
                startClock.setText(stopClock.getText());
                startClock.setEditable(false);
            } else {
                stopStartClock();
            }
            try {
                Integer appID = DBSearch.getAppID(appChoice.getValue().toString());
                DBSearch.clockIn(startClock.getText(), appID);
            } catch (Exception ex) {
                Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
            startStopClock();
            running=true;
            clockedIn=true;
            clockSS.setText("Edit");
            clockOut.setDisable(false);
            clockIn.setText("Switch");
        }
    }

    @FXML
    private void handleClockOut(ActionEvent e) {
        DBSearch.clockOut(stopClock.getText());
        clockedIn = false;
        stopStopClock();
        startStartClock();
        running=true;
        clockSS.setText("Edit");
        clockIn.setText("Clock In");
        stopClock.setEditable(true);
        stopClock.setText(null);
        clockOut.setDisable(true);
    }

    private void stopStartClock() {
        Bindings.unbindBidirectional(startClock.textProperty(), Time.todayNow);
        startClock.setEditable(true);
    }

    private void stopStopClock() {
        Bindings.unbindBidirectional(stopClock.textProperty(), Time.todayNow);
        stopClock.setEditable(true);
    }

    private void startStartClock() {
        Bindings.bindBidirectional(startClock.textProperty(), Time.todayNow);
        startClock.setEditable(false);
    }

    private void startStopClock() {
        Bindings.bindBidirectional(stopClock.textProperty(), Time.todayNow);
        stopClock.setEditable(false);
    }

    private void updateAppChoice() throws Exception {
        appChoice.setTooltip(new Tooltip("Select working application."));
        appChoice.setItems(DBSearch.getAppList());
    }
    
    /*private void startTicking() throws SQLException, Exception {
        TimelineBuilder.create().cycleCount(Timeline.INDEFINITE)
                    .keyFrames(new KeyFrame(Duration.seconds(1), updateTable()))
                    .build().play();
    }*/
    
    private void updateTable() {
        try {
            DBSearch.getWeekTimeList();
        } catch (SQLException ex) {
            Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeClockFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateTableHighlight() {
        int i = 0;
        for (Node n : timeSheet.lookupAll("TableRow")) {
            if (n instanceof TableRow) {
                TableRow row = (TableRow) n;
                if (timeSheet.getItems().get(i).getActiveApp()) {
                    row.getStyleClass().add("setActive");
                    System.out.println("Active row = " + timeSheet.getItems().get(i).getAppName());
                } else {
                    row.getStyleClass().add("notActive");
                }
                if (timeSheet.getItems().get(i).getIdNum() == 0) {
                    row.getStyleClass().add("setTotal");
                }
                i++;
                if (i == timeSheet.getItems().size()) {
                    break;
                }
            }
        }
    }
}
