package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

import javafx.event.Event;
import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public abstract class VisualizerMouseEvent extends VisualizerEvent {

    public VisualizerMouseEvent(EventType<? extends VisualizerEvent> eventType) {
        super(eventType);
    }
}
