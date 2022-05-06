package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.camera.Camera;
import org.graphstream.ui.view.util.InteractiveElement;
import pl.edu.ur.pnes.petriNet.*;
import pl.edu.ur.pnes.petriNet.visualizer.events.VGVLEvent;

import java.util.*;


class Visualizer {
    // Zoom factor is 'reversed' -> smaller number = bigger zoom
    static final int MIN_ZOOM = 20;
    static final double MAX_ZOOM = 0.08;
    static final int RECT_WIDTH_BASE = 18;
    static final int RECT_HEIGHT_BASE = 12;
    static final int ARROW_WIDTH_BASE = 9;
    static final int ARROW_HEIGHT_BASE = 4;
    static double BASE_MOVE_SPEED = 0.15;

    final DoubleProperty zoomFactor;
    final BooleanProperty autoLayout = new SimpleBooleanProperty(false);
    SpriteManager spriteManager;

    private final static Logger logger = LogManager.getLogger();
    private final FxViewer viewer;
    private final FxViewPanel view;
    final Graph graph;
    private final FxGraphRenderer renderer;
    private double moveSpeed;
    private Point3 dragStart;
    private Point3 originalViewCenter;
    private final Map<Place, Sprite> placeTokensSpriteMap = new HashMap<>();
    private final Map<Node, Sprite> nodeIdSpriteMap = new HashMap<>();
    private final Map<Arc, Sprite> arcIdSpriteMap = new HashMap<>();
    private final Map<Arc, Sprite> arcWeightSpriteMap = new HashMap<>();

    private final VisualizerGraphViewerListener visualizerGraphViewerListener;
    private boolean loop = true;
    private Net net;
    private List<BooleanProperty> THIS_IS_MAGIC_DO_NOT_TOUCH_ME = new ArrayList<>();

    {
        zoomFactor = new SimpleDoubleProperty(1) {
            @Override
            public void set(double v) {
                v = Math.min(MIN_ZOOM, Math.max(MAX_ZOOM, v));
                super.set(v);
            }
        };
        moveSpeed = BASE_MOVE_SPEED * zoomFactor.doubleValue();


    }

    static {
        System.setProperty("org.graphstream.ui", "javafx");
    }


    double getSizeScaleFactor(double zoomFactor) {
        return (1 / Math.sqrt(zoomFactor)) * 2;
    }

    Pane getElement() {
        return this.view;
    }

    void addStyles(String stylesheetPath) {
        stylesheetPath = "url(file://" + stylesheetPath + ")";
        logger.debug("Loading graph stylesheet: {}", stylesheetPath);
        graph.setAttribute("ui.stylesheet", stylesheetPath);
    }

    Visualizer() {
        System.out.println("System.getProperty(\"log4j2.configurationFile\") = " + System.getProperty("log4j2.configurationFile"));
        this.graph = new DefaultGraph("Graph 1");
        this.spriteManager = new SpriteManager(graph);
        this.renderer = new FxGraphRenderer();
        this.viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        this.view = (FxViewPanel) viewer.addView(FxViewer.DEFAULT_VIEW_ID, renderer);

        this.visualizerGraphViewerListener = new VisualizerGraphViewerListener(viewer);


        // EXAMPLE VGVL EVENT HANDLER
        visualizerGraphViewerListener.addEventHandler(VGVLEvent.NODE_CLICKED, event -> {
            System.out.println("Node Clicked! (handler) #" + event.getClickedNodeId());
        });
        // EXAMPLE VGVL EVENT FILTER
        visualizerGraphViewerListener.addEventFilter(VGVLEvent.NODE_CLICKED, event -> {
            System.out.println("Node Clicked! (filter) #" + event.getClickedNodeId());
        });

        view.getCamera().setGraphViewport(0, 0, 100, 100);

        view.getCamera().setViewPercent(1);

//        generateTestNodes(40);

        autoLayout.addListener(this::onAutoLayoutPropertyChanged);
        zoomFactor.addListener(this::onCanvasZoom);
        view.setOnScroll(this::handleScroll);
        view.setOnMousePressed(this::onCanvasMousePressed);
        view.setOnMouseDragged(this::onCanvasMouseDragged);
        setupGraph();
        spawnUpdateThread();
    }

