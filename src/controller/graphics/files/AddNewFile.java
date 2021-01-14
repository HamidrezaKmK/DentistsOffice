package controller.graphics.files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.files.FilesGUI;


public class AddNewFile {

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField occupationTextField;

    @FXML
    private TextField referenceTextField;

    @FXML
    private ChoiceBox<String> edicationChoiceBox;

    @FXML
    private TextArea homeAddressTextArea;

    @FXML
    private TextArea workAddressTextArea;

    @FXML
    private Button addButton;

    @FXML
    private void addButtonPress(ActionEvent event) {
        // TODO: query to add new file
        backToPatientListButtonPress(event);
    }

    @FXML
    private void backToPatientListButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.files.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }


}
