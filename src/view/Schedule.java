package view;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class Schedule {
    private static Schedule singleInstance = null;

    private ArrayList<TimeInterval> intervals;
    private Schedule() {
        intervals = new ArrayList<>();
    }

    public static Schedule getInstance() {
        if (singleInstance == null)
            singleInstance = new Schedule();
        return singleInstance;
    }

    public void addBusyInterval(TimeInterval interval) {
        intervals.add(interval);
    }

    public void clear() {
        intervals.clear();
    }

    public void printIntervals() {
        Collections.sort(intervals);
        for (TimeInterval interval : intervals) {
            System.out.print("\n> beginning: " + interval.beginDate + " " + interval.beginTime + "\n");
            System.out.print("  ending: " + interval.endDate + " " + interval.endTime + "\n");
            System.out.print("  Description: " + interval.description + "\n");
        }
    }
}
