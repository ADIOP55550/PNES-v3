package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public class NodesMovedEvent extends VisualizerEvent {
    public final Node[] nodes;
    public final double[] offset;

    public NodesMovedEvent(Node[] nodes, double[] offset) {
        super(NODES_MOVED);
        this.nodes = nodes;
        this.offset = offset;
    }
}
