package model;


import java.util.ArrayList;

public class Patient {

    private static Patient singleInstance = null;

    // ArrayLists below, will have just first element (eg: first_name.get(0))
    private ArrayList<String> first_name = new ArrayList<>();
    private ArrayList<String> last_name = new ArrayList<>();
    private ArrayList<String> age = new ArrayList<>();
    private ArrayList<String> gender = new ArrayList<>();
    private ArrayList<String> occupation = new ArrayList<>();
    private ArrayList<String> reference = new ArrayList<>();
    private ArrayList<String> education = new ArrayList<>();
    private ArrayList<String> homeAddr = new ArrayList<>();
    private ArrayList<String> workAddr = new ArrayList<>();


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

    public ArrayList<String> getFirst_name() {
        return first_name;
    }

    public void setFirst_name(ArrayList<String> first_name) {
        this.first_name = first_name;
    }

    public ArrayList<String> getLast_name() {
        return last_name;
    }

    public void setLast_name(ArrayList<String> last_name) {
        this.last_name = last_name;
    }

    public ArrayList<String> getAge() {
        return age;
    }

    public void setAge(ArrayList<String> age) {
        this.age = age;
    }

    public ArrayList<String> getGender() {
        return gender;
    }

    public void setGender(ArrayList<String> gender) {
        this.gender = gender;
    }

    public ArrayList<String> getOccupation() {
        return occupation;
    }

    public void setOccupation(ArrayList<String> occupation) {
        this.occupation = occupation;
    }

    public ArrayList<String> getEducation() {
        return education;
    }

    public void setEducation(ArrayList<String> education) {
        this.education = education;
    }

    public ArrayList<String> getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(ArrayList<String> homeAddr) {
        this.homeAddr = homeAddr;
    }

    public ArrayList<String> getWorkAddr() {
        return workAddr;
    }

    public void setWorkAddr(ArrayList<String> workAddr) {
        this.workAddr = workAddr;
    }

    public ArrayList<String> getReference() {
        return reference;
    }

    public void setReference(ArrayList<String> reference) {
        this.reference = reference;
    }
}
