package pl.edu.ur.pnes.petriNet.visualizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphstream.stream.AttributeSink;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public final class VisualizerGraphViewerListener implements ViewerListener {
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
}
