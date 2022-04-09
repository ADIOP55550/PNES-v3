package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import pl.edu.ur.pnes.petriNet.Net;

public class VisualizerFacade {
    final Visualizer visualizer;

    VisualizerFacade(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void visualizeNet(Net net) {
        throw new UnsupportedOperationException("Not implemented yet.");
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
}
