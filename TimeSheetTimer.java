/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

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
public class TimeSheetTimer {

    public static SimpleStringProperty current = new SimpleStringProperty();

    public TimeSheetTimer(String current) {
        startTicking(current);
    }

    public static SimpleStringProperty formatTime(String input) {
        String timeOutput;
        String[] hm = input.split(":");
        Integer h = Integer.decode(hm[0]);
        Integer m = Integer.decode(hm[1]);
        Integer total = (h * 60) + m + 1;
        Integer hours = total / 60;
        Integer mins = total - (hours * 60);
        timeOutput = hours + ":";
        if (mins < 10) {
            timeOutput += "0" + mins;
        } else {
            timeOutput += mins;
        }
        SimpleStringProperty top = new SimpleStringProperty(timeOutput);
        return top;
    }

    public static EventHandler updateTime(final String cur) {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                current = formatTime(cur);
            }
        };
    }

    public static void startTicking(String current) {
        TimelineBuilder.create().cycleCount(Timeline.INDEFINITE)
                .keyFrames(new KeyFrame(Duration.seconds(60), updateTime(current)))
                .build().play();
    }
}
