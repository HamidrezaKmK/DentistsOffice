package controller.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import view.FilesGUI;
import view.FxmlFileLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PersonalFile implements Initializable {

    ArrayList<String> pages = new ArrayList<>();
    @FXML
    private AnchorPane filePagePane = new AnchorPane();
    @FXML
    private AnchorPane mainPane = new AnchorPane();
    @FXML
    private ListView pagesList = new ListView();
    //ersonalFile() {
        //FxmlFileLoader object = new FxmlFileLoader();
        //filePagePane = object.getPage("PersonalFile", FilesGUI.class);
    //
    // }

    @FXML
    private void backToPatientListPress(ActionEvent event) {
        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.FilesGUI.class);
        mainPane.getChildren().add(view);
    }

    @FXML
    private void paneClicked(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = null;

        String selected = pagesList.getSelectionModel().getSelectedItem().toString();

        System.out.println("CLICKED ON! " + selected);
        switch (selected) {
            case "personal info":
                view = object.getPage("PersonalInfoPage", view.FilesGUI.class);
                break;
            case "medical page":
                view = object.getPage("MedicalImagePage", view.FilesGUI.class);
                break;
            case "appointment page":
                view = object.getPage("AppointmentPage", view.FilesGUI.class);
                break;
        }
        if (view != null) {
            filePagePane.getChildren().clear();
            filePagePane.getChildren().add(view);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pages.add("personal info");
        pages.add("medical page");
        pages.add("appointment page");
        pagesList.getItems().addAll(pages);
    }
}
