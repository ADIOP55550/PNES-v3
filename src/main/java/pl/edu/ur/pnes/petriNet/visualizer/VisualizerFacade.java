package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.util.InteractiveElement;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

import java.awt.*;
import java.util.List;
import java.util.*;


public class VisualizerFacade {
    final Visualizer visualizer;
    VisualizerFacade(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void visualizeNet(Net net) {
        Platform.runLater(() -> visualizer.visualizeNet(net));
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

    public Point3 mousePositionToGraphPosition(Point3 mousePoint) {
        return visualizer.mousePositionToGraphPosition(mousePoint);
    }

    public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        visualizer.getVisualizerEventsHandler().addEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        visualizer.getVisualizerEventsHandler().removeEventHandler(eventType, eventHandler);
    }

    public <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        visualizer.getVisualizerEventsHandler().addEventFilter(eventType, eventFilter);
    }

    public <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        visualizer.getVisualizerEventsHandler().removeEventFilter(eventType, eventFilter);
    }
}
