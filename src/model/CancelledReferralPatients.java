package model;

import java.util.ArrayList;

public class CancelledReferralPatients {

    private static CancelledReferralPatients singleInstance = null;

    private ArrayList<String> first_names = new ArrayList<>();
    private ArrayList<String> last_names = new ArrayList<>();
    private ArrayList<String> patient_ids = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> begin_times = new ArrayList<>();

    private ArrayList<ArrayList<String>> phone_numbers = new ArrayList<>();

    public static CancelledReferralPatients getInstance() {
        if (singleInstance == null)
            singleInstance = new CancelledReferralPatients();
        return singleInstance;
    }

    public void clear() {
        this.first_names = null;
        this.last_names  = null;
        this.dates = null;
        this.begin_times = null;
        this.phone_numbers = null;
        this.patient_ids = null;
    }

    public ArrayList<String> getFirst_names() {
        return first_names;
    }

    public void setFirst_names(ArrayList<String> first_names) {
        this.first_names = first_names;
    }

    public ArrayList<String> getLast_names() {
        return last_names;
    }

    public void setLast_names(ArrayList<String> last_names) {
        this.last_names = last_names;
    }

    public ArrayList<String> getPatient_ids() {
        return patient_ids;
    }

    public void setPatient_ids(ArrayList<String> patient_ids) {
        this.patient_ids = patient_ids;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

    public ArrayList<String> getBegin_times() {
        return begin_times;
    }

    public void setBegin_times(ArrayList<String> begin_times) {
        this.begin_times = begin_times;
    }

    public ArrayList<ArrayList<String>> getPhone_numbers() {
        return phone_numbers;
    }

    public void setPhone_numbers(ArrayList<ArrayList<String>> phone_numbers) {
        this.phone_numbers = phone_numbers;
    }
}
