package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import controller.graphics.booking.PopupAddAvailableTime;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    private ListView phoneNumbersListView = new ListView();

    @FXML
    private void addButtonPress(ActionEvent event) {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.MAIN_SEARCH, null, null, null, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int mex = 0;

        HashSet<Integer> S = new HashSet<>(model.SearchResult.getInstance().getPatientIds());
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        int newId = (((firstName + lastName).hashCode())%10000  + 10000) % 10000 + 1;
        while (S.contains(newId))
            newId = newId+1;
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
            ArrayList<String> args = new ArrayList<>();
            args.addAll(Arrays.asList(Integer.toString(newId), firstName, lastName, age, gender,
                    occupation, ref, education, homeAddr, workAddr, generalMedicalRecords,
                    dentalRec, sensitiveMed, smoke, signitureAddr, fileCreationDate));
            for (int i = 0; i < phoneNumbersListView.getItems().size(); i++) {
                args.add(phoneNumbersListView.getItems().get(i).toString());
            }
            String[] allArgs = new String[args.size() - 1];
            for (int i = 0; i < args.size() - 1; i++)
                allArgs[i] = args.get(i);

            for (int i = 0; i < allArgs.length; i++)
                System.err.print(allArgs[i] + " ");
            System.err.println("\n----");
            DataBaseQueryController.getInstance().handleQuery(QueryType.CREATE_FILE, allArgs);
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
        phoneNumbersListView.getItems().add("2xClick to add...");
        phoneNumbersListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        FxmlFileLoader object = new FxmlFileLoader();
                        Parent root1 = object.getPage("PopupAddPhoneNumber", view.files.FilesGUI.class);
                        //PopupAddAvailableTime controller = FXMLLoadersCommunicator.getLoader("PopupAddAvailableTime").getController();
                        //controller.setDay(daysNames.get(d));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.show();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addPhoneNumber(String phoneNumber) {
        int delInd = phoneNumbersListView.getItems().size() - 1;
        phoneNumbersListView.getItems().remove(delInd);
        phoneNumbersListView.getItems().add(phoneNumber);
        phoneNumbersListView.getItems().add("2xClick to add...");
    }
}
