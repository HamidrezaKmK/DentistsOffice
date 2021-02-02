package controller.graphics.booking;

import controller.DataBaseQueryController;
import controller.graphics.FXMLLoadersCommunicator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.QueryType;
import view.FxmlFileLoader;
import view.PopUpCreater;
import view.booking.BookingGUI;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddWeeklySchedule implements Initializable {
    @FXML
    AnchorPane mainPane = new AnchorPane();

    ArrayList<ListView> daysListViews = new ArrayList<>();

    @FXML
    ListView saturdayListView = new ListView();

    @FXML
    ListView sundayListView = new ListView();

    @FXML
    ListView mondayListView = new ListView();

    @FXML
    ListView tuesdayListView = new ListView();

    @FXML
    ListView wednesdayListView = new ListView();

    @FXML
    ListView thursdayListView = new ListView();

    @FXML
    ListView fridayListView = new ListView();

    @FXML
    TextField fromDateTextField = new TextField();

    @FXML
    TextField endDateTextField = new TextField();

    @FXML
    Button addScheduleButton = new Button();

    ArrayList<String>daysNames = new ArrayList<>();

    @FXML
    private void backToBookingMenuButtonPress(ActionEvent event) {
        FxmlFileLoader object = new FxmlFileLoader();
        Pane view = object.getPage("BookingMenu", BookingGUI.class);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(view);
    }

    @FXML
    private void addScheduleButtonPress(ActionEvent event) {
        boolean hasPopedUp = false;
        try {
            DataBaseQueryController.getInstance().handleQuery(QueryType.CREATE_NEW_WEEKLY_SCHEDULE,
                    fromDateTextField.getText(), endDateTextField.getText());
        } catch (SQLException e) {
            PopUpCreater.createSQLErrorPopUp(e.getMessage());
            hasPopedUp = true;
        }
        int sz = daysNames.size();
        for (int i = 0; i < sz; i++) {
            String day = daysNames.get(i);
            for (int j = 0; j < daysListViews.get(i).getItems().size() - 1; j++) {
                String item = daysListViews.get(i).getItems().get(j).toString();
                String[] parts = item.split("-");
                String fromTime = parts[0].trim();
                String toTime = parts[1].trim();
                try {
                    DataBaseQueryController.getInstance().handleQuery(QueryType.ADD_NEW_AVAILABLE_TIME, fromDateTextField.getText(),
                            day, fromTime, toTime);
                } catch (Exception e) {
                    PopUpCreater.createSQLErrorPopUp(e.getMessage());
                    hasPopedUp = true;
                }
            }
        }
        //System.out.println("HOWDY");
        if (!hasPopedUp)
            backToBookingMenuButtonPress(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        daysListViews.addAll(Arrays.asList(saturdayListView, sundayListView, mondayListView, tuesdayListView, wednesdayListView,
                thursdayListView, fridayListView));
        daysNames.addAll(Arrays.asList(
                "SAT",
                "SUN",
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI"
        ));

        for (int i = 0; i < daysListViews.size(); i++) {
            final int d = i;
            daysListViews.get(i).getItems().add("2xClick to add");
            daysListViews.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        try {
                            FxmlFileLoader object = new FxmlFileLoader();
                            Parent root1 = object.getPage("PopupAddAvailableTime", view.booking.BookingGUI.class);
                            PopupAddAvailableTime controller = FXMLLoadersCommunicator.getLoader("PopupAddAvailableTime").getController();
                            controller.setDay(daysNames.get(d));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.show();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void addAvailableTime(String day, String fromTime, String toTime) {
        int i = daysNames.indexOf(day);
        daysListViews.get(i).getItems().remove(daysListViews.get(i).getItems().size() - 1);
        daysListViews.get(i).getItems().add(fromTime + " - " + toTime);
        daysListViews.get(i).getItems().add("2xClick to add");
    }
}
