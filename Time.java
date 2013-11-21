/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *
 * @author cmanson
 */
public class Time {
    public static SimpleStringProperty todayNow = new SimpleStringProperty();
    
    public Time() {
        startTicking();
    }
    
    public static Integer getHour() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static Integer getMinute() {
        Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);
        return minute;
    }

    public static Integer getSecond() {
        Calendar now = Calendar.getInstance();
        int second = now.get(Calendar.SECOND);
        return second;
    }
    
    public static Integer getDay() {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DATE);
        return day;
    }

    public static Integer getMonth() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH);
        return month;
    }

    public static Integer getYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return year;
    }

    public static Integer getTZ() {
        Calendar now = Calendar.getInstance();
        int tz = now.get(Calendar.ZONE_OFFSET);
        tz = tz / 3600000;
        return tz;
    }

    public static Integer getDayOfWeek() {
        Calendar now = Calendar.getInstance();
        int wd = now.get(Calendar.DAY_OF_WEEK);
        return wd;
    }

    public static Integer getSundayDay() {
        //Sunday = 1, no zero
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -(getDayOfWeek() - 1));
        int sunday = now.get(Calendar.DATE);
        return sunday;
    }

    public static Integer getSundayMonth() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -(getDayOfWeek() - 1));
        int sunday = now.get(Calendar.MONTH) + 1;
        return sunday;
    }

    public static Integer getSundayYear() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -(getDayOfWeek() - 1));
        int sunday = now.get(Calendar.YEAR);
        return sunday;
    }

    public static Boolean validateDate(String inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate;
        try {
            testDate = sdf.parse(inDate);
        } catch (ParseException e) {
            return false;
        }
        if (!sdf.format(testDate).equals(inDate)) {
            return false;
        }
        return true;
    }

    public static Boolean validateTime(String inDate) {
        if (inDate.contains(":")) {

            String[] splitTime = inDate.split(":");
            int hour = Integer.valueOf(splitTime[0]);
            int minute = Integer.valueOf(splitTime[1]);
            int second = Integer.valueOf(splitTime[2]);
            if (hour < 24) {
                if (minute < 60) {
                    if (second < 60) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static String getToday() {
        String today;
        today = getYear() + "-";
        if (getMonth() +1 < 10) {
            today+= "0" + (getMonth()+1) + "-";
        } else {
            today+= getMonth() + 1 + "-";
        }
        if (getDay() < 10) {
            today+= "0" + getDay();
        } else {
            today+= getDay();
        }
        return today;
    }

    public static String getSunday() {
        String sunday;
        sunday = getSundayYear() + "-" + getSundayMonth() + "-" + getSundayDay();
        return sunday;
    }

    public static String addADay(Integer add) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, (-(getDayOfWeek() - 1))+add);
        String newDate;
        newDate=now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH)+1) + "-" + now.get(Calendar.DATE);
        //System.out.println(newDate);
        return newDate;
    }

    public static String formatTime(Integer total) {
        String timeOutput;
        Integer hours = total / 60;
        Integer mins= total - (hours * 60);
        timeOutput = hours + ":";
        if (mins < 10) {
            timeOutput += "0" + mins;
        } else {
            timeOutput += mins;
        }
        return timeOutput;
    }
    
    public static String formatTime(Float total) {
        String timeOutput;
        Integer intTotal = total.intValue();
        Integer hours = intTotal / 60;
        Integer mins= intTotal - (hours * 60);
        timeOutput = hours + ":";
        if (mins < 10) {
            timeOutput += "0" + mins;
        } else {
            timeOutput += mins;
        }
        return timeOutput;
    }
    
    public static String getNow() {
        String now;
        now = getHour() + ":";
        if (getMinute() < 10) {
            now += "0" + getMinute() + ":";
        } else {
            now += getMinute() + ":";
        }
        if (getSecond() < 10) {
            now += "0" + getSecond();
        } else {
            now += getSecond();
        }
        return now;
    }
    
    public static EventHandler updateTime() {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                todayNow.set(getToday() + " " + getNow());
            }
          };    
    }

    public static void startTicking() {
        TimelineBuilder.create().cycleCount(Timeline.INDEFINITE)
                    .keyFrames(new KeyFrame(Duration.seconds(1), updateTime()))
                    .build().play();
    }
}
