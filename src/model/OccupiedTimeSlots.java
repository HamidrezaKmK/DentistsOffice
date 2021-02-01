package model;

import java.util.ArrayList;

public class OccupiedTimeSlots {

    private static OccupiedTimeSlots singleInstance = null;

    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> begin_time = new ArrayList<>();
    private ArrayList<String> duration = new ArrayList<>();
    private ArrayList<String> available_time_slots_ref_from_date = new ArrayList<>();
    private ArrayList<String> available_time_slots_ref_week_day = new ArrayList<>();
    private ArrayList<String> available_time_slots_ref_begin_time = new ArrayList<>();

    public static OccupiedTimeSlots getInstance() {
        if (singleInstance == null)
            singleInstance = new OccupiedTimeSlots();
        return singleInstance;
    }

    public void clear() {
        this.date = null;
        this.begin_time = null;
        this.duration = null;
        this.available_time_slots_ref_from_date = null;
        this.available_time_slots_ref_week_day = null;
        this.available_time_slots_ref_begin_time = null;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<String> getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(ArrayList<String> begin_time) {
        this.begin_time = begin_time;
    }

    public ArrayList<String> getDuration() {
        return duration;
    }

    public void setDuration(ArrayList<String> duration) {
        this.duration = duration;
    }

    public ArrayList<String> getAvailable_time_slots_ref_from_date() {
        return available_time_slots_ref_from_date;
    }

    public void setAvailable_time_slots_ref_from_date(ArrayList<String> available_time_slots_ref_from_date) {
        this.available_time_slots_ref_from_date = available_time_slots_ref_from_date;
    }

    public ArrayList<String> getAvailable_time_slots_ref_week_day() {
        return available_time_slots_ref_week_day;
    }

    public void setAvailable_time_slots_ref_week_day(ArrayList<String> available_time_slots_ref_week_day) {
        this.available_time_slots_ref_week_day = available_time_slots_ref_week_day;
    }

    public ArrayList<String> getAvailable_time_slots_ref_begin_time() {
        return available_time_slots_ref_begin_time;
    }

    public void setAvailable_time_slots_ref_begin_time(ArrayList<String> available_time_slots_ref_begin_time) {
        this.available_time_slots_ref_begin_time = available_time_slots_ref_begin_time;
    }
}
