package model;

public enum QueryType {
    // file edit queries:
    CREATE_FILE,
    ADD_PERSONAL_INFO_PAGE,
    ADD_APPOINTMENT_PAGE,
    EDIT_MEDICAL_IMAGE_PAGE,
    EDIT_APPOINTMENT_PAGE,
    EDIT_PERSONAL_INFO,
    DELETE_PAGE,

    // show file queries
    SHOW_FILE_SUMMARY,
    SHOW_PAGE,
    SHOW_LIST_OF_PATIENT_FILES_BY_CREATION_DATE,
    SHOW_PATIENTS_WHO_OWE_MONEY,

    // appointment edit queries
    CANCEL_APPOINTMENT,
    ADD_REFERRAL_TIME,

    // scheduling
    CREATE_NEW_WEEKLY_SCHEDULE,
    ADD_NEW_AVAILABLE_TIME,

    //
    SHOW_SCHEDULE_IN_TIME_INTERVAL
}
