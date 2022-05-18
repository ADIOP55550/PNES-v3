package pl.edu.ur.pnes.petriNet.events;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class NetEvent extends Event {

    public static EventType<NetEvent> ANY = new EventType<>("NET_ANY");

    public static EventType<NetElementAddedEvent> ELEMENT_ADDED = new EventType<>(ANY, "NET_ELEMENT_ADDED");
    public static EventType<NetElementRemovedEvent> ELEMENT_REMOVED = new EventType<>(ANY, "NET_ELEMENT_REMOVED");

    public static EventType<NodesMovedEvent> NODES_MOVED = new EventType<>(ANY, "NET_ANY");


    public NetEvent(EventType<? extends NetEvent> eventType) {
        super(eventType);
    }

}
