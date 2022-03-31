package pl.edu.ur.pnes.panels;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class CenterPanel extends CustomPanel {

    @FXML
    public HBox centerToolbarRight;
    @FXML
    public HBox centerToolbarLeft;
    @FXML
    public HBox centerToolbar;
    @FXML
    public Pane graphPane;


    public void initialize() {
        System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");


//        graph.display();
        Viewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);


// ...
        View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
//        view.getCamera().setViewPercent(0.5);
// ...
        graphPane.getChildren().add((Node) view);
    }
}
