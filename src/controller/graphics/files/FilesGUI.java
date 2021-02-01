package controller.graphics.files;

import controller.DataBaseQueryController;
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

        //
        DataBaseQueryController dbcontroller = DataBaseQueryController.getInstance();
        dbcontroller.setUsername("postgres");
        dbcontroller.setPassword("dibimibi");
        dbcontroller.setUrl("jdbc:postgresql://localhost:5432/DentistOfficeDB");
        try {
            dbcontroller.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connection successfully created!");


        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("PatientsList", view.files.FilesGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

}
