package controller.graphics.files;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
        // TODO : add appointment page query
        System.out.println(treatmentSummaryTextArea.getText());
        System.out.println(paidAmountTextField.getText());
        controller.addPage("appointment page");

        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
        mainPane.getChildren().add(view);
    }

}
