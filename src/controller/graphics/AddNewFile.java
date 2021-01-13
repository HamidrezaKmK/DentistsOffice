package controller.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;

public class AddNewFile {

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @FXML
    private void backToPatientListButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }


}
