package model;

public class AppointmentPage {

    private static AppointmentPage singleInstance = null;

    private String patient_id;
    private String page_no;
    private String treatment_summary;
    private String next_appointment_date;
    private String whole_payment_amount;
    private String paid_payment_amount;
    private String date;
    private String from;
    private String to;

    public static AppointmentPage getInstance() {
        if (singleInstance == null)
            singleInstance = new AppointmentPage();
        return singleInstance;
    }

    public void clear() {
        this.patient_id = null;
        this.page_no = null;
        this.treatment_summary = null;
        this.next_appointment_date = null;
        this.whole_payment_amount = null;
        this.paid_payment_amount = null;
        this.date = null;
        this.from = null;
        this.to = null;
    }

    public static void setSingleInstance(AppointmentPage singleInstance) {
        AppointmentPage.singleInstance = singleInstance;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPage_no() {
        return page_no;
    }

    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }

    public String getTreatment_summary() {
        return treatment_summary;
    }

    public void setTreatment_summary(String treatment_summary) {
        this.treatment_summary = treatment_summary;
    }

    public String getNext_appointment_date() {
        return next_appointment_date;
    }

    public void setNext_appointment_date(String next_appointment_date) {
        this.next_appointment_date = next_appointment_date;
    }

    public String getWhole_payment_amount() {
        return whole_payment_amount;
    }

    public void setWhole_payment_amount(String whole_payment_amount) {
        this.whole_payment_amount = whole_payment_amount;
    }

    public String getPaid_payment_amount() {
        return paid_payment_amount;
    }

    public void setPaid_payment_amount(String paid_payment_amount) {
        this.paid_payment_amount = paid_payment_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
