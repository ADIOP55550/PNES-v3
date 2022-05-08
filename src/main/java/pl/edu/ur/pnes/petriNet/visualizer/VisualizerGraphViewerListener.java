package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.stream.AttributeSink;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import pl.edu.ur.pnes.petriNet.visualizer.events.VGVLEventDispatcher;
import pl.edu.ur.pnes.petriNet.visualizer.events.VGVLNodeClickedEvent;

public final class VisualizerGraphViewerListener implements ViewerListener, EventTarget {
    private boolean loop = true;
    private boolean log = false;
    private final static Logger logger = LogManager.getLogger(VisualizerGraphViewerListener.class);

    public VisualizerGraphViewerListener(Viewer viewer) {


        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
//            fromViewer.addSink(graph);

        fromViewer.addAttributeSink(new AttributeSink() {
            @Override
            public void graphAttributeAdded(String s, long l, String s1, Object o) {
                if (log) logger.debug("graphAttributeAdded");
                if (log) logger.debug("eventId = " + l + ", attribute = " + s1 + ", Value = " + o);
            }

            @Override
            public void graphAttributeChanged(String s, long l, String s1, Object o, Object o1) {
                if (log) logger.debug("graphAttributeChanged");
                if (log)
                    logger.debug("eventId = " + l + ", attribute = " + s1 + ", old Value = " + o + ", new Value = " + o1);
            }

            @Override
            public void graphAttributeRemoved(String s, long l, String s1) {
                if (log) logger.debug("graphAttributeRemoved");
                if (log) logger.debug("eventId = " + l + ", attribute = " + s1);
            }

            @Override
            public void nodeAttributeAdded(String s, long l, String s1, String s2, Object o) {
                if (log) logger.debug("nodeAttributeAdded");
                if (log)
                    logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2 + ", Value = " + o);
            }

            @Override
            public void nodeAttributeChanged(String s, long l, String s1, String s2, Object o, Object o1) {
                if (log) logger.debug("nodeAttributeChanged");
                if (log)
                    logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2 + ", old Value = " + o + ", new Value = " + o1);
            }

            @Override
            public void nodeAttributeRemoved(String s, long l, String s1, String s2) {
                if (log) logger.debug("nodeAttributeRemoved");
                if (log) logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2);
            }

            @Override
            public void edgeAttributeAdded(String s, long l, String s1, String s2, Object o) {
                if (log) logger.debug("edgeAttributeAdded");
                if (log)
                    logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2 + ", Value = " + o);
            }

            @Override
            public void edgeAttributeChanged(String s, long l, String s1, String s2, Object o, Object o1) {
                if (log) logger.debug("edgeAttributeChanged");
                if (log)
                    logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2 + ", old Value = " + o + ", new Value = " + o1);
            }

            @Override
            public void edgeAttributeRemoved(String s, long l, String s1, String s2) {
                if (log) logger.debug("edgeAttributeRemoved");
                if (log) logger.debug("eventId = " + l + ", node Id = " + s1 + ", attribute = " + s2);
            }
        });

        new Thread(() -> {
            try {
                while (loop) {
                    fromViewer.pump();

                    Thread.sleep(50);

                    // here your simulation code.
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        if (log) logger.debug("Button pushed on node " + id);
        this.fireEvent(new VGVLNodeClickedEvent(id));
    }

    public void buttonReleased(String id) {
        if (log) logger.debug("Button released on node " + id);
    }

    public void mouseOver(String id) {
        if (log) logger.debug("Hover over node " + id);
    }

    public void mouseLeft(String id) {
        if (log) logger.debug("End hover over node " + id);
    }

    /****************************************
     *                                      *
     *           EVENT DISPATCHING          *
     *                                      *
     ****************************************/

    private VGVLEventDispatcher internalEventDispatcher;
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

    private VGVLEventDispatcher getInternalEventDispatcher() {
        initializeInternalEventDispatcher();
        return internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (internalEventDispatcher == null) {
            internalEventDispatcher = createInternalEventDispatcher();
            eventDispatcher = new SimpleObjectProperty<EventDispatcher>(
                    VisualizerGraphViewerListener.this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    private VGVLEventDispatcher createInternalEventDispatcher() {
        return new VGVLEventDispatcher(this);
    }

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
    public <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
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
    public <T extends Event> void removeEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
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
    public <T extends Event> void addEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
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
    public <T extends Event> void removeEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
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
    private <T extends Event> void setEventHandler(
            final EventType<T> eventType,
            final EventHandler<? super T> eventHandler) {
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
    public EventDispatchChain buildEventDispatchChain(
            EventDispatchChain tail) {
        tail = tail.prepend(this.getEventDispatcher());
        return tail;
    }

    public void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }
}
