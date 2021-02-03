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
        if (!FXMLLoadersCommunicator.hasLoader("PopUpSQLError")) {
            root = object.getPage("PopUpSQLError", PopupsGUI.class);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        ((PopUpSQLError) FXMLLoadersCommunicator.getLoader("PopUpSQLError").getController()).addErrorText(errorMessage);

    }
}
