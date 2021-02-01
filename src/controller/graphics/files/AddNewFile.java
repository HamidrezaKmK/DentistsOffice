package controller.graphics.files;

import controller.DataBaseQueryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import view.FxmlFileLoader;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.*;


public class AddNewFile implements Initializable {

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
    private ChoiceBox<String> educationChoiceBox;

    @FXML
    private TextArea homeAddressTextArea;

    @FXML
    private TextArea workAddressTextArea;

    @FXML
    private Button addButton;

    @FXML
    private void addButtonPress(ActionEvent event) {
        // TODO: query to add new file
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.MAIN_SEARCH, null, null, null, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int mex = 0;

        HashSet<Integer> S = new HashSet<>(model.SearchResult.getInstance().getPatientIds());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        int newId = (firstName + lastName).hashCode();
        while (S.contains(newId))
            newId++;
        String age = ageTextField.getText();
        String gender = genderChoiceBox.getValue();
        String occupation = occupationTextField.getText();
        String ref = referenceTextField.getText();
        String education = educationChoiceBox.getValue();
        String homeAddr = homeAddressTextArea.getText();
        String workAddr = workAddressTextArea.getText();
        String generalMedicalRecords = "null";
        String dentalRec = "null";
        String sensitiveMed = "null";
        String smoke = "null";
        String signitureAddr = "null";
        // TODO: this shouldn't be local date
        String fileCreationDate = java.time.LocalDate.now().toString();

        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.CREATE_FILE,
                    Integer.toString(newId), firstName, lastName, age, gender,
                    occupation, ref, education, homeAddr, workAddr, generalMedicalRecords,
                    dentalRec, sensitiveMed, smoke, signitureAddr, fileCreationDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        backToPatientListButtonPress(event);
    }

    @FXML
    private void backToPatientListButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.files.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderChoiceBox.getItems().addAll(Arrays.asList("MALE", "FEMALE"));
        educationChoiceBox.getItems().addAll(Arrays.asList(
                "High-School-Diploma",
                "Associate-Degree",
                "Bachelors-Degree",
                "Masters-Degree",
                "Doctoral-Degree",
                "Other"
        ));
    }
}
