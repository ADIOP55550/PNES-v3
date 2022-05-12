package pl.edu.ur.pnes.petriNet.events;

import pl.edu.ur.pnes.petriNet.NetElement;

public class NetElementAddedEvent extends NetEvent {

    private final NetElement element;

    public NetElementAddedEvent(NetElement element) {
        super(ELEMENT_ADDED);
        this.element = element;
    }

    public NetElement getElement() {
        return element;
    }
}
