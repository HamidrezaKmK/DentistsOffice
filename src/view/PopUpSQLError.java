package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class PopUpSQLError {
    @FXML
    private TextArea sqlErrorMessageTextArea = new TextArea();

    public void setText(String error) {
        sqlErrorMessageTextArea.setEditable(false);
        sqlErrorMessageTextArea.setText(error);
    }
}
