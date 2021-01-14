package controller.graphics.booking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.FxmlFileLoader;
import view.booking.BookingGUI;

public class BookingMenu {

    @FXML
    private ScrollPane timeLinePane = new ScrollPane();

    @FXML
    private AnchorPane mainPane = new AnchorPane();

    @FXML
    private Button addWeeklyScheduleButton;

    @FXML
    private void scheduleQueryEnterButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("TimeLine", view.booking.BookingGUI.class);
        timeLinePane.setContent(view);
    }

    @FXML
    private void addWeeklyScheduleButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("AddWeeklySchedule", view.booking.BookingGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }
}
