package pl.edu.ur.pnes.petriNet.visualizer.events.net;

import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public abstract class VisualizerNetElementRemovedEvent extends VisualizerNetEvent {

    private final NetElement removedElement;
    private final Class<? extends NetElement> removedElementType;

    public NetElement getRemovedElement() {
        return removedElement;
    }

    public Class<? extends NetElement> getRemovedElementType() {
        return removedElementType;
    }

    public VisualizerNetElementRemovedEvent(NetElement removedElement, Class<? extends NetElement> removedElementType) {
        super(VisualizerEvent.NET_ELEMENT_REMOVED);
        this.removedElement = removedElement;
        this.removedElementType = removedElementType;
    }
}
