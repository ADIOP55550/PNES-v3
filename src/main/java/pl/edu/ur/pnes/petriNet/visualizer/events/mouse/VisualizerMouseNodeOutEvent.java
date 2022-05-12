package pl.edu.ur.pnes.petriNet.visualizer.events.mouse;

public class VisualizerMouseNodeOutEvent extends VisualizerMouseEvent {

    public String getClickedNodeId() {
        return clickedNodeId;
    }

    private final String clickedNodeId;


    public VisualizerMouseNodeOutEvent(String clickedNodeId) {
        super(MOUSE_NODE_OUT);
        this.clickedNodeId = clickedNodeId;
    }
}
