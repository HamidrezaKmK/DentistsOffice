package controller.graphics.files;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.files.FilesGUI;

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

    @FXML
    private Button addNewPageButton;

    //ersonalFile() {
        //FxmlFileLoader object = new FxmlFileLoader();
        //filePagePane = object.getPage("PersonalFile", FilesGUI.class);
    //
    // }

    @FXML
    private void backToPatientListPress(ActionEvent event) {
        mainPane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", FilesGUI.class);
        mainPane.getChildren().add(view);
    }

    @FXML
    private void addNewPageButtonPress(ActionEvent event) {
        filePagePane.getChildren().clear();
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("SelectNewPageType", FilesGUI.class);
        filePagePane.getChildren().add(view);
        pagesList.getItems().add("new page...");
        addNewPageButton.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pages.add("personal info");
        pages.add("medical page");
        pages.add("appointment page");
        pagesList.getItems().addAll(pages);

        // select page
        pagesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    FxmlFileLoader object = new FxmlFileLoader();
                    Pane view = null;

                    String selected = pagesList.getSelectionModel().getSelectedItem().toString();

                    System.out.println("CLICKED ON! " + selected);
                    switch (selected) {
                        case "personal info":
                            view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
                            break;
                        case "medical page":
                            view = object.getPage("MedicalImagePage", view.files.FilesGUI.class);
                            break;
                        case "appointment page":
                            view = object.getPage("AppointmentPage", view.files.FilesGUI.class);
                            break;
                    }
                    if (view != null) {
                        filePagePane.getChildren().clear();
                        filePagePane.getChildren().add(view);
                    }
                }
            }
        });
    }

    public void addPage(String pageName) {
        System.out.println("HOWDY FROM THE INSIDE");
        pagesList.getItems().remove("new page...");
        pagesList.getItems().add(pageName);
        System.out.println(pagesList.getItems());
        addNewPageButton.setDisable(false);
    }
}
