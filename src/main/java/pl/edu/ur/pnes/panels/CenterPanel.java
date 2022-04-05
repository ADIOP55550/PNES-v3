package pl.edu.ur.pnes.panels;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.camera.Camera;

import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CenterPanel extends CustomPanel {
    public static BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);

    @FXML
    public HBox centerToolbarRight;
    @FXML
    public HBox centerToolbarLeft;
    @FXML
    public HBox centerToolbar;
    @FXML
    public Pane graphPane;

    MouseEvent last;
    private Camera camera;

    public void processDrag(MouseEvent event) {
        if (last != null) {
//see DefaultShortcutManager
            Point3 p1 = camera.getViewCenter();
            Point3 p2 = camera.transformGuToPx(p1.x, p1.y, 0);
            int xdelta = event.getX() - last.getX();//determine direction
            int ydelta = event.getY() - last.getY();//determine direction
//sysout("xdelta "+xdelta+" ydelta "+ydelta);
            p2.x -= xdelta;
            p2.y -= ydelta;
            Point3 p3 = camera.transformPxToGu(p2.x, p2.y);
            camera.setViewCenter(p3.x, p3.y, 0);
        }
        last = event;
    }

    public void resetDrag() {
        this.last = null;
    }

    public void initialize() {
        System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new DefaultGraph("Tutorial 1");


//        Generator gen = new RandomGenerator(2);
//        gen.addSink(graph);
//        gen.begin();
//        for (int i = 0; i < 100; i++)
//            gen.nextEvents();
//        gen.end();
//        graph.display();

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");

        Platform.runLater(() -> {
            graphPane.getScene().setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.CONTROL)
                    ctrlPressed.setValue(true);
            });

            graphPane.getScene().setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.CONTROL)
                    ctrlPressed.setValue(false);
            });

        });


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

//        Viewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        View view = viewer.addDefaultView(false);   // false indicates "no JFrame".


        DoubleProperty zoomFactor = new SimpleDoubleProperty(1) {
            @Override
            public void set(double v) {
                v = Math.min(5, Math.max(0.1, v));
                super.set(v);
            }
        };


        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        FxViewPanel view = (FxViewPanel) viewer.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        this.camera = view.getCamera();


        ((Button) centerToolbarLeft.getChildren().get(centerToolbarLeft.getChildren().size() - 1)).setOnAction(actionEvent -> {
            CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS).execute(() -> {
                Platform.runLater(viewer::disableAutoLayout);
            });
        });

        zoomFactor.addListener((observableValue, oldVal, newVal) -> {
            System.out.println(ctrlPressed.get());
            Camera cam = view.getCamera();
            if (newVal.doubleValue() == cam.getViewPercent())
                return;
            cam.setViewPercent(newVal.doubleValue());
            graph.nodes().forEach(node -> {
                node.setAttribute("ui.style", new StringBuilder().append("shape: box; fill-color: blue; stroke-color: black; size: ").append(9 / Math.sqrt(newVal.doubleValue())).append("px, ").append(6 / Math.sqrt(newVal.doubleValue())).append("px;").toString());
            });
        });

        graph.nodes().forEach(node -> {
            node.setAttribute("xy", r.nextInt(5800) - 400, r.nextInt(5800) - 400);
            node.setAttribute("ui.style", "shape: box; fill-color: blue; stroke-color: black; size: 50px, 30px;");
        });

        view.getCamera().setViewPercent(1);
        ((Node) view).setOnScroll(e -> {
            e.consume();
            int i = (int) -e.getDeltaY() / 40;
            double factor = Math.pow(1.25, i);
            Camera cam = view.getCamera();
            zoomFactor.setValue(cam.getViewPercent() * factor);
            Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
            Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
            double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
            double x = guClicked.x + (pxCenter.x - e.getX()) / newRatioPx2Gu;
            double y = guClicked.y - (pxCenter.y - e.getY()) / newRatioPx2Gu;
            cam.setViewCenter(x, y, 0);
        });


        graphPane.getChildren().add((Node) view);
    }
}
