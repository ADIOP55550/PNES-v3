package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.PetriNet;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFactory;
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
    private SimulatorFacade simulatorFacade;

    public void initialize() {
        this.visualizerFacade = new VisualizerFactory().create(graphPane, "/css/petri-net-graph.css");

        graphPane.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isControlDown() || mouseEvent.isMiddleButtonDown())
                graphPane.setCursor(Cursor.MOVE);
        });

        graphPane.setOnMouseReleased(mouseEvent -> {
            graphPane.setCursor(Cursor.DEFAULT);
        });

        var net = new PetriNet();
        final Place place1 = new Place(net);
        final Place place2 = new Place(net);
        final Place place3 = new Place(net);
        final Place place4 = new Place(net);
        place1.setTokens(2);
        final Transition transition1 = new Transition(net);
        final Transition transition2 = new Transition(net);
        final Transition transition3 = new Transition(net);
        place3.setTokens(1);
        final Arc arc1 = new Arc(net, place1, transition1);
        final Arc arc2 = new Arc(net, transition1, place2);
        final Arc arc3 = new Arc(net, place2, transition2);
        final Arc arc4 = new Arc(net, transition2, place1);
        arc4.setWeight(2);
        final Arc arc5 = new Arc(net, transition2, place3);
        arc5.setWeight(2);
        final Arc arc6 = new Arc(net, place2, transition3);
//        arc5.setWeight(3);
        final Arc arc7 = new Arc(net, transition3, place4);
        final Arc arc8 = new Arc(net, place3, transition2);

        net.addElements(place1, place2, place3, place4, transition1, transition2, transition3, arc1, arc2, arc3, arc4, arc5, arc6, arc7, arc8);


        this.simulatorFacade = SimulatorFactory.create(net);
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
//        ((Button) centerToolbarLeft.getChildren().get(centerToolbarLeft.getChildren().size() - 2)).setOnAction(actionEvent -> {
//            visualizerFacade.setMode(Mode.ADD_TOKENS);
//        });

        ((Button) centerToolbarRight.getChildren().get(1)).setOnAction(actionEvent -> {
            simulatorFacade.automaticStep();
        });
        ((Button) centerToolbarRight.getChildren().get(1)).setText("Step ⏩");
    }
}
