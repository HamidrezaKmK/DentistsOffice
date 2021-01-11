package controller;

import model.QueryType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseQueryController {

    private String username;
    private String password;
    private String url;
    private Connection current_connection;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect() throws Exception {
        try {
            Properties props = new Properties();
            props.setProperty("user", username); // it is good to change this!
            props.setProperty("password", password); //change to your password
            current_connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (current_connection != null) {
                    current_connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void handleQuery(QueryType queryType, String[] args) throws Exception {
        Connection conn = null;
        try {
            //String url = "jdbc:postgresql://localhost:5432/DentistOfficeDB";
            switch (queryType) {
                case CREATE_FILE:
                    handleCreateFile(args);
                    break;
                case ADD_PERSONAL_INFO_PAGE:
                    handleAddPersonalInfoPage(args);
                    break;
                case ADD_APPOINTMENT_PAGE:
                    handleAddAppointmentPage(args);
                    break;
                case EDIT_MEDICAL_IMAGE_PAGE:
                    handleEditMedicalImagePage(args);
                    break;
                case EDIT_APPOINTMENT_PAGE:
                    handleEditAppointmentPage(args);
                    break;
                case EDIT_PERSONAL_INFO:
                    handleEditPersonalInfo(args);
                    break;
                case DELETE_PAGE:
                    handleDeletePage(args);
                    break;
                case SHOW_FILE_SUMMARY:
                    handleShowFileSummary(args);
                    break;
                case SHOW_PAGE:
                    handleShowPage(args);
                    break;
                case SHOW_LIST_OF_PATIENT_FILES_BY_CREATION_DATE:
                    handleShowListOfPatientFilesByCreationDate(args);
                    break;
                case SHOW_PATIENTS_WHO_OWE_MONEY:
                    handleShowPatientsWhoOweMoney(args);
                    break;
                case CANCEL_APPOINTMENT:
                    handleCancelAppointment(args);
                    break;
                case ADD_REFERRAL_TIME:
                    handleAddReferralTime(args);
                    break;
                case CREATE_NEW_WEEKLY_SCHEDULE:
                    handleCreateNewWeeklySchedule(args);
                    break;
                case ADD_NEW_AVAILABLE_TIME:
                    handleAddNewAvailableTime(args);
                    break;
                case SHOW_SCHEDULE_IN_TIME_INTERVAL:
                    handleShowScheduleInTimeInterval(args);
                    break;
            }

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (current_connection != null) {
                    current_connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void handleShowScheduleInTimeInterval(String[] args) throws Exception {
        System.out.println("yay");
        for (String arg : args)
            System.out.println(arg);
    }

    private void handleAddNewAvailableTime(String[] args) throws Exception {
    }

    private void handleCreateNewWeeklySchedule(String[] args) throws Exception {
    }

    private void handleAddReferralTime(String[] args) throws Exception {
    }

    private void handleCancelAppointment(String[] args) throws Exception {
    }

    private void handleShowPatientsWhoOweMoney(String[] args) throws Exception {
    }

    private void handleShowListOfPatientFilesByCreationDate(String[] args) throws Exception {
    }

    private void handleShowPage(String[] args) throws Exception {
    }

    private void handleShowFileSummary(String[] args) throws Exception {
    }

    private void handleDeletePage(String[] args) throws Exception {
    }

    private void handleEditPersonalInfo(String[] args) throws Exception {
    }

    private void handleEditAppointmentPage(String[] args) throws Exception {
    }

    private void handleEditMedicalImagePage(String[] args) throws Exception {
    }

    private void handleAddAppointmentPage(String[] args) throws Exception {
    }

    private void handleAddPersonalInfoPage(String[] args) throws Exception {
    }

    private void handleCreateFile(String[] args) throws Exception {
    }
}
