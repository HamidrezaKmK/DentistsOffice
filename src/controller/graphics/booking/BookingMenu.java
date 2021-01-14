package controller.graphics.booking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.booking.BookingGUI;

public class BookingMenu {

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @FXML
    private void addWeeklyScheduleButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("AddWeeklySchedule", BookingGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }
}
