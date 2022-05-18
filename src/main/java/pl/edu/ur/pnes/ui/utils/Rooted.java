package pl.edu.ur.pnes.ui.utils;

import javafx.scene.Node;

public interface Rooted {
    /**
     * Provides access to root node of the view managed by the controller.
     * @return Root node of the view managed by the controller.
     */
    Node getRoot();
}
