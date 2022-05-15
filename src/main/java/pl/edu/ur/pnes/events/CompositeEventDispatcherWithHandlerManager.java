package pl.edu.ur.pnes.events;

import javafx.beans.property.ObjectProperty;

public class CompositeEventDispatcherWithHandlerManager extends CompositeEventDispatcher {
    public EventHandlerManager getEventHandlerManager() {
        return eventHandlerManager;
    }


    private final EventHandlerManager eventHandlerManager;

    public CompositeEventDispatcherWithHandlerManager(final Object eventSource) {
        this(new EventHandlerManager(eventSource));
    }

    public CompositeEventDispatcherWithHandlerManager(EventHandlerManager eventHandlerManager) {
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
