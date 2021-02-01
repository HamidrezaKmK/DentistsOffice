package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import view.FxmlFileLoader;
import view.files.FilesGUI;


public class CreateAppointmentPage {

    @FXML
    private TextArea treatmentSummaryTextArea;

    @FXML
    private TextField nextAppointmentTextField;

    @FXML
    private TextField paidAmountTextField;

    @FXML
    private TextField wholeAmountTextField;

    @FXML
    private TextField appointmentDateFromTextField;

    @FXML
    private TextField appointmentDateToTextField;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void addNewAppointmentPageButtonPress(ActionEvent event) {
        PersonalFile controller = (PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController();
        PersonalFile pf = FXMLLoadersCommunicator.getLoader("PersonalFile").getController();
        int patientID = Integer.valueOf(pf.getPatientID());
        int pageCnt = pf.getPageCount() + 1;

        // TODO: get occupied times which have no appointment page referenced to it

        // TODO: add page not complete
//        DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_APPOINTMENT_PAGE, Integer.toString(patientID), Integer.toString(pageCnt),
//                treatmentSummaryTextArea.getText(), nextAppointmentTextField.getText(), wholeAmountTextField.getText(),
//                paidAmountTextField.getText(), , ?? );



        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
        mainPane.getChildren().add(view);
    }

}
