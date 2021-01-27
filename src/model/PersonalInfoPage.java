package model;

public class PersonalInfoPage {

    private static PersonalInfoPage singleInstance = null;

    private String patient_id;
    private String general_medical_records;
    private String dental_records;
    private String sensitive_medicine;
    private String does_smoke;
    private String signature_image_address;

    public static PersonalInfoPage getInstance() {
        if (singleInstance == null)
            singleInstance = new PersonalInfoPage();
        return singleInstance;
    }

    public void clear() {
        this.dental_records = null;
        this.does_smoke = null;
        this.general_medical_records = null;
        this.patient_id = null;
        this.signature_image_address = null;
        this.sensitive_medicine = null;
    }

    public static PersonalInfoPage getSingleInstance() {
        return singleInstance;
    }

    public static void setSingleInstance(PersonalInfoPage singleInstance) {
        PersonalInfoPage.singleInstance = singleInstance;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getGeneral_medical_records() {
        return general_medical_records;
    }

    public void setGeneral_medical_records(String general_medical_records) {
        this.general_medical_records = general_medical_records;
    }

    public String getDental_records() {
        return dental_records;
    }

    public void setDental_records(String dental_records) {
        this.dental_records = dental_records;
    }

    public String getSensitive_medicine() {
        return sensitive_medicine;
    }

    public void setSensitive_medicine(String sensitive_medicine) {
        this.sensitive_medicine = sensitive_medicine;
    }

    public String getDoes_smoke() {
        return does_smoke;
    }

    public void setDoes_smoke(String does_smoke) {
        this.does_smoke = does_smoke;
    }

    public String getSignature_image_address() {
        return signature_image_address;
    }

    public void setSignature_image_address(String signature_image_address) {
        this.signature_image_address = signature_image_address;
    }
}
