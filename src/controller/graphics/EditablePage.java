package controller.graphics;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collection;

public interface EditablePage {
    ArrayList<Node> components = new ArrayList<>();

    default void addComponents(Collection<Node> list) {
        components.addAll(list);
    }

    default void switchEditing(boolean isEnabled) {
        for (Node node : components) {
            if (node instanceof TextField)
                ((TextField) node).setEditable(isEnabled);
            else if (node instanceof TextArea)
                ((TextArea) node).setEditable(isEnabled);
            else
                node.setDisable(!isEnabled);
        }
    }

    void setPageNo(int pageNo);

}
