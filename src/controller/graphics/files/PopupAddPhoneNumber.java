package controller.graphics.files;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.FxmlFileLoader;


public class PopupAddPhoneNumber {

    @FXML
    TextField fromTimeTextField = new TextField();

    @FXML
    private void addPhoneNumberButtonPress(ActionEvent event) {
        AddNewFile controller = FXMLLoadersCommunicator.getLoader("AddNewFile").getController();
        controller.addPhoneNumber(fromTimeTextField.getText());
        Stage stage = (Stage) fromTimeTextField.getScene().getWindow();
        stage.close();
    }
}
