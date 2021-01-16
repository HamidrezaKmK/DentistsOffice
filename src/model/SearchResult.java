package model;

import java.util.ArrayList;

public class SearchResult {

    private static SearchResult singleInstance = null;

    private ArrayList<String> firstNames = new ArrayList<>();
    private ArrayList<String> lastNames = new ArrayList<>();
    private ArrayList<Integer> patientIds = new ArrayList<>();
    private ArrayList<Integer> debt = new ArrayList<>();


    public static SearchResult getInstance() {
        if (singleInstance == null)
            singleInstance = new SearchResult();
        return singleInstance;
    }

    public void clear() {
        this.firstNames = null;
        this.lastNames = null;
        this.patientIds = null;
        this.debt = null;
    }

    public ArrayList<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(ArrayList<String> firstNames) {
        this.firstNames = firstNames;
    }

    public ArrayList<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(ArrayList<String> lastNames) {
        this.lastNames = lastNames;
    }

    public ArrayList<Integer> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(ArrayList<Integer> patientIds) {
        this.patientIds = patientIds;
    }

    public ArrayList<Integer> getDebt() {
        return debt;
    }

    public void setDebt(ArrayList<Integer> debt) {
        this.debt = debt;
    }
}
