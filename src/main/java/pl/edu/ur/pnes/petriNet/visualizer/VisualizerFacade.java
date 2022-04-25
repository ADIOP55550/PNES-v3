package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.NetElement;

import java.util.Locale;

public class VisualizerFacade {
    final Visualizer visualizer;

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

//    public void markForRedraw(NetElement element) {
//        if (visualizer.toBeRedrawn.contains(element))
//            return;
//        visualizer.toBeRedrawn.add(element);
//    }
//
//    public void setMode(Mode mode) {
//        visualizer.graph.setAttribute("ui.class", mode.toString().toLowerCase(Locale.ROOT).replaceAll("[_]", ""));
//    }
}
