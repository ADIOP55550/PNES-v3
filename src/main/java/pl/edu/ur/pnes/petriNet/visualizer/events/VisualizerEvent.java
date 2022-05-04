package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class VisualizerEvent extends Event {
    public static final EventType<VisualizerEvent> VISUALIZER_EVENT_TYPE = new EventType<>(ANY);

    public VisualizerEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
