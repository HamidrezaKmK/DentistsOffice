package controller;

import com.sun.codemodel.internal.JMod;
import model.QueryType;
import model.Schedule;
import model.TimeInterval;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public void handleQuery(QueryType queryType, String... args) throws Exception {
        try {
            switch (queryType) {
                case MAIN_SEARCH:
                    // args = {"fn", "ln", "id", "1" or "0"} (1 as in_debt and 0 as not in_debt)
                    // Does: It fills the single instance of SearchResults.
                    mainSearch(args);
                case CREATE_FILE:
                    // args: {"id", "fn", "ln", "age", "gander", "occupation", "ref", "edu', "homeAddr", "workAddr",
                    // "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr", "fileCreationDate"}
                    // important: if any of entries is null, put "null" (string of null) in its place.
                    // fileCreationDate format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    handleCreateFile(args);
                    break;
                case ADD_PERSONAL_INFO_PAGE:
                    // Personal info page is created while creating the file. This function is empty.
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
                    // args: {"id", "1", "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr"}
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

    // args = {"fn", "ln", "id", "1" or "0"} (1 as in_debt and 0 as not in_debt)
    // Does: It fills the single instance of SearchResults.
    public void mainSearch(String[] args) throws Exception {
        String in_debt = args[3];
        boolean checked_in_debt = in_debt.equals("1");
        String query = null;
        if (checked_in_debt) {
            query = mainSearchInDebt(args);
        } else {
            query = mainSearchNotInDebt(args);
        }
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> last_names = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            ArrayList<Integer> debts = new ArrayList<>();
            while (rs.next()) {
                names.add(rs.getString("first_name"));
                last_names.add(rs.getString("last_name"));
                ids.add(rs.getInt("patient_id"));
                if (checked_in_debt) {
                    debts.add(rs.getInt("debt"));
                }
            }
            model.SearchResult.getInstance().clear();
            model.SearchResult.getInstance().setFirstNames(names);
            model.SearchResult.getInstance().setLastNames(last_names);
            model.SearchResult.getInstance().setPatientIds(ids);
            if (checked_in_debt) {
                model.SearchResult.getInstance().setDebt(debts);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // mainSearch uses this function.
    private String mainSearchInDebt(String[] args) throws Exception {
        String first_name = args[0];
        String last_name = args[1];
        String patient_id = args[2];
        String query = null;

        if (patient_id != null) {
            query = "select patient_id, first_name, last_name, whole_payment_amount - paid_payment_amount as debt\n"
                    + "from patientt natural join appointmentpaget\n" + "where patient_id = " + patient_id
                    + " and whole_payment_amount - paid_payment_amount > 0";
        } else {
            query = "select patient_id, first_name, last_name, whole_payment_amount - paid_payment_amount as debt\n"
                    + "from patientt natural join appointmentpaget\n"
                    + "where whole_payment_amount - paid_payment_amount > 0";
            if (first_name != null) {
                query += " and first_name = '" + first_name + "'";
            }
            if (last_name != null) {
                query += " and last_name = '" + last_name + "'";
            }
        }
        query += ";";
        return query;
    }


    // mainSearch uses this function.
    private String mainSearchNotInDebt(String[] args) throws Exception {
        String first_name = args[0];
        String last_name = args[1];
        String patient_id = args[2];
        String first_name_condition = "first_name = '" + first_name + "'";
        String last_name_condition = "last_name = '" + last_name + "'";
        String patient_id_condition = "patient_id = " + patient_id;

        String query = "";
        boolean had_first_name = false;
        boolean had_last_name = false;
        query = "select patient_id, first_name, last_name from patientt\nwhere ";
        if (first_name != null) {
            query += first_name_condition;
            had_first_name = true;
        }
        if (last_name != null) {
            if (had_first_name) {
                query += " and ";
            }
            query += last_name_condition;
            had_last_name = true;
        }
        if (patient_id != null) {
            if (had_last_name || had_first_name) {
                query += " and ";
            }
            query += patient_id_condition;
            query += ";";
        }
        return query;
    }


    // Hamid you wrote this function.
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


    // args: Nothing (empty string)
    // does: It fills the single instance of SearchResult class.
    private void handleRefreshPatientsWhoOweMoney(String[] args) throws Exception {
        String[] searchArgs = {null, null, null, "1"};
        mainSearch(searchArgs);
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


    // args: {"id", "1", "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr"}
    private void handleEditPersonalInfo(String[] args) throws Exception {
        String query = "update personalinfopaget\nset ";
        if (!args[2].toLowerCase().equals("null")) {
            query += "general_medical_records = '" + args[2] + "', ";
        } else {
            query += "general_medical_records = " + args[2] + ", ";
        }
        if (!args[3].toLowerCase().equals("null")) {
            query += "dental_records = '" + args[3] + "', ";
        } else {
            query += "dental_records = " + args[3] + ", ";
        }
        if (!args[4].toLowerCase().equals("null")) {
            query += "sensitive_medicine = '" + args[4] + "', ";
        } else {
            query += "sensitive_medicine = " + args[4] + ", ";
        }
        if (!args[5].toLowerCase().equals("null")) {
            query += "does_smoke = '" + args[5] + "', ";
        } else {
            query += "does_smoke = " + args[5] + ", ";
        }
        if (!args[6].toLowerCase().equals("null")) {
            query += "signature_image_address = '" + args[5] + "'";
        } else {
            query += "signature_image_address = " + args[5];
        }
        query += "\nwhere patient_id = " + args[0] + ";";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private void handleEditAppointmentPage(String[] args) throws Exception {
    }

    private void handleEditMedicalImagePage(String[] args) throws Exception {
    }

    private void handleAddAppointmentPage(String[] args) throws Exception {
    }

    private void handleAddPersonalInfoPage(String[] args) throws Exception {
    }


    // args: {"id", "fn", "ln", "age", "gander", "occupation", "ref", "edu', "homeAddr", "workAddr",
    // "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr", "fileCreationDate"}
    // important: if any of entries is null, put "null" (string of null) in its place.
    // fileCreationDate format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    private void handleCreateFile(String[] args) throws Exception {
        String id = args[0];
        String add_patient_query = InsertPatientQuery(args);
        String add_page_in_page_table_query = addPageQueryForPersonalInfoPage(args);
        String add_personal_info_page_query = addPersonalInfoPageQuery(args);

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(add_patient_query);
            stmt.executeUpdate(add_page_in_page_table_query);
            stmt.executeUpdate(add_personal_info_page_query);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // CreateFile uses this function.
    private String addPersonalInfoPageQuery(String[] args) {
        String query = "insert into personalinfopaget values(";
        String q = "'";
        query += args[0] + ", 1, ";
        for (int i = 10; i < 15; i++) {
            if (args[i].toLowerCase().equals("null")) {
                if (i == 14) {
                    String adding = args[i];
                    query += adding;
                } else {
                    String adding = args[i] + ", ";
                    query += adding;
                }
            } else {
                if (i == 14) {
                    String adding = q + args[i] + q;
                    query += adding;
                } else {
                    String adding = q + args[i] + q + ", ";
                    query += adding;
                }
            }
        }
        query += ");";
        return query;
    }


    // CreateFile uses this function.
    private String addPageQueryForPersonalInfoPage(String[] args) {
        String query;
        if (args[15].toLowerCase().equals("null")) {
            query = "insert into paget values(" + args[0] + ", " + "1, " + args[15] + ");";
        } else {
            query = "insert into paget values(" + args[0] + ", " + "1, " + "'" + args[15] + "');";
        }
        return query;
    }


    // CreateFile uses this function.
    private String InsertPatientQuery(String[] args) {
        String query = "insert into patientt values(";
        String q = "'";
        for (int i = 0; i < 10; i++) {
            if (args[i].toLowerCase().equals("null")) {
                if (i == 9) {
                    String adding = args[i];
                    query += adding;
                } else {
                    String adding = args[i] + ", ";
                    query += adding;
                }
            } else {
                if (i == 9) {
                    String adding = q + args[i] + q;
                    query += adding;
                } else {
                    String adding = q + args[i] + q + ", ";
                    query += adding;
                }
            }
        }
        query += ");";
        return query;
    }

}
