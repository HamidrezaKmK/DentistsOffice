package controller.graphics.booking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.booking.BookingGUI;

public class AddWeeklySchedule {
    @FXML
    AnchorPane mainPane = new AnchorPane();

    @FXML
    private void backToBookingMenuButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("BookingMenu", BookingGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }
}
