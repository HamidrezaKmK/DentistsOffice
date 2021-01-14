package controller.graphics.files;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class FilesGUI implements Initializable {

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.files.FilesGUI.class);
        mainPane.getChildren().add(view);

        //

    }

}
