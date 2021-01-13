package controller.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientsList implements Initializable {

    @FXML
    private ListView patientList;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void selectFilePress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PersonalFile", view.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    @FXML
    private void addNewFileButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("AddNewFile", view.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientList.getItems().add("hamid kamkari");
    }
}
