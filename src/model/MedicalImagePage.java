package model;

public class MedicalImagePage {

    private static MedicalImagePage singleInstance = null;

    private String patient_id;
    private String page_no;
    private String content_address;
    private String image_type;
    private String reason;

    public static MedicalImagePage getInstance() {
        if (singleInstance == null)
            singleInstance = new MedicalImagePage();
        return singleInstance;
    }

    public void clear() {
        this.patient_id = null;
        this.content_address = null;
        this.image_type = null;
        this.reason = null;
        this.page_no = null;
    }

    public static MedicalImagePage getSingleInstance() {
        return singleInstance;
    }

    public static void setSingleInstance(MedicalImagePage singleInstance) {
        MedicalImagePage.singleInstance = singleInstance;
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

    public String getContent_address() {
        return content_address;
    }

    public void setContent_address(String content_address) {
        this.content_address = content_address;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
