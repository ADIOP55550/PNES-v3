package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.PetriNet;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;
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

        var net = new PetriNet();
        final Place place1 = new Place(net);
        final Place place2 = new Place(net);
        final Transition transition1 = new Transition(net);
        final Transition transition2 = new Transition(net);
        final Arc arc1 = new Arc(net, place1, transition1);
        final Arc arc2 = new Arc(net, transition1, place2);
        final Arc arc3 = new Arc(net, place2, transition2);
        final Arc arc4 = new Arc(net, transition2, place1);

        net.addElement(place1);
        net.addElement(place2);
        net.addElement(transition1);
        net.addElement(transition2);
        net.addElement(arc1);
        net.addElement(arc2);
        net.addElement(arc3);
        net.addElement(arc4);


        visualizerFacade.visualizeNet(net);


        ((Button) centerToolbarLeft.getChildren().get(centerToolbarLeft.getChildren().size() - 1)).setOnAction(actionEvent -> {
            CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(() -> {
                Platform.runLater(() -> {
//                    TODO:
                    visualizerFacade.disableAutoLayout();
                });
            });
            visualizerFacade.enableAutoLayout();
        });
    }
}
