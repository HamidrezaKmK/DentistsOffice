package model;

import java.util.ArrayList;

public class ReferralOccupiedTimeSlots {

    private static ReferralOccupiedTimeSlots singleInstance = null;

    private ArrayList<String> patient_id = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> begin_time = new ArrayList<>();
    private ArrayList<String> reason = new ArrayList<>();


    public static ReferralOccupiedTimeSlots getInstance() {
        if (singleInstance == null)
            singleInstance = new ReferralOccupiedTimeSlots();
        return singleInstance;
    }

    public void clear() {
        this.begin_time = null;
        this.date = null;
        this.patient_id = null;
        this.reason = null;
    }

    public ArrayList<String> getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(ArrayList<String> patient_id) {
        this.patient_id = patient_id;
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

    public ArrayList<String> getReason() {
        return reason;
    }

    public void setReason(ArrayList<String> reason) {
        this.reason = reason;
    }
}
