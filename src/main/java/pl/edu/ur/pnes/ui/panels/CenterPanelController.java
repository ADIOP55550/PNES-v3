package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.editor.actions.AddArcAction;
import pl.edu.ur.pnes.editor.actions.MoveNodesAction;
import pl.edu.ur.pnes.editor.actions.AddNodeAction;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.Transition;
import pl.edu.ur.pnes.petriNet.*;
import pl.edu.ur.pnes.petriNet.events.NetEvent;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFactory;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFactory;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeClickedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOutEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOverEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeReleasedEvent;
import pl.edu.ur.pnes.editor.Mode;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Rooted;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CenterPanelController implements Initializable, Rooted {
    private final static Logger logger = LogManager.getLogger();

    @FXML VBox root;
    @Override
    public javafx.scene.Node getRoot() {
        return root;
    }

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
    private final ToggleButton addPlaceButton = new ToggleButton("Place");
    private final ToggleButton addTransitionButton = new ToggleButton("Transition");
    private final ToggleButton addArcButton = new ToggleButton("Arc");
    private final Button toggleModeButton = new Button("RUN mode 🏁");
    private final ToggleGroup addingGroup = new ToggleGroup();

    private final double MIN_SLIDER_DURATION = 100;
    private final double MAX_SLIDER_DURATION = 5000;
    private final double DEFAULT_SLIDER_DURATION = 1000;
    private final Slider speedSlider = new Slider(MIN_SLIDER_DURATION, MAX_SLIDER_DURATION, DEFAULT_SLIDER_DURATION);

    private EventHandler<MouseEvent> mouseEventEventHandler;
    private EventHandler<VisualizerMouseNodeClickedEvent> clickedEventEventHandler;

    final Node[] inputNode = {null};
    final Node[] outputNode = {null};

    final EventHandler<VisualizerMouseNodeOverEvent> nodeOverEventEventHandler = event -> {
        if (inputNode[0] == null)
            return;
        visualizerFacade.getElementById(event.getNodeId()).ifPresent(element -> {
            if (element instanceof Node node)
                element.getClasses().add(0, inputNode[0].canBeConnectedTo(node) ? "goodHover" : "badHover");
        });
    };

    final EventHandler<VisualizerMouseNodeOutEvent> nodeOutEventEventHandler = event -> {
        visualizerFacade.getElementById(event.getNodeId()).ifPresent(element -> {
            element.getClasses().remove("badHover");
            element.getClasses().remove("goodHover");
        });
    };

    /**
     * Used to consume visualizer mouse release events when in adding mode, to prevent false producing the nodes moved events.
     */
    private static final EventHandler<VisualizerMouseNodeReleasedEvent> nodeReleasedEventEventHandler = Event::consume;

    protected Session session;

    /**
     * The session represented by the panel.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Loads new instance of the panel to represent specified session.
     * @param session Session represented by the panel.
     */
    static public CenterPanelController prepare(final Session session) {
        final var loader = FXMLUtils.getLoader(CenterPanelController.class);
        final var controller = new CenterPanelController(session);
        try {
            loader.setController(controller);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    protected CenterPanelController(final Session session) {
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addPlaceButton.setToggleGroup(addingGroup);
        addTransitionButton.setToggleGroup(addingGroup);
        addArcButton.setToggleGroup(addingGroup);

        session.mode().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case RUN -> {
                    visualizerFacade.setBackgroundColor(new Color(169, 202, 227));
                }
                case EDIT -> {
                    visualizerFacade.setBackgroundColor(new Color(220, 220, 220));
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


        final var net = session.net;
        this.visualizerFacade = new VisualizerFactory().create(graphPane, "/css/petri-net-graph.css");
        this.simulatorFacade = SimulatorFactory.create(net);
        visualizerFacade.visualizeNet(net);

        net.addEventHandler(NetEvent.NODES_MOVED, event -> {
            session.undoHistory.push(new MoveNodesAction(visualizerFacade, Arrays.asList(event.nodes), event.offset).asApplied());
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


        session.mode().addListener((observable, oldValue, newValue) -> {
            if (newValue != Mode.RUN) {
                simulatorFacade.stopAutoStep();
                simulatorFacade.stopAutoStep();
            }
        });

        toggleModeButton.setOnAction(event -> {
            // toggle mode
            session.mode().set(session.mode().getValue() == Mode.RUN ? Mode.EDIT : Mode.RUN);
            
            // deselect all tools
            if (addingGroup.getSelectedToggle() != null) {
                addingGroup.getSelectedToggle().setSelected(false);
            }
            
//             clean up arc adding filters
            inputNode[0] = null;
            
//             visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
//             visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
//             visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);

        });

        toggleModeButton.textProperty().bind(session.mode().asString("%s mode"));

        progressCircle.setProgress(0);

        centerToolbarLeft.getChildren().addAll(toggleModeButton);
        centerToolbarRight.getChildren().addAll(speedSlider, progressCircle, stepButton, playPauseButton, stopButton, addArcButton, addPlaceButton, addTransitionButton);

        simulatorFacade.setProgressCallback(value -> Platform.runLater(() -> progressCircle.setProgress(value)));

        speedSlider.setSnapToTicks(true);
        speedSlider.setMajorTickUnit(1000);
        speedSlider.setBlockIncrement(1000);
        speedSlider.setMinorTickCount(1);
        speedSlider.setOrientation(Orientation.HORIZONTAL);
        speedSlider.setShowTickMarks(true);
        speedSlider.setTooltip(new Tooltip("Duration between steps (in ms)"));

        simulatorFacade.autoStepWaitDurationProperty().bindBidirectional(speedSlider.valueProperty());

        stepButton.disableProperty().bind(simulatorFacade.autoStepProperty().isEqualTo(SimulatorFacade.AutoStepState.DONE).or(session.mode().isNotEqualTo(Mode.RUN)));
        stepButton.setOnAction(actionEvent -> simulatorFacade.singleAutomaticStep());

        stopButton.disableProperty().bind(session.mode().isNotEqualTo(Mode.RUN));
        stopButton.setOnAction(actionEvent -> simulatorFacade.stopAutoStep());

        playPauseButton.disableProperty().bind(simulatorFacade.autoStepProperty().isEqualTo(SimulatorFacade.AutoStepState.DONE));
        playPauseButton.setOnAction(actionEvent -> {
            if (session.mode().getValue() == Mode.EDIT) {
                session.mode().set(Mode.RUN);
                return;
            }
            simulatorFacade.startOrPauseAutoStep();
        });

        // Add new Place on mouse point
        addPlaceButton.disableProperty().bind(session.mode().isNotEqualTo(Mode.EDIT));
        addPlaceButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                this.mouseEventEventHandler = mouseEvent -> {
                    final var place = new Place(net);
                    getSession().undoHistory.push(
                        new AddNodeAction(net, place, visualizerFacade.mousePositionToGraphPosition(mouseEvent)).andApply()
                    );
                };
                graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            } else {
                graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            }
        });


        /*
         * Add new Transition on mouse point
         */
        addTransitionButton.disableProperty().bind(session.mode().isNotEqualTo(Mode.EDIT));
        addTransitionButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.mouseEventEventHandler = mouseEvent -> {
                    final var transition = new Transition(net);
                    getSession().undoHistory.push(
                        new AddNodeAction(net, transition, visualizerFacade.mousePositionToGraphPosition(mouseEvent)).andApply()
                    );
                };
                graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            } else {
                graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            }
        });

        /*
         * Set new Arc
         * If button is pressed, choose your first and second node to create arc
         */
        addArcButton.disableProperty().bind(session.mode().isNotEqualTo(Mode.EDIT));
        addArcButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.clickedEventEventHandler = event -> {
                    event.consume();
                    if (inputNode[0] != null) {
                        // Input node already selected, now select output node
                        var el = visualizerFacade.getElementById(event.getClickedNodeId());
                        if (el.isEmpty())
                            return;
                        outputNode[0] = net.getAllNodesStream().filter(v -> Objects.equals(v.getId(), el.get().getId())).findAny().orElseThrow();

                        if (!inputNode[0].canBeConnectedTo(outputNode[0])) {
                            // cleanup
                            outputNode[0].getClasses().remove("badHover");
                            outputNode[0].getClasses().remove("goodHover");
                            outputNode[0] = null;
                            // Cannot be connected, try again
                            return;
                        }

                        final var arc = new Arc(net, inputNode[0], outputNode[0]);
                        getSession().undoHistory.push(new AddArcAction(net, arc).andApply());
                        logger.debug("[Adding arc] Got output node: " + el.get().getName());

                        // cleanup
                        outputNode[0].getClasses().remove("badHover");
                        outputNode[0].getClasses().remove("goodHover");
                        visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
                        visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
                        visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);
                    } else {
                        // first click -> find input node for Arc
                        var el = visualizerFacade.getElementById(event.getClickedNodeId());
                        if (el.isEmpty())
                            return;
                        inputNode[0] = net.getAllNodesStream().filter(v -> Objects.equals(v.getId(), el.get().getId())).findAny().orElseThrow();
                        logger.debug("[Adding arc] Got input node: " + el.get().getName());
                    }

                };
                
                // attach event filters
                visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
                visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);
                visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
                visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_RELEASED, nodeReleasedEventEventHandler);
            } else {
                // cleanup
                outputNode[0].getClasses().remove("badHover");
                outputNode[0].getClasses().remove("goodHover");
                visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
                visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
                visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);
                visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_RELEASED, nodeReleasedEventEventHandler);
                inputNode[0] = null;
            }
        });
    }
}
