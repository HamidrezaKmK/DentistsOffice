package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.EditablePage;
import controller.graphics.FXMLLoadersCommunicator;
import controller.graphics.StageSavable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.QueryType;
import view.FxmlFileLoader;
import view.PopUpCreater;
import view.PopUpSQLError;
import view.popups.PopupsGUI;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable, EditablePage, StageSavable {
    int pageNo, patientID;
    AnchorPane savedPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextArea treatmentSummaryTextArea;

    @FXML
    private TextField nextAppointmentDateTextField;

    @FXML
    private TextField paidAmountTextField;

    @FXML
    private Label wholeAmountLabel;

    @FXML
    private Label appointmentDateLabel;

    @FXML
    private Label appointmentFromTimeLabel;

    @FXML
    private Label appointmentToTimeLabel;

    @FXML
    private Button editButton;

    String ref1, ref2;

    @FXML
    private void editButtonPress(ActionEvent event) {
        try {
            saveStage();

            if (editButton.getText().equals("Edit")) {
                switchEditing(true);
                editButton.setText("Submit");
            } else if (editButton.getText().equals("Submit")) {
                switchEditing(false);
                editButton.setText("Edit");
                submit();
                refreshPage();
            }
        } catch (SQLException e) {
            loadStage();
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
    }

    private void submit() throws SQLException {

        DataBaseQueryController.getInstance().handleQuery(QueryType.EDIT_APPOINTMENT_PAGE, Integer.toString(patientID), Integer.toString(pageNo),
                treatmentSummaryTextArea.getText(), nextAppointmentDateTextField.getText(), wholeAmountLabel.getText(), paidAmountTextField.getText());



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addComponents(Arrays.asList(treatmentSummaryTextArea, nextAppointmentDateTextField, paidAmountTextField));
        switchEditing(false);
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPatientID(String patientID) {
        this.patientID = Integer.valueOf(patientID);
    }

    public void refreshPage() {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_PAGE, Integer.toString(patientID), Integer.toString(pageNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.AppointmentPage inst = model.AppointmentPage.getInstance();
        treatmentSummaryTextArea.setText(inst.getTreatment_summary());
        nextAppointmentDateTextField.setText(inst.getNext_appointment_date());
        paidAmountTextField.setText(inst.getPaid_payment_amount());
        wholeAmountLabel.setText(inst.getWhole_payment_amount());

        appointmentDateLabel.setText(inst.getDate());
        appointmentFromTimeLabel.setText(inst.getFrom());
        appointmentToTimeLabel.setText(inst.getTo());

    }

    @Override
    public void saveStage() {
        savedPane = mainPane;
    }

    @Override
    public void loadStage() {
        mainPane = savedPane;
        refreshPage();
    }
}
