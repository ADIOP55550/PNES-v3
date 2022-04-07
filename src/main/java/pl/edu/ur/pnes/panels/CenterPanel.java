package pl.edu.ur.pnes.panels;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;

import java.awt.event.MouseEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CenterPanel extends CustomPanel {
    public static BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);
    public static BooleanProperty scrollPressed = new SimpleBooleanProperty(false);
    @FXML
    public HBox centerToolbarRight;
    @FXML
    public HBox centerToolbarLeft;
    @FXML
    public HBox centerToolbar;
    @FXML
    public Pane graphPane;

    MouseEvent last;
    Point2D dragFrom = null;
    double scrollZoomMultiplier = 0.01;

    private class ZoomingPane extends Pane {
        Node content;
        private final DoubleProperty zoomFactor = new SimpleDoubleProperty(1);

        private ZoomingPane(Node content) {
            this.content = content;
            getChildren().add(content);
            Scale scale = new Scale(1, 1);
//            CompletableFuture.delayedExecutor(200, TimeUnit.MILLISECONDS).execute(() -> {
//                Platform.runLater(() -> {
//
//                    scale.setPivotX(this.getWidth() / 2);
//                    scale.setPivotY(this.getHeight() / 2);
//                    Circle circle = new Circle(5, Color.RED);
//                    this.getChildren().add(circle);
//                    circle.setCenterX(scale.getPivotX());
//                    circle.setCenterY(scale.getPivotY());
//                });
//
//            });
            content.getTransforms().add(scale);

            zoomFactor.addListener((observable, oldValue, newValue) -> {
                scale.setX(newValue.doubleValue());
                scale.setY(newValue.doubleValue());
                requestLayout();
            });
        }

        protected void layoutChildren() {
            Pos pos = Pos.TOP_LEFT;
            double width = getWidth();
            double height = getHeight();
            double top = getInsets().getTop();
            double right = getInsets().getRight();
            double left = getInsets().getLeft();
            double bottom = getInsets().getBottom();
            double contentWidth = (width - left - right) / zoomFactor.get();
            double contentHeight = (height - top - bottom) / zoomFactor.get();
            layoutInArea(content, left, top,
                    contentWidth, contentHeight,
                    0, null,
                    pos.getHpos(),
                    pos.getVpos());
        }

        public final Double getZoomFactor() {
            return zoomFactor.get();
        }

        public final void setZoomFactor(Double zoomFactor) {
            this.zoomFactor.set(zoomFactor);
        }

        public final DoubleProperty zoomFactorProperty() {
            return zoomFactor;
        }
    }


    public void initialize() {

        graphPane.setPrefHeight(1000);
        graphPane.setPrefWidth(1000);

        DoubleProperty zoomFactor = new SimpleDoubleProperty(1) {
            @Override
            public void set(double v) {
                v = Math.min(5, Math.max(0.1, v));
                super.set(v);
            }
        };

        Graph<String, String> g = new GraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

//yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
        ZoomingPane zoomingPane = new ZoomingPane(graphView);
        graphPane.getChildren().add(zoomingPane);
        zoomingPane.zoomFactor.bind(zoomFactor);

        graphView.setPrefWidth(graphPane.getPrefWidth());
        graphView.setPrefHeight(graphPane.getPrefHeight());

        graphView.getStylableVertex("A").setStyleClass("myVertex");

        centerToolbar.toFront();

        centerToolbar.setMinWidth(graphPane.getWidth());

//        Delay for scene to load
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(() -> {
            Platform.runLater(graphView::init);
        });

        graphView.setOnMousePressed(mouseEvent -> {
            dragFrom = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        });

        graphView.setOnMouseDragged(mouseDragEvent -> {
            if (dragFrom == null)
                return;

            g.vertices().forEach(vertex -> {
                var oldX = graphView.getVertexPositionX(vertex);
                var oldY = graphView.getVertexPositionY(vertex);
                graphView.setVertexPosition(vertex, oldX - dragFrom.getX() + mouseDragEvent.getX(), oldY - dragFrom.getY() + mouseDragEvent.getY());
//                vertex.setStyle();
            });

            dragFrom = new Point2D(mouseDragEvent.getX(), mouseDragEvent.getY());
        });


        graphPane.setOnScroll(scrollEvent -> {
            zoomFactor.set(zoomFactor.getValue() + scrollEvent.getDeltaY() * scrollZoomMultiplier);
        });
    }
}
