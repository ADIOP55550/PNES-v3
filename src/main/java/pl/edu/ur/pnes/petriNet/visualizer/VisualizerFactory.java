package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Objects;

public class VisualizerFactory {
    private final Logger logger = LogManager.getLogger(VisualizerFactory.class);

    public VisualizerFacade create(Pane parentNode, String cssResourceName) {

        var facade = new VisualizerFacade(new Visualizer());
        Platform.runLater(() -> {
            Pane element =facade.visualizer.getElement();
            parentNode.getChildren().add(element);
            element.prefWidthProperty().bind(parentNode.widthProperty());
            element.prefHeightProperty().bind(parentNode.heightProperty());
        });

        if (cssResourceName != null && !cssResourceName.isBlank()) {
            logger.debug("Trying to get resource {}", cssResourceName);
            URL url = getClass().getResource(cssResourceName);
            logger.debug("url = " + url);
            String stylesheetPath = Objects.requireNonNull(url).getPath();
            logger.debug("stylesheetPath = " + stylesheetPath);
            facade.visualizer.addStyles(stylesheetPath);
        }

        return facade;
    }
}
