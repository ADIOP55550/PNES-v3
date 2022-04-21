package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.DoubleProperty;
import pl.edu.ur.pnes.petriNet.Net;
import pl.edu.ur.pnes.petriNet.Place;
import pl.edu.ur.pnes.petriNet.Transition;

public class VisualizerFacade {
    final Visualizer visualizer;

    VisualizerFacade(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    public void visualizeNet(Net net) {
        visualizer.visualizeNet(net);
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
}
