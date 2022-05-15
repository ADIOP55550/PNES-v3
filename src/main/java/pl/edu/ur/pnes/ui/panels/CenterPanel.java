package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.editor.actions.MoveNodeAction;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.PetriNet;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFactory;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFactory;
import pl.edu.ur.pnes.petriNet.visualizer.events.VGVLEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;
import pl.edu.ur.pnes.ui.EditorMode;

import java.awt.*;
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

    @FXML
    public ProgressIndicator progressCircle = new ProgressIndicator();

    private VisualizerFacade visualizerFacade;
    private SimulatorFacade simulatorFacade;

    private final Button stepButton = new Button("Step ⏩");
    private final Button stopButton = new Button("Stop ⏹");
    private final Button playPauseButton = new Button("Play/pause ⏯");
    private final Button layoutButton = new Button("Layout");
    private final Button toggleModeButton = new Button("RUN mode 🏁");

    private final double MIN_SLIDER_DURATION = 100;
    private final double MAX_SLIDER_DURATION = 5000;
    private final double DEFAULT_SLIDER_DURATION = 1000;
    private final Slider speedSlider = new Slider(MIN_SLIDER_DURATION, MAX_SLIDER_DURATION, DEFAULT_SLIDER_DURATION);


    ObjectProperty<EditorMode> editorMode = new SimpleObjectProperty<>(EditorMode.EDIT);

    public Session session = new Session();



    public void initialize() {

        editorMode.addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case RUN -> {
                    visualizerFacade.setBackgroundColor(Color.RED);
                }
                case EDIT -> {
                    visualizerFacade.setBackgroundColor(Color.BLUE);
                }
            }
        });

        graphPane.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isControlDown() || mouseEvent.isMiddleButtonDown())
                graphPane.setCursor(Cursor.MOVE);
        });

        graphPane.setOnMouseReleased(mouseEvent -> {
            graphPane.setCursor(Cursor.DEFAULT);
        });

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

        this.visualizerFacade = new VisualizerFactory().create(graphPane, "/css/petri-net-graph.css");
        this.simulatorFacade = SimulatorFactory.create(net);
        visualizerFacade.visualizeNet(net);

        // TODO: Probably there should be VisualizerEvent instead VGVLEvent, but it wasn't my responsibility to prepare those....
        visualizerFacade.addEventHandler(VGVLEvent.NODES_MOVED_EVENT, event -> {
            session.undoHistory.push(new MoveNodeAction(visualizerFacade, event.nodesIds, event.offset));
        });


        centerToolbarLeft.getChildren().add(layoutButton);
        layoutButton.setOnAction(actionEvent -> {
            CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(() -> {
                Platform.runLater(() -> {
//                    TODO:
                    visualizerFacade.disableAutoLayout();
                });
            });
            visualizerFacade.enableAutoLayout();
        });


        toggleModeButton.setOnAction(event -> editorMode.set(editorMode.getValue() == EditorMode.RUN ? EditorMode.EDIT : EditorMode.RUN));
        toggleModeButton.textProperty().bind(editorMode.asString("%s mode"));

        progressCircle.setProgress(0);

        centerToolbarLeft.getChildren().addAll(toggleModeButton);
        centerToolbarRight.getChildren().addAll(speedSlider, progressCircle, stepButton, playPauseButton, stopButton);

        simulatorFacade.setProgressCallback(value -> Platform.runLater(() -> progressCircle.setProgress(value)));

        speedSlider.setSnapToTicks(true);
        speedSlider.setMajorTickUnit(1000);
        speedSlider.setBlockIncrement(1000);
        speedSlider.setMinorTickCount(1);
        speedSlider.setOrientation(Orientation.HORIZONTAL);
        speedSlider.setShowTickMarks(true);
        speedSlider.setTooltip(new Tooltip("Duration between steps (in ms)"));

        simulatorFacade.autoStepWaitDurationProperty().bindBidirectional(speedSlider.valueProperty());

        stepButton.disableProperty().bind(simulatorFacade.autoStepProperty().isEqualTo(SimulatorFacade.AutoStepState.DONE).or(editorMode.isNotEqualTo(EditorMode.RUN)));
        stepButton.setOnAction(actionEvent -> simulatorFacade.singleAutomaticStep());

        stopButton.disableProperty().bind(editorMode.isNotEqualTo(EditorMode.RUN));
        stopButton.setOnAction(actionEvent -> simulatorFacade.stopAutoStep());

        playPauseButton.disableProperty().bind(simulatorFacade.autoStepProperty().isEqualTo(SimulatorFacade.AutoStepState.DONE));
        playPauseButton.setOnAction(actionEvent -> {
            if (editorMode.getValue() == EditorMode.EDIT) {
                editorMode.set(EditorMode.RUN);
                return;
            }
            simulatorFacade.startOrPauseAutoStep();
        });

    }
}
