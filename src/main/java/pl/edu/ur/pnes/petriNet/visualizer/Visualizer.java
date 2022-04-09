package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseButton;
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
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.camera.Camera;

import java.util.Random;


class Visualizer {
    // Zoom factor is 'reversed' -> smaller number = bigger zoom
    static final int MIN_ZOOM = 20;
    static final double MAX_ZOOM = 0.1;
    public static final int RECT_WIDTH_BASE = 9;
    public static final int RECT_HEIGHT_BASE = 6;
    static double BASE_MOVE_SPEED = 8.5;
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
    private final FxGraphRenderer renderer;
    private final DoubleProperty moveSpeed = new SimpleDoubleProperty();
    private Point3 dragStart;
    private Point3 originalViewCenter;

    Point2 getRectangleSize(double zoomFactor) {
        double zoomrsqrt = 1/Math.sqrt(zoomFactor);
        return new Point2(
                RECT_WIDTH_BASE * zoomrsqrt,
                RECT_HEIGHT_BASE * zoomrsqrt
        );
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
        this.renderer = new FxGraphRenderer();
        this.viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        this.view = (FxViewPanel) viewer.addView(FxViewer.DEFAULT_VIEW_ID, renderer);

        view.getCamera().setViewPercent(1);
        moveSpeed.bind(zoomFactor.multiply(BASE_MOVE_SPEED));


//      #region test nodes
        logger.debug("adding test nodes...");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");

        Random r = new Random();
        final int NODE_COUNT = 800;
        for (int i = 0; i < NODE_COUNT; i++) {
            graph.addNode("a" + i);
        }
        for (int i = 0; i < NODE_COUNT; i++) {
            int a1 = r.nextInt(NODE_COUNT);
            int a2 = r.nextInt(NODE_COUNT);
            try {
                graph.addEdge("a" + a1 + "-" + a2, "a" + a1, "a" + a2);
            } catch (Exception ignored) {
            }
        }

        graph.addNode("F");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("EF", "E", "F");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
//      #endregion test nodes


        autoLayout.addListener((observableValue, oldVal, newVal) -> {
            if (newVal)
                viewer.enableAutoLayout();
            else
                viewer.disableAutoLayout();
        });

        zoomFactor.addListener((observableValue, oldVal, newVal) -> {
            Camera cam = view.getCamera();
            if (newVal.doubleValue() == cam.getViewPercent())
                return;
            cam.setViewPercent(newVal.doubleValue());
            Point2 rectSize = getRectangleSize(newVal.doubleValue());
            graph.nodes().forEach(node -> {
                node.setAttribute("ui.style", "size: " + rectSize.x + "px, " + rectSize.y + "px;");
            });
        });

        view.setOnScroll(this::handleScroll);

        Point2 rectSize = getRectangleSize(zoomFactor.doubleValue());
        graph.nodes().forEach(node -> {
            node.setAttribute("xy", r.nextInt(5800) - 400, r.nextInt(5800) - 400);
//            initial rect size
//            node.setAttribute("ui.style", "size: 50px, 30px;");
            node.setAttribute("ui.style", "size: " + rectSize.x + "px, " + rectSize.y + "px;");
        });

        view.setOnMousePressed(mouseEvent -> {
            this.dragStart = new Point3(mouseEvent.getScreenX(), mouseEvent.getScreenY());
            this.originalViewCenter = new Point3(view.getCamera().getViewCenter());
        });

        view.setOnMouseDragged(e -> {
//            only move if button is middle mouse button or ctrl is pressed
            if (!e.isControlDown() && e.getButton() != MouseButton.MIDDLE) return;

            e.consume();
            renderer.endSelectionAt(e.getX(), e.getY());

            view.getCamera().setViewCenter(
                    originalViewCenter.x + (dragStart.x - e.getScreenX()) * moveSpeed.getValue(),
                    originalViewCenter.y + -(dragStart.y - e.getScreenY()) * moveSpeed.getValue(),
                    originalViewCenter.z
            );
        });

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
        Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
        Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
        double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
        double x = guClicked.x + (pxCenter.x - e.getX()) / newRatioPx2Gu;
        double y = guClicked.y - (pxCenter.y - e.getY()) / newRatioPx2Gu;
        cam.setViewCenter(x, y, 0);
    }
}
