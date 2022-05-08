package pl.edu.ur.pnes.petriNet.visualizer.events;

public class VGVLEventDispatcher extends CompositeEventDispatcher {


    public EventHandlerManager getEventHandlerManager() {
        return eventHandlerManager;
    }

    private final EventHandlerManager eventHandlerManager;

    public VGVLEventDispatcher(final Object eventSource) {
        this(new EventHandlerManager(eventSource));
    }

    public VGVLEventDispatcher(EventHandlerManager eventHandlerManager) {
        this.eventHandlerManager = eventHandlerManager;
    }

    @Override
    public BasicEventDispatcher getFirstDispatcher() {
        return eventHandlerManager;
    }

    @Override
    public BasicEventDispatcher getLastDispatcher() {
        return eventHandlerManager;
    }
}
