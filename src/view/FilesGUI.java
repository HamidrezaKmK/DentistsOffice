package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FilesGUI extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FilesGUI.fxml"));
        stage.setTitle("Files");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
