package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BookingGUI extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("BookingMenu.fxml"));
        stage.setTitle("Booking");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}