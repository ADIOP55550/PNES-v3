package pl.edu.ur.pnes.panels;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
//import org.graphstream.algorithm.generator.Generator;
//import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import org.graphstream.ui.view.camera.Camera;

import java.awt.event.MouseEvent;

public class CenterPanel extends CustomPanel {

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
        graph.addEdge("AB", "A", "B");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

//        Viewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        View view = viewer.addDefaultView(false);   // false indicates "no JFrame".


        FxViewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        FxViewPanel view = (FxViewPanel) viewer.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        this.camera = view.getCamera();

        graphPane.widthProperty().addListener((observableValue, prev, curr) -> {

        });

        viewer.enableAutoLayout();
        view.getCamera().setViewPercent(1);
        ((Node) view).setOnScroll(e -> {
            e.consume();
            int i = (int) -e.getDeltaY() / 40;
            double factor = Math.pow(1.25, i);
            Camera cam = view.getCamera();
            double zoom = cam.getViewPercent() * factor;
            zoom = Math.min(100, Math.max(0.1, zoom));
            System.out.println(zoom);
            if (zoom == cam.getViewPercent())
                return;
            Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
            Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
            double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
            double x = guClicked.x + (pxCenter.x - e.getX()) / newRatioPx2Gu;
            double y = guClicked.y - (pxCenter.y - e.getY()) / newRatioPx2Gu;
            cam.setViewCenter(x, y, 0);
            cam.setViewPercent(zoom);
        });


        graphPane.getChildren().add((Node) view);
    }
}
