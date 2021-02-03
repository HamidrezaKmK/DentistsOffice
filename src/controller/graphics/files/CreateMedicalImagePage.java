package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import view.FxmlFileLoader;
import view.PopUpCreater;

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
        controller.refereshPagesList();
        int pageNo = controller.getPageCount() + 1;
        String patientID = controller.getPatientID();

        // TODO: add medical image address
        // TODO: insert global date from dbms instead of localDate
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_MEDICAL_IMAGE_PAGE, patientID, Integer.toString(pageNo),
                    "./chiz", imageTypeChoiceBox.getValue(), explanationTextArea.getText(), java.time.LocalDate.now().toString());
        } catch (Exception e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
        }
        controller.pageAdded();

        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
        mainPane.getChildren().add(view);
        controller.refereshPagesList();
        ((PersonalInfoPage) FXMLLoadersCommunicator.getLoader("PersonalInfoPage").getController()).refreshPage("1", patientID);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageTypeChoiceBox.getItems().add("CT-Scan");
        imageTypeChoiceBox.getItems().add("OPG");
        imageTypeChoiceBox.getItems().add("Radiographic-Image");

    }
}
