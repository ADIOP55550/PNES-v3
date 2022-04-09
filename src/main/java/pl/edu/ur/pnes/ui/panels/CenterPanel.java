package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CenterPanel extends CustomPanel {
    @FXML
    public HBox centerToolbarRight;
    @FXML
    public HBox centerToolbarLeft;
    @FXML
    public HBox centerToolbar;
    @FXML
    public Pane graphPane;
    private VisualizerFacade visualizerFacade;


    public void initialize() {
        this.visualizerFacade = new VisualizerFactory().create(graphPane, "/css/petri-net-graph.css");


//        ((Button) centerToolbarLeft.getChildren().get(centerToolbarLeft.getChildren().size() - 1)).setOnAction(actionEvent -> {
//            CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(() -> {
//                Platform.runLater(() -> {
////                    TODO:
////                    visualizerFacade.autoLayout.enable
//                });
//            });
//        });
    }
}
