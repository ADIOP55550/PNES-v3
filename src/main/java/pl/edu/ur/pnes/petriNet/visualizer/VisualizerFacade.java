package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
}
