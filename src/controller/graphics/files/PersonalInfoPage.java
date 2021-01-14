package controller.graphics.files;

import controller.graphics.EditablePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PersonalInfoPage implements Initializable, EditablePage {

    @FXML
    private TextField firstNameTextField = new TextField();

    @FXML
    private TextField lastNameTextField = new TextField();

    @FXML
    private TextField ageTextField = new TextField();

    @FXML
    private ChoiceBox<String>genderChoiceBox = new ChoiceBox<>();

    @FXML
    private TextField occupationTextField = new TextField();

    @FXML
    private TextField referenceTextField = new TextField();

    @FXML
    private ChoiceBox<String>educationChoiceBox = new ChoiceBox<>();

    @FXML
    private TextArea homeAddressTextArea = new TextArea();

    @FXML
    private TextArea workAddressTextArea = new TextArea();

    @FXML
    private TextArea generalMedicalRecordsTextArea = new TextArea();

    @FXML
    private TextArea dentalRecordsTextArea = new TextArea();

    @FXML
    private ListView<String> sensitiveMedicineListView = new ListView<>();

    @FXML
    private CheckBox doesSmokeCheckBox = new CheckBox();

    @FXML
    private Label patientIDLabel = new Label();

    @FXML
    private Label titleFirstNameLastNameLabel = new Label();

    @FXML
    private Button editPersonalInfoButton;


    @FXML
    private void editPersonalInfoButtonPress(ActionEvent event) {
        System.out.println("CLICKED ON EDIT PERSONAL INFO" + " " + editPersonalInfoButton.getText());
        if (editPersonalInfoButton.getText().equals("Edit")) {
            switchEditing(true);
            editPersonalInfoButton.setText("Submit");
        } else if (editPersonalInfoButton.getText().equals("Submit")){
            // TODO : query to edit personal info
            // TODO : query to get personal info again
            editPersonalInfoButton.setText("Edit");
            switchEditing(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO : add query to get personal info of patient
        addComponents(Arrays.asList(firstNameTextField, lastNameTextField, ageTextField, genderChoiceBox,
                occupationTextField, referenceTextField, educationChoiceBox, homeAddressTextArea, workAddressTextArea,
                generalMedicalRecordsTextArea, dentalRecordsTextArea, sensitiveMedicineListView, doesSmokeCheckBox
                ));
        switchEditing(false);

    }
}
