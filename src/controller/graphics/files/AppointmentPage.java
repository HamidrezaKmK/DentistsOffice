package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.EditablePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.QueryType;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable, EditablePage {
    int pageNo, patientID;

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
        if (editButton.getText().equals("Edit"))  {
            switchEditing(true);
            editButton.setText("Submit");
        } else if (editButton.getText().equals("Submit")) {
            switchEditing(false);
            submit();
            refreshPage();
            editButton.setText("Edit");
        }
    }

    private void submit() {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.EDIT_APPOINTMENT_PAGE, Integer.toString(patientID), Integer.toString(pageNo),
                    treatmentSummaryTextArea.getText(), nextAppointmentDateTextField.getText(), wholeAmountLabel.getText(), paidAmountTextField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
}
