package controller.graphics.booking;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupAddAvailableTime implements Initializable {
    @FXML
    Label dayOfWeekLabel = new Label();

    @FXML
    TextField fromTimeTextField = new TextField();

    @FXML
    TextField toTimeTextField = new TextField();

    @FXML
    private void addAvailableTimeButtonPress(ActionEvent event) {
        AddWeeklySchedule controller = FXMLLoadersCommunicator.getLoader("AddWeeklySchedule").getController();
        controller.addAvailableTime(dayOfWeekLabel.getText(), fromTimeTextField.getText(), toTimeTextField.getText());
        Stage stage = (Stage) dayOfWeekLabel.getScene().getWindow();
        stage.close();
    }

    public void setDay(String day) {
        dayOfWeekLabel.setText(day);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
