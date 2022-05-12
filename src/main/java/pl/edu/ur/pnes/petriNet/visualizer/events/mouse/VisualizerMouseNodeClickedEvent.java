package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

public class VisualizerMouseNodeClickedEvent extends VisualizerMouseEvent {

    public String getClickedNodeId() {
        return clickedNodeId;
    }

    private final String clickedNodeId;


    public VisualizerMouseNodeClickedEvent(String clickedNodeId) {
        super(MOUSE_NODE_CLICKED);
        this.clickedNodeId = clickedNodeId;
    }
}
