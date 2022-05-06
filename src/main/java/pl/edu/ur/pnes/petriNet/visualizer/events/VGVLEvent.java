package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.Event;
import javafx.event.EventType;

public class VGVLEvent extends Event {

    public static EventType<VGVLEvent> ANY = new EventType<>("VGVL_ANY");
    public static EventType<VGVLNodeClickedEvent> NODE_CLICKED = new EventType<>(ANY, "NODE_CLICKED");


    public VGVLEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
