package pl.edu.ur.pnes.petriNet.events;

import pl.edu.ur.pnes.petriNet.NetElement;

public class NetElementRemovedEvent extends NetEvent {

    private final NetElement element;

    public NetElementRemovedEvent(NetElement element) {
        super(ELEMENT_REMOVED);
        this.element = element;
    }

    public NetElement getElement() {
        return element;
    }
}
