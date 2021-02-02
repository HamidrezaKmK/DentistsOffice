package controller;

//import com.sun.codemodel.internal.JMod;

import model.FileTable;
import model.QueryType;
import model.Schedule;
import model.TimeInterval;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.jar.Attributes;

import static java.lang.Integer.parseInt;


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
                    break;

                case REFRESH_PATIENT:
                    // args: {"id"}
                    // Does: it fills the single instance of patient.
                    refreshPatient(args);
                    break;

                case CREATE_FILE:
                    // args: {"id", "fn", "ln", "age", "gander", "occupation", "ref", "edu', "homeAddr", "workAddr",
                    // "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr", "fileCreationDate",
                    // "phone_number", otherPhoneNumbers...}
                    // important: if any of entries is null, put "null" (string of null) in its place.
                    // fileCreationDate format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    handleCreateFile(args);
                    break;

                case ADD_PERSONAL_INFO_PAGE:
                    // Personal info page is created while creating the file. This function is empty.
                    handleAddPersonalInfoPage(args);
                    break;

                case ADD_MEDICAL_IMAGE_PAGE:
                    // args: {"id", "page_no", "content_address", "image_type", "reason", "creation_date"}
                    addMedicalImagePage(args);
                    break;

                case ADD_APPOINTMENT_PAGE:
                    // args: {"id", "page_no", "treatment_summary", "next_appointment_date", "whole_payment_amount", "paid_payment_amount",
                    // "occupied_time_slot_date_ref", "occupied_time_slot_begin_time_ref", "creation_date"}
                    handleAddAppointmentPage(args);
                    break;

                case EDIT_MEDICAL_IMAGE_PAGE:
                    // args: {"id", "page_no", "content_address", "image_type", "reason"}
                    handleEditMedicalImagePage(args);
                    break;

                case EDIT_APPOINTMENT_PAGE:
                    // args: {"id", "page_no", "treatment_summary", "next_appointment_date", "whole_payment_amount", "paid_payment_amount",
                    // "occupied_time_slot_date_ref", "occupied_time_slot_begin_time_ref"}
                    handleEditAppointmentPage(args);
                    break;

                case EDIT_PERSONAL_INFO:
                    // args: {"id", "1", "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr"}
                    handleEditPersonalInfo(args);
                    break;

                case DELETE_PAGE:
                    // args: {"id", "page_no"}
                    handleDeletePage(args);
                    break;

                case REFRESH_FILE_SUMMARY:
                    // args: {"id"}
                    // does: fills the single object of FileSummary
                    handleRefreshFileSummary(args);
                    break;

                case REFRESH_PAGE:
                    // args: {"id", "page_no"}
                    // does: fills the single object of the relative page (eg: AppointmentPage)
                    handleRefreshPage(args);
                    break;

                case REFRESH_LIST_OF_PATIENT_FILES_BY_CREATION_DATE:
                    // args: {} (empty array of Strings)
                    // does: fills the single object of FileTable (fills arrayLists in matching order)
                    handleRefreshListOfPatientFilesByCreationDate(args);
                    break;

                case REFRESH_PATIENTS_WHO_OWE_MONEY:
                    // args: Nothing (empty string)
                    // does: It fills the single instance of SearchResult class.
                    handleRefreshPatientsWhoOweMoney(args);
                    break;

                case CANCEL_APPOINTMENT:
                    // args: {"date", "begin_time", "isWebUser"} (isWebUser is 1 or 0)
                    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    // Time format: HH:MM:SS
                    handleCancelAppointment(args);
                    break;

                case ADD_REFERRAL_TIME:
                    // args: {"date", "begin_time", "reason", "patient_id"}
                    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    // Time format: HH:MM:SS
                    handleAddReferralTime(args);
                    break;

                case ADD_OCCUPIED_TIME_SLOT:
                    // args: {"date","begin_time", "to_time"}
                    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    // Time format: HH:MM:SS
                    addOccupiedTimeSlot(args);
                    break;

                case CREATE_NEW_WEEKLY_SCHEDULE:
                    // args: {"from_date", "to_date}
                    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    handleCreateNewWeeklySchedule(args);
                    break;

                case ADD_NEW_AVAILABLE_TIME:
                    // args: {"weekly_schedule_from_date_ref", "day_of_week", "begin_time", "to_time"}
                    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
                    // Time format: HH:MM:SS
                    handleAddNewAvailableTime(args);
                    break;

                case REFRESH_SCHEDULE_IN_TIME_INTERVAL:
                    // Hamid you wrote this function.
                    handleRefreshScheduleInTimeInterval(args);
                    break;

                case REFRESH_OCCUPIED_TIME_SLOT:
                    refreshOccupiedTimeSlots(args);
                    break;

                case REFRESH_REFERRALS_WITHOUT_APPOINTMENT_PAGE:
                    // args: {"patientID"}
                    refreshReferralWithNoAppointmentPage(args);
                    break;

                case GET_CURRENT_DATE_TIME:
                    // No args needed
                    getCurrentDateTime();
                    break;

                case GET_CURRENT_WEEKLY_SCHEDULE:
                    // No args needed.
                    getCurrentWeeklySchedule();
                    break;

                case GET_WEEKLY_SCHEDULE_BY_DATE:
                    // args: {"date"}
                    getWeeklyScheduleByDate(args);
                    break;

                case GET_AVAILABLE_TIMES_BY_DATE:
                    // args: {"date"}
                    getAvailableTimeSlotsByDate(args);
                    break;

                case GET_CURRENT_AVAILABLE_TIMES:
                    // No args needed.
                    getCurrentAvailableTimes();
                    break;

                case GET_NEXT_REFERRAL_TIME:
                    // args: {"id"}
                    getNextAppointmentTime(args);
                    break;

                case GET_PAYMENTS_IN_TIME_INTERVAL:
                    // args: {"from_date", "to_date"}
                    getPaymentsInTimeInterval(args);
                    break;
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }


    // args: {"from_date", "to_date"}
    private void getPaymentsInTimeInterval(String[] args) throws SQLException {
        String from = args[0];
        String to = args[1];

        ArrayList<String> patient_ids = new ArrayList<>();
        ArrayList<String> first_names = new ArrayList<>();
        ArrayList<String> last_names = new ArrayList<>();
        ArrayList<String> whole_payment = new ArrayList<>();
        ArrayList<String> paid_payment = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        String query = "select patient_id, first_name, last_name, whole_payment_amount, paid_payment_amount, occupied_time_slot_date_ref\n" +
                "from patientt natural join appointmentpaget\n" +
                "where occupied_time_slot_date_ref between '" + from + "' and '" + to + "';";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                patient_ids.add(rs.getString("patient_id"));
                first_names.add(rs.getString("first_name"));
                last_names.add(rs.getString("last_name"));
                whole_payment.add(rs.getString("whole_payment_amount"));
                paid_payment.add(rs.getString("paid_payment_amount"));
                dates.add(rs.getString("occupied_time_slot_date_ref"));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        model.PaymentsHistory.getInstance().clear();
        model.PaymentsHistory.getInstance().setPatient_ids(patient_ids);
        model.PaymentsHistory.getInstance().setFirst_names(first_names);
        model.PaymentsHistory.getInstance().setLast_names(last_names);
        model.PaymentsHistory.getInstance().setDates(dates);
        model.PaymentsHistory.getInstance().setWhole_payment(whole_payment);
        model.PaymentsHistory.getInstance().setPaid_payment(paid_payment);
    }


    // args: {"id"}
    private void getNextAppointmentTime(String[] args) throws SQLException {
        String id = args[0];
        String query = "select next_appointment_date from appointmentpaget\n" +
                "where patient_id = " + id + "\norder by next_appointment_date;";

        ArrayList<String> next_ap_date = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                next_ap_date.add(rs.getString("next_appointment_date"));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        model.AppointmentPage.getInstance().clear();
        model.AppointmentPage.getInstance().setNext_appointment_date(next_ap_date.get(next_ap_date.size() - 1));
    }


    // No args needed.
    private void getCurrentAvailableTimes() throws SQLException {
        model.CurrentDateAndTime.getInstance().clear();
        getCurrentDateTime();
        String current_date = model.CurrentDateAndTime.getInstance().getCurrent_date();
        String[] args = {current_date};
        model.AvailableTimeSlots.getInstance().clear();
        getAvailableTimeSlotsByDate(args);
    }


    // args: {"date"}
    private void getAvailableTimeSlotsByDate(String[] args) throws SQLException {
        getWeeklyScheduleByDate(args);
        String ref_from_date = model.WeeklySchedule.getInstance().getFrom_date();

        ArrayList<String> week_days = new ArrayList<>();
        ArrayList<String> begin_times = new ArrayList<>();
        ArrayList<String> duration = new ArrayList<>();
        ArrayList<String> to_times = new ArrayList<>();

        String query = "select * from availabletimeslotst\n" +
                "where weekly_schedule_from_date_ref = '" + ref_from_date + "';";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                week_days.add(rs.getString("day_of_week"));
                begin_times.add(rs.getString("begin_time"));
                duration.add(rs.getString("duration"));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        for (int i = 0; i < begin_times.size(); i++) {
            to_times.add(sumTime(begin_times.get(i), duration.get(i)));
        }
        model.AvailableTimeSlots.getInstance().clear();
        model.AvailableTimeSlots.getInstance().setBegin_times(begin_times);
        model.AvailableTimeSlots.getInstance().setEnd_times(to_times);
        model.AvailableTimeSlots.getInstance().setWeek_days(week_days);
    }


    // No args needed.
    private void getCurrentWeeklySchedule() throws SQLException {
        String query = "select current_date";
        String date = null;
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                date = rs.getString("date");
            }
            String[] date_args = {date};
            getWeeklyScheduleByDate(date_args);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // args: {"date"}
    private void getWeeklyScheduleByDate(String[] args) throws SQLException {
        String date = args[0];
        String query = "select * from weeklyschedulet\n" +
                "where '" + date + "' between from_date and to_date;";
        String from = null;
        String to = null;
        Statement stmt = null;

        System.out.println(query);

        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                from = rs.getString("from_date");
                to = rs.getString("to_date");
            }
            model.WeeklySchedule.getInstance().clear();
            model.WeeklySchedule.getInstance().setFrom_date(from);
            model.WeeklySchedule.getInstance().setTo_date(to);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // Some other functions use this.
    private String getWeekDayByDate(String date) throws SQLException {
        String query = "select extract(dow from date '" + date + "');";
        Statement stmt = null;
        String week_day = null;
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                week_day = rs.getString("date_part");
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        int index = parseInt(week_day);
        return days[index];
    }


    // No args needed
    private void getCurrentDateTime() throws SQLException {
        String query = "select current_date";
        String date = null;
        model.CurrentDateAndTime.getInstance().clear();
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                date = rs.getString("date");
            }
            String week_day = getWeekDayByDate(date);
            model.CurrentDateAndTime.getInstance().setCurrent_date(date);
            model.CurrentDateAndTime.getInstance().setCurrent_week_day(week_day);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        query = "select NOW();";
        stmt = null;
        String time = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                time = rs.getString("now");
            }
            time = time.substring(11, 19);
            model.CurrentDateAndTime.getInstance().setCurrent_time(time);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // args: {"webUser"} (webUser is 1 or 0)
    private void refreshOccupiedTimeSlots(String[] args) throws SQLException {
        String web = args[0];
        String query = null;

        if (web.equals("1")) {
            query = "select * from useroccupiedtimeslotst;";
        } else if (web.equals("0")) {
            query = "select * from occupiedtimeslotst;";
        }

        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> begin_time = new ArrayList<>();
        ArrayList<String> duration = new ArrayList<>();
        ArrayList<String> available_time_slots_ref_from_date = new ArrayList<>();
        ArrayList<String> available_time_slots_ref_week_day = new ArrayList<>();
        ArrayList<String> available_time_slots_ref_begin_time = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (web.equals("1")) {

                while (rs.next()) {

                }
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // args = {"fn", "ln", "id", "1" or "0"} (1 as in_debt and 0 as not in_debt)
    // Does: It fills the single instance of SearchResults.
    // "null" if an argument does not exist
    public void mainSearch(String[] args) throws Exception {
        String in_debt = args[3];
        boolean checked_in_debt = in_debt.equals("1");
        String query = null;
        if (checked_in_debt) {
            query = mainSearchInDebt(args);
        } else {
            query = mainSearchNotInDebt(args);
        }

        System.out.println(query);

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
        query = "select patient_id, first_name, last_name from patientt\nwhere ";
        query += "True";

        if (first_name != null) {
            query += " and " + first_name_condition;
        }
        if (last_name != null) {
            query += " and " + last_name_condition;
        }
        if (patient_id != null) {
            query += " and " + patient_id_condition;
        }
        query += ";";
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


    // args: {"id"}
    // Does: it fills the single instance of patient.
    private void refreshPatient(String[] args) throws SQLException {
        String id = args[0];
        String query = "select * from patientt\nwhere patient_id = " + id + ";";

        ArrayList<String> first_name = new ArrayList<>();
        ArrayList<String> last_name = new ArrayList<>();
        ArrayList<String> age = new ArrayList<>();
        ArrayList<String> gender = new ArrayList<>();
        ArrayList<String> occupation = new ArrayList<>();
        ArrayList<String> reference = new ArrayList<>();
        ArrayList<String> education = new ArrayList<>();
        ArrayList<String> homeAddr = new ArrayList<>();
        ArrayList<String> workAddr = new ArrayList<>();

        model.Patient.getInstance().clear();
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                first_name.add(rs.getString("first_name"));
                last_name.add(rs.getString("last_name"));
                age.add(rs.getString("age"));
                gender.add(rs.getString("gender"));
                occupation.add(rs.getString("occupation"));
                reference.add(rs.getString("reference"));
                education.add(rs.getString("education"));
                homeAddr.add(rs.getString("homeaddress"));
                workAddr.add(rs.getString("workaddress"));
            }
            model.Patient.getInstance().setFirst_name(first_name);
            model.Patient.getInstance().setLast_name(last_name);
            model.Patient.getInstance().setAge(age);
            model.Patient.getInstance().setEducation(education);
            model.Patient.getInstance().setOccupation(occupation);
            model.Patient.getInstance().setGender(gender);
            model.Patient.getInstance().setReference(reference);
            model.Patient.getInstance().setHomeAddr(homeAddr);
            model.Patient.getInstance().setWorkAddr(workAddr);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        ArrayList<String> phones = new ArrayList<>();
        query = "select * from patientphonest\n" +
                "where patient_id = " + id + ";";

        stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                phones.add(rs.getString("phone_number"));
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        model.Patient.getInstance().setPhones(phones);
    }


    // args: {"weekly_schedule_from_date_ref", "day_of_week", "begin_time", "to_time"}
    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    // Time format: HH:MM:SS
    private void handleAddNewAvailableTime(String[] args) throws Exception {
        String to_time = args[3];
        String begin_time = args[2];
        String duration = "'" + to_time + "'::time - '" + begin_time + "'::time";
        String query = "insert into availabletimeslotst values('" +
                args[0] + "', '" + args[1] + "', '" + args[2] + "', " + duration + ");";
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


    // args: {"date","begin_time", "to_time"}
    private void addOccupiedTimeSlot(String[] args) throws SQLException {
        String date = args[0];
        String begin_time = args[1];
        String to_time = args[2];
        String duration = "'" + to_time + "'::time - '" + begin_time + "'::time";
        String query = "insert into occupiedtimeslotst values('" + date + "', '" + begin_time + "', " + duration + ", '";

        model.WeeklySchedule.getInstance().clear();
        String[] from_date_args = {date};
        getWeeklyScheduleByDate(from_date_args);
        String from_date = model.WeeklySchedule.getInstance().getFrom_date();
        String week_day = getWeekDayByDate(date);
        String fk_begin_time = null;

        String query_on_availableTimeT = "select begin_time from availabletimeslotst\n" +
                "where weekly_schedule_from_date_ref = '" + from_date + "' and day_of_week = '" + week_day + "' and ('" +
                begin_time + "'::time between begin_time and (begin_time + duration::interval));";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query_on_availableTimeT);
            while (rs.next()) {
                fk_begin_time = rs.getString("begin_time");
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        query += from_date + "', '" + week_day + "', '" + fk_begin_time + "');";
        stmt = null;
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


    // args: {"from_date", "to_date}
    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    private void handleCreateNewWeeklySchedule(String[] args) throws Exception {
        String from = args[0];
        String to = args[1];
        String query = "insert into weeklyschedulet values('" + from + "', '" + to + "');";

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


    // args: {"date", "begin_time", "reason", "patient_id"}
    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    // Time format: HH:MM:SS
    private void handleAddReferralTime(String[] args) throws Exception {
        String query = "insert into referraloccupiedtimeslotst values('" + args[0] + "', '" +
                args[1] + "', '" + args[2] + "', '" + args[3] + "');";
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

    // args: {"date", "begin_time"}
    // Date format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    // Time format: HH:MM:SS
    private void handleCancelAppointment(String[] args) throws Exception {
        String date = args[0];
        String begin_time = args[1];
        String del_occ_query = "delete from occupiedtimeslotst \n" +
                "where date = '" + date + "' and begin_time = '" + begin_time + "';";
        String del_referral_query = "delete from referraloccupiedtimeslotst\n" +
                "where date = '" + date + "' and begin_time = '" + begin_time + "';";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(del_occ_query);
            stmt.executeUpdate(del_referral_query);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // args: Nothing (empty string)
    // does: It fills the single instance of SearchResult class.
    private void handleRefreshPatientsWhoOweMoney(String[] args) throws Exception {
        String[] searchArgs = {null, null, null, "1"};
        mainSearch(searchArgs);
    }


    // args: {} (empty array of Strings)
    // does: fills the single object of FileTable (fills arrayLists in matching order)
    private void handleRefreshListOfPatientFilesByCreationDate(String[] args) throws Exception {
        String query = "select * from filet\norder by creation_date;";
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> patientIds = new ArrayList<>();
            ArrayList<String> creation_dates = new ArrayList<>();
            while (rs.next()) {
                patientIds.add(rs.getString("patient_id"));
                creation_dates.add(rs.getString("creation_date"));
            }
            model.FileTable.getInstance().clear();
            model.FileTable.getInstance().setCreation_date(creation_dates);
            model.FileTable.getInstance().setPatient_id(patientIds);

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    private String sumTime(String t, String b) {
        int hr1 = parseInt(t.substring(0, 2));
        int hr2 = parseInt(b.substring(0, 2));
        int s1 = parseInt(t.substring(6, 8));
        int s2 = parseInt(b.substring(6, 8));
        int m1 = parseInt(t.substring(3, 5));
        int m2 = parseInt(b.substring(3, 5));


        int totalHours = hr1 + hr2;
        int totalMinutes = m1 + m2;
        int totalSeconds = s1 + s2;
        if (totalSeconds >= 60) {
            totalMinutes++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours++;
            totalMinutes = totalMinutes % 60;
        }
        if (totalHours > 23) {
            totalHours = totalHours - 24;
        }
        String h = Integer.toString(totalHours);
        String m = Integer.toString(totalMinutes);
        String s = Integer.toString(totalSeconds);
        String z = "0";
        if (h.length() < 2) {
            h = z + h;
        }
        if (m.length() < 2) {
            m = z + m;
        }
        if (s.length() < 2) {
            s = z + s;
        }
        String sum = h + ":" + m + ":" + s;
        return sum;
    }


    // args: {"id", "page_no"}
    // does: fills the single object of the relative page (eg: AppointmentPage)
    private void handleRefreshPage(String[] args) throws Exception {
        String id = args[0];
        String pn = args[1];
        String query_on_personalinfopaget = "select * from personalinfopaget\nwhere patient_id = " + id + " and page_no = 1;";
        String query_on_appointmentpaget = "select * from appointmentpaget\nwhere patient_id = " + id + " and page_no = " +
                pn + ";";
        String query_on_medicalimagepaget = "select * from medicalimagepaget\nwhere patient_id = " + id + " and page_no = " +
                pn + ";";
        Statement stmt = null;
        System.out.println(pn);

        try {
            stmt = current_connection.createStatement();
            if (pn.equals("1")) {
                System.out.println(query_on_personalinfopaget);


                ResultSet rs = stmt.executeQuery(query_on_personalinfopaget);
                model.PersonalInfoPage.getInstance().clear();
                while (rs.next()) {
                    model.PersonalInfoPage.getInstance().setPatient_id(id);
                    model.PersonalInfoPage.getInstance().setDoes_smoke(rs.getString("does_smoke"));
                    model.PersonalInfoPage.getInstance().setDental_records(rs.getString("dental_records"));
                    model.PersonalInfoPage.getInstance().setGeneral_medical_records(rs.getString("general_medical_records"));
                    model.PersonalInfoPage.getInstance().setSensitive_medicine(rs.getString("sensitive_medicine"));
                    model.PersonalInfoPage.getInstance().setSignature_image_address(rs.getString("signature_image_address"));
                }
            } else {
                ResultSet rs_app = stmt.executeQuery(query_on_appointmentpaget);
                model.AppointmentPage.getInstance().clear();
                while (rs_app.next()) {
                    model.AppointmentPage.getInstance().setPatient_id(rs_app.getString("patient_id"));
                    model.AppointmentPage.getInstance().setPage_no(rs_app.getString("page_no"));
                    model.AppointmentPage.getInstance().setTreatment_summary(rs_app.getString("treatment_summary"));
                    model.AppointmentPage.getInstance().setNext_appointment_date(rs_app.getString("next_appointment_date"));
                    model.AppointmentPage.getInstance().setWhole_payment_amount(rs_app.getString("whole_payment_amount"));
                    model.AppointmentPage.getInstance().setPaid_payment_amount(rs_app.getString("paid_payment_amount"));
                    model.AppointmentPage.getInstance().setDate(rs_app.getString("occupied_time_slot_date_ref"));
                    model.AppointmentPage.getInstance().setFrom(rs_app.getString("occupied_time_slot_begin_time_ref"));
                }
                if (model.AppointmentPage.getInstance().getPage_no() != null) {
                    String query_on_occ = "select begin_time, duration from occupiedtimeslotst\n" +
                            "where date = '" + model.AppointmentPage.getInstance().getDate() + "' " +
                            "and begin_time = '" + model.AppointmentPage.getInstance().getFrom() + "';";
                    ResultSet rs_occ = stmt.executeQuery(query_on_occ);
                    model.OccupiedTimeSlots.getInstance().clear();
                    String d = null;
                    String from = null;
                    while (rs_occ.next()) {
                        from = rs_occ.getString("begin_time");
                        d = rs_occ.getString("duration");
                    }
                    String to = sumTime(from, d);
                    model.AppointmentPage.getInstance().setTo(to);
                }

                ResultSet rs_medicalImage = stmt.executeQuery(query_on_medicalimagepaget);
                model.MedicalImagePage.getInstance().clear();
                while (rs_medicalImage.next()) {
                    model.MedicalImagePage.getInstance().setPatient_id(rs_medicalImage.getString("patient_id"));
                    model.MedicalImagePage.getInstance().setContent_address(rs_medicalImage.getString("content_address"));
                    model.MedicalImagePage.getInstance().setImage_type(rs_medicalImage.getString("image_type"));
                    model.MedicalImagePage.getInstance().setReason(rs_medicalImage.getString("reason"));
                    model.MedicalImagePage.getInstance().setPage_no(rs_medicalImage.getString("page_no"));
                }
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    // args: {"id"}
    // does: fills the single object of FileSummary
    private void handleRefreshFileSummary(String[] args) throws Exception {
        String id = args[0];
        ArrayList<String> medicalImagePage_page_numbers = new ArrayList<>();
        ArrayList<String> appointmentPage_page_numbers = new ArrayList<>();
        ArrayList<String> medicalImagePage_creation_dates = new ArrayList<>();
        ArrayList<String> appointmentPage_creation_dates = new ArrayList<>();

        String query_AP = "select page_no, creation_date\n" +
                "from paget natural join appointmentpaget\n" +
                "where patient_id = " + id + ";";
        String query_MIP = "select page_no, creation_date\n" +
                "from paget natural join medicalimagepaget\n" +
                "where patient_id = " + id + ";";

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs_AP = stmt.executeQuery(query_AP);
            ResultSet rs_MIP = stmt.executeQuery(query_MIP);
            while (rs_AP.next()) {
                appointmentPage_page_numbers.add(rs_AP.getString("page_no"));
                appointmentPage_creation_dates.add(rs_AP.getString("creation_date"));
            }
            while (rs_MIP.next()) {
                medicalImagePage_page_numbers.add(rs_MIP.getString("page_no"));
                medicalImagePage_creation_dates.add(rs_MIP.getString("creation_date"));
            }

            model.FileSummary.getInstance().clear();
            model.FileSummary.getInstance().setAppointmentPage_creation_dates(appointmentPage_creation_dates);
            model.FileSummary.getInstance().setAppointmentPage_page_numbers(appointmentPage_page_numbers);
            model.FileSummary.getInstance().setMedicalImagePage_creation_dates(medicalImagePage_creation_dates);
            model.FileSummary.getInstance().setMedicalImagePage_page_numbers(medicalImagePage_page_numbers);

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


    // args: {"id", "page_no"}
    private void handleDeletePage(String[] args) throws Exception {
        String patient_id = args[0];
        String page_no = args[1];
        Statement stmt = null;
        String query_page = "delete from paget\n" + "\twhere patient_id = " + patient_id + " and page_no = " + page_no + ";";
        String query_personal = "delete from personalinfopaget\nwhere patient_id = " + patient_id + " and page_no = " + page_no + ";";
        String query_appointment = "delete from appointmentpaget\nwhere patient_id = " + patient_id + " and page_no = " + page_no + ";";
        String query_medical = "delete from medicalimagepaget\nwhere patient_id = " + patient_id + " and page_no = " + page_no + ";";
        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(query_medical);
            stmt.executeUpdate(query_personal);
            stmt.executeUpdate(query_appointment);
            stmt.executeUpdate(query_page);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
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
            query += "signature_image_address = '" + args[6] + "'";
        } else {
            query += "signature_image_address = " + args[6];
        }
        query += "\nwhere patient_id = " + args[0] + " and page_no = 1";

        System.out.println(query);

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


    // args: {"id", "page_no", "treatment_summary", "next_appointment_date", "whole_payment_amount", "paid_payment_amount",
    // "occupied_time_slot_date_ref", "occupied_time_slot_begin_time_ref"}
    private void handleEditAppointmentPage(String[] args) throws Exception {
        String query = "update appointmentpaget\nset ";
        if (!args[2].toLowerCase().equals("null")) {
            query += "treatment_summary = '" + args[2] + "', ";
        } else {
            query += "treatment_summary = " + args[2] + ", ";
        }
        if (!args[3].toLowerCase().equals("null")) {
            query += "next_appointment_date = '" + args[3] + "', ";
        } else {
            query += "next_appointment_date = " + args[3] + ", ";
        }
        if (!args[4].toLowerCase().equals("null")) {
            query += "whole_payment_amount = '" + args[4] + "', ";
        } else {
            query += "whole_payment_amount = " + args[4] + ", ";
        }
        if (!args[5].toLowerCase().equals("null")) {
            query += "paid_payment_amount = '" + args[5] + "'";
        } else {
            query += "paid_payment_amount = " + args[5];
        }
//        if (!args[6].toLowerCase().equals("null")) {
//            query += "occupied_time_slot_date_ref = '" + args[6] + "', ";
//        } else {
//            query += "occupied_time_slot_date_ref = " + args[6] + ", ";
//        }
//        if (!args[7].toLowerCase().equals("null")) {
//            query += "occupied_time_slot_begin_time_ref = '" + args[7] + "'";
//        } else {
//            query += "occupied_time_slot_begin_time_ref = " + args[7];
//        }
        query += "\nwhere patient_id = " + args[0] + " and page_no = " + args[1] + ";";

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


    // args: {"id", "page_no", "content_address", "image_type", "reason"}
    private void handleEditMedicalImagePage(String[] args) throws Exception {
        String query = "update medicalimagepaget\nset ";
        if (!args[2].toLowerCase().equals("null")) {
            query += "content_address = '" + args[2] + "', ";
        } else {
            query += "content_address = " + args[2] + ", ";
        }
        if (!args[3].toLowerCase().equals("null")) {
            query += "image_type = '" + args[3] + "', ";
        } else {
            query += "image_type = " + args[3] + ", ";
        }
        if (!args[4].toLowerCase().equals("null")) {
            query += "reason = '" + args[4] + "'";
        } else {
            query += "reason = " + args[4];
        }
        query += "\nwhere patient_id = " + args[0] + " and page_no = " + args[1] + ";";

        System.out.println(query);


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

    // args: {"id", "page_no", "treatment_summary", "next_appointment_date", "whole_payment_amount", "paid_payment_amount",
    // "occupied_time_slot_date_ref", "occupied_time_slot_begin_time_ref", "creation_date"}
    private void handleAddAppointmentPage(String[] args) throws Exception {
        String addPageQuery = "insert into paget values(" +
                args[0] + ", " + args[1] + ", '" + args[8] + "');";
        Statement stmt = null;

        System.out.println(addPageQuery);

        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(addPageQuery);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        String query = "insert into appointmentpaget values(";
        String q = "'";
        for (int i = 0; i < 8; i++) {
            if (args[i].toLowerCase().equals("null")) {
                if (i == 7) {
                    String adding = args[i];
                    query += adding;
                } else {
                    String adding = args[i] + ", ";
                    query += adding;
                }
            } else {
                if (i == 7) {
                    String adding = q + args[i] + q;
                    query += adding;
                } else {
                    String adding = q + args[i] + q + ", ";
                    query += adding;
                }
            }
        }
        query += ");";
        Statement stmt2 = null;

        System.out.println(query);

        try {
            stmt2 = current_connection.createStatement();
            stmt2.executeUpdate(query);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt2 != null) {
                stmt2.close();
            }
        }
    }


    // Personal info page is created while creating the file. This function is empty.
    private void handleAddPersonalInfoPage(String[] args) throws Exception {
    }


    // args: {"id", "page_no", "content_address", "image_type", "reason", "creation_date"}
    private void addMedicalImagePage(String[] args) throws Exception {
        String addPageQuery = "insert into paget values(" +
                args[0] + ", " + args[1] + ", '" + args[5] + "');";
        System.out.println(addPageQuery);
        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            stmt.executeUpdate(addPageQuery);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        String query = "insert into medicalimagepaget values(";
        String q = "'";
        for (int i = 0; i < 5; i++) {
            if (args[i].toLowerCase().equals("null")) {
                if (i == 4) {
                    String adding = args[i];
                    query += adding;
                } else {
                    String adding = args[i] + ", ";
                    query += adding;
                }
            } else {
                if (i == 4) {
                    String adding = q + args[i] + q;
                    query += adding;
                } else {
                    String adding = q + args[i] + q + ", ";
                    query += adding;
                }
            }
        }
        query += ");";
        System.out.println("----");
        System.out.println(query);
        System.out.println("----");
        Statement stmt2 = null;
        try {
            stmt2 = current_connection.createStatement();
            stmt2.executeUpdate(query);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt2 != null) {
                stmt2.close();
            }
        }
    }


    // args: {"id", "fn", "ln", "age", "gander", "occupation", "ref", "edu', "homeAddr", "workAddr",
    // "generalMedicalRec", "dentalRec", "sensitiveMed", "smoke", "signatureAddr", "fileCreationDate",
    // "phone_number", otherPhoneNumbers...}
    // fileCreationDate format: MM/DD/YYYY or YYYY-MM-DD both are ok.
    private void handleCreateFile(String[] args) throws Exception {
        String id = args[0];
        String add_patient_query = InsertPatientQuery(args);
        String add_page_in_page_table_query = addPageQueryForPersonalInfoPage(args);
        String add_personal_info_page_query = addPersonalInfoPageQuery(args);

        for(int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.println("\n===");
        System.out.println(add_patient_query);
        System.out.println("----");
        System.out.println(add_page_in_page_table_query);
        System.out.println("----");
        System.out.println(add_personal_info_page_query);
        System.out.println("----");
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
        addPhones(args, id);
    }

    // CreateFile uses this function.
    private void addPhones(String[] args, String id) throws SQLException {
        int first = 16;
        ArrayList<String> queries = new ArrayList<>();
        for (int i = first; i < args.length; i++) {
            queries.add("insert into patientphonest values('" + id + "', '" + args[i] + "');");
        }
        Statement stmt = null;
        stmt = current_connection.createStatement();
        try {
            for (int i = 0; i < queries.size(); i++) {
                stmt.executeUpdate(queries.get(i));
            }
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

    // args: {"id"}
    private void refreshReferralWithNoAppointmentPage(String[] args) throws SQLException {
        String id = args[0];

        String main_query = "select *\n" +
                "\tfrom referraloccupiedtimeslotst as T1\n" +
                "    where T1.patient_id = " + id + " and not exists(\n" +
                "        select *\n" +
                "        from appointmentPageT as T2\n" +
                "        where T2.occupied_time_slot_date_ref = T1.date and\n" +
                "        \tT2.occupied_time_slot_begin_time_ref = T1.begin_time and\n" +
                "        T2.patient_id = T1.patient_id);";


//        String query_on_ref = "select * from referraloccupiedtimeslotst\n" +
//                "where patient_id = " + id + ";";
//
//        String query_on_ap = "select occupied_time_slot_date_ref, occupied_time_slot_begin_time_ref from appointmentpaget\n" +
//                "where patient_id = " + id + ";";

        ArrayList<String> ref_dates = new ArrayList<>();
        ArrayList<String> ref_begin_times = new ArrayList<>();
        ArrayList<String> ref_reasons = new ArrayList<>();

//        ArrayList<String> ap_dates = new ArrayList<>();
//        ArrayList<String> ap_begin_times = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = current_connection.createStatement();
            ResultSet rs = stmt.executeQuery(main_query);
            while (rs.next()) {
                ref_dates.add(rs.getString("date"));
                ref_begin_times.add(rs.getString("begin_time"));
                ref_reasons.add(rs.getString("reason"));
            }

//            stmt = current_connection.createStatement();
//            ResultSet rs2 = stmt.executeQuery(query_on_ap);
//            while (rs.next()) {
//                ap_dates.add(rs2.getString("occupied_time_slots_date_ref"));
//                ap_begin_times.add(rs2.getString("occupied_time_slot_begin_time_ref"));
//            }
//
//            ArrayList<Integer> toRemoveIndices = new ArrayList<>();
//            for (int i = 0; i < ref_begin_times.size(); i++) {
//                for (int j = 0; j < ap_begin_times.size(); j++) {
//                    if (ref_dates.get(i).equals(ap_dates.get(j)) && ref_begin_times.get(i).equals(ap_begin_times.get(j))) {
//                        toRemoveIndices.add(i);
//                    }
//                }
//            }
//            for(int index : toRemoveIndices) {
//                ref_dates.remove(index);
//                ref_begin_times.remove(index);
//                ref_reasons.remove(index);
//            }
            model.ReferralOccupiedTimeSlots.getInstance().clear();
            model.ReferralOccupiedTimeSlots.getInstance().setBegin_time(ref_begin_times);
            model.ReferralOccupiedTimeSlots.getInstance().setDate(ref_dates);
            model.ReferralOccupiedTimeSlots.getInstance().setReason(ref_reasons);

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }


}
