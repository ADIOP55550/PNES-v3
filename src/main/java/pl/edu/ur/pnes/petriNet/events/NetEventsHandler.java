package pl.edu.ur.pnes.petriNet.events;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventDispatcher;
import pl.edu.ur.pnes.events.CompositeEventDispatcherWithHandlerManager;
import pl.edu.ur.pnes.events.EventHandler;

public class NetEventsHandler implements EventHandler<CompositeEventDispatcherWithHandlerManager> {


    private CompositeEventDispatcherWithHandlerManager internalEventDispatcher;
    private ObjectProperty<EventDispatcher> eventDispatcher;

    public final void setEventDispatcher(EventDispatcher value) {
        eventDispatcherProperty().set(value);
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public final ObjectProperty<EventDispatcher> eventDispatcherProperty() {
        initializeInternalEventDispatcher();
        return eventDispatcher;
    }

    public CompositeEventDispatcherWithHandlerManager getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (internalEventDispatcher == null) {
            internalEventDispatcher = createInternalEventDispatcher();
            eventDispatcher = new SimpleObjectProperty<>(
                    NetEventsHandler.this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    private CompositeEventDispatcherWithHandlerManager createInternalEventDispatcher() {
        return new CompositeEventDispatcherWithHandlerManager(this);
    }
}
