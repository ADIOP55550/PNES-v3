package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

/**
 * Occurs when mouse exits a Node
 */
public class VisualizerMouseNodeOutEvent extends VisualizerMouseEvent {

    public String getNodeId() {
        return nodeId;
    }

    private final String nodeId;


    public VisualizerMouseNodeOutEvent(String nodeId) {
        super(MOUSE_NODE_OUT);
        this.nodeId = nodeId;
    }
}
