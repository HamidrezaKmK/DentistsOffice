package view;

import controller.graphics.FXMLLoadersCommunicator;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.popups.PopupsGUI;

public class PopUpCreater {
    public static void createSQLErrorPopUp(String errorMessage) {
        Parent root = null;
        FxmlFileLoader object = new FxmlFileLoader();
        root = object.getPage("PopUpSQLError", PopupsGUI.class);
        ((PopUpSQLError) FXMLLoadersCommunicator.getLoader("PopUpSQLError").getController()).setText(errorMessage);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
