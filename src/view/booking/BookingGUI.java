package view.booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.FxmlFileLoader;

public class BookingGUI extends Application {
    public void start(Stage stage) throws Exception {
        FxmlFileLoader object = new FxmlFileLoader();
        Parent root = object.getPage("BookingMenu", view.booking.BookingGUI.class);
        //Parent root =
        //Parent root = FXMLLoader.load(getClass().getResource("BookingMenu.fxml"));
        stage.setTitle("booking");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}