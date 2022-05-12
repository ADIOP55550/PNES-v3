package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

public class VisualizerMouseNodeReleasedEvent extends VisualizerMouseEvent {

    public String getClickedNodeId() {
        return clickedNodeId;
    }

    private final String clickedNodeId;

    public VisualizerMouseNodeReleasedEvent(String clickedNodeId) {
        super(MOUSE_NODE_RELEASED);
        this.clickedNodeId = clickedNodeId;
    }
}
