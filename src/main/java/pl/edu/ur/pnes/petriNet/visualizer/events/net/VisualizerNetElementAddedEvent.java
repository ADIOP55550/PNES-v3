package pl.edu.ur.pnes.petriNet.visualizer.events.net;

import pl.edu.ur.pnes.petriNet.NetElement;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public class VisualizerNetElementAddedEvent extends VisualizerNetEvent {

    private final NetElement addedElement;
    private final Class<? extends NetElement> addedElementType;

    public NetElement getAddedElement() {
        return addedElement;
    }

    public Class<? extends NetElement> getAddedElementType() {
        return addedElementType;
    }

    public VisualizerNetElementAddedEvent(NetElement addedElement, Class<? extends NetElement> addedElementType) {
        super(VisualizerEvent.NET_ELEMENT_ADDED);
        this.addedElement = addedElement;
        this.addedElementType = addedElementType;
    }
}
