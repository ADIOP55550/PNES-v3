package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

import pl.edu.ur.pnes.petriNet.Node;
import pl.edu.ur.pnes.petriNet.visualizer.events.VisualizerEvent;

public class NodesMovedEvent extends VisualizerEvent {
    public final String[] nodesIds;
    public final double[] offset;

    public NodesMovedEvent(String[] nodesIds, double[] offset) {
        super(NODES_MOVED);
        this.nodesIds = nodesIds;
        this.offset = offset;
    }
}
