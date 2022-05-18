package pl.edu.ur.pnes.petriNet.events;

import pl.edu.ur.pnes.petriNet.Node;

public class NodesMovedEvent extends NetEvent {
    public final Node[] nodes;
    public final double[] offset;

    public NodesMovedEvent(Node[] nodes, double[] offset) {
        super(NODES_MOVED);
        this.nodes = nodes;
        this.offset = offset;
    }
}
