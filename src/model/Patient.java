package model;


import java.util.ArrayList;

public class Patient {

    private static Patient singleInstance = null;

    // ArrayLists below, will have just first element (eg: first_name.get(0))
    private String first_name = null;
    private String last_name = null;
    private String age = null;
    private String gender = null;
    private String occupation = null;
    private String reference = null;
    private String education = null;
    private String homeAddr = null;
    private String workAddr = null;

    private ArrayList<String> phones = null;

    public static Patient getSingleInstance() {
        return singleInstance;
    }

    public static void setSingleInstance(Patient singleInstance) {
        Patient.singleInstance = singleInstance;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public void clear() {
        this.age = null;
        this.education = null;
        this.first_name = null;
        this.last_name = null;
        this.occupation = null;
        this.reference = null;
        this.workAddr = null;
        this.homeAddr = null;
        this.gender = null;
    }


    public static Patient getInstance() {
        if (singleInstance == null)
            singleInstance = new Patient();
        return singleInstance;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(ArrayList<String> first_name) {
        this.first_name = first_name.get(0);
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(ArrayList<String> last_name) {
        this.last_name = last_name.get(0);
    }

    public String getAge() {
        return age;
    }

    public void setAge(ArrayList<String> age) {
        this.age = age.get(0);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(ArrayList<String> gender) {
        this.gender = gender.get(0);
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(ArrayList<String> occupation) {
        this.occupation = occupation.get(0);
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(ArrayList<String> education) {
        this.education = education.get(0);
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(ArrayList<String> homeAddr) {
        this.homeAddr = homeAddr.get(0);
    }

    public String getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(ArrayList<String> workAddr) {
        this.workAddr = workAddr.get(0);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(ArrayList<String> reference) {
        this.reference = reference.get(0);
    }
}
