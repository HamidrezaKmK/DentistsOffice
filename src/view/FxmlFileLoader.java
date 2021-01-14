package view;

import controller.graphics.FXMLLoadersCommunicator;
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
            FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            FXMLLoadersCommunicator.addLoader(filename, fxmlLoader);
            view = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println("No page " + filename + " please check FxmlFileLoader");
        }
        return view;
    }
}
