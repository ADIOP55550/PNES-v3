package pl.edu.ur.pnes.petriNet.visualizer.events.net;

import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public abstract class VisualizerNetEvent extends VisualizerEvent {

    public VisualizerNetEvent(EventType<? extends VisualizerEvent> eventType) {
        super(eventType);
    }
}
