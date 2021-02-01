package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.QueryType;
import view.FxmlFileLoader;
import view.files.FilesGUI;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientsList implements Initializable {

    @FXML
    private ListView patientList;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField patientIDField;

    @FXML
    private CheckBox isInDebt;

    @FXML
    private void addNewFileButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("AddNewFile", FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    private void sendSeachQuery(String firstName, String lastName, String patientID, String isInDebtQ) {
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.MAIN_SEARCH, firstName, lastName,
                    patientID, isInDebtQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void searchButtonPress(ActionEvent event) {
        String firstName = firstNameField.getText();
        if (firstName.isEmpty())
            firstName = null;
        String lastName = lastNameField.getText();
        if (lastName.isEmpty())
            lastName = null;
        String patientID = patientIDField.getText();
        if (patientID.isEmpty())
            patientID = null;
        String isInDebtQ = isInDebt.isSelected() ? "1" : "0";
        sendSeachQuery(firstName, lastName, patientID, isInDebtQ);
        updateListView();
        // TODO: send query to search
    }

    private void updateListView() {

        patientList.getItems().clear();
        int sz = model.SearchResult.getInstance().getFirstNames().size();
        for (int i = 0; i < sz; i++) {
            StringBuilder stb = new StringBuilder();
            stb.append(model.SearchResult.getInstance().getFirstNames().get(i));
            stb.append(" ");
            stb.append(model.SearchResult.getInstance().getLastNames().get(i));
            String firstPart = stb.toString();
            stb = new StringBuilder();
            if (model.SearchResult.getInstance().getDebt() != null) {
                stb.append(" Debt value: ");
                stb.append(model.SearchResult.getInstance().getDebt().get(i));
            }
            String secondPart = stb.toString();
            stb = new StringBuilder();

            stb.append("Patient id: ");
            stb.append(model.SearchResult.getInstance().getPatientIds().get(i));
            String thirdPart = stb.toString();

            String formatted = String.format("%-60s%s%60s", firstPart, secondPart, thirdPart);

            patientList.getItems().add(formatted);
            //Node n = (Node) patientList.getItems().get(patientList.getItems().size() - 1);
            //n.setOnMouseClicked();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendSeachQuery(null, null, null, "0");
        updateListView();
        patientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    int currentItemSelected = patientList.getSelectionModel().getSelectedIndex();
                    int patientID = model.SearchResult.getInstance().getPatientIds().get(currentItemSelected);
                    // send query to select file
                    try {
                        DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_FILE_SUMMARY, Integer.toString(patientID));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FxmlFileLoader object = new FxmlFileLoader();
                    Pane view = object.getPage("PersonalFile", view.files.FilesGUI.class);
                    ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).setPatientIDLabel(patientID);
                    ((PersonalFile) FXMLLoadersCommunicator.getLoader("PersonalFile").getController()).refreshFirstPage();
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(view);
                }
            }
        });
    }
}
