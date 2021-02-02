package model;

public enum QueryType {
    // main search query:
    MAIN_SEARCH,

    // file edit queries:
    CREATE_FILE,
    ADD_PERSONAL_INFO_PAGE,
    ADD_APPOINTMENT_PAGE,
    ADD_MEDICAL_IMAGE_PAGE,
    EDIT_MEDICAL_IMAGE_PAGE,
    EDIT_APPOINTMENT_PAGE,
    EDIT_PERSONAL_INFO,
    DELETE_PAGE,

    // refresh file queries
    REFRESH_PATIENT,
    REFRESH_FILE_SUMMARY,
    REFRESH_PAGE,
    REFRESH_LIST_OF_PATIENT_FILES_BY_CREATION_DATE,
    REFRESH_PATIENTS_WHO_OWE_MONEY,

    // appointment edit queries
    CANCEL_APPOINTMENT,
    ADD_REFERRAL_TIME,

    // scheduling
    CREATE_NEW_WEEKLY_SCHEDULE,
    REFRESH_REFERRALS_WITHOUT_APPOINTMENT_PAGE,
    ADD_NEW_AVAILABLE_TIME,
    ADD_OCCUPIED_TIME_SLOT,
    REFRESH_OCCUPIED_TIME_SLOT,
    GET_CURRENT_DATE_TIME,
    GET_CURRENT_WEEKLY_SCHEDULE,


    //
    REFRESH_SCHEDULE_IN_TIME_INTERVAL
}
