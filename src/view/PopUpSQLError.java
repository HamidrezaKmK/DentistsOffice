package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class PopUpSQLError {
    @FXML
    private TextArea sqlErrorMessageTextArea = new TextArea();

    String currentText;

    public void addErrorText(String error) {
        sqlErrorMessageTextArea.setEditable(false);
        currentText += "\n - " + error;
        sqlErrorMessageTextArea.setText(currentText);
    }
}
