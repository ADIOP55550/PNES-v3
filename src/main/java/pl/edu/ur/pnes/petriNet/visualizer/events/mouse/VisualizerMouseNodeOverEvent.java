package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

/**
 * Occurs when mouse is hovered over a Node
 */
public class VisualizerMouseNodeOverEvent extends VisualizerMouseEvent {

    public String getNodeId() {
        return nodeId;
    }

    private final String nodeId;


    public VisualizerMouseNodeOverEvent(String nodeId) {
        super(MOUSE_NODE_OVER);
        this.nodeId = nodeId;
    }
}
