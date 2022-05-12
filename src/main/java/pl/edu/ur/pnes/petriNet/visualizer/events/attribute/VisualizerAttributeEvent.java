package pl.edu.ur.pnes.petriNet.visualizer.events.attribute;

import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public abstract class VisualizerAttributeEvent extends VisualizerEvent {
    public VisualizerAttributeEvent(EventType<? extends VisualizerEvent> eventType) {
        super(eventType);
    }
}
