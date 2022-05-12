package pl.edu.ur.pnes.petriNet.visualizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.stream.AttributeSink;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.AttributeEventTargetType;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeAddedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeChangedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.attribute.VisualizerAttributeRemovedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeClickedEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOutEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeOverEvent;
import pl.edu.ur.pnes.petriNet.visualizer.events.mouse.VisualizerMouseNodeReleasedEvent;


public class GraphstreamViewerListener implements ViewerListener {

    private final VisualizerEventsHandler handler;
    private final Logger logger = LogManager.getLogger(GraphstreamViewerListener.class);
    private final boolean log = false;
    private boolean hooked;
    private boolean loop = true;
    private Thread pumpThread;
    private ViewerPipe fromViewerPipe;

    public boolean isHooked() {
        return hooked;
    }

    void hookInto(Viewer viewer) {
        if (hooked)
            throw new IllegalStateException("EventsHandler already hooked");
        this.hooked = true;
        this.fromViewerPipe = viewer.newViewerPipe();
        fromViewerPipe.addViewerListener(this);
        fromViewerPipe.addAttributeSink(sink);


        this.pumpThread = new Thread(() -> {
            try {
                while (loop) {
                    fromViewerPipe.pump();
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        pumpThread.start();
    }

    void unhook() {
        this.pumpThread.interrupt();
        this.pumpThread = null;
        fromViewerPipe.removeViewerListener(this);
        fromViewerPipe.removeAttributeSink(this.sink);
        this.hooked = false;
    }


    final AttributeSink sink = new AttributeSink() {

        @Override
        public void graphAttributeAdded(String sourceId, long timeId, String attribute, Object value) {
            if (log) {
                logger.debug("graphAttributeAdded");
                logger.debug("eventId = " + sourceId + ", attribute = " + attribute + ", Value = " + value);
            }
            handler.fireEvent(new VisualizerAttributeAddedEvent(AttributeEventTargetType.GRAPH, "", attribute, value));
        }

        @Override
        public void graphAttributeChanged(String sourceId, long timeId, String attribute, Object oldValue, Object newValue) {
            if (log) {
                logger.debug("graphAttributeChanged");
                logger.debug("eventId = " + timeId + ", attribute = " + attribute + ", old Value = " + oldValue + ", new Value = " + newValue);
            }
            handler.fireEvent(new VisualizerAttributeChangedEvent(AttributeEventTargetType.GRAPH, "", attribute, oldValue, newValue));
        }

        @Override
        public void graphAttributeRemoved(String sourceId, long timeId, String attribute) {
            if (log) {
                logger.debug("graphAttributeRemoved");
                logger.debug("eventId = " + timeId + ", attribute = " + attribute);
            }
            handler.fireEvent(new VisualizerAttributeRemovedEvent(AttributeEventTargetType.GRAPH, "", attribute));
        }

        @Override
        public void nodeAttributeAdded(String sourceId, long timeId, String nodeId, String attribute, Object value) {
            if (log) {
                logger.debug("nodeAttributeAdded");
                logger.debug("eventId = " + timeId + ", node Id = " + nodeId + ", attribute = " + attribute + ", Value = " + value);
            }
            handler.fireEvent(new VisualizerAttributeAddedEvent(AttributeEventTargetType.NODE, nodeId, attribute, value));
        }

        @Override
        public void nodeAttributeChanged(String sourceId, long timeId, String nodeId, String attribute, Object oldValue, Object newValue) {
            if (log) {
                logger.debug("nodeAttributeChanged");
                logger.debug("eventId = " + timeId + ", node Id = " + nodeId + ", attribute = " + attribute + ", old Value = " + oldValue + ", new Value = " + newValue);
            }
            handler.fireEvent(new VisualizerAttributeChangedEvent(AttributeEventTargetType.NODE, nodeId, attribute, oldValue, newValue));
        }

        @Override
        public void nodeAttributeRemoved(String sourceId, long timeId, String nodeId, String attribute) {
            if (log) {
                logger.debug("nodeAttributeRemoved");
                logger.debug("eventId = " + timeId + ", node Id = " + nodeId + ", attribute = " + attribute);
            }
            handler.fireEvent(new VisualizerAttributeRemovedEvent(AttributeEventTargetType.NODE, attribute, attribute));
        }

        @Override
        public void edgeAttributeAdded(String sourceId, long timeId, String edgeId, String attribute, Object value) {
            if (log) {
                logger.debug("edgeAttributeAdded");
                logger.debug("eventId = " + timeId + ", node Id = " + edgeId + ", attribute = " + attribute + ", Value = " + value);
            }
            handler.fireEvent(new VisualizerAttributeAddedEvent(AttributeEventTargetType.EDGE, edgeId, attribute, value));
        }

        @Override
        public void edgeAttributeChanged(String sourceId, long timeId, String edgeId, String attribute, Object oldValue, Object newValue) {
            if (log) {
                logger.debug("edgeAttributeChanged");
                logger.debug("eventId = " + timeId + ", node Id = " + edgeId + ", attribute = " + attribute + ", old Value = " + oldValue + ", new Value = " + newValue);
            }
            handler.fireEvent(new VisualizerAttributeChangedEvent(AttributeEventTargetType.EDGE, edgeId, attribute, oldValue, newValue));
        }

        @Override
        public void edgeAttributeRemoved(String sourceId, long timeId, String edgeId, String attribute) {
            if (log) {
                logger.debug("edgeAttributeRemoved");
                logger.debug("eventId = " + timeId + ", node Id = " + edgeId + ", attribute = " + attribute);
            }
            handler.fireEvent(new VisualizerAttributeRemovedEvent(AttributeEventTargetType.EDGE, attribute, attribute));
        }
    };

    public void viewClosed(String id) {
        loop = false;
    }

    public void buttonPushed(String id) {
        if (log) logger.debug("Button pushed on node " + id);
        handler.fireEvent(new VisualizerMouseNodeClickedEvent(id));
    }

    public void buttonReleased(String id) {
        if (log) logger.debug("Button released on node " + id);
        handler.fireEvent(new VisualizerMouseNodeReleasedEvent(id));
    }

    public void mouseOver(String id) {
        if (log) logger.debug("Hover over node " + id);
        handler.fireEvent(new VisualizerMouseNodeOverEvent(id));
    }

    public void mouseLeft(String id) {
        if (log) logger.debug("End hover over node " + id);
        handler.fireEvent(new VisualizerMouseNodeOutEvent(id));
    }

    public GraphstreamViewerListener(VisualizerEventsHandler handler) {
        this.handler = handler;
    }
}
