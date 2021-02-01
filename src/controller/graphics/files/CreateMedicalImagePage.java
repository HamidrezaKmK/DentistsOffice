package controller.graphics.files;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;

import java.net.URL;
import java.util.ResourceBundle;


public class CreateMedicalImagePage implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextArea explanationTextArea;

    @FXML
    private ChoiceBox<String> imageTypeChoiceBox;

    @FXML
    private void addPageButtonPress(ActionEvent event) {
        PersonalFile controller = (PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController();
        // TODO: add medical page query to database
        controller.addPage("medical page");

        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
        mainPane.getChildren().add(view);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageTypeChoiceBox.getItems().add("CT-Scan");
        imageTypeChoiceBox.getItems().add("OPG");
        imageTypeChoiceBox.getItems().add("Radiographic-Image");

    }
}
