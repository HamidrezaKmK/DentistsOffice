package controller;

import model.QueryType;
import model.Schedule;
import model.TimeInterval;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class DataBaseQueryController {

    private String username;
    private String password;
    private String url;
    private Connection current_connection = null;

    private static DataBaseQueryController dBController = null;

    public static DataBaseQueryController getInstance() {
        if (dBController == null)
            dBController = new DataBaseQueryController();
        return dBController;
    }

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
        }
    }

    public void disconnect() throws Exception {
        if (current_connection != null)
            current_connection.close();
    }

    public void handleQuery(QueryType queryType, String[] args) throws Exception {
        try {
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
                case REFRESH_FILE_SUMMARY:
                    handleRefreshFileSummary(args);
                    break;
                case REFRESH_PAGE:
                    handleRefreshPage(args);
                    break;
                case REFRESH_LIST_OF_PATIENT_FILES_BY_CREATION_DATE:
                    handleRefreshListOfPatientFilesByCreationDate(args);
                    break;
                case REFRESH_PATIENTS_WHO_OWE_MONEY:
                    handleRefreshPatientsWhoOweMoney(args);
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
                case REFRESH_SCHEDULE_IN_TIME_INTERVAL:
                    handleRefreshScheduleInTimeInterval(args);
                    break;
            }

        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    private void handleRefreshScheduleInTimeInterval(String[] args) throws Exception {
        Schedule.getInstance().clear();
        Statement stmt = null;
        System.err.println("1.begin_date\n2.begin_time\n3.end_date\n4.end_time");
        String begin_date = args[0];
        String begin_time = args[1];
        String end_date = args[2];
        String end_time = args[3];

        // add occupied times
        try {
            stmt = current_connection.createStatement();
            String query = "select T1.date, T1.begin_time, T1.begin_time + T1.duration::interval as end_time, T3.patient_id, T3.first_name, T3.last_name, T2.reason\n" +
                    "\tfrom OccupiedTimeSlotsT as T1 left join ReferralOccupiedTimeSlotsT as T2 on \n" +
                    "    \t\tT1.date = T2.date and T1.begin_time = T2.begin_time left join Patientt as T3 on T3.patient_id = T2.patient_id" +
                    "    where T1.date >= ('" + begin_date + "'::date) and\n" +
                    "\t\t\tT1.date <= ('" + end_date + "'::date) and\n" +
                    "            (T1.date != '" + begin_date + "'::date or T1.begin_time >= '" + begin_time + "'::time) and\n" +
                    "            (T1.date != '" + end_date + "'::date or T1.begin_time + T1.duration::interval <= '" + end_time + "'::time)";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                LocalDate beginDate = LocalDate.parse(rs.getString("date"));
                LocalDate endDate = LocalDate.parse(rs.getString("date"));
                LocalTime beginTime = LocalTime.parse(rs.getString("begin_time"));
                LocalTime endTime = LocalTime.parse(rs.getString("end_time"));
                String reason = rs.getString("reason");
                if (reason == null)
                    reason = "Doctor not available!";
                else
                    reason = reason + " patient name: " + rs.getString("first_name") + " " + rs.getString("last_name");
                Schedule.getInstance().addBusyInterval(new TimeInterval(beginDate, endDate, beginTime, endTime, reason));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        // add unavailable times
        LocalDate beginDate = LocalDate.parse(begin_date);
        LocalDate endDate = LocalDate.parse(end_date);
        for (LocalDate date = beginDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            String dow = date.getDayOfWeek().toString().toUpperCase().substring(0, 3);
            try {
                stmt = current_connection.createStatement();
                String query = "select T1.begin_time, \n" +
                        "\tT1.begin_time + T1.duration::interval as end_time \n" +
                        "\tfrom AvailableTimeSlotsT as T1 join WeeklyScheduleT as T2 on T1.weekly_schedule_from_date_ref = T2.from_date\n" +
                        "    where T2.from_date <= '" + date.toString() + "'::date and '" + date.toString() +
                        "'::date <= T2.to_date and T1.day_of_week = '" + dow + "'\n" +
                        "    order by T1.begin_time";

                ResultSet rs = stmt.executeQuery(query);
                LocalTime lastTime = LocalTime.parse("00:00:00");
                if (date.equals(beginDate) && lastTime.isBefore(LocalTime.parse(begin_time))) {
                    lastTime = LocalTime.parse(begin_time);
                 //   System.out.println("Chiiiz " + lastTime);
                }

                    while (rs.next()) {
                    LocalTime availableTimeL = LocalTime.parse(rs.getString("begin_time"));
                    if (availableTimeL.isAfter(LocalTime.parse(end_time)))
                        availableTimeL = LocalTime.parse(end_time);
                    LocalTime availableTimeR = LocalTime.parse(rs.getString("end_time"));
                    String reason = "Doctor not available!";
                    if (lastTime.isBefore(availableTimeL))
                        Schedule.getInstance().addBusyInterval(new TimeInterval(date, date, lastTime, availableTimeL, reason));

                    lastTime = availableTimeR;
                    if (date.equals(endDate) && lastTime.isAfter(LocalTime.parse(end_time)))
                        break;
                }
                LocalTime timeR = LocalTime.parse("23:59:59");
                if (date.equals(endDate))
                    timeR = LocalTime.parse(end_time);
                if (lastTime.isBefore(timeR))
                    Schedule.getInstance().addBusyInterval(new TimeInterval(date, date, lastTime, timeR, "Doctor not available"));
            } catch (SQLException e) {
                throw new Error("Problem", e);
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }

        // output
        Schedule.getInstance().printIntervals();
    }

    private void handleAddNewAvailableTime(String[] args) throws Exception {
    }

    private void handleCreateNewWeeklySchedule(String[] args) throws Exception {
    }

    private void handleAddReferralTime(String[] args) throws Exception {
    }

    private void handleCancelAppointment(String[] args) throws Exception {
    }

    private void handleRefreshPatientsWhoOweMoney(String[] args) throws Exception {
        Statement stmt = null;
        String query = "select occupied_time_slot_date_ref as date_of_appointment, " +
                "patient_id, first_name, last_name, whole_payment_amount - paid_payment_amount as debt\n" +
                "\tfrom AppointmentPageT natural join PatientT\n" +
                "\twhere whole_payment_amount > paid_payment_amount;";
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("first_name");
                System.out.println(name + " has debt = " + rs.getString("debt"));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private void handleRefreshListOfPatientFilesByCreationDate(String[] args) throws Exception {
    }

    private void handleRefreshPage(String[] args) throws Exception {
    }

    // 1st argument: patient_id
    private void handleRefreshFileSummary(String[] args) throws Exception {
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
