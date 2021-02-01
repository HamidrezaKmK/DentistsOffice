package controller.graphics.files;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.booking.BookingGUI;

public class SelectNewPageType {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void createMedicalImagePageButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("CreateMedicalImagePage", view.files.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    @FXML
    private void createAppointmentPageButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("CreateAppointmentPage", view.files.FilesGUI.class);
        String patientID = ((PersonalFile)FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).getPatientID();
        ((CreateAppointmentPage) FXMLLoadersCommunicator.getLoader("CreateAppointmentPage").getController()).setPatientID(Integer.valueOf(patientID));
        ((CreateAppointmentPage) FXMLLoadersCommunicator.getLoader("CreateAppointmentPage").getController()).setChoices();

        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }
}
