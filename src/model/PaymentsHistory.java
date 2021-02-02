package model;

import java.util.ArrayList;

public class PaymentsHistory {

    private static PaymentsHistory singleInstance = null;

    private ArrayList<String> patient_ids = new ArrayList<>();
    private ArrayList<String> first_names = new ArrayList<>();
    private ArrayList<String> last_names = new ArrayList<>();
    private ArrayList<String> whole_payment = new ArrayList<>();
    private ArrayList<String> paid_payment = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    public static PaymentsHistory getInstance() {
        if (singleInstance == null)
            singleInstance = new PaymentsHistory();
        return singleInstance;
    }

    public void clear() {
        this.dates = null;
        this.first_names = null;
        this.last_names = null;
        this.paid_payment = null;
        this.patient_ids = null;
        this.whole_payment = null;
    }

    public ArrayList<String> getPatient_ids() {
        return patient_ids;
    }

    public void setPatient_ids(ArrayList<String> patient_ids) {
        this.patient_ids = patient_ids;
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

    public ArrayList<String> getWhole_payment() {
        return whole_payment;
    }

    public void setWhole_payment(ArrayList<String> whole_payment) {
        this.whole_payment = whole_payment;
    }

    public ArrayList<String> getPaid_payment() {
        return paid_payment;
    }

    public void setPaid_payment(ArrayList<String> paid_payment) {
        this.paid_payment = paid_payment;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }
}
