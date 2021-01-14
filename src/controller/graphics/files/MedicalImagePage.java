package controller.graphics.files;

import controller.graphics.EditablePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MedicalImagePage implements Initializable, EditablePage {

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
            // TODO: query to edit medical image page
            // TODO: query to retrieve medical image page
            switchEditing(false);
            editButton.setText("Edit");
            imageButton.setText("download image");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addComponents(Arrays.asList(explanationTextArea, imageTypeChoiceBox));
        switchEditing(false);
        // TODO: query to retrieve medical image page
    }
}
