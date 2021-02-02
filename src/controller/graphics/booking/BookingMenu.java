package controller.graphics.booking;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import controller.graphics.StageSavable;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import model.Schedule;
import model.TimeInterval;
import view.FxmlFileLoader;
import view.PopUpCreater;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BookingMenu implements Initializable, StageSavable {

    private AnchorPane savedPane;

    @FXML
    private ScrollPane timeLinePane = new ScrollPane();

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @FXML
    private Button addWeeklyScheduleButton;

    @FXML
    private TextField scheduleQueryFromDateTextField;

    @FXML
    private TextField scheduleQueryToDateTextField;

    @FXML
    private TextField selectedTimeDateTextField;

    @FXML
    private TextField selectedTimeBeginTimeTextField;

    @FXML
    private TextField selectedTimeEndTimeTextField;

    @FXML
    private TextField enterOccupiedTimeDate = new TextField();

    @FXML
    private TextField enterOccupiedTimeBeginTime = new TextField();

    @FXML
    private TextField enterOccupiedTimeEndTime = new TextField();

    @FXML
    private TextArea enterOccupiedTimeReason = new TextArea();

    @FXML
    private TextField enterReferralTimePatientID = new TextField();

    @FXML
    private Label fromDateLabel = new Label();

    @FXML
    private Label toDateLabel = new Label();


    @FXML
    ListView saturdayListView = new ListView();

    @FXML
    ListView sundayListView = new ListView();

    @FXML
    ListView mondayListView = new ListView();

    @FXML
    ListView tuesdayListView = new ListView();

    @FXML
    ListView wednesdayListView = new ListView();

    @FXML
    ListView thursdayListView = new ListView();

    @FXML
    ListView fridayListView = new ListView();

    @FXML
    TextField dateInputTextField = new TextField();

    ArrayList<String>daysNames = new ArrayList<>();

    ArrayList<ListView> daysListViews = new ArrayList<>();

    @FXML
    private void getScheduleButtonPress(ActionEvent event) {
        refreshCurrentSchedule();
    }

    @FXML
    private void addTimeToScheduleButtonPress(ActionEvent event) {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_OCCUPIED_TIME_SLOT, enterOccupiedTimeDate.getText(),
                    enterOccupiedTimeBeginTime.getText(), enterOccupiedTimeEndTime.getText());
        } catch (SQLException e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
        if (!enterReferralTimePatientID.getText().isEmpty()) {
            try {
                DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_REFERRAL_TIME, enterOccupiedTimeDate.getText(),
                        enterOccupiedTimeBeginTime.getText(), enterOccupiedTimeReason.getText(), enterReferralTimePatientID.getText());
            } catch (SQLException e) {
                PopUpCreater.createSQLErrorPopUp(e.getMessage());
            }
        }
        scheduleQueryEnterButtonPress(event);
    }

    @FXML
    private void cancelAppointmentButtonPress(ActionEvent event) {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.CANCEL_APPOINTMENT,
                    selectedTimeDateTextField.getText(), selectedTimeBeginTimeTextField.getText(), "0");
        } catch (SQLException e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
        scheduleQueryEnterButtonPress(event);
    }


    @FXML
    private void scheduleQueryEnterButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("ScheduleTimeLine", view.booking.BookingGUI.class);
        timeLinePane.setContent(view);

        ScheduleTimeLine timelineController = (ScheduleTimeLine) FXMLLoadersCommunicator.getLoader("ScheduleTimeLine").getController();
        String fromDate, fromTime, toDate, toTime;
        {
            String[] parsedParts = scheduleQueryFromDateTextField.getText().trim().split(" ");
            fromDate = parsedParts[0];
            fromTime = parsedParts[1];
            parsedParts = scheduleQueryToDateTextField.getText().trim().split(" ");
            toDate = parsedParts[0];
            toTime = parsedParts[1];
        }
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_SCHEDULE_IN_TIME_INTERVAL, fromDate, fromTime, toDate, toTime);
        } catch (SQLException e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
        timelineController.createTimeLine(Schedule.getInstance());
    }

    @FXML
    private void addWeeklyScheduleButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("AddWeeklySchedule", view.booking.BookingGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        daysListViews.addAll(Arrays.asList(saturdayListView, sundayListView, mondayListView, tuesdayListView, wednesdayListView,
                thursdayListView, fridayListView));
        daysNames.addAll(Arrays.asList(
                "SAT",
                "SUN",
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI"
        ));

        DataBaseQueryController dbcontroller = DataBaseQueryController.getInstance();
        dbcontroller.setUsername("postgres");
        dbcontroller.setPassword("dibimibi");
        dbcontroller.setUrl("jdbc:postgresql://localhost:5432/DentistOfficeDB");
        try {
            dbcontroller.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection successfully created!");

        scheduleQueryFromDateTextField.setText("2021-01-02 00:00:00");
        scheduleQueryToDateTextField.setText("2021-02-21 00:00:00");

        refreshCurrentSchedule();
    }

    public void selectOccupiedTimeOnTimeLine(TimeInterval interval) {
        selectedTimeDateTextField.setText(interval.beginDate.toString());
        selectedTimeBeginTimeTextField.setText(interval.beginTime.toString());
        selectedTimeEndTimeTextField.setText(interval.endTime.toString());
    }

    public void refreshCurrentSchedule() {

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.GET_CURRENT_DATE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.WeeklySchedule inst = model.WeeklySchedule.getInstance();
        String currentDate = model.CurrentDateAndTime.getInstance().getCurrent_date();

        String chosenDate = currentDate;
        if (!dateInputTextField.getText().isEmpty())
            chosenDate = dateInputTextField.getText();


        System.out.println(currentDate);

        System.out.println(chosenDate);

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.GET_WEEKLY_SCHEDULE_BY_DATE, chosenDate);
            DataBaseQueryController.getInstance().handleQuery(QueryType.GET_AVAILABLE_TIMES_BY_DATE,
                    chosenDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.AvailableTimeSlots inst1 = model.AvailableTimeSlots.getInstance();
        int sz = inst1.getWeek_days().size();
        for (int i = 0; i < 7; i++) {
            daysListViews.get(i).getItems().clear();
        }
        for (int i = 0; i < sz; i++) {
            String weekDay = inst1.getWeek_days().get(i);
            String beginTime = inst1.getBegin_times().get(i);
            String endTime = inst1.getEnd_times().get(i);

            int id = daysNames.indexOf(weekDay);
            daysListViews.get(id).getItems().add(beginTime + " - " + endTime);
        }
        for (int i = 0; i < 7; i++) {
            daysListViews.get(i).getItems().add(" ");
        }

        fromDateLabel.setText(inst.getFrom_date());
        toDateLabel.setText(inst.getTo_date());
    }

    @Override
    public void saveStage() {
        savedPane = mainPane;
    }

    @Override
    public void loadStage() {
        mainPane = savedPane;
    }
}
