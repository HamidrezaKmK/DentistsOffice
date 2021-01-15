package model;

import java.util.ArrayList;

public class SearchResult {

    private static SearchResult singleInstance = null;

    private String[] firstName;
    private String[] lastName;
    private Integer[] patientId;
    private Integer[] debt;


    public static SearchResult getInstance() {
        if (singleInstance == null)
            singleInstance = new SearchResult();
        return singleInstance;
    }

    public void clear() {
        this.firstName = null;
        this.lastName = null;
        this.patientId = null;
        this.debt = null;
    }

    public void setFirstName(String[] firstName) {
        this.firstName = firstName;
    }

    public String[] getFirstName() {
        return firstName;
    }

    public String[] getLastName() {
        return lastName;
    }

    public void setLastName(String[] lastName) {
        this.lastName = lastName;
    }

    public Integer[] getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer[] patientId) {
        this.patientId = patientId;
    }

    public Integer[] getDebt() {
        return debt;
    }

    public void setDebt(Integer[] debt) {
        this.debt = debt;
    }
}
