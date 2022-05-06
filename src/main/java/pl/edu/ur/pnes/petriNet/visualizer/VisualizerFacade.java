package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.util.InteractiveElement;
import pl.edu.ur.pnes.petriNet.Arc;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

import java.awt.*;
import java.util.List;
import java.util.*;


public class VisualizerFacade {


    final Visualizer visualizer;
    private final HashMap<EventType<? extends VisualizerEvent>, List<EventHandler<? extends VisualizerEvent>>> handlers = new HashMap<>();
    private final HashMap<EventType<? extends VisualizerEvent>, List<EventHandler<? extends VisualizerEvent>>> filters = new HashMap<>();

    VisualizerFacade(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void visualizeNet(Net net) {
        Platform.runLater(() -> {
            visualizer.visualizeNet(net);
        });
    }

    public DoubleProperty zoomProperty() {
        return visualizer.zoomFactor;
    }

    public double getZoom() {
        return visualizer.zoomFactor.get();
    }

    public void setZoom(double zoom) {
        visualizer.zoomFactor.set(zoom);
    }

    public void enableAutoLayout() {
        visualizer.autoLayout.setValue(true);
    }

    public void disableAutoLayout() {
        visualizer.autoLayout.setValue(false);
    }

    public void printSelectedNodes() {
        visualizer.printSelectedNodes();
    }

    public <T extends VisualizerEvent> void addEventHandler(EventType<T> eventType, EventHandler<T> handler) {
        if (!this.handlers.containsKey(eventType)) {
            this.handlers.put(eventType, new ArrayList<>());
        }

        this.handlers.get(eventType).add(handler);
    }

    public <T extends VisualizerEvent> void addEventFilter(EventType<T> eventType, EventHandler<T> handler) {
        if (!this.handlers.containsKey(eventType)) {
            this.handlers.put(eventType, new ArrayList<>());
        }

        this.handlers.get(eventType).add(handler);
    }

    public void setBackgroundColor(Color color) {
        visualizer.graph.setAttribute("ui.color", color);
    }

    public void setNodePosition(Node node, Point3 point3) {
        visualizer.setNodePosition(node, point3);
    }

    public Optional<GraphicElement> findGraphicElementAt(double x, double y, EnumSet<InteractiveElement> elementType) {
        return visualizer.findGraphicElementAt(x, y, elementType);
    }

    public Optional<GraphicElement> findGraphicElementAt(double x, double y) {
        return visualizer.findGraphicElementAt(x, y);
    }

    public void addNodeToNet(Node node) {
        visualizer.addNodeToNet(node);
    }

    public void addArcToNet(Arc arc) {
        visualizer.addArcToNet(arc);
    }

    public Point3 mousePositionToGraphPosition(Point3 mousePoint) {
        return visualizer.mousePositionToGraphPosition(mousePoint);
    }
}