    private Thread spawnUpdateThread() {
        var t = new Thread(() -> {
            try {
                while (loop) {
                    this.tick();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        return t;
    }

    private void tick() {
//        while (!toBeRedrawn.isEmpty()) {
//            var element = toBeRedrawn.remove();
//            redrawElement(element);
//        }
    }

    private void redrawElement(NetElement element) {
        // TODO other redrawal parts
        logger.info("Redrawing element " + element.getName());

        var graphNode = graph.getNode(element.getId());
        graphNode.setAttribute("ui.class", element.getClasses().stream().reduce("", (s, s2) -> s + ", " + s2));
    }

    private void generateTestNodes(int node_count) {
        Random r = new Random();
        logger.debug("adding test nodes...");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");

        for (int i = 0; i < node_count; i++) {
            graph.addNode("a" + i);
            graph.getNode("a" + i).setAttribute("xy", r.nextInt(20000) - 10000, r.nextInt(20000) - 10000);
        }
        for (int i = 1; i < node_count; i++) {
            graph.addEdge("a" + (i - 1) + "-" + i, "a" + (i - 1), "a" + i, true);
        }

        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("EF", "E", "F", true);
        graph.addEdge("AD", "A", "D", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);
    }

    private void setupGraph() {
        view.getCamera().setAutoFitView(false);
        view.getCamera().setBounds(0, 0, 0, 100, 100, 100);

        view.enableMouseOptions();
//        equal to:
//        viewer.getDefaultView().setMouseManager(new MouseOverMouseManager(EnumSet.of(InteractiveElement.EDGE, InteractiveElement.NODE, InteractiveElement.SPRITE)));


        view.getCamera().setAutoFitView(false);
    }

    private void handleScroll(ScrollEvent e) {
        e.consume();
        int i = (int) -e.getDeltaY() / 40;
        double factor = Math.pow(1.25, i);
        Camera cam = view.getCamera();
        double finalZoomFactor = cam.getViewPercent() * factor;
        zoomFactor.setValue(finalZoomFactor);
        // Prevent moving camera if zoom is over max or min (this condition should be as is as zoom factor is reversed - min zoom is bigger than max zoom)
        if (finalZoomFactor <= MAX_ZOOM || finalZoomFactor >= MIN_ZOOM)
            return;
        Point2 guCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
        Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
        double newRatiogu2Gu = cam.getMetrics().ratioPx2Gu / factor;
        double x = guClicked.x + (guCenter.x - e.getX()) / newRatiogu2Gu;
        double y = guClicked.y - (guCenter.y - e.getY()) / newRatiogu2Gu;
        cam.setViewCenter(x, y, 0);
    }

    private void onCanvasZoom(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
        Camera cam = view.getCamera();
        if (newVal.doubleValue() == cam.getViewPercent())
            return;

        cam.setViewPercent(newVal.doubleValue());
        this.recalculateMoveSpeed();
    }

    private void onAutoLayoutPropertyChanged(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
        if (newVal)
            viewer.enableAutoLayout();
        else {
            viewer.disableAutoLayout();
            view.getCamera().setAutoFitView(false);
            view.getCamera().setBounds(0, 0, 0, 100, 100, 100);
        }
    }

    private void onCanvasMousePressed(MouseEvent mouseEvent) {
        this.dragStart = new Point3(mouseEvent.getScreenX(), mouseEvent.getScreenY());
        this.originalViewCenter = new Point3(view.getCamera().getViewCenter());
        this.recalculateMoveSpeed();
    }

    private void recalculateMoveSpeed() {
        this.moveSpeed = zoomFactor.doubleValue() * BASE_MOVE_SPEED;
    }

    private void onCanvasMouseDragged(MouseEvent e) {
//            only move if button is middle mouse button or ctrl is pressed
        if (!e.isControlDown() && e.getButton() != MouseButton.MIDDLE) return;

        e.consume();

        renderer.endSelectionAt(0, 0);

        view.getCamera().setViewCenter(
                originalViewCenter.x + (dragStart.x - e.getScreenX()) * moveSpeed,
                originalViewCenter.y + -(dragStart.y - e.getScreenY()) * moveSpeed,
                originalViewCenter.z
        );
    }

    public void printSelectedNodes() {
        graph.nodes().forEach(node -> {
            if (node.hasAttribute("ui.selected"))
                System.out.println(node);
        });
    }

    public void visualizeNet(Net net) {


        this.net = net;

        // hook redraw
        net.allElementsStream().forEach(element -> {
            logger.info("Hooking redraw listener to {}", element.getName());
            element.needsRedraw.addListener((observableValue, oldVal, newVal) -> {
                logger.info("Needs redraw changed for node {}", element.getName());
                if (newVal) {
                    this.redrawElement(element);
                    element.needsRedraw.set(false);
                }
            });

            if (element.needsRedraw.get())
                element.needsRedraw.set(false);
        });

        net.getAllNodesStream().forEach(this::addNodeToNet);

        net.getArcs().forEach(this::addArcToNet);
    }

    void addNodeToNet(Node node) {

        final double sizeX = 1;
        final double sizeY = 0.6;
        logger.info("Adding node " + node.getName());

        var graphNode = graph.addNode(node.getId());
        if (node instanceof Place)
            graphNode.setAttribute("isCircle");
        graphNode.setAttribute("ui.style", "size: " + sizeX + "gu, " + (graphNode.hasAttribute("isCircle") ? sizeX : sizeY) + "gu;");
        graphNode.setAttribute("ui.class", node.getClasses().stream().reduce("", (s, s2) -> s + ", " + s2));

        Sprite nameSprite = getAttachedTextSprite(
                node,
                "label",
                new Point3(0.5, -0.5, 0),
                node.getName()
        );
        // attach listener for future label changes
        node.nameProperty().addListener((observableValue, oldVal, newVal) -> {
            logger.debug("New value for sprite {}: {}", nameSprite.getId(), newVal);
            nameSprite.setAttribute("ui.label", newVal);
        });

        nodeIdSpriteMap.put(node, nameSprite);

        // only Places can display Tokens
        if (node instanceof Place) {
            Sprite tokensSprite = getAttachedTextSprite(
                    node,
                    "tokens",
                    Point3.NULL_POINT3,
                    String.valueOf(((Place) node).getTokens()),
                    List.of("tokens")
            );

            // attach listener for future label changes
            ((Place) node).tokensProperty().addListener((observableValue, oldVal, newVal) -> {
                logger.debug("New value for sprite {}: {}", tokensSprite.getId(), newVal);
                tokensSprite.setAttribute("ui.label", newVal);
            });

            showSpriteConditionally(tokensSprite, ((Place) node).tokensProperty().isNotEqualTo(0));

            placeTokensSpriteMap.put((Place) node, tokensSprite);
        }
    }

    void addArcToNet(Arc arc) {
        logger.info("Adding arc " + arc.getId() + " (" + arc.input.getId() + "-" + arc.output.getId() + ")");
        Edge graphArc;
        graphArc = graph.addEdge(arc.getId(), arc.input.getId(), arc.output.getId(), true);
        graphArc.setAttribute("ui.class", arc.getClasses().stream().reduce("", (s, s2) -> s + ", " + s2));

        Sprite nameSprite = getAttachedTextSprite(
                arc,
                "label",
                new Point3(0.5, -0.5, 0),
                arc.getName()
        );
        // attach listener for future label changes
        arc.nameProperty().addListener((observableValue, oldVal, newVal) -> {
            logger.debug("New value for sprite {}: {}", nameSprite.getId(), newVal);
            nameSprite.setAttribute("ui.label", newVal);
        });
        arcIdSpriteMap.put(arc, nameSprite);

        Sprite weightSprite = getAttachedTextSprite(
                arc,
                "weight",
                new Point3(0.5, -0.85, 0),
                String.valueOf(arc.getWeight())
        );
        // attach listener for future label changes
        arc.weightProperty().addListener((observableValue, oldVal, newVal) -> {
            logger.debug("New value for sprite {}: {}", weightSprite.getId(), newVal);
            weightSprite.setAttribute("ui.label", newVal);
        });

        showSpriteConditionally(weightSprite, arc.weightProperty().isNotEqualTo(1));
        arcWeightSpriteMap.put(arc, weightSprite);
    }

    private Sprite getAttachedTextSprite(NetElement element, String idAppendix, Point3 offset, String initialValue) {
        return getAttachedTextSprite(element, idAppendix, offset, initialValue, List.of("label"));
    }

    private Sprite getAttachedTextSprite(NetElement element, String idAppendix, Point3 offset, String initialValue, List<String> uiClasses) {
        System.out.println();
        System.out.println("Visualizer.getAttachedTextSprite");
        System.out.println("element = " + element + ", idAppendix = " + idAppendix + ", offset = " + offset + ", initialValue = " + initialValue + ", uiClasses = " + uiClasses);
        // setting up sprite
        Sprite sprite = spriteManager.addSprite(element.getId() + idAppendix);

        // attach to the given element
        if (element instanceof Arc)
            sprite.attachToEdge(element.getId());
        else
            sprite.attachToNode(element.getId());

        // Apply offset
        sprite.setPosition(StyleConstants.Units.GU, offset.x, offset.y, offset.z);

        // Apply classes
        sprite.setAttribute("ui.class", uiClasses.stream().reduce("", (s, s2) -> s + ", " + s2));

        // set label text
        sprite.setAttribute("ui.label", initialValue);

        return sprite;
    }

    private void showSpriteConditionally(Sprite sprite, ObservableBooleanValue shownCondition) {

        final SimpleBooleanProperty property = new SimpleBooleanProperty();
        this.THIS_IS_MAGIC_DO_NOT_TOUCH_ME.add(property);
        property.bind(shownCondition);

        // Create changes listener
        final ChangeListener<Boolean> changeListener = (observableValue, oldVal, newVal) -> {
            if (newVal)
                logger.info("Snowing sprite {} conditionally", sprite.getId());
            else
                logger.info("Hiding sprite {} conditionally", sprite.getId());

            if (!sprite.hasAttribute("ui.class"))
                sprite.setAttribute("ui.class", "");

            if (newVal)
                // If it needs to be shown, remove all .hidden classes
                sprite.setAttribute("ui.class", ((String) sprite.getAttribute("ui.class")).replaceAll("hide, ", ""));

            else
                // Else, add .hide class
                sprite.setAttribute("ui.class", "hide, " + sprite.getAttribute("ui.class"));
        };

        // Attach the listener
        property.addListener(changeListener);

        // Fire it once to set initial class
        changeListener.changed(shownCondition, !shownCondition.get(), shownCondition.get());
    }

    Optional<GraphicElement> findGraphicElementAt(double x, double y, EnumSet<InteractiveElement> elementType) {
        return Optional.ofNullable(view.getCamera().findGraphicElementAt(viewer.getGraphicGraph(), elementType, x, y));
    }

    Optional<GraphicElement> findGraphicElementAt(double x, double y) {
        return findGraphicElementAt(x, y, EnumSet.of(InteractiveElement.NODE));
    }

    void setNodePosition(Node node, Point3 graphPoint) {
        graph.getNode(node.getId()).setAttribute("xyz", graphPoint.x, graphPoint.y, graphPoint.z);
    }

    Point3 mousePositionToGraphPosition(Point3 mousePoint) {
        final Point3 loVisible = view.getCamera().getMetrics().loVisible;
        final Point3 hiVisible = view.getCamera().getMetrics().hiVisible;
        final double inverseRatioPx2Gu = 1 / view.getCamera().getMetrics().ratioPx2Gu;
        return new Point3(
                loVisible.x + (mousePoint.x * inverseRatioPx2Gu),
                hiVisible.y - (mousePoint.y * inverseRatioPx2Gu),
                loVisible.z + (mousePoint.z * inverseRatioPx2Gu)
        );
    }

}
