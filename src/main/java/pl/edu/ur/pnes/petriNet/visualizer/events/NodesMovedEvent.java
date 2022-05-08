package pl.edu.ur.pnes.petriNet.visualizer.events;

import javafx.event.EventType;
import pl.edu.ur.pnes.petriNet.Node;

public class NodesMovedEvent extends VisualizerEvent {
    public static final EventType<NodesMovedEvent> NODES_MOVED_EVENT = new EventType<>(VisualizerEvent.VISUALIZER_EVENT_TYPE, "NodesMoved");

    public final Node[] nodes;
    public final double[] offset;

    public NodesMovedEvent(Node[] nodes, double[] offset) {
        super(NODES_MOVED_EVENT);
        this.nodes = nodes;
        this.offset = offset;
    }
}
