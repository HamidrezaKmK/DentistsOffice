package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlFileLoader {

    private Pane view;

    public Pane getPage(String filename, Class cl) {
        try {
            URL fileUrl = cl.getResource(filename + ".fxml");
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML can't be found!");
            }
            view = new FXMLLoader().load(fileUrl);
        } catch (Exception e) {
            System.out.println("No page " + filename + " please check FxmlFileLoader");
        }
        return view;
    }
}
