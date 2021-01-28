package model;

import java.util.ArrayList;

public class FileTable {

    private static FileTable singleInstance = null;

    private ArrayList<String> patient_id = new ArrayList<>();
    private ArrayList<String> creation_date = new ArrayList<>();

    public static FileTable getInstance() {
        if (singleInstance == null)
            singleInstance = new FileTable();
        return singleInstance;
    }


    public void clear() {
        if (singleInstance != null)
            singleInstance = null;
    }

    public ArrayList<String> getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(ArrayList<String> patient_id) {
        this.patient_id = patient_id;
    }

    public ArrayList<String> getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(ArrayList<String> creation_date) {
        this.creation_date = creation_date;
    }
}
