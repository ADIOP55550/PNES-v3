package pl.edu.ur.pnes.ui.panels;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.io.IOException;

public class ProjectTreePanelController implements Rooted {
    @FXML
    AnchorPane root;
    @Override
    public javafx.scene.Node getRoot() {
        return root;
    }

    /**
     * Loads new instance of the panel.
     */
    static public ProjectTreePanelController prepare() {
        final var loader = FXMLUtils.getLoader(ProjectTreePanelController.class);
        final var controller = new ProjectTreePanelController();
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    protected ProjectTreePanelController() {

    }
}
