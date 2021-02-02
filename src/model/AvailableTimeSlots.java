package model;

import java.util.ArrayList;

public class AvailableTimeSlots {

    private static AvailableTimeSlots singleInstance = null;

    private ArrayList<String> week_days = new ArrayList<>();
    private ArrayList<String> begin_times = new ArrayList<>();
    private ArrayList<String> end_times = new ArrayList<>();

    public static AvailableTimeSlots getInstance() {
        if(singleInstance == null)
            singleInstance = new AvailableTimeSlots();
        return singleInstance;
    }

    public void clear() {
        this.begin_times = null;
        this.end_times = null;
        this.week_days = null;
    }

    public ArrayList<String> getWeek_days() {
        return week_days;
    }

    public void setWeek_days(ArrayList<String> week_days) {
        this.week_days = week_days;
    }

    public ArrayList<String> getBegin_times() {
        return begin_times;
    }

    public void setBegin_times(ArrayList<String> begin_times) {
        this.begin_times = begin_times;
    }

    public ArrayList<String> getEnd_times() {
        return end_times;
    }

    public void setEnd_times(ArrayList<String> end_times) {
        this.end_times = end_times;
    }
}
