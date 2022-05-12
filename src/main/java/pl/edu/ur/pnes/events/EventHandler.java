package pl.edu.ur.pnes.events;

import javafx.beans.property.ObjectProperty;
import javafx.event.*;

public interface EventHandler<U extends CompositeEventDispatcherWithHandlerManager> extends EventTarget {

    void setEventDispatcher(EventDispatcher value);

    EventDispatcher getEventDispatcher();

    ObjectProperty<EventDispatcher> eventDispatcherProperty();

    U getInternalEventDispatcher();

    /**
     * Registers an event handler to this node. The handler is called when the
     * node receives an {@code Event} of the specified type during the bubbling
     * phase of event delivery.
     *
     * @param <T>          the specific event class of the handler
     * @param eventType    the type of the events to receive by the handler
     * @param eventHandler the handler to register
     * @throws NullPointerException if the event type or handler is null
     */
    default <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final javafx.event.EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventHandler(eventType, eventHandler);
    }


    /**
     * Unregisters a previously registered event handler from this node. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param <T>          the specific event class of the handler
     * @param eventType    the event type from which to unregister
     * @param eventHandler the handler to unregister
     * @throws NullPointerException if the event type or handler is null
     */
    default <T extends Event> void removeEventHandler(
            final EventType<T> eventType,
            final javafx.event.EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher()
                .getEventHandlerManager()
                .removeEventHandler(eventType, eventHandler);
    }

    /**
     * Registers an event filter to this node. The filter is called when the
     * node receives an {@code Event} of the specified type during the capturing
     * phase of event delivery.
     *
     * @param <T>         the specific event class of the filter
     * @param eventType   the type of the events to receive by the filter
     * @param eventFilter the filter to register
     * @throws NullPointerException if the event type or filter is null
     */
    default <T extends Event> void addEventFilter(
            final EventType<T> eventType,
            final javafx.event.EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .addEventFilter(eventType, eventFilter);
    }

    /**
     * Unregisters a previously registered event filter from this node. One
     * filter might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the filter.
     *
     * @param <T>         the specific event class of the filter
     * @param eventType   the event type from which to unregister
     * @param eventFilter the filter to unregister
     * @throws NullPointerException if the event type or filter is null
     */
    default <T extends Event> void removeEventFilter(
            final EventType<T> eventType,
            final javafx.event.EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().getEventHandlerManager()
                .removeEventFilter(eventType, eventFilter);
    }

    /**
     * Sets the handler to use for this event type. There can only be one such handler
     * specified at a time. This handler is guaranteed to be called as the last, after
     * handlers added using {@link #addEventHandler(javafx.event.EventType, javafx.event.EventHandler)}.
     * This is used for registering the user-defined onFoo event handlers.
     *
     * @param <T>          the specific event class of the handler
     * @param eventType    the event type to associate with the given eventHandler
     * @param eventHandler the handler to register, or null to unregister
     * @throws NullPointerException if the event type is null
     */
    default <T extends Event> void setEventHandler(
            final EventType<T> eventType,
            final javafx.event.EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().getEventHandlerManager()
                .setEventHandler(eventType, eventHandler);
    }

    /**
     * Construct an event dispatch chain for this node. The event dispatch chain
     * contains all event dispatchers from the stage to this node.
     *
     * @param tail the initial chain to build from
     * @return the resulting event dispatch chain for this node
     */
    @Override
    default EventDispatchChain buildEventDispatchChain(
            EventDispatchChain tail) {
        tail = tail.prepend(this.getEventDispatcher());
        return tail;
    }

    default void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }
}

