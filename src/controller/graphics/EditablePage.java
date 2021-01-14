package controller.graphics;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collection;

public interface EditablePage {
    ArrayList<Node> components = new ArrayList<>();

    default void addComponents(Collection<Node> list) {
        components.addAll(list);
    }

    default void switchEditing(boolean isEnabled) {
        for (Node node : components) {
            node.setDisable(!isEnabled);
        }
    }

}
