package controller.graphics.files;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.FileSummary;
import model.QueryType;
import view.FxmlFileLoader;
import view.files.FilesGUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalFile implements Initializable {

    @FXML
    private Label patientIDLabel = new Label();

    ArrayList<String> pages = new ArrayList<>();
    @FXML
    private AnchorPane filePagePane = new AnchorPane();
    @FXML
    private AnchorPane mainPane = new AnchorPane();
    @FXML
    private ListView pagesList = new ListView();

    @FXML
    private Button addNewPageButton;

    @FXML
    private Button deletePageButton;

    @FXML
    private void deletePageButtonPress(ActionEvent event) {
        // TODO: query to delete page
    }

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
        pagesList.setDisable(true);
        addNewPageButton.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        refereshPagesList();

        // select page
        pagesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    FxmlFileLoader object = new FxmlFileLoader();
                    Pane view = null;

                    int selectedPageNo = pagesList.getSelectionModel().getSelectedIndex() + 1;
                    String selected = pagesList.getSelectionModel().getSelectedItem().toString();

                    System.out.println("CLICKED ON! " + selected);
                    if (Pattern.matches("Personal Info.*", selected)) {
                        view = object.getPage("PersonalInfoPage", view.files.FilesGUI.class);
                    } else if (Pattern.matches("Appointment page.*", selected)) {
                        view = object.getPage("AppointmentPage", view.files.FilesGUI.class);
                        ((AppointmentPage) FXMLLoadersCommunicator.getLoader("AppointmentPage").getController()).setPageNo(selectedPageNo);

                    } else if (Pattern.matches("Medical image page.*", selected)) {
                        view = object.getPage("MedicalImagePage", view.files.FilesGUI.class);
                        ((MedicalImagePage) FXMLLoadersCommunicator.getLoader("MedicalImagePage").getController()).setPageNo(selectedPageNo);
                    }

                    if (view != null) {
                        filePagePane.getChildren().clear();
                        filePagePane.getChildren().add(view);
                    }
                }
            }
        });
    }

    public void setPatientIDLabel(int id) {
        patientIDLabel.setText(Integer.toString(id));
    }

    private void refereshPagesList() {
        int pgNum = 1;
        while (true) {
            if (pgNum == 1) {
                pages.add("Personal Info " + pgNum);
            } else if (FileSummary.getInstance().getAppointmentPage_page_numbers().contains(Integer.toString(pgNum))) {
                pages.add("Appointment page " + pgNum);
            } else if (FileSummary.getInstance().getMedicalImagePage_page_numbers().contains(Integer.toString(pgNum))) {
                pages.add("Medical image page " + pgNum);
            } else {
                break;
            }
            pgNum++;
        }
        pagesList.getItems().addAll(pages);
    }

    public void addPage(String pageName) {
        System.out.println("HOWDY FROM THE INSIDE");
        pagesList.setDisable(false);
        pagesList.getItems().remove("new page...");
        pagesList.getItems().add(pageName);
        System.out.println(pagesList.getItems());
        addNewPageButton.setDisable(false);
    }

    public String getPatientID() {
        return patientIDLabel.getText();
    }

    public void refreshFirstPage() {
        Pane view = (new FxmlFileLoader()).getPage("PersonalInfoPage", view.files.FilesGUI.class);
        filePagePane.getChildren().add(view);
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.REFRESH_PAGE, getPatientID(), "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((PersonalInfoPage) FXMLLoadersCommunicator.getLoader("PersonalInfoPage").getController()).refreshPage("1", getPatientID());
    }
}
