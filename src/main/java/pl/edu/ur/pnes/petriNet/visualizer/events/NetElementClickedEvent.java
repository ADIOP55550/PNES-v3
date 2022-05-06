package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.NetElement;

public class NetElementClickedEvent extends VisualizerEvent {
    public static final EventType<NetElementClickedEvent> NET_ELEMENT_CLICKED_EVENT = new EventType<>(VisualizerEvent.VISUALIZER_EVENT_TYPE, "NetElementClicked");
    private final NetElement clickedElement;

    public NetElementClickedEvent(NetElement clickedElement) {
        super(NET_ELEMENT_CLICKED_EVENT);
        this.clickedElement = clickedElement;
    }

    public NetElement getClickedElement() {
        return clickedElement;
    }
}
