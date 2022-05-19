package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.io.IOException;

public class PropertiesPanelController implements Rooted {
    @FXML
    AnchorPane root;
    @Override
    public javafx.scene.Node getRoot() {
        return root;
    }

    /**
     * Loads new instance of the panel.
     */
    static public PropertiesPanelController prepare() {
        final var loader = FXMLUtils.getLoader(PropertiesPanelController.class);
        final var controller = new PropertiesPanelController();
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }
}
