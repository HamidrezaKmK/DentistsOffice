package controller.graphics.files;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;

public class CreateMedicalImagePage {



    @FXML
    private void addPageButtonPress(ActionEvent event) {
        PersonalFile controller = (PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController();

        controller.addPage("medical page");
    }
}
