package controller.graphics.files;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.files.FilesGUI;

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

    @FXML
    private void searchButtonPress(ActionEvent event) {
        // TODO: send query to search
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: send query to get a list of patients
        patientList.getItems().add("hamid kamkari");
        patientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    String currentItemSelected = patientList.getSelectionModel().getSelectedItem().toString();
                    // TODO: send query to select file
                    FxmlFileLoader object = new FxmlFileLoader();
                    Pane view = object.getPage("PersonalFile", view.files.FilesGUI.class);
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(view);
                }
            }
        });
    }
}
