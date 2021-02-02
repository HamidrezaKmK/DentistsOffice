package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.EditablePage;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.Patient;
import model.QueryType;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PersonalInfoPage implements Initializable, EditablePage {

    int patientID;

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
    private TextArea sensitiveMedicineTextArea = new TextArea();

    @FXML
    private CheckBox doesSmokeCheckBox = new CheckBox();

    @FXML
    private Label patientIDLabel = new Label();

    @FXML
    private Label titleFirstNameLastNameLabel = new Label();

    @FXML
    private Button editPersonalInfoButton;

    @FXML
    private ListView phoneNumbersListView = new ListView();

    @FXML
    private void editPersonalInfoButtonPress(ActionEvent event) {
        System.out.println("CLICKED ON EDIT PERSONAL INFO" + " " + editPersonalInfoButton.getText());
        if (editPersonalInfoButton.getText().equals("Edit")) {
            switchEditing(true);
            editPersonalInfoButton.setText("Submit");
        } else if (editPersonalInfoButton.getText().equals("Submit")){
            submitPage();
            refreshPage("1", Integer.toString(patientID));
            editPersonalInfoButton.setText("Edit");
            switchEditing(false);
        }
    }

    private void submitPage() {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.EDIT_PERSONAL_INFO, Integer.toString(patientID),
                    "1", generalMedicalRecordsTextArea.getText(),
                    dentalRecordsTextArea.getText(), sensitiveMedicineTextArea.getText(), doesSmokeCheckBox.isSelected() ? "YES" : "NO",
                    "null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String id = ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).getPatientID();
        genderChoiceBox.getItems().addAll(Arrays.asList("MALE", "FEMALE"));
        educationChoiceBox.getItems().addAll(Arrays.asList(
                "High-School-Diploma",
                "Associate-Degree",
                "Bachelors-Degree",
                "Masters-Degree",
                "Doctoral-Degree",
                "Other"
        ));

        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        ageTextField.setEditable(false);
        genderChoiceBox.setDisable(true);
        occupationTextField.setEditable(false);
        referenceTextField.setEditable(false);
        educationChoiceBox.setDisable(true);
        homeAddressTextArea.setEditable(false);
        workAddressTextArea.setEditable(false);

        addComponents(Arrays.asList(
                generalMedicalRecordsTextArea, dentalRecordsTextArea, sensitiveMedicineTextArea, doesSmokeCheckBox
                ));
        switchEditing(false);

    }

    @Override
    public void setPageNo(int pageNo) {
        if (pageNo != 1) {
            System.err.println("set page no for perseonal info is not 1");
        }
    }

    public void refreshPage(String pageNo, String patientID) {
        setPageNo(Integer.valueOf(pageNo));
        this.patientID = Integer.valueOf(patientID);

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_PATIENT, patientID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_PAGE, patientID, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.Patient inst = model.Patient.getInstance();
        firstNameTextField.setText(inst.getFirst_name());
        lastNameTextField.setText(inst.getLast_name());
        ageTextField.setText(inst.getAge());
        genderChoiceBox.setValue(inst.getGender());
        occupationTextField.setText(inst.getOccupation());
        referenceTextField.setText(inst.getReference());
        educationChoiceBox.setValue(inst.getEducation());
        homeAddressTextArea.setText(inst.getHomeAddr());
        workAddressTextArea.setText(inst.getWorkAddr());
        phoneNumbersListView.getItems().clear();
        phoneNumbersListView.getItems().addAll(inst.getPhones());

        titleFirstNameLastNameLabel.setText(inst.getFirst_name() + " " + inst.getLast_name());
        patientIDLabel.setText(patientID);

        model.PersonalInfoPage inst1 = model.PersonalInfoPage.getInstance();
        if (inst1.getDental_records() != null) {
            generalMedicalRecordsTextArea.setText(inst1.getGeneral_medical_records());
            dentalRecordsTextArea.setText(inst1.getDental_records());
            sensitiveMedicineTextArea.setText(inst1.getSensitive_medicine());
            doesSmokeCheckBox.setSelected(inst1.getDoes_smoke().equals("t"));
        }
    }
}
