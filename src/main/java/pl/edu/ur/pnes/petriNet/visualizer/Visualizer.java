package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.camera.Camera;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Place;

import java.util.Random;


class Visualizer {
    // Zoom factor is 'reversed' -> smaller number = bigger zoom
    static final int MIN_ZOOM = 20;
    static final double MAX_ZOOM = 0.08;
    static final int RECT_WIDTH_BASE = 18;
    static final int RECT_HEIGHT_BASE = 12;
    static final int ARROW_WIDTH_BASE = 9;
    static final int ARROW_HEIGHT_BASE = 4;
    static double BASE_MOVE_SPEED = 0.2;
    final DoubleProperty zoomFactor = new SimpleDoubleProperty(1) {
        @Override
        public void set(double v) {
            v = Math.min(MIN_ZOOM, Math.max(MAX_ZOOM, v));
            super.set(v);
        }
    };
    final BooleanProperty autoLayout = new SimpleBooleanProperty(false);

    private final static Logger logger = LogManager.getLogger();

    private final FxViewer viewer;
    private final FxViewPanel view;
    private final Graph graph;
    SpriteManager spriteManager;
    private final FxGraphRenderer renderer;
    private double moveSpeed = BASE_MOVE_SPEED * zoomFactor.doubleValue();
    private Point3 dragStart;
    private Point3 originalViewCenter;

    double getSizeScaleFactor(double zoomFactor) {
        return (1 / Math.sqrt(zoomFactor)) * 2;
    }

    static {
        System.setProperty("org.graphstream.ui", "javafx");
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

        view.getCamera().setGraphViewport(0, 0, 100, 100);

        view.getCamera().setViewPercent(1);
//        moveSpeed.bind(zoomFactor.multiply(BASE_MOVE_SPEED));

//        generateTestNodes(40);

        autoLayout.addListener(this::onAutoLayoutPropertyChanged);
        zoomFactor.addListener(this::onCanvasZoom);
        view.setOnScroll(this::handleScroll);
        view.setOnMousePressed(this::onCanvasMousePressed);
        view.setOnMouseDragged(this::onCanvasMouseDragged);
        setupGraph();
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


//            int a1 = r.nextInt(node_count);
//            int a2 = r.nextInt(node_count);
//            try {
//                graph.addEdge("a" + a1 + "-" + a2, "a" + a1, "a" + a2);
//            } catch (Exception ignored) {
//            }
        }

        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("EF", "E", "F", true);
        graph.addEdge("AD", "A", "D", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);
    }

    private void setupGraph() {
        double scaleFactor = getSizeScaleFactor(zoomFactor.doubleValue());
        final double sizeX = scaleFactor * RECT_WIDTH_BASE;
        final double sizeY = scaleFactor * RECT_HEIGHT_BASE;

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

//        System.out.println("renderer.getCamera().getMetrics() = " + renderer.getCamera().getMetrics());

        renderer.endSelectionAt(0, 0);

        view.getCamera().setViewCenter(
                originalViewCenter.x + (dragStart.x - e.getScreenX()) * moveSpeed,
                originalViewCenter.y + -(dragStart.y - e.getScreenY()) * moveSpeed,
                originalViewCenter.z
        );
    }

    public void visualizeNet(Net net) {
//        double scaleFactor = getSizeScaleFactor(zoomFactor.doubleValue());
//        final double sizeX = scaleFactor * RECT_WIDTH_BASE;
//        final double sizeY = scaleFactor * RECT_HEIGHT_BASE;

        final double sizeX = 1;
        final double sizeY = 0.6;

        net.allNodesStream().forEach(node -> {
            logger.info("Adding node " + node.getId());
            var graphNode = graph.addNode(node.getId());
            if (node instanceof Place)
                graphNode.setAttribute("isCircle");
            graphNode.setAttribute("ui.style", "size: " + sizeX + "gu, " + (graphNode.hasAttribute("isCircle") ? sizeX : sizeY) + "gu;");
            graphNode.setAttribute("ui.class", node.getClasses().stream().reduce("", (s, s2) -> s + ", " + s2));
            Sprite aSprite = spriteManager.addSprite(node.getId() + "label");
            aSprite.attachToNode(node.getId()); //this sets where the Label will be (adjust to fit your purpose)
            aSprite.setPosition(StyleConstants.Units.GU, 0.5, -0.5, 0);
            aSprite.setAttribute("ui.label", node.getId());
            aSprite.setAttribute("ui.class", "label");

//            graphNode.setAttribute("ui.label", node.getId());
        });

        net.getArcs().forEach(arc -> {
            logger.info("Adding arc " + arc.getId() + " (" + arc.input.getId() + "-" + arc.output.getId() + ")");
            var graphArc = graph.addEdge(arc.getId(), arc.input.getId(), arc.output.getId(), true);
            graphArc.setAttribute("ui.class", arc.getClasses().stream().reduce("", (s, s2) -> s + ", " + s2));
        });
    }
}
