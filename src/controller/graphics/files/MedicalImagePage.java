package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.EditablePage;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import model.QueryType;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MedicalImagePage implements Initializable, EditablePage {

    private int pageNo;
    private int patientID;

    @FXML
    private TextArea explanationTextArea;

    @FXML
    private ChoiceBox<String> imageTypeChoiceBox;

    @FXML
    private Button editButton;

    @FXML
    private Button imageButton;


    @FXML
    private void editButtonPress(ActionEvent event) {
        if (editButton.getText().equals("Edit")) {
            switchEditing(true);
            imageButton.setText("upload image");
            editButton.setText("Submit");
        } else if (editButton.getText().equals("Submit")) {
            submit();
            refreshPage();
            switchEditing(false);
            editButton.setText("Edit");
            imageButton.setText("download image");
        }
    }

    private void submit() {
        // TODO: medical image address not correct
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.EDIT_MEDICAL_IMAGE_PAGE, Integer.toString(patientID), Integer.toString(pageNo),
                    "./chiz", imageTypeChoiceBox.getValue(), explanationTextArea.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addComponents(Arrays.asList(explanationTextArea, imageTypeChoiceBox));
        switchEditing(false);
        imageTypeChoiceBox.getItems().clear();
        imageTypeChoiceBox.getItems().addAll(Arrays.asList("CT-Scan", "OPG", "Radiographic-Image"));
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    private String getPatinetID() {
        return ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).getPatientID();
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

        model.MedicalImagePage inst = model.MedicalImagePage.getInstance();

        explanationTextArea.setText(inst.getReason());
        imageTypeChoiceBox.setValue(inst.getImage_type());
    }
}
