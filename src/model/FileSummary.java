package model;

import java.awt.geom.Area;
import java.util.ArrayList;

public class FileSummary {

    private static FileSummary singleInstance = null;

    private ArrayList<String> medicalImagePage_page_numbers = new ArrayList<>();
    private ArrayList<String> appointmentPage_page_numbers = new ArrayList<>();
    private ArrayList<String> medicalImagePage_creation_dates = new ArrayList<>();
    private ArrayList<String> appointmentPage_creation_dates = new ArrayList<>();


    public static FileSummary getInstance() {
        if (singleInstance == null)
            singleInstance =  new FileSummary();
        return singleInstance;
    }

    public void clear() {
        this.appointmentPage_creation_dates = null;
        this.appointmentPage_page_numbers = null;
        this.medicalImagePage_page_numbers = null;
        this.medicalImagePage_creation_dates = null;
    }

    public ArrayList<String> getMedicalImagePage_page_numbers() {
        return medicalImagePage_page_numbers;
    }

    public void setMedicalImagePage_page_numbers(ArrayList<String> medicalImagePage_page_numbers) {
        this.medicalImagePage_page_numbers = medicalImagePage_page_numbers;
    }

    public ArrayList<String> getAppointmentPage_page_numbers() {
        return appointmentPage_page_numbers;
    }

    public void setAppointmentPage_page_numbers(ArrayList<String> appointmentPage_page_numbers) {
        this.appointmentPage_page_numbers = appointmentPage_page_numbers;
    }

    public ArrayList<String> getMedicalImagePage_creation_dates() {
        return medicalImagePage_creation_dates;
    }

    public void setMedicalImagePage_creation_dates(ArrayList<String> medicalImagePage_creation_dates) {
        this.medicalImagePage_creation_dates = medicalImagePage_creation_dates;
    }

    public ArrayList<String> getAppointmentPage_creation_dates() {
        return appointmentPage_creation_dates;
    }

    public void setAppointmentPage_creation_dates(ArrayList<String> appointmentPage_creation_dates) {
        this.appointmentPage_creation_dates = appointmentPage_creation_dates;
    }
}
