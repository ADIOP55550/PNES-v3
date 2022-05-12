package pl.edu.ur.pnes.petriNet.visualizer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventDispatcher;
import org.graphstream.ui.view.Viewer;
import pl.edu.ur.pnes.events.CompositeEventDispatcherWithHandlerManager;
import pl.edu.ur.pnes.events.EventHandler;


final class VisualizerEventsHandler implements EventHandler<CompositeEventDispatcherWithHandlerManager> {

//    private final static Logger logger = LogManager.getLogger(VisualizerEventsHandler.class);

    private final GraphstreamViewerListener graphstreamViewerListener;

    public VisualizerEventsHandler() {
        graphstreamViewerListener = new GraphstreamViewerListener(this);
    }

    public VisualizerEventsHandler(Viewer viewer) {
        this();
        graphstreamViewerListener.hookInto(viewer);
    }

    private CompositeEventDispatcherWithHandlerManager internalEventDispatcher;
    private ObjectProperty<EventDispatcher> eventDispatcher;

    public void setEventDispatcher(EventDispatcher value) {
        eventDispatcherProperty().set(value);
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public ObjectProperty<EventDispatcher> eventDispatcherProperty() {
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
                    VisualizerEventsHandler.this,
                    "eventDispatcher",
                    internalEventDispatcher);
        }
    }

    private CompositeEventDispatcherWithHandlerManager createInternalEventDispatcher() {
        return new CompositeEventDispatcherWithHandlerManager(this);
    }
}
