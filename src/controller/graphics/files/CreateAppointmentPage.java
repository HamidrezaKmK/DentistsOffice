package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import model.ReferralOccupiedTimeSlots;
import view.FxmlFileLoader;
import view.PopUpCreater;
import view.files.FilesGUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;



public class CreateAppointmentPage implements Initializable {

    @FXML
    private TextArea treatmentSummaryTextArea;

    @FXML
    private TextField nextAppointmentTextField;

    @FXML
    private TextField paidAmountTextField;

    @FXML
    private TextField wholeAmountTextField;

    @FXML
    private ChoiceBox referralTimeList = new ChoiceBox();

    private ArrayList<String> refDates = new ArrayList<>();

    private ArrayList<String> refStartTimes = new ArrayList<>();

    @FXML
    private AnchorPane mainPane;
    private int patientID;

    @FXML
    private void addNewAppointmentPageButtonPress(ActionEvent event) {
        PersonalFile controller = (PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController();
        PersonalFile pf = FXMLLoadersCommunicator.getLoader("PersonalFile").getController();
        int patientID = Integer.valueOf(pf.getPatientID());
        int pageCnt = pf.getPageCount() + 1;

        // TODO: should be global date instead of local date
        int selectedIndex = referralTimeList.getSelectionModel().getSelectedIndex();

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_APPOINTMENT_PAGE, Integer.toString(patientID), Integer.toString(pageCnt),
                    treatmentSummaryTextArea.getText(), nextAppointmentTextField.getText(), wholeAmountTextField.getText(),
                    paidAmountTextField.getText(), refDates.get(selectedIndex), refStartTimes.get(selectedIndex), java.time.LocalDate.now().toString());
        } catch (Exception e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
        ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).refereshPagesList();
        ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).pageAdded();

        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
        ((PersonalInfoPage) FXMLLoadersCommunicator.getLoader("PersonalInfoPage").getController()).refreshPage("1", Integer.toString(patientID));
        mainPane.getChildren().add(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setChoices() {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_REFERRALS_WITHOUT_APPOINTMENT_PAGE, Integer.toString(patientID));
            System.err.println("HOOOO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.ReferralOccupiedTimeSlots inst = ReferralOccupiedTimeSlots.getInstance();
        for (int i = 0; i < inst.getBegin_time().size(); i++) {
            String beginTime = inst.getBegin_time().get(i);
            String date = inst.getDate().get(i);
            //int reasonsSz = inst.getReason().size();
            String reason = inst.getReason().get(i);
            //.substring(0, Math.min(10, reasonsSz - 1)) + "...";
            referralTimeList.getItems().add(date + " " + beginTime + " " + reason);
            refDates.add(date);
            refStartTimes.add(beginTime);
        }
    }
}
