/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author cmanson
 */
public class TimeSheet {
    private SimpleBooleanProperty activeApp;
    private SimpleIntegerProperty idNum;
    private SimpleStringProperty appName;
    private SimpleStringProperty sundayValue;
    private SimpleStringProperty mondayValue;
    private SimpleStringProperty tuesdayValue;
    private SimpleStringProperty wednesdayValue;
    private SimpleStringProperty thursdayValue;
    private SimpleStringProperty fridayValue;
    private SimpleStringProperty saturdayValue;
    private SimpleStringProperty totalValue;
    private TimeSheetTimer tst;
    
    TimeSheet(Boolean actApp,Integer idV,String applName, String sunValue, String monValue, String tueValue,
            String wedValue, String thuValue, String friValue, String satValue, String totValue) {
        this.activeApp = new SimpleBooleanProperty(actApp);
        this.idNum = new SimpleIntegerProperty(idV);
        this.appName = new SimpleStringProperty(applName);
        this.sundayValue = new SimpleStringProperty(sunValue);
        this.mondayValue = new SimpleStringProperty(monValue);
        this.tuesdayValue = new SimpleStringProperty(tueValue);
        this.wednesdayValue = new SimpleStringProperty(wedValue);
        this.thursdayValue = new SimpleStringProperty(thuValue);
        this.fridayValue = new SimpleStringProperty(friValue);
        this.saturdayValue = new SimpleStringProperty(satValue);
        this.totalValue = new SimpleStringProperty(totValue);
        //System.out.println("Application Name " + getAppName());
        if (activeApp.getValue()) {
            //TODO what dow is it
        }
    }
    
    public Boolean getActiveApp() {
        return activeApp.get();
    }
    
    public void setActiveApp(Boolean actApp) {
        activeApp.set(actApp);
    }
    
    public Integer getIdNum() {
        return idNum.get();
    }

    public void setIdNum(Integer idV) {
        idNum.set(idV);
    }

    public void setAppName(String applName) {
        appName.set(applName);
    }

    public String getAppName() {
        return appName.get();
    }
    
    public void setSundayValue(String sunValue) {
        sundayValue.set(sunValue);
    }
    
    public String getSundayValue() {
        return sundayValue.get();
    }
    
    public void setMondayValue(String monValue) {
        mondayValue.set(monValue);
    }

    public void setTuesdayValue(String tueValue) {
        tuesdayValue.set(tueValue);
    }

    public void setWednesdayValue(String wedValue) {
        wednesdayValue.set(wedValue);
    }

    public void setThursdayValue(String thuValue) {
        thursdayValue.set(thuValue);
    }

    public void setFridayValue(String friValue) {
        fridayValue.set(friValue);
    }

    public void setSaturdayValue(String satValue) {
        saturdayValue.set(satValue);
    }

    public void setTotalValue(String totValue) {
        totalValue.set(totValue);
    }
    
    public String getMondayValue() {
        return mondayValue.get();
    }
    
    public String getTuesdayValue() {
        return tuesdayValue.get();
    }
    
    public String getWednesdayValue() {
        return wednesdayValue.get();
    }
    
    public String getThursdayValue() {
        return thursdayValue.get();
    }
    
    public String getFridayValue() {
        return fridayValue.get();
    }
    
    public String getSaturdayValue() {
        return saturdayValue.get();
    }
    
    public String getTotalValue() {
        return totalValue.get();
    }
    
}

