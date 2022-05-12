package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.Event;
import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeAddedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeChangedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeRemovedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.*;
import pl.edu.ur.pnes.petriNet.visualizer.events.net.VisualizerNetElementAddedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.net.VisualizerNetElementRemovedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.net.VisualizerNetEvent;

public abstract class VisualizerEvent extends Event {

    public static EventType<VisualizerEvent> ANY = new EventType<>("VISUALIZER_ANY");

    public static EventType<VisualizerMouseEvent> MOUSE = new EventType<>(ANY, "VISUALIZER_MOUSE");
    public static EventType<VisualizerMouseNodeClickedEvent> MOUSE_NODE_CLICKED = new EventType<>(MOUSE, "VISUALIZER_MOUSE_NODE_CLICKED");
    public static EventType<VisualizerMouseNodeReleasedEvent> MOUSE_NODE_RELEASED = new EventType<>(MOUSE, "VISUALIZER_MOUSE_NODE_RELEASED");
    public static EventType<VisualizerMouseNodeOverEvent> MOUSE_NODE_OVER = new EventType<>(MOUSE, "VISUALIZER_MOUSE_NODE_OVER");
    public static EventType<VisualizerMouseNodeOutEvent> MOUSE_NODE_OUT = new EventType<>(MOUSE, "VISUALIZER_MOUSE_NODE_OUT");

    public static EventType<VisualizerAttributeEvent> ATTRIBUTE = new EventType<>(ANY, "VISUALIZER_ATTRIBUTE");
    public static EventType<VisualizerAttributeAddedEvent> ATTRIBUTE_ADDED = new EventType<>(ATTRIBUTE, "VISUALIZER_ATTRIBUTE_ADDED");
    public static EventType<VisualizerAttributeChangedEvent> ATTRIBUTE_CHANGED = new EventType<>(ATTRIBUTE, "VISUALIZER_ATTRIBUTE_CHANGED");
    public static EventType<VisualizerAttributeRemovedEvent> ATTRIBUTE_REMOVED = new EventType<>(ATTRIBUTE, "VISUALIZER_ATTRIBUTE_REMOVED");

    public static EventType<VisualizerNetEvent> NET = new EventType<>(ANY, "NET");
    public static EventType<VisualizerNetElementAddedEvent> NET_ELEMENT_ADDED = new EventType<>(NET, "NET_ELEMENT_ADDED");
    public static EventType<VisualizerNetElementRemovedEvent> NET_ELEMENT_REMOVED = new EventType<>(NET, "NET_ELEMENT_REMOVED");


    public VisualizerEvent(EventType<? extends VisualizerEvent> eventType) {
        super(eventType);
    }

}
