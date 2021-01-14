package controller.graphics.files;

import controller.graphics.EditablePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AppointmentPage implements Initializable, EditablePage {
    @FXML
    private TextArea treatmentSummaryTextArea;

    @FXML
    private TextField nextAppointmentDateTextField;

    @FXML
    private TextField paidAmountTextField;

    @FXML
    private Label wholeAmountLabel;

    @FXML
    private Label appointmentFromDateAndTime;

    @FXML
    private Label appointmentToDateAndTime;

    @FXML
    private Button editButton;

    @FXML
    private void editButtonPress(ActionEvent event) {
        if (editButton.getText().equals("Edit"))  {
            switchEditing(true);
            editButton.setText("Submit");
        } else if (editButton.getText().equals("Submit")) {
            switchEditing(false);
            // TODO : query to edit
            // TODO : query to get changes
            editButton.setText("Edit");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addComponents(Arrays.asList(treatmentSummaryTextArea, nextAppointmentDateTextField, paidAmountTextField));
        switchEditing(false);
    }
}
