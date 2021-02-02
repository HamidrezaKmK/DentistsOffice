package controller.graphics.booking;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
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

import java.net.URL;
import java.util.ResourceBundle;

public class BookingMenu implements Initializable {

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
    private void addTimeToScheduleButtonPress(ActionEvent event) {
        // TODO: add time to schedule
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_OCCUPIED_TIME_SLOT, enterOccupiedTimeDate.getText(),
                    enterOccupiedTimeBeginTime.getText(), enterOccupiedTimeEndTime.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!enterReferralTimePatientID.getText().isEmpty()) {
            try {
                DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_REFERRAL_TIME, enterOccupiedTimeDate.getText(),
                        enterOccupiedTimeBeginTime.getText(), enterOccupiedTimeReason.getText(), enterReferralTimePatientID.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scheduleQueryEnterButtonPress(event);
    }

    @FXML
    private void cancelAppointmentButtonPress(ActionEvent event) {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.CANCEL_APPOINTMENT,
                    selectedTimeDateTextField.getText(), selectedTimeBeginTimeTextField.getText(), "0");
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
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
        scheduleQueryFromDateTextField.setText("2021-01-02 09:00:00");
        scheduleQueryToDateTextField.setText("2021-02-21 09:00:00");
        refreshCurrentSchedule();
    }

    public void selectOccupiedTimeOnTimeLine(TimeInterval interval) {
        selectedTimeDateTextField.setText(interval.beginDate.toString());
        selectedTimeBeginTimeTextField.setText(interval.beginTime.toString());
        selectedTimeEndTimeTextField.setText(interval.endTime.toString());
    }

    public void refreshCurrentSchedule() {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.GET_CURRENT_WEEKLY_SCHEDULE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.WeeklySchedule inst = model.WeeklySchedule.getInstance();
        fromDateLabel.setText(inst.getFrom_date());
        toDateLabel.setText(inst.getTo_date());
        // TODO: this query should consider database date not local date
        // TODO: this query should be implemented

    }
}
