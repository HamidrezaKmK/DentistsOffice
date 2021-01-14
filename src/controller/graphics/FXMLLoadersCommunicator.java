package controller.graphics;

import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.Hashtable;

public class FXMLLoadersCommunicator {
    private static Hashtable<String, FXMLLoader> fxmlLoaders = new Hashtable<String, FXMLLoader>();

    public static void addLoader(String loaderName, FXMLLoader fxmlLoader) {
        if (fxmlLoaders.containsKey(loaderName))
            fxmlLoaders.remove(loaderName);
        fxmlLoaders.put(loaderName, fxmlLoader);
    }

    public static FXMLLoader getLoader(String loaderName) {
        return fxmlLoaders.get(loaderName);
    }
}
