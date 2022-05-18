package pl.edu.ur.pnes.ui.panels;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.editor.actions.MoveNodesAction;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.PetriNet;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;
import org.graphstream.ui.geom.Point3;
import pl.edu.ur.pnes.petriNet.*;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFacade;
import pl.edu.ur.pnes.petriNet.simulator.SimulatorFactory;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFacade;
import pl.edu.ur.pnes.petriNet.visualizer.VisualizerFactory;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeClickedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOutEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOverEvent;
import pl.edu.ur.pnes.ui.EditorMode;
import pl.edu.ur.pnes.ui.utils.FXMLUtils;
import pl.edu.ur.pnes.ui.utils.Populatable;
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
    private final Button addPlaceButton = new Button("Place");
    private final Button addTransitionButton = new Button("Transition");
    private final Button addArcButton = new Button("Arc");
    private final Button psabutton = new Button("PrintSelNodes");
    private final Button toggleModeButton = new Button("RUN mode 🏁");

    private final double MIN_SLIDER_DURATION = 100;
    private final double MAX_SLIDER_DURATION = 5000;
    private final double DEFAULT_SLIDER_DURATION = 1000;
    private final Slider speedSlider = new Slider(MIN_SLIDER_DURATION, MAX_SLIDER_DURATION, DEFAULT_SLIDER_DURATION);


    ObjectProperty<EditorMode> editorMode = new SimpleObjectProperty<>(EditorMode.EDIT);
    private EventHandler<MouseEvent> mouseEventEventHandler;
    private EventHandler<VisualizerMouseNodeClickedEvent> clickedEventEventHandler;

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
        editorMode.addListener((observable, oldValue, newValue) -> {
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

        visualizerFacade.addEventHandler(VisualizerEvent.NODES_MOVED, event -> {
            session.undoHistory.push(new MoveNodesAction(visualizerFacade, Arrays.asList(event.nodesIds), event.offset));
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


        editorMode.addListener((observable, oldValue, newValue) -> {
            if (newValue != EditorMode.RUN) {
                simulatorFacade.stopAutoStep();
                simulatorFacade.stopAutoStep();
            }
        });

        toggleModeButton.setOnAction(event -> editorMode.set(editorMode.getValue() == EditorMode.RUN ? EditorMode.EDIT : EditorMode.RUN));
        toggleModeButton.textProperty().bind(editorMode.asString("%s mode"));

        progressCircle.setProgress(0);

        centerToolbarLeft.getChildren().addAll(toggleModeButton, psabutton);
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

        // Add new Place on mouse point
        addPlaceButton.disableProperty().bind(editorMode.isNotEqualTo(EditorMode.EDIT));
        addPlaceButton.setOnAction(ActionEvent -> {
            this.mouseEventEventHandler = mouseEvent -> {
                Place place = new Place(net);
                Point3 mousePoint = new Point3(mouseEvent.getX(), mouseEvent.getY(), 0);
                net.addElement(place, visualizerFacade.mousePositionToGraphPosition(mousePoint));
                graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            };
            graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
        });

        /*
         * Add new Transition on mouse point
         */
        addTransitionButton.disableProperty().bind(editorMode.isNotEqualTo(EditorMode.EDIT));
        addTransitionButton.setOnAction(ActionEvent -> {
            this.mouseEventEventHandler = mouseEvent -> {
                Transition transition = new Transition(net);
                Point3 mousePoint = new Point3(mouseEvent.getX(), mouseEvent.getY(), 0);
                net.addElement(transition, visualizerFacade.mousePositionToGraphPosition(mousePoint));
                graphPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
            };
            graphPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
        });

        psabutton.setOnAction(event -> {
            visualizerFacade.printSelectedNodes();
        });


        /*
         * Set new Arc
         * If button is pressed, choose your first and second node to create arc
         */
        addArcButton.disableProperty().bind(editorMode.isNotEqualTo(EditorMode.EDIT));
        addArcButton.setOnAction(ActiveEvent -> {
            var ref = new Object() {
                Node inputNode = null;
                Node outputNode = null;
            };
            final EventHandler<VisualizerMouseNodeOverEvent> nodeOverEventEventHandler = event -> {
                if (ref.inputNode == null)
                    return;
                visualizerFacade.getElementById(event.getNodeId()).ifPresent(element -> {
                    if (element instanceof Node node)
                        element.getClasses().add(0, ref.inputNode.canBeConnectedTo(node) ? "goodHover" : "badHover");
                });
            };
            final EventHandler<VisualizerMouseNodeOutEvent> nodeOutEventEventHandler = event -> {
                visualizerFacade.getElementById(event.getNodeId()).ifPresent(element -> {
                    element.getClasses().remove("badHover");
                    element.getClasses().remove("goodHover");
                });
            };
            this.clickedEventEventHandler = event -> {
                event.consume();
                if (ref.inputNode != null) {
                    var el = visualizerFacade.getElementById(event.getClickedNodeId());
                    if (el.isEmpty())
                        return;
                    ref.outputNode = net.getAllNodesStream().filter(v -> Objects.equals(v.getId(), el.get().getId())).findAny().orElseThrow();

                    if (!ref.inputNode.canBeConnectedTo(ref.outputNode)) {
                        // cleanup
                        ref.outputNode.getClasses().remove("badHover");
                        ref.outputNode.getClasses().remove("goodHover");
                        ref.outputNode = null;
                        // try again
                        return;
                    }

                    Arc arc = new Arc(net, ref.inputNode, ref.outputNode);
                    net.addElement(arc);
                    System.out.println("Got output node: " + el.get().getName());

                    // cleanup
                    ref.outputNode.getClasses().remove("badHover");
                    ref.outputNode.getClasses().remove("goodHover");
                    visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
                    visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
                    visualizerFacade.removeEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);
                } else {
                    var el = visualizerFacade.getElementById(event.getClickedNodeId());
                    if (el.isEmpty())
                        return;
                    ref.inputNode = net.getAllNodesStream().filter(v -> Objects.equals(v.getId(), el.get().getId())).findAny().orElseThrow();
                    System.out.println("Got input node: " + el.get().getName());
                }
            };

            visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_OVER, nodeOverEventEventHandler);
            visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_OUT, nodeOutEventEventHandler);
            visualizerFacade.addEventFilter(VisualizerEvent.MOUSE_NODE_CLICKED, clickedEventEventHandler);
        });
    }
}
