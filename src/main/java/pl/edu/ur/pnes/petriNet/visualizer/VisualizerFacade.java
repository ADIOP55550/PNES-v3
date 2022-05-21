package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.util.InteractiveElement;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.Node;

import java.awt.*;
import java.util.EnumSet;
import java.util.Optional;


public class VisualizerFacade {
    final Visualizer visualizer;
    private Net net;

    VisualizerFacade(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void visualizeNet(Net net) {
        this.net = net;
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

    /**
     * Sets graph background color
     * @param color new background color
     */
    public void setBackgroundColor(Color color) {
        visualizer.graph.setAttribute(
                "ui.stylesheet",
                String.format("graph { fill-color: #%02x%02x%02x; }", color.getRed(), color.getGreen(), color.getBlue()));

    }

    public void setNodePosition(String id, Point3D xy) {
        visualizer.setNodePosition(id, xy);
    }
    public Point3D getNodePosition(String id) {
        return visualizer.getNodePosition(id);
    }

    public void setNodePosition(Node node, Point3D xy) {
        visualizer.setNodePosition(node.getId(), xy);
    }
    public Point3D getNodePosition(Node node) {
        return visualizer.getNodePosition(node.getId());
    }

    public Optional<GraphicElement> findGraphicElementAt(double x, double y, EnumSet<InteractiveElement> elementType) {
        return visualizer.findGraphicElementAt(x, y, elementType);
    }

    public Optional<GraphicElement> findGraphicElementAt(double x, double y) {
        return visualizer.findGraphicElementAt(x, y);
    }

    public Point3D mousePositionToGraphPosition(Point3D mousePoint) {
        return visualizer.mousePositionToGraphPosition(mousePoint);
    }

    public Point3D mousePositionToGraphPosition(MouseEvent mouseEvent) {
        return visualizer.mousePositionToGraphPosition(new Point3D(mouseEvent.getX(), mouseEvent.getY(), 0));
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

    /**
     * Find element in the {@link #net} with the given id.
     *
     * @param id id of the element to find
     * @return Optional of the {@link NetElement} found.
     */
    public Optional<NetElement> getElementById(String id) {
        return this.net.getElementById(id);
    }
}
